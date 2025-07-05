package cn.izualzhy;

import org.redisson.api.RHyperLogLog;
import org.redisson.api.RedissonClient;

public class RedissonHyperLogLogDemo {
    public static void main(String[] args) {
        // 创建 Redisson 客户端
        RedissonClient redisson = RedissonManager.getClient();

        RHyperLogLog<String> hyperLogLog = redisson.getHyperLogLog("myHyperLogLog");

        // 添加元素
        hyperLogLog.add("user1");
        hyperLogLog.add("user2");
        hyperLogLog.add("user1");  // 重复元素不会影响基数估计

        // 获取估算的基数（不重复计数）
        long count = hyperLogLog.count();
        System.out.println("估算的去重数量: " + count);  // 输出大约是 2

        redisson.shutdown();
    }
}
