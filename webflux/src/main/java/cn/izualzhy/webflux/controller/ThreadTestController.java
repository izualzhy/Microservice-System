package cn.izualzhy.webflux.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
public class ThreadTestController {
    @GetMapping(value = "/getPerson")
    public Mono<String> getPerson() {
        log.info("getPerson start");
        return Mono.just("izualzhy");
    }

    @GetMapping(value = "/getPersonList")
    public Flux<String> getPersonList() {
        log.info("getPersonList start");
        return Flux.just("izualzhy-1", "izualzhy-2", "izualzhy-3")
                .map(i -> {
                    System.out.println(Thread.currentThread());
                    return i;
                });
    }
}
