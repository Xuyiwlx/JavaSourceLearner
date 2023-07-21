package test.juc;

import java.util.concurrent.*;

/*
 * Created by wlx on 2022-03-25
 */
public class FutureAndCallableExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 使用Callable,可以获取返回值
        /*Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return null;
            }
        };*/

        // 使用Callable,可以获取返回值
        Callable<String> callable = ()->{
            System.out.println("进入 Callable 的 call 方法");
            // 模拟子线程任务,在此睡眠2秒
            //小细节:由于 call 方法会抛出Execption,这里不用像使用
            // Runnable 的 run 方法那样try/catch
            Thread.sleep(5000);
            return "Hello from Callable";
        };
        long startTime = System.nanoTime();
        System.out.println("提交 Callable 到线程池");
        Future<String> future = executorService.submit(callable);

        System.out.println("主线程继续执行");

        System.out.println("主线程等待获取 Future 结果");

        // 如果⼦线程没有结束，则睡眠 1s 重新检查
        while(!future.isDone()) {
            System.out.println("子线程任务还没结束");
            Thread.sleep(1000);

            double elapsedTimeInSec = (System.nanoTime() - startTime) / 1000000000.0;

            // 如果程序运行时间大于1s,则取消子线程的运行
            if (elapsedTimeInSec > 1) {
                future.cancel(true);
            }
        }

        if (!future.isCancelled()) {

            System.out.println("子线程任务已经完成!");

            String result = future.get();

            System.out.println("主线程获取到 Future 结果:"+result);
        } else {
            System.out.println("子线程任务被取消!");
        }



        executorService.shutdown();

    }
}
