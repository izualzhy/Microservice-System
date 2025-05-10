package cn.izualzhy.webflux.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
}
