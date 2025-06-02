package cn.izualzhy.purewebflux.handler;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FunctionPersonHandler {
    // 1.0创建线程池
    private static final ThreadPoolExecutor bizPoolExecutor = new
            ThreadPoolExecutor(8, 8, 1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(10));

    public Mono<ServerResponse> getPersonList(ServerRequest request) {
        System.out.println("getPersonList: " + Thread.currentThread().getName());
        // 1．根据request查找person列表
        Flux<String> personList = Flux.just("jiaduo", "zhailuxu", "guoheng")
                .publishOn(Schedulers.fromExecutor(bizPoolExecutor))// 1.1 切换到异步线程执行
                .map(e -> {// 1.2打印调用线程
                    System.out.println("getPersonList map: " + Thread.currentThread().getName());
                    return e;
                });
        // 2．返回查找结果
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).
                body(personList, String.class);
    }

    public Mono<ServerResponse> getPerson(ServerRequest request) {
        System.out.println("getPerson: " + Thread.currentThread().getName());
        // 1．根据request查找person,
        Mono<String> person = Mono.just("jiaduo");
        // 2．返回查找结果
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).
                body(person, String.class);
    }
}
