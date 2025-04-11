package cn.izualzhy.demo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/welcome")
public class WelcomeController {
    @Data
    @AllArgsConstructor
    class WelcomeMsg {
        public String name;
        public String msg;
    }
    @GetMapping("/{name}")
    public WelcomeMsg get(@PathVariable("name") String name) {
        return new WelcomeMsg(name, "welcome " + name);
    }
}
