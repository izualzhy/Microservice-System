package cn.izualzhy.gateway.function;

import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class ResponseRewriteFunction implements RewriteFunction<byte[], byte[]> {
    @Override
    public Publisher<byte[]> apply(ServerWebExchange serverWebExchange, byte[] body) {
        String contentType = serverWebExchange.getRequest().getHeaders().getFirst("Content-Type");
        if (contentType != null && contentType.contains("text/event-stream")) {
            // 不修改 SSE 请求，直接透传
            return Mono.just(body);
        }
        log.info("!!!!!! ResponseRewriteFunction.apply() request: URI: {} path: {} queryParams: {} headers: {} statusCode: {} headers: {} body: {}",
                serverWebExchange.getRequest().getURI(),
                serverWebExchange.getRequest().getPath(),
                serverWebExchange.getRequest().getQueryParams(),
                serverWebExchange.getRequest().getHeaders(),
                serverWebExchange.getResponse().getStatusCode(),
                serverWebExchange.getResponse().getHeaders(),
                new String(body));

        return Mono.just(body);
    }
}
