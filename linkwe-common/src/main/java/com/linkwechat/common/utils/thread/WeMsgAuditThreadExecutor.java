package com.linkwechat.common.utils.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 会话存档线程池
 *
 * @author danmo
 */
public class WeMsgAuditThreadExecutor {

    /**
     * 空闲线程回收时间
     */
    private static long KEEP_ALIVE_TIME = TimeUnit.SECONDS.toNanos(60);

    /**
     * 当线程阻塞（block）时的异常处理器，所谓线程阻塞即线程池和等待队列已满，无法处理线程时采取的策略
     */
    private RejectedExecutionHandler handler;

    private volatile static ThreadPoolExecutor executor;

    private WeMsgAuditThreadExecutor() {
    }

    ;

    // 获取单例的线程池对象
    public static ThreadPoolExecutor getInstance() {
        if (executor == null) {
            synchronized (WeMsgAuditThreadExecutor.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(5,// 核心线程数
                            10, // 最大线程数
                            KEEP_ALIVE_TIME, // 闲置线程存活时间
                            TimeUnit.NANOSECONDS,// 时间单位
                            new LinkedBlockingDeque<Runnable>(Integer.MAX_VALUE),// 线程队列
                            new ThreadFactoryBuilder().setNameFormat("caht-msg-audit-pool-%d").build(),
                            new ThreadPoolExecutor.AbortPolicy() //拒绝策觉
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
