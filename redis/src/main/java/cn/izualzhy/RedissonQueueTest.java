package cn.izualzhy;

import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedissonQueueTest {
    public static void main(String[] args) throws Exception {
        // Redisson 客户端配置
        RedissonClient redisson = RedissonManager.getClient();

        RBlockingQueue<String> queue = redisson.getBlockingQueue("queue:demo");

        // 先写入 10 条数据
        for (int i = 1; i <= 10; i++) {
            queue.add("任务-" + i);
        }

        // 启动 3 个线程消费队列（只消费一次）
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            final int threadId = i;
            pool.submit(() -> {
                while (true) {
                    try {
                        String task = queue.take();  // 阻塞式获取，消费即删除
                        System.out.println("线程 " + threadId + " 处理 " + task);
                        // 模拟处理耗时
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }

        // 等待一段时间后关闭（演示用）
        Thread.sleep(6000);
        pool.shutdownNow();
        redisson.shutdown();
    }
}
