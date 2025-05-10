package cn.izualzhy.reactor.netty;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class SimpleHttpServer {
    public static void main(String[] args) {
        DisposableServer server = HttpServer.create()
                .port(6080)
                .route(routes ->
                        routes.get("/hello", (request, response) ->
                                response.sendString(Mono.just("Hello from Reactor Netty Server!"))
                        )
                )
                .route(routes ->
                        // The server will respond only on POST requests
                        // where the path starts with /test and then there is path parameter
                        routes.post("/test/{param}", (request, response) ->
                                response.sendString(request.receive()
                                        .asString()
                                        .map(s -> s + ' ' + request.param("param") + '!')
                                        .log("http-server"))))
                .bindNow();

        System.out.println("Server started on http://localhost:6080/hello");

        server.onDispose().block(); // 让 server 持续运行
    }
}
