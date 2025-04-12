package cn.izualzhy.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/{name}")
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
}