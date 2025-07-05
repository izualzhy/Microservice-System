package cn.izualzhy;

import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RedissonPermitSemaphoreDemo {
    public static void main(String[] args) throws InterruptedException {
        // 1. 初始化 Redisson 客户端（默认连接本地 Redis）
        RedissonClient redisson = RedissonManager.getClient();

        // 2. 获取/创建信号量对象，并设置最多允许 3 个线程同时执行
        RPermitExpirableSemaphore semaphore = redisson.getPermitExpirableSemaphore("semaphore:demo");

        System.out.println("------- first test ------");
        semaphore.trySetPermits(5); // 设置最大并发数为3
        ExecutorService executor = test(semaphore);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("------- second test ------");
        semaphore.setPermits(2); // 设置最大并发数为3
        executor = test(semaphore);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        redisson.shutdown();
    }

    private static ExecutorService test(RPermitExpirableSemaphore semaphore) {
        // 3. 创建一个固定线程池模拟并发
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            executor.submit(() -> {
                String permitId = null;
                try {
                    // 4. 尝试申请一个许可（最多等待2秒），使用期限是5秒
                    permitId = semaphore.tryAcquire(2, 5, TimeUnit.SECONDS);
                    if (permitId != null) {
                        System.out.println("线程 " + threadId + " 获取到许可：" + permitId);

                        // 模拟处理任务
                        Thread.sleep(1000); // 模拟执行任务1秒

                    } else {
                        System.out.println("线程 " + threadId + " 获取许可失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 5. 释放许可
                    if (permitId != null) {
                        semaphore.release(permitId);
                        System.out.println("线程 " + threadId + " 释放许可：");
                    }
                }
            });
        }
        return executor;
    }
}
