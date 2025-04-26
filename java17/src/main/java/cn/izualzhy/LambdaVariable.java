package cn.izualzhy;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LambdaVariable {
    public static void main(String[] args) throws InterruptedException {
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
