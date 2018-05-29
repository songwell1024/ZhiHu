package com.springboot.springboot;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by WilsonSong on 2018/05/28
 * 多线程测试
 */
public class MultiThreadTests {

    static Object object = new Object();
    //锁测试
    public static void synchronizedTest1(){
         synchronized (object){
             try{
                 for (int i = 0; i<5; i++){
                     Thread.sleep(1000);
                     System.out.println(String.format("T1%d",i));
                 }
             }catch (Exception e){
                 e.printStackTrace();
             }

         }
    }

    public static void synchronizedTest2(){
         //synchronized (new Object()){
        synchronized (object){
             try{
                 for (int i = 0; i<5; i++){
                     Thread.sleep(1000);
                     System.out.println(String.format("T2%d",i));
                 }
             }catch (Exception e){
                 e.printStackTrace();
             }

         }
    }

     public static void myThreadsTest(){
           for (int i = 0; i<2; ++i){
              new myThread(i).start();
           }
     }


    public static void  synchronizedTest(){
        for (int j=0; j<3;++j){
            new Thread(new Runnable() {
                @Override
                public void run() {
                     try {
                        synchronizedTest1();
                        synchronizedTest2();
                     }catch (Exception e){
                         e.printStackTrace();
                     }
                }
            }).start();
        }
    }


    public static void blockingDequeTest(){
       BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
       new Thread(new Producer(q)).start();
       new Thread(new Customer(q),"Customer1").start();
       new Thread(new Customer(q),"Customer2").start();
    }

    private static ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();
    static String AB = "AB";
    public static void ThreadLocalTest(){
        for (int i=0; i< AB.length()-1; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 2; ++i) {
                            stringThreadLocal.set(String.valueOf(AB.charAt(i)));
                            Thread.sleep(1000);
                            System.out.println("TheadLocal:" + stringThreadLocal.get());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //线程池
    public static void ExecutorTest(){
        //ExecutorService service = Executors.newSingleThreadExecutor();
        ExecutorService service = Executors.newFixedThreadPool(2);

        service.submit(new Runnable(){

            @Override
            public void run() {
                 try{
                for(int i = 0; i<10; ++i){
                    Thread.sleep(1000);
                    System.out.println("Executor1:" + i);
                }
            }catch (Exception e){
                     e.printStackTrace();
                 }
            }
        });


        service.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    for (int i = 0; i<10; ++i){
                        Thread.sleep(1000);
                        System.out.println("Executor2:" + i);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        service.shutdown();  //任务执行完之后关闭

        //轮询当前线程是否结束
        while(!service.isTerminated()){
            try{
                Thread.sleep(1000);
                System.out.println("Waited for termination");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    //原子型变量与基本类型变量的在多线程执行操作时的区别

    private static int counter  = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    //基本类型变量在多线程中是线程不安全的，就是两个线程同时访问一个变量的时候只能一个线程实现操作
    private  static  void testWithoutAtomic(){          //静态方法不能访问非静态的方法，但是非静态的方法是能够访问静态的变量的
        for (int i = 0; i <10; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(3000);
                        for (int j = 0; j<10; j++){
                            counter ++;
                            System.out.println(counter);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    //原子型数据
    private static void testWithAtomic(){
        for (int i = 0; i<10; ++i){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        for (int j = 0; j<10; ++j){
                            System.out.println(atomicInteger.incrementAndGet());  //原子型的数据自增，线程安全的
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //Future  等待异步的结果 阻塞的等待结果或者是捕获线程中的异常
    private static void testFuture(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception{
                Thread.sleep(1000);
                //return  1;
                throw new IllegalArgumentException("异常");

            }
        });

        service.shutdown();
        try{
             System.out.println(future.get());         //等待线程执行完成数据的操作之后再获取这个值
            //System.out.println(future.get(100,TimeUnit.MILLISECONDS));    //要求100ms执行完要不然报错
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] argd){
        //myThreadsTest();
        //synchronizedTest();
        //blockingDequeTest();
        //ThreadLocalTest();
        //ExecutorTest();
        //testWithAtomic();
        testFuture();
    }
}

class myThread extends Thread{
    private int tid;
    //char[] array = {'A','B'};

    public myThread(int tid){
        this.tid = tid;
    }

     @Override
     public void run(){
        try{

            for(int i =0; i< 10; ++i){
               Thread.sleep(1000);
               System.out.println(String.format("%d:%d",tid,i));
                //System.out.println((String.format("%d:%c",tid,array[i])));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
     }
}


//同步队列
class Customer implements Runnable{
    private BlockingQueue<String> q;
    public Customer(BlockingQueue<String> q) {
        this.q = q;
    }

    @Override
     public void run(){
          try{
            while (true){
                System.out.println(Thread.currentThread().getName() + ":" + q.take());
            }
          } catch (Exception e){
              e.printStackTrace();
          }
     }
}

class Producer implements Runnable{

    private BlockingQueue<String> q;
    public Producer(BlockingQueue<String> q ){
        this.q = q;
    }

    @Override
    public void run(){
        try{
            for(int i = 0; i<100;++i){
                Thread.sleep(10);
                q.put(String.valueOf(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


//////////////////////////////////////////////////////////////////////
//第二种方式
//public class MultiThreadTests {
//
//    public static void myThreadsTest(){
//          for (int i = 0; i<2; ++i){
//             // new myThread(i).start();
//          }
//
//          for (int i = 0; i< 2; i++){
//              new Thread(new Runnable() {
//                  @Override
//                  public void run() {
//                         try{
//                             for (int i =0; i<2; i++){
//                                Thread.sleep(1000);
//                                System.out.println(String.format("T2:%d",i));
//                             }
//                         }catch (Exception e){
//                             e.printStackTrace();
//                         }
//                  }
//              }).start();
//          }
//
//
//    }
//
//    public static void main(String[] argd){
//                myThreadsTest();
//    }
//}