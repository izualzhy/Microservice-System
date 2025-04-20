package cn.izualzhy.controller;

import cn.izualzhy.pojo.User;
import cn.izualzhy.service.MyBatisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MyBatisUserService myBatisUserService;

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID (路径变量)
     * @return 用户信息JSON
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        // 1. 参数校验
        if (userId == null || userId <= 0) {
            return ResponseEntity.badRequest().body("用户ID必须为正整数");
        }

        try {
            // 2. 调用服务层获取用户
            User user = myBatisUserService.getUser(userId);

            // 3. 处理查询结果
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            // 4. 异常处理
            return ResponseEntity.internalServerError()
                    .body("服务器错误: " + e.getMessage());
        }
    }
}
