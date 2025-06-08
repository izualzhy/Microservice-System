package cn.izualzhy.sentinel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/product")
    public String product() {
        return "product";
    }

    @GetMapping("/order")
    public String order() {
        return "order";
    }
}
