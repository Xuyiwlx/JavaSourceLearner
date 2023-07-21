package test.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/*
 * Created by wlx on 2022-03-20
 * 自定义互斥锁(代码不是很严谨,只是简单说明)
 */
public class MyLock implements Lock {

    // 静态内部-自定义同步器
    private static class MySync extends AbstractQueuedSynchronizer {

        // 独占式获取同步状态
        @Override
        protected boolean tryAcquire(int arg) {
            // 调用的AQS提供的方法,通过CAS保证原子性
            if (compareAndSetState(0,arg)) {
                // 我们实现的是互斥锁,所以标记获取到同步状态(更新state成功)的线程
                // 主要是为了判断是否可以重入
                setExclusiveOwnerThread(Thread.currentThread());
                // 获取同步状态成功,返回true
                return true;
            }
            return false;
        }

        // 独占式释放同步状态
        @Override
        protected boolean tryRelease(int arg) {
            // 未有锁却让释放,抛异常
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            // 可以释放,清空排他线程标记
            setExclusiveOwnerThread(null);
            // 设置同步状态为0,表示释放锁
            setState(0);
            return true;
        }

        // 当前同步器是否在独占模式下被线程使用
        // 一般该方法表示是否被当前线程所独占
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        // 后续会用到,主要用于等待/通知机制
        // 每个condition都有一个与之对应的条件等待队列
        Condition newCondition() {
            return new ConditionObject();
        }
    }

    // 聚合自定义同步器
    private final MySync mySync = new MySync();


    @Override
    public void lock() {
        // 阻塞式的获取锁,调用同步器模板方法,独占式获取同步状态
        mySync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

        // 调用同步器模板方法,可中断式获取同步状态
        mySync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        // 调用自己重写的方法,非阻塞式的获取同步状态
        return mySync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        // 调用同步器模板方法,可响应中断和超时时间限制
        return mySync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        // 释放锁
        mySync.release(1);

    }

    @Override
    public Condition newCondition() {
        // 使用自定义的条件
        return mySync.newCondition();
    }
}
