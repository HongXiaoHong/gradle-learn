### 研究如何使用spring的多线程池

ThreadPoolExecutor 参数
> 使用给定的初始参数创建一个新的ThreadPoolExecutor 。

参数：
  - corePoolSize –保留在池中的线程数，即使它们处于空闲状态，除非设置了allowCoreThreadTimeOut
  - maximumPoolSize –池中允许的最大线程数
  - keepAliveTime –当线程数大于内核数时，这是多余的空闲线程将在终止之前等待新任务的最长时间。
  - unit – keepAliveTime参数的时间单位
  - workQueue –在执行任务之前用于保留任务的队列。 此队列将仅保存execute方法提交的Runnable任务。
  - threadFactory –执行程序创建新线程时要使用的工厂
  - handler –因达到线程界限和队列容量而被阻止执行时使用的处理程序
  

### 学而时习之
> @Async

这个注解可以让我们异步执行任务
当然我们需要自己配置一个线程池到容器里面 而且需要加上 **@EnableAsync**

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 多线程池配置
 */
@Slf4j
@EnableAsync
@Configuration
public class ThreadExecutorConfig {

    /**
     * SpringBoot会优先使用名称为"taskExecutor"的线程池。
     * 如果没有找到，才会使用其他类型为TaskExecutor或其子类的线程池。
     * @return
     */
    @Bean
    public Executor taskExecutor() {
        log.info("start taskExecutor");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(5);
        // 设置最大线程数
        executor.setMaxPoolSize(10);
        // 设置队列容量
        executor.setQueueCapacity(20);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");

        // 设置拒绝策略
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 执行初始化
        executor.initialize();

        return executor;
    }

}
```

```java
import com.piao.sys.sysconfig.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DemoServiceImpl implements DemoService {

    @Async
    @Override
    public void getDemo() {
        log.info("this is Thread demo");
    }

}
```


继承线程池 打印线程池当时的情况，
有多少线程在执行，多少在队列中等待呢？
这里我创建了一个ThreadPoolTaskExecutor的子类，
在每次提交线程的时候都会将当前线程池的运行状况打印出来

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 显示线程执行情况：任务总数、已完成数、活跃线程数，队列大小信息
 */
@Slf4j
public class VisiableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private void showThreadPoolInfo(String prefix){
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
        if(null == threadPoolExecutor){
            return;
        }

        log.info("{}{},taskCount [{}], completedTaskCount [{}], activeCount [{}], queueSize [{}]",
                this.getThreadNamePrefix(),
                prefix,
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getQueue().size());
    }

    @Override
    public void execute(Runnable task) {
        showThreadPoolInfo("1. do execute");
        super.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        showThreadPoolInfo("2. do execute");
        super.execute(task, startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        showThreadPoolInfo("1. do submit");
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        showThreadPoolInfo("2. do submit");
        return super.submit(task);
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        showThreadPoolInfo("1. do submitListenable");
        return super.submitListenable(task);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        showThreadPoolInfo("2. do submitListenable");
        return super.submitListenable(task);
    }

}
```


### 参考
[SpringBoot + ThreadPoolExecutor多线程配置和使用](https://my.oschina.net/piaoxianren/blog/4521604)