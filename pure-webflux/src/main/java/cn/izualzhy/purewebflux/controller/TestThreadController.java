package cn.izualzhy.purewebflux.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.client.ReactorResourceFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Log4j2
@RestController
public class TestThreadController {
    private final ReactorResourceFactory reactorResourceFactory;

    public TestThreadController(ReactorResourceFactory reactorResourceFactory) {
        this.reactorResourceFactory = reactorResourceFactory;
    }

    @GetMapping(value = "/test")
    public String thread() {
        log.info("test");
        return "izualzhy";
    }

    @GetMapping(value = "/mono-thread")
    public Mono<String> monoThread() {
        log.info("monoThread start");
        return Mono.just("izualzhy");
    }

    @GetMapping(value = "/flux-thread")
    public Flux<String> fluxThread() {
        log.info("fluxThread start");
        return Flux.just("izualzhy-1", "izualzhy-2", "izualzhy-3")
                .map(i -> {
                    System.out.println(Thread.currentThread());
                    return i;
                });
    }

    @GetMapping("/flux-elastic-thread")
    public Flux<String> fluxElasticThread() {
        log.info("fluxElasticThread start");
        return Flux.just("izualzhy-1", "izualzhy-2", "izualzhy-3")
                .publishOn(Schedulers.boundedElastic())
                .map(i -> {
                    System.out.println(Thread.currentThread());
                    return i;
                });
    }

    @GetMapping("/compare-subscribe-on-and-publish-on")
    public void compareSchedulers() {
        System.out.println("==== subscribeOn ====");
        Mono.just("hello")
                .map(data -> {
                    System.out.println("[subscribeOn] map-1: " + Thread.currentThread().getName());
                    return data;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(data -> {
                    System.out.println("[subscribeOn] map-2: " + Thread.currentThread().getName());
                    return data;
                })
                .subscribe(result -> {
                    System.out.println("[subscribeOn] subscribe: " + Thread.currentThread().getName() + "\tresult: " + result);
                });

        System.out.println("==== publishOn ====");
        Mono.just("hello")
                .map(data -> {
                    System.out.println("[publishOn] map-1: " + Thread.currentThread().getName());
                    return data;
                })
                .publishOn(Schedulers.boundedElastic())
                .map(data -> {
                    System.out.println("[publishOn] map-2: " + Thread.currentThread().getName());
                    return data;
                })
                .subscribe(result -> {
                    System.out.println("[subscribeOn] subscribe: " + Thread.currentThread().getName() + "\tresult: " + result);
                });
    }
}
