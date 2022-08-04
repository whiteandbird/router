package com.itwang.router.lock;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class ThreadMain {
    public static void main(String[] args) throws InterruptedException {
        List<String> as = Collections.emptyList();
        System.out.println(as.size());


        int i = new BigDecimal("-1").compareTo(new BigDecimal(0));
        System.out.println(i);
        ReLock reLock = new ReLock();

        Thread a = new Thread(()->{
            try{
                System.out.println("return 1 start");
                reLock.lock();
                System.out.println("return 1 getLock");
                Thread.sleep(5000);
                reLock.lock();
                System.out.println("lock again");
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                reLock.unlock();
                reLock.unlock();
                System.out.println("reutrn 1 release lock");
            }

        });
        a.setName("aaaa");

        Thread b = new Thread(()->{
            try{
                System.out.println("return 2 start");
                reLock.lock();
                System.out.println("return 2 getLock");
                Thread.sleep(5000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                reLock.unlock();
                System.out.println("reutrn 2 release lock");
            }

        });
        b.setName("bbbbbbbbbbb");
        a.start();
        b.start();
        a.join();
        b.join();
    }
}
