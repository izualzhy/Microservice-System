package cn.izualzhy.reactor.netty;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;

public class SimpleHttpClient {
    public static void main(String[] args) {
        HttpClient client = HttpClient.create();

        String response = client.get()
            .uri("http://localhost:6080/hello")
            .responseSingle((res, bytes) -> bytes.asString())
            .block();

        HttpClient.create()             // Prepares an HTTP client ready for configuration
          .port(6080)  // Obtains the server's port and provides it as a port to which this
                                // client should connect
          .post()               // Specifies that POST method will be used
          .uri("/test/World")   // Specifies the path
          .send(ByteBufFlux.fromString(Flux.just("Hello")))  // Sends the request body
          .responseContent()    // Receives the response body
          .aggregate()
          .asString()
          .log("http-client")
          .block();

        System.out.println("Response from server: " + response);
    }
}
