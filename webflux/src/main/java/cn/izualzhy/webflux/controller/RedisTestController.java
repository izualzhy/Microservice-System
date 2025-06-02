package cn.izualzhy.webflux.controller;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.RedisClient;
import reactor.core.publisher.Mono;
import redis.clients.jedis.Jedis;

@RestController
public class RedisTestController {
    @GetMapping(value = "/test/redis")
    public void testRedis() {
        System.out.println("current thread: " + Thread.currentThread());

        try (Jedis jedis = new Jedis("localhost", 6379)) {
            jedis.auth("foobared");  // 认证密码
            String value = jedis.get("hello");  // 阻塞调用
            System.out.println("Got from Jedis: " + value +  " in thread: " + Thread.currentThread());
        }

        RedisClient client = RedisClient.create("redis://:foobared@localhost:6379");
        RedisReactiveCommands<String, String> reactiveCommands = client.connect().reactive();

        Mono<String> valueMono = reactiveCommands.get("hello");

        valueMono.subscribe(value -> System.out.println("Got from Reactive: " + value + " in thread: " + Thread.currentThread()));
    }
}
