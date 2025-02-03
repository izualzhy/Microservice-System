package cn.izualzhy.database.controller;

import cn.izualzhy.database.pojo.User;
import cn.izualzhy.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public String user(@PathVariable Long id) {
        return userService.getUser(id).toString();
    }

    @PostMapping("/user")
    public String user(@RequestBody User user) {
        return userService.insertUser(user) + "";
    }
}
