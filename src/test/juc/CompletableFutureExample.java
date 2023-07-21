package test.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/*
 * Created by wlx on 2022-03-26
 */
public class CompletableFutureExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 创建一个CompletableFuture对象
        /*CompletableFuture<String> completableFuture = new CompletableFuture<>();

        completableFuture.complete("手动结束!");

        String result = completableFuture.get();

        System.out.println(result);*/

        // runAsync
        /*CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("运行在一个单独的线程之中!");
        });

        System.out.println(future.get());*/

        // supplyAsync
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("运行在一个单独的线程之中!");
            return "我有返回值!";
        });
        System.out.println(future.get());
    }
}
