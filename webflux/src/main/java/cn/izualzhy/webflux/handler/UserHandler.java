package cn.izualzhy.webflux.handler;

import cn.izualzhy.webflux.pojo.User;
import cn.izualzhy.webflux.repository.FakeUserRepository;
import cn.izualzhy.webflux.repository.UserRepository;
import cn.izualzhy.webflux.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

@Service
public class UserHandler {

    @Autowired
    private UserRepository userRepository = null;
    @Autowired
    private FakeUserRepository fakeUserRepository = null;

    public Mono<ServerResponse> getUser(ServerRequest request) {
        // 获取请求URI参数
        String idStr = request.pathVariable("id");
        Long id = Long.valueOf(idStr);
        Mono<UserVo> userVoMono = userRepository.findById(id)
                // 转换为UserVo
                .map(u -> translate(u));
        return ServerResponse
                // 响应成功
                .ok()
                // 响应体类型
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                // 响应体
                .body(userVoMono, UserVo.class);
    }

    public Mono<ServerResponse> insertUser(ServerRequest request) {
        // Mono<User> userMonoParam = request.bodyToMono(User.class);
        // Mono<UserVo> userVoMono = userMonoParam
        //         // 缓存请求体
        //         .cache()
        //         // 处理业务逻辑，转变数据流
        //         .flatMap(user  ->userRepository.save(user)
        //                 // 转换为UserVo对象
        //                 .map(u->translate(u)));
        // return ServerResponse
        //         // 响应成功
        //         .ok()
        //         // 响应体类型
        //         .contentType(MediaType.APPLICATION_JSON_UTF8)
        //         // 响应体
        //         .body(userVoMono, UserVo.class);
        Flux<User> userFlux = request.bodyToFlux(User.class);

        System.out.println("userFlux:" + userFlux);
        Flux<UserVo> savedUsers = userFlux
                .concatMap(user -> fakeUserRepository.save(user))
                // .flatMap(user -> fakeUserRepository.save(user))
                // .flatMap(user -> userRepository.save(user))
                .map(u -> translate(u));

        System.out.println("savedUsers:" + savedUsers);

        Mono<ServerResponse> response = ServerResponse
                .ok()
                .contentType(APPLICATION_STREAM_JSON)  // 注意这里保持流式返回
                .body(savedUsers, UserVo.class);

        System.out.println("response:" + response);
        return response;
    }

    public Mono<ServerResponse> updateUser(ServerRequest request) {
        Mono<User> userMonoParam = request.bodyToMono(User.class);
        Mono<UserVo> userVoMono = userMonoParam.cache()
                .flatMap(user ->userRepository.save(user)
                        .map(u->translate(u)));
        return ServerResponse
                // 响应成功
                .ok()
                // 响应体类型
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                // 响应体
                .body(userVoMono, UserVo.class);
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        // 获取请求URI参数
        String idStr = request.pathVariable("id");
        Long id = Long.valueOf(idStr);
        Mono<Void> monoVoid = userRepository.deleteById(id);
        return ServerResponse
                // 响应成功
                .ok()
                // 响应体类型
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                // 响应体
                .body(monoVoid, Void.class);
    }

    public Mono<ServerResponse> findUsers(ServerRequest request) {
        String userName = request.pathVariable("userName");
        String note = request.pathVariable("note");
        Flux<UserVo> userVoFlux =
                userRepository.findByUserNameLikeAndNoteLike(userName, note)
                        .map(u -> translate(u));
        // 可参考getUser方法的注释
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(userVoFlux, UserVo.class);
    }

    public Mono<ServerResponse> updateUserName(ServerRequest request) {
        // 获取请求头数据
        String idStr = request.headers().header("id").get(0);
        Long id = Long.valueOf(idStr);
        String userName = request.headers().header("userName").get(0);
        // 获取原有用户信息
        Mono<User> userMono = userRepository.findById(id);
        User user = userMono.block();
        // 修改用户名
        user.setUserName(userName);
        Mono<UserVo> result = userRepository.save(user).map(u -> translate(u));
        // 响应结果
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(result, UserVo.class);
    }

    /***
     * 完成PO到VO的转换
     *
     * @param user PO 持久对象
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