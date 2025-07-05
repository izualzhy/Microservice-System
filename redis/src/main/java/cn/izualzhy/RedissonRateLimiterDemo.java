package cn.izualzhy;

import org.redisson.Redisson;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RedissonRateLimiterDemo {
    public static void main(String[] args) throws InterruptedException {
        RedissonClient redisson = RedissonManager.getClient();

        RRateLimiter limiter = redisson.getRateLimiter("limiter:demo");
        limiter.trySetRate(RateType.OVERALL, 5, 1, RateIntervalUnit.SECONDS); // 每秒限流 5 次

        ExecutorService executor = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            final int threadId = i;
            executor.submit(() -> {
                if (limiter.tryAcquire()) {
                    System.out.println("线程 " + threadId + " 获取许可成功");
                } else {
                    System.out.println("线程 " + threadId + " 被限流");
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        redisson.shutdown();
    }
}
