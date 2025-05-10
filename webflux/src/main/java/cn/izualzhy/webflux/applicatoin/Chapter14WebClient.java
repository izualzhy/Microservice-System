package cn.izualzhy.webflux.applicatoin;

import cn.izualzhy.webflux.enumeration.SexEnum;
import cn.izualzhy.webflux.pojo.User;
import cn.izualzhy.webflux.vo.UserVo;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chapter14WebClient {
    public static void main(String[] args) {
        // 创建WebClient对象，并且设置请求基础路径
        WebClient client = WebClient.create("http://localhost:6081");
        // 一个新的用户
        User newUser = new User();
        newUser.setId(2L);
        newUser.setNote("note_new");
        newUser.setUserName("user_name_new");
        newUser.setSex(SexEnum.MALE);
        // 新增用户
        insertUser(client, newUser);
        // 获取用户
        getUser(client, 2L);
        User updUser = new User();
        updUser.setId(1L);
        updUser.setNote("note_update");
        updUser.setUserName("user_name_update");
        updUser.setSex(SexEnum.FEMALE);
        // 更新用户
        updateUser(client, updUser);
        // 查询用户
        findUsers(client, "user", "note");
        // 删除用户
        deleteUser(client, 2L);

    }

    private static void insertUser(WebClient client, User newUser) {
        // 注意，这只是定义一个时间，并不会发送请求
        Mono<UserVo> userMono =
            // 定义POST请求
            client.post()
                // 设置请求URI
                .uri("/user")
                // 请求体为JSON数据流
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                // 请求体内容
                .body(Mono.just(newUser), User.class)
                // 接收请求结果类型
                .accept(MediaType.APPLICATION_STREAM_JSON)
                // 设置请求结果检索规则
                .retrieve()
                // 将结果体转换为一个Mono封装的数据流
                .bodyToMono(UserVo.class);
        System.out.println("【用户ID】" + newUser.getId() + " 待请求");
        // 获取服务器发布的数据流，此时才会发送请求
        UserVo user = userMono.block();
        System.out.println("【用户名称】" + user.getUserName());
    }

    private static void getUser(WebClient client, Long id) {
        Mono<UserVo> userMono =
            // 定义GET请求
            client.get()
                // 定义请求URI和参数
                .uri("/user/{id}", id)
                // 接收请求结果类型
                .accept(MediaType.APPLICATION_STREAM_JSON)
                // 设置请求结果检索规则
                .retrieve()
                // 将结果体转换为一个Mono封装的数据流
                .bodyToMono(UserVo.class);
        System.out.println("【用户ID】" + id + " 待请求");
        // 获取服务器发布的数据流，此时才会发送请求
        UserVo user = userMono.block();
        System.out.println("【用户名称】" + user.getUserName());
    }

    private static void updateUser(WebClient client, User updUser) {
        Mono<UserVo> userMono =
            // 定义PUT请求
            client.put().uri("/user")
                // 请求体为JSON数据流
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                // 请求体内容
                .body(Mono.just(updUser), User.class)
                // 接收请求结果类型
                .accept(MediaType.APPLICATION_STREAM_JSON)
                // 设置请求结果检索规则
                .retrieve()
                // 将结果体转换为一个Mono封装的数据流
                .bodyToMono(UserVo.class);
        // 获取服务器发布的数据流，此时才会发送请求
        UserVo user = userMono.block();
        System.out.println("【用户名称】" + user.getUserName());
    }

    private static void findUsers(WebClient client, String userName, String note) {
        // 定义参数map
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userName", userName);
        paramMap.put("note", note);
        Flux<UserVo> userFlux =
            // 定义PUT请求，使用Map传递多个参数
            client.get().uri("/user/{userName}/{note}", paramMap)
                // 接收请求结果类型
                .accept(MediaType.APPLICATION_STREAM_JSON)
                // 设置请求结果检索规则
                .retrieve()
                // 将结果体转换为一个Mono封装的数据流
                .bodyToFlux(UserVo.class);

        // 通过Iterator遍历结果数据流，执行后服务器才会响应
        Iterator<UserVo> iterator = userFlux.toIterable().iterator();
        // 遍历
        while (iterator.hasNext()) {
            UserVo item = iterator.next();
            System.out.println("【用户名称】" + item.getUserName());
        }
    }

    private static void deleteUser(WebClient client, Long id) {
        Mono<Void> result =
            client.delete()
                // 设置请求URI
                .uri("/user/{id}", id)
                // 接收请求结果类型
                .accept(MediaType.APPLICATION_STREAM_JSON)
                // 设置请求结果检索规则
                .retrieve()
                // 将结果体转换为一个Mono封装的数据流
                .bodyToMono(Void.class);
        // 获取服务器发布的数据流，此时才会发送请求
        Void voidResult = result.block();
        System.out.println(voidResult);
    }
}