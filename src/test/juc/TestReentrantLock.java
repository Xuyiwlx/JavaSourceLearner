package test.juc;

import java.util.concurrent.locks.ReentrantLock;

/*
 * Created by wlx on 2020/3/28
 */
public class TestReentrantLock {

    private volatile static int i = 0 ;

    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        i++;
        System.out.println(i);
        lock.unlock();
    }
}
