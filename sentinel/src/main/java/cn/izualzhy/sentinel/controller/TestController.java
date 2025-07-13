package cn.izualzhy.sentinel.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/product")
    public String product() {
        log.info("enter product");
        return "product\n";
    }

    @GetMapping("/order")
    public String order() {
        log.info("enter order");
        return "order\n";
    }
}
