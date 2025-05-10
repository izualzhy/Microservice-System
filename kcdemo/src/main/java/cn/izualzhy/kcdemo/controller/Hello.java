package cn.izualzhy.kcdemo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/hello")
public class Hello {
    @GetMapping("/world")
    public String hello() {
        return "hello";
    }
}
