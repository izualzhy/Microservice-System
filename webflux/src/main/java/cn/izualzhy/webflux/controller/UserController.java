package cn.izualzhy.webflux.controller;

import cn.izualzhy.webflux.pojo.User;
import cn.izualzhy.webflux.service.UserService;
import cn.izualzhy.webflux.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// REST风格控制器
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // 获取用户
    @GetMapping("/user/{id}")
    public Mono<UserVo> getUser(@PathVariable Long id) {
        System.out.println("收到请求 getUser: " + id);
        return userService.getUser(id)
                // 从User对象转换为UserVo对象
                .map(u -> translate(u));
    }

    // 新增用户
    @PostMapping("/user")
    public Mono<UserVo> insertUser(@RequestBody User user) {
        return userService.insertUser(user)
                // 从User对象转换为UserVo对象
                .map(u -> translate(u));
    }

    // 更新用户
    @PutMapping("/user")
    public Mono<UserVo> updateUser(@RequestBody User user) {
        return userService.updateUser(user)
                // 从User对象转换为UserVo对象
                .map(u -> translate(u));
    }

    // 删除用户
    @DeleteMapping("/user/{id}")
    public Mono<Void> deleteUser(@PathVariable Long id) {
         return userService.deleteUser(id);
    }

    // 查询用户
    @GetMapping("/user/{userName}/{note}")
    public Flux<UserVo> findUsers(@PathVariable String userName, @PathVariable String note) {
        return userService.findUsers(userName, note)
                // 从User对象转换为UserVo对象
                .map(u -> translate(u));
    }

    /***
     * 完成PO到VO的转换
     * @param user ——PO 持久对象
     * @return UserVo ——VO 视图对象
     */
    private UserVo translate(User user) {
        UserVo userVo = new UserVo();
        userVo.setUserName(user.getUserName());
        userVo.setSexCode(user.getSex().getCode());
        userVo.setSexName(user.getSex().getName());
        userVo.setNote(user.getNote());
        userVo.setId(user.getId());
        return userVo;
    }

}