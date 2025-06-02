package cn.izualzhy;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LambdaSample {

    static class Student {
        String name;
        double score;
    }


    public static void main(String[] args) {
        LambdaSample lambdaSample = new LambdaSample();
        lambdaSample.testLambda();
    }

    private static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    void testLambda() {
        List<String> words = List.of("apple", "banana", "apricot", "blueberry", "avocado");

        // 1️⃣ 基本 Predicate —— 判断单词是否以 "a" 开头
        Predicate<String> startsWithA = s -> s.startsWith("a");

        // 2️⃣ 组合 Predicate —— 长度大于 6 的单词
        Predicate<String> lengthGreaterThan6 = s -> s.length() > 6;

        // 3️⃣ 使用 and 组合：以 a 开头 且 长度 > 6
        Predicate<String> startsWithAAndLong = startsWithA.and(lengthGreaterThan6);

        // 4️⃣ 使用 or 组合：以 a 开头 或 长度 > 6
        Predicate<String> startsWithAOrLong = startsWithA.or(lengthGreaterThan6);

        // 5️⃣ 使用 negate：不是以 a 开头
        Predicate<String> notStartsWithA = startsWithA.negate();

        // 过滤并打印结果
        System.out.println("以 a 开头的: " +
                filter(words, startsWithA));              // [apple, apricot, avocado]

        System.out.println("以 a 开头且长度>6 的: " +
                filter(words, startsWithAAndLong));       // [avocado]

        System.out.println("以 a 开头或长度>6 的: " +
                filter(words, startsWithAOrLong));        // [apple, banana, apricot, blueberry, avocado]

        System.out.println("不是以 a 开头的: " +
                filter(words, notStartsWithA));           // [banana, blueberry]
    }

    public static void testLocalVariable(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        String msg = "Hello";
        System.out.println("In main, msg: " + msg + " hashcode: " + System.identityHashCode(msg));

//        String msgArray[] = new String[1];
//        msgArray[0] = "Hello";

        executorService.submit(() -> {
            for (int i = 0; i < 60; i++) {
//                System.out.println("msgArray: " + msgArray + " readable: " + Arrays.toString(msgArray));
                System.out.println("In lambda, msg: " + msg + " hashcode: " + System.identityHashCode(msg));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

//        for (int i = 0; i < 10; i++) {
//            msgArray[0] = "Hello , i = " + i;
//            System.out.println("change msgArray to: " + msgArray + " readable: " + Arrays.toString(msgArray));
//            TimeUnit.SECONDS.sleep(1);
//        }
    }
}
