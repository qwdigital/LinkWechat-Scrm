package com.linkwechat.common.utils.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 策略线程池
 *
 * @author danmo
 */
public class JourneyThreadExecutor {

    /**
     * 线程池保持ALIVE状态线程数
     */
    public static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    ;

    /**
     * 空闲线程回收时间
     */
    public static final int KEEP_ALIVE_TIME = 1000;

    /**
     * 线程池等待队列
     */
    public static final int BLOCKING_QUEUE_SIZE = 1000;

    private volatile static ThreadPoolExecutor executor;

    private JourneyThreadExecutor() {
    }

    ;

    // 获取单例的线程池对象
    public static ThreadPoolExecutor getInstance() {
        if (executor == null) {
            synchronized (JourneyThreadExecutor.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(CORE_POOL_SIZE,// 核心线程数
                            CORE_POOL_SIZE + 1, // 最大线程数
                            Integer.MAX_VALUE, // 闲置线程存活时间
                            TimeUnit.MILLISECONDS,// 时间单位
                            new LinkedBlockingDeque<Runnable>(Integer.MAX_VALUE),// 线程队列
                            new ThreadFactoryBuilder().setNameFormat("journey-pool-%d").build()
                    );
                }
            }
        }
        return executor;
    }

    public void execute(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        executor.execute(runnable);
    }

    // 从线程队列中移除对象
    public void cancel(Runnable runnable) {
        if (executor != null) {
            executor.getQueue().remove(runnable);
        }
    }

}
