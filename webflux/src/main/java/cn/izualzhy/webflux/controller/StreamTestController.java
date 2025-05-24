package cn.izualzhy.webflux.controller;

import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RestController
public class StreamTestController {

    @GetMapping(value = "/test/concat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> testConcatMap() {
        return Flux.just("A", "B", "C", "D")
                .map(this::simulateChunk)
                .concatMap(f -> f);
    }

    @GetMapping(value = "/test/flat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> testFlatMap() {
        return Flux.just("A", "B", "C", "D")
                .map(this::simulateChunk)
                .flatMap(f -> f); // 可能乱序
    }

    @GetMapping(value = "/test/flatSeq", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> testFlatMapSequential() {
        return Flux.just("A", "B", "C", "D")
                .map(this::simulateChunk)
                .flatMapSequential(f -> f);
    }

    // 模拟每个 SSE chunk 的处理延迟
    private Flux<String> simulateChunk(String prefix) {
        Duration delay;
        switch (prefix) {
            case "A": delay = Duration.ofMillis(5000); break;
            case "B": delay = Duration.ofMillis(1000); break;
            case "C": delay = Duration.ofMillis(3000); break;
            case "D": delay = Duration.ofMillis(2000); break;
            default: delay = Duration.ofMillis(10000);
        }

        return Flux.range(1, 3)
                .delayElements(delay)
                .map(i -> String.format("[%s-%d] @%s", prefix, i, LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"))));
    }

    @GetMapping(value = "/test/MDC")
    public String testMDCMultiThread() throws InterruptedException {
        MDC.put("trace_id", "multiThread");

        Flux.range(1, 5)
            .doOnNext(i -> System.out.println("[before publishOn] " + i + " on thread: " + Thread.currentThread().getName() + ", trace_id=" + MDC.get("trace_id")))
            .publishOn(Schedulers.boundedElastic()) // 切换线程池
            .doOnNext(i -> System.out.println("[after publishOn] " + i + " on thread: " + Thread.currentThread().getName() + ", trace_id=" + MDC.get("trace_id")))
            .subscribe();


        System.out.println("WebClient create Thread: " + Thread.currentThread().getName());
        WebClient.create()
        .get()
        .uri("http://127.0.0.1:6081/test/MDC/singleThread")
        .retrieve()
        .bodyToMono(String.class)
        .doOnNext(body -> {
            System.out.println("doOnNext Thread: " + Thread.currentThread().getName());
        })
        .block(); // 只是为了让主线程等一下

        Thread.sleep(1000); // 等待异步任务执行完成

        return "testMDC";
    }

    @GetMapping(value = "/test/MDC/singleThread")
    public String testMDCSingleThread() throws InterruptedException {
        MDC.put("trace_id", "singleThread");

        Flux.range(1, 5)
                .doOnNext(i -> System.out.println("Thread: " + Thread.currentThread().getName() + ", trace_id=" + MDC.get("trace_id")))
                .subscribe();
        Thread.sleep(1000); // 等待异步任务执行完成

        return "testMDC";
    }
}
