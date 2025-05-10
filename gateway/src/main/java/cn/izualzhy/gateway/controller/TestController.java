package cn.izualzhy.gateway.controller;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;


@RestController
public class TestController {
    @GetMapping("/data")
    public Mono<String> getData() {
        return Mono.just("Hello Data");
    }

    @GetMapping("/stream1")
    public Mono<Void> streamData(ServerHttpResponse response) {
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);

        Flux<DataBuffer> data = Flux.just("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
                .map(s -> response.bufferFactory().wrap(s.getBytes()));
        return response.writeWith(data);
    }

    @GetMapping("/static")
    public Mono<Void> staticResponse(ServerHttpResponse response) {
        String json = "{\"name\":\"Alice\"}";
        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes());
        return response.writeWith(Mono.just(buffer));
    }

    @GetMapping(value = "/stream2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Void> streamResponse(ServerHttpResponse response) {
        //SSE：以 \n\n 分隔消息（推荐用 data: 格式）

        // return response.writeAndFlushWith(
        //         Flux.interval(Duration.ofSeconds(1))
        //                 // .take(5)
        //                 .map(i -> Flux.just(
        //                         response.bufferFactory().wrap((i + "\n").getBytes())
        //                         // response.bufferFactory().wrap(("data: Chunk " + i + "\n\n").getBytes())
        //                 )));

        Flux<Flux<DataBuffer>> chunks = Flux.interval(Duration.ofSeconds(1))
                .map(i ->
                        Flux.just(
                                response.bufferFactory().wrap((i + "\n").getBytes())
                        )
                );
        return response.writeAndFlushWith(chunks);
    }
}
