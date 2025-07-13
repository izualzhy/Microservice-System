package cn.izualzhy.reactor.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;

public class SpringCloudGatewayHttpClient {
    private static final String GATEWAY_URL = "http://localhost:6001/welcome/ying";
    private static final String POST_BODY = "{\"name\":\"ying\"}";
    public void testNettyRoutingFilter() {
        Mono<HttpClient> client = Mono.just(HttpClient.create())
                .flatMapMany(
                        httpClient -> httpClient.headers(
                                        headers -> headers.add("X-Custom-Req-Header", "testNettyRoutingFilter-req")
                                ).request(HttpMethod.valueOf("POST")).uri(GATEWAY_URL)
                                .send((req, nettyOutbound) -> {
                                    return nettyOutbound.send(
                                            Mono.just(POST_BODY).map(this::getByteBuf)
                                    );
                                }).responseConnection((res, connection) -> {
                                    res.responseHeaders().add("X-Custom-Resp-Header", "testNettyRoutingFilter-resp");
                                })
                );

    }

    private ByteBuf getByteBuf(String str) {
        return Unpooled.copiedBuffer(str, StandardCharsets.UTF_8);
    }


    public static void main(String[] args) {
    }
}
