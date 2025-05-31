package cn.izualzhy.webflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RestController
public class TestGatewayFilterChain {
    @GetMapping("/test/gatewayFilterChain")
    public String testGatewayFilterChain() {
        List<TestGlobalFilter> filters = Arrays.asList(
                new CFilter(),
                new AFilter(),
                new BFilter()
        );

        // 按 order 升序排列（请求阶段顺序）
        filters.sort((f1, f2) -> Integer.compare(f1.getOrder(), f2.getOrder()));

        GatewayFilterChain chain = new GatewayFilterChain(filters, 0);

        chain.filter("请求上下文").block();

        return "testGatewayFilterChain";
    }

    public interface TestGlobalFilter {
        Mono<Void> filter(String exchange, GatewayFilterChain chain);
        int getOrder();
    }

    public static class GatewayFilterChain {
        private final List<TestGlobalFilter> filters;
        private final int index;

        public GatewayFilterChain(List<TestGlobalFilter> filters, int index) {
            this.filters = filters;
            this.index = index;
        }

        public Mono<Void> filter(String exchange) {
            if (index >= filters.size()) {
                return Mono.empty();
            }
            TestGlobalFilter current = filters.get(index);
            GatewayFilterChain next = new GatewayFilterChain(filters, index + 1);
            return Mono.defer(() -> current.filter(exchange, next));
        }
    }

    public static class AFilter implements TestGlobalFilter {
        public int getOrder() {
            return 1;
        }

        public Mono<Void> filter(String exchange, GatewayFilterChain chain) {
            System.out.println("AFilter 请求阶段");
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> System.out.println("AFilter 响应阶段")));
        }
    }

    public static class BFilter implements TestGlobalFilter {
        public int getOrder() {
            return 2;
        }

        public Mono<Void> filter(String exchange, GatewayFilterChain chain) {
            System.out.println("BFilter 请求阶段");
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> System.out.println("BFilter 响应阶段")));
        }
    }

    public static class CFilter implements TestGlobalFilter {
        public int getOrder() {
            return 3;
        }

        public Mono<Void> filter(String exchange, GatewayFilterChain chain) {
            System.out.println("CFilter 请求阶段");
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> System.out.println("CFilter 响应阶段")));
        }
    }
}
