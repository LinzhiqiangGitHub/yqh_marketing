package com.yqh.marketing.basedevss.base.util;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static  void runThread(int threadCount, final Runnable run){
         /*
          * 比赛开始：只要裁判说开始，那么所有跑步选手就可以开始跑了
          * */


        /*
          * 每个队员跑到末尾时，则报告一个到达，所有人员都到达时，则比赛结束
          * */
        CountDownLatch end = new CountDownLatch(threadCount);

        ExecutorService exe = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            exe.execute(run);


            System.out.println(index);
            end.countDown();
        }
        System.out.println("开始");
        try {
            end.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println("结束");
        }


        //注意：此时main线程已经要结束了，但是exe线程如果不关闭是不会结束的
        exe.shutdown();
    }
}
