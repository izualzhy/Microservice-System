package cn.izualzhy.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;

@RestController
@RequestMapping("/test/pathD")
public class TestPathDController {
    @AllArgsConstructor
    class TestData {
        public String name;
        public String msg;
    }

    @RequestMapping("/{name}")
    public TestData get(@PathVariable("name") String name) {
        return new TestData(name, "hello from demo-service, " + name);
    }

    @GetMapping(value = "/sse/time", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamTime() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(seq -> "data: Current time is " + LocalTime.now().toString() + "\n\n");
    }
}
