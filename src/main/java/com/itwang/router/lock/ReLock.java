package com.itwang.router.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 可重入锁
 */
public class ReLock implements Lock {
    private InnerLock innerLock = new InnerLock();
    @Override
    public void lock() {
        innerLock.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        System.out.println("lock interrupptibly");
    }


    @Override
    public boolean tryLock() {
        return innerLock.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return innerLock.tryAcquireNanos(1, time);
    }

    @Override
    public void unlock() {
        innerLock.release(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    class InnerLock extends AbstractQueuedSynchronizer{

        @Override
        protected boolean tryAcquire(int acquires) {
            Thread exclusiveOwnerThread = getExclusiveOwnerThread();
            int state = getState();
            if(state == 0){
                boolean b = compareAndSetState(0, 1);
                // 成功抢占
                if(b){
                    System.out.println("sucesss get lock ============:"+Thread.currentThread().getName());
                    setExclusiveOwnerThread(Thread.currentThread());
                }
                return b;
            }else{
                if(exclusiveOwnerThread == Thread.currentThread()){
                    System.out.println("=====lock again=======");
                    setState(state + acquires);
                    return true;
                }
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            Thread exclusiveOwnerThread = getExclusiveOwnerThread();
            if(Thread.currentThread() != exclusiveOwnerThread){
                return false;
            }
            int state = getState()-arg;
            if(state == 0){
                setState(0);
                setExclusiveOwnerThread(null);
            }else{
                setState(state);
            }
            return true;

        }
    }
}
