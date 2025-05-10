package cn.izualzhy.webflux.repository;

import cn.izualzhy.webflux.pojo.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FakeUserRepository {
    private AtomicLong counter = new AtomicLong(1);

    public Mono<User> save(User user) {
        // 模拟数据库写入延迟，比如 1 秒
        return Mono.fromCallable(() -> {
                    user.setId(counter.getAndIncrement()); // 模拟生成 ID
                    System.out.println(System.currentTimeMillis() +  " Saving user: " + user.getUserName());
                    return user;
                })
                .delayElement(Duration.ofSeconds(1));  // 模拟延迟
    }
}
