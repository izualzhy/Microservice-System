package cn.izualzhy.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/{name}")
    public String get(@PathVariable("name") String name) {
        System.out.println("getUser name = " + name);
        return "hello , " + name + " user.";
    }
}
