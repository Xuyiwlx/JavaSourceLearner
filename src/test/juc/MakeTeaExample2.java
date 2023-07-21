package test.juc;

import java.util.concurrent.*;

/*
 * Created by wlx on 2022-03-25
 */
public class MakeTeaExample2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // 创建线程2的FutureTask
        FutureTask<String> ft2 = new FutureTask<>(new T2Task());

        // 创建线程1的FutureTask
        FutureTask<String> ft1 = new FutureTask<>(new T1Task(ft2));

        executorService.submit(ft1);

        executorService.submit(ft2);

        executorService.shutdown();
    }


    static class T1Task implements Callable<String> {

        private FutureTask<String> ft2;

        public T1Task(FutureTask<String> ft2) {
            this.ft2 = ft2;
        }

        @Override
        public String call() throws Exception {

            System.out.println("T1:洗水壶...");
            TimeUnit.SECONDS.sleep(1);

            System.out.println("T1:烧开水...");
            TimeUnit.SECONDS.sleep(15);

            String t2Result = ft2.get();

            System.out.println("T1:拿到T2的茶"+t2Result+",开始泡茶");

            return "T1:上茶!";
        }
    }

    static class T2Task implements Callable<String> {

        @Override
        public String call() throws Exception {

            System.out.println("T2:洗茶壶...");
            TimeUnit.SECONDS.sleep(1);

            System.out.println("T2:洗茶杯...");
            TimeUnit.SECONDS.sleep(2);

            System.out.println("T2:拿茶叶...");
            TimeUnit.SECONDS.sleep(1);

            return "T2:福鼎白茶了";
        }
    }
}
