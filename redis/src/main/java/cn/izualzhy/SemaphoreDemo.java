package cn.izualzhy;

import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    public static void main(String[] args) throws InterruptedException {
        RedissonClient redissonClient = RedissonManager.getClient();

        String semaphoreName = "demo:semaphore";

        // 1. 获取可过期信号量对象
        RPermitExpirableSemaphore semaphore = redissonClient.getPermitExpirableSemaphore(semaphoreName);

        // 2. 初始化许可总数为 3（只需初始化一次）
        boolean initSuccess = semaphore.trySetPermits(3);
        System.out.println("初始化是否成功: " + initSuccess);

        // 3. 获取一个许可（等待最多 5 秒，占用时间为 10 秒）
        String permitId = semaphore.tryAcquire(5, 60, TimeUnit.SECONDS);
        if (permitId != null) {
            System.out.println("成功获取 permitId: " + permitId);

            // 模拟处理业务
            Thread.sleep(30000);

            // 4. 释放许可
            semaphore.release(permitId);
            System.out.println("释放许可是否成功: ");
        } else {
            System.out.println("获取许可失败（超时或无可用许可）");
        }

        // 关闭 Redisson
        redissonClient.shutdown();
    }
}