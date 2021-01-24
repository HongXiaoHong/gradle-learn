package cn.gd.cz.hong.caculate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: <h1>日期计算</h1>
 * @author: 洪晓鸿
 * @time: 2021/1/24 11:42
 */

public class DateCaculate {
    public static void main(String[] args) {
        parse();
        threads();
    }

    /**
     * <h2>当提供的日期比匹配模式少的时候会出现异常</h2>
     */
    public static void parse() {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        try {
            System.out.println(sfd.parse("2000-10"));
        } catch (ParseException e) {
            e.printStackTrace();
            /*
            java.text.ParseException: Unparseable date: "2000-10"
                at java.base/java.text.DateFormat.parse(DateFormat.java:396)
                at cn.gd.cz.hong.caculate.DateCaculate.parse(DateCaculate.java:23)
                at cn.gd.cz.hong.caculate.DateCaculate.main(DateCaculate.java:14)
             */
        }
        try {
            System.out.println(sfd.parse("2000-10-01 10:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h2>SimpleDateFormat 多线程下会有问题</h2>
     *
     * <ol>
     *     <li>lampda表达式</li>
     *     <li>多线程池</li>
     * </ol>
     */
    private static void threads() {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2,
                1000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                try {
                    System.out.println(sfd.parse("2000-10-01 10:00:00"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
        /*
        Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task cn.gd.cz.hong.caculate.DateCaculate$$Lambda$21/0x0000000800bb0240@4783da3f rejected from java.util.concurrent.ThreadPoolExecutor@378fd1ac[Running, pool size = 2, active threads = 2, queued tasks = 0, completed tasks = 0]
            at java.base/java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2057)
            at java.base/java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:827)
            at java.base/java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1357)
            at cn.gd.cz.hong.caculate.DateCaculate.threads(DateCaculate.java:57)
            at cn.gd.cz.hong.caculate.DateCaculate.main(DateCaculate.java:19)
         */
    }
}
