package cn.izualzhy.gateway.function;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class RequestRewriteFunction implements RewriteFunction<String, String> {
    @Override
    public Publisher<String> apply(ServerWebExchange serverWebExchange, String body) {
        String safeBody = (body != null) ? body : "";

        // body 可能为 null
        log.info("!!!!!! RequestRewriteFunction.apply() request: URI: {} path: {} queryParams: {} headers: {} statusCode: {} headers: {} body: {}",
                serverWebExchange.getRequest().getURI(),
                serverWebExchange.getRequest().getPath(),
                serverWebExchange.getRequest().getQueryParams(),
                serverWebExchange.getRequest().getHeaders(),
                serverWebExchange.getResponse().getStatusCode(),
                serverWebExchange.getResponse().getHeaders(),
                new String(safeBody));
        // 返回自定义错误信息
//        return Mono.just("{\"code\": 500, \"message\": \"服务异常\"}".getBytes());

        // 如果 body 既不是 String 也不是 byte[]，则原样返回
        return Mono.just(safeBody);
    }
}
