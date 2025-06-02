package cn.izualzhy.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/{name}")
    public String get(@PathVariable("name") String name) {
        return "Test , " + name;
    }

    @GetMapping(value = "/slowApi")
    public String slowApi() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Slow API";
    }
    @GetMapping(value = "/pathD/helloFor5M")
    public String slowHello() {
        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Slow Hello";
    }

    // @PostMapping("/large")
    // public ResponseEntity<String> large() {
    //     StringBuilder sb = new StringBuilder();
    //     for (int i = 0; i < 100000; i++) {
    //         sb.append("data").append(i).append(",");
    //     }
    //     return ResponseEntity.ok(sb.toString());
    // }

}