package cn.izualzhy;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonManager {
    private static final RedissonClient redissonClient;

    static {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379") // 本地 Redis 地址
                .setDatabase(1)
                .setPassword("foobared");

        redissonClient = Redisson.create(config);
    }

    public static RedissonClient getClient() {
        return redissonClient;
    }
}
