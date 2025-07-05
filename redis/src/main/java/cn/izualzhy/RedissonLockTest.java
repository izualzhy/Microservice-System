package cn.izualzhy;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RedissonLockTest {
    public static void main(String[] args) throws InterruptedException {
        RedissonClient redisson = RedissonManager.getClient();
        RLock lock = redisson.getLock("lock:demo");

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                        System.out.println("线程 " + threadId + " 获得锁");
                        Thread.sleep(2000); // 模拟业务处理
                    } else {
                        System.out.println("线程 " + threadId + " 获取锁失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        redisson.shutdown();
    }
}
