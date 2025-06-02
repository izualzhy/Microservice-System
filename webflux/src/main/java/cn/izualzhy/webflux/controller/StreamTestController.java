package cn.izualzhy.webflux.controller;

import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@RestController
public class StreamTestController {

    @GetMapping(value = "/test/concat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> testConcatMap() {
        Flux<Mono<String>> handlerMonos = Flux.just(
                Mono.empty(),
                Mono.empty(),
                Mono.just("matchHandler-1"),
                Mono.just("matchHandler-2")
        );

        Flux<String> handlerFlux = handlerMonos.concatMap(m -> m);
        handlerFlux.subscribe(result -> System.out.println("concatMap: " + result));

        handlerFlux.next()
                .defaultIfEmpty("Error")
                .subscribe(result -> System.out.println("next: " + result));

        Flux.just(1, 2, 3)
                .concatMap(i -> {
                    if (i == 3) return Mono.just("concatMap matched");
                    else return Mono.empty();
                })
                .subscribe(System.out::println);

        Flux.just(1, 2, 3)
                .map(i -> {
                    if (i == 3) return Mono.just("map matched");
                    else return Mono.empty();
                })
                .subscribe(System.out::println);

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

        Flux.range(1, 3)
                .map(i -> {
                    System.out.println("[before publishOn] " + i + " on thread: " + Thread.currentThread().getName() + ", trace_id=" + MDC.get("trace_id"));
                    return i;
                })
                .publishOn(Schedulers.boundedElastic()) // 切换线程池
                .map(i -> {
                    System.out.println("[after publishOn] " + i + " on thread: " + Thread.currentThread().getName() + ", trace_id=" + MDC.get("trace_id"));
                    return i;
                })
                .subscribe();

        System.out.println("testMDC in Thread: " + Thread.currentThread().getName());

        Flux.range(1, 3)
                .map(i -> {
                    System.out.println("[before publishOn] " + i + " on thread: " +
                            Thread.currentThread().getName() + ", trace_id=" + MDC.get("trace_id"));
                    return i;
                })
                .publishOn(Schedulers.boundedElastic())
                .flatMap(i ->
                        Mono.deferContextual(ctx -> {
                            String traceId = ctx.getOrDefault("trace_id", "N/A");
                            MDC.put("trace_id", traceId);
                            System.out.println("[after publishOn] " + i + " on thread: " +
                                    Thread.currentThread().getName() + ", trace_id=" + MDC.get("trace_id"));
                            MDC.clear(); // 避免污染线程池
                            return Mono.just(i);
                        })
                )
                .contextWrite(Context.of("trace_id", "multiThread"))
                .subscribe();

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

    @GetMapping(value = "/test/flatmapvsconcatmap")
    public void testFlatMapVsConcatMap() {
        System.out.println("===== flatMap 示例 =====");
        Flux.range(1, 5)
                .flatMap(i -> Mono.fromCallable(() -> {
                                    System.out.println("flatMap: " + i + " on thread " + Thread.currentThread().getName() +
                                            " currentTime: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
                                    Thread.sleep(500); // 模拟耗时任务
                                    return i;
                                })
                                .subscribeOn(Schedulers.parallel())
                )
                .blockLast();

        System.out.println("\n===== concatMap 示例 =====");
        Flux.range(1, 5)
                .concatMap(i -> Mono.fromCallable(() -> {
                                    System.out.println("concatMap: " + i + " on thread " + Thread.currentThread().getName() +
                                            " currentTime: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
                                    Thread.sleep(500); // 模拟耗时任务
                                    return i;
                                })
                                .subscribeOn(Schedulers.parallel())
                )
                .blockLast();
    }

    @GetMapping(value = "/test/defer")
    public String testDefer() {
        System.out.println("testDefer: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
        Mono<Void> chain = createFilterChain();

        System.out.println("Before subscribe");
        chain.subscribe();

        chain = Mono.defer(() -> createFilterChain());

        System.out.println("Before subscribe");
        chain.subscribe();

        return "success";
    }

    private static Mono<Void> createFilterChain() {
        System.out.println("Building filter chain...");

        return Mono.just("filter1")
                .flatMap(name -> {
                    System.out.println("Executing " + name);
                    return Mono.empty();
                });
    }

    @GetMapping(value = "/test/thread")
    public String testThread() {
        Flux.just(1)
                .map(i -> {
                    System.out.println("map1: " + Thread.currentThread().getName());
                    return i;
                })
                // .publishOn(Schedulers.parallel()) //指定在parallel线程池中执行
                .map(i -> {
                    System.out.println("map2: " + Thread.currentThread().getName());
                    return i;
                })
                // .publishOn(Schedulers.boundedElastic()) // 指定下游的执行线程
                .map(i -> {
                    System.out.println("map3: " + Thread.currentThread().getName());
                    return i;
                })
                // .subscribeOn(Schedulers.newBoundedElastic( 10, 10, "custom-thread"))
                .map(i -> {
                    System.out.println("map4: " + Thread.currentThread().getName());
                    return i;
                })
                .subscribe(
                        // i -> System.out.println("subscribe: " + Thread.currentThread().getName())
                );
        return "success";
    }

    @GetMapping(value = "/test/create")
    public String testCreate() {
        Flux<String> flux1 = Flux.just("foo", "bar", "foobar");
        flux1.map(s -> s.toUpperCase())
                .subscribe(System.out::println);

        Flux<String> flux2 = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3*state);
                    if (state == 10) sink.complete();
                    return state + 1;
                });
        flux2.subscribe(System.out::println);

        return "success";
    }
}
