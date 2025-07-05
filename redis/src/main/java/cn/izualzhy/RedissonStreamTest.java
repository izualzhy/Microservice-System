package cn.izualzhy;

import org.redisson.Redisson;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamAddArgs;
import org.redisson.api.stream.StreamCreateGroupArgs;
import org.redisson.api.stream.StreamReadGroupArgs;
import org.redisson.client.RedisBusyException;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RedissonStreamTest {
    private static final String STREAM_NAME = "mystream";
    private static final String GROUP_NAME = "groupA";

    public static void main(String[] args) throws Exception {
        // 初始化 Redisson
        RedissonClient redisson = RedissonManager.getClient();
        RStream<String, String> stream = redisson.getStream(STREAM_NAME);

        // 创建消费组（如果已存在则忽略）
        try {
            stream.createGroup(StreamCreateGroupArgs.name(GROUP_NAME).makeStream());
        } catch (RedisBusyException e) {
            System.out.println("消费组已存在，忽略创建");
        }

        // 模拟写入消息（可以用定时器或单独线程）
        ScheduledExecutorService producer = Executors.newScheduledThreadPool(1);
        producer.scheduleAtFixedRate(() -> {
            String id = UUID.randomUUID().toString();
            Map<String, String> message = Map.of("orderId", id, "amount", "99.9");
            stream.add(StreamAddArgs.entries(message));
            System.out.println("写入消息: " + message);
        }, 0, 1, TimeUnit.SECONDS);

        // 多线程消费者
        ExecutorService consumerPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            final String consumerName = "consumer-" + i;
            consumerPool.submit(() -> {
                while (true) {
                    try {
                        Map<StreamMessageId, Map<String, String>> messages =
                                stream.readGroup(GROUP_NAME, consumerName,
                                        StreamReadGroupArgs.neverDelivered().count(10)
                                                .timeout(Duration.ofSeconds(2)));

                        if (!messages.isEmpty()) {
                            for (Map.Entry<StreamMessageId, Map<String, String>> entry : messages.entrySet()) {
                                StreamMessageId id = entry.getKey();
                                Map<String, String> data = entry.getValue();

                                System.out.printf("[%s] 处理消息: id=%s data=%s%n", consumerName, id, data);

                                // 模拟处理逻辑
                                Thread.sleep(500);

                                // ack 确认
                                stream.ack(GROUP_NAME, id);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        // 程序运行一段时间后关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("正在关闭...");
            producer.shutdown();
            consumerPool.shutdownNow();
            redisson.shutdown();
        }));
    }
}
