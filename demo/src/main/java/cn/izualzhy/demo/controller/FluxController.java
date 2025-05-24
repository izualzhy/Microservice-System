package cn.izualzhy.demo.controller;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/flux")
public class FluxController {
    @lombok.Data
    @AllArgsConstructor
    @JsonSerialize(using = SlowSerializer.class)
    public class Data {
        private int id;
        private String value;
    }

    public class SlowSerializer extends JsonSerializer<Data> {
        @Override
        public void serialize(Data value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            try {
                System.out.println("write begin." + value.getId());
                Thread.sleep(5000); // 每条写入都延迟 5ms
                System.out.println("write end." + value.getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gen.writeStartObject();
            gen.writeNumberField("id", value.getId());
            gen.writeStringField("value", value.getValue());
            gen.writeEndObject();
        }
    }

    @PostMapping("/large/{count}")
    public Flux<Data> bigJson(@PathVariable("count") String count) {
        int cnt = Integer.parseInt(count);
        return Flux.range(1, cnt)
                .map(i -> new Data(i, "value" + i));
    }

    @GetMapping("/lazy")
    public void testLazyExecute() {
        Flux<Integer> flux = Flux.just(1, 2, 3)
                .map(x -> {
                    System.out.println("Mapping " + x + " thread: " + Thread.currentThread().getName());
                    return x * 2;
                });

        System.out.println("Before subscribe" + " thread: " + Thread.currentThread().getName());
        flux.subscribe(System.out::println);


        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        list.stream()
                .map(x -> x * 2)
                .filter(x -> x > 4)
                .forEach(System.out::println); // 同步，立即执行


        Mono.fromCallable(() -> {
                    System.out.println("Callable thread: " + Thread.currentThread().getName());
                    Thread.sleep(100); // 模拟 I/O
                    return "result";
                })
                .subscribeOn(Schedulers.boundedElastic()) // 切换线程
                .subscribe(i -> System.out.println("Subscribe thread: " + Thread.currentThread().getName()));


    }
}
