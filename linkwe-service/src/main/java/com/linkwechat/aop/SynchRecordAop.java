package com.linkwechat.aop;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.annotation.SynchRecord;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.domain.WeSynchRecord;
import com.linkwechat.service.IWeSynchRecordService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * 数据同步记录
 */
@Aspect
@Component
public class SynchRecordAop {

    @Autowired
    private IWeSynchRecordService iWeSynchRecordService;

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;


    @Pointcut("@annotation(com.linkwechat.common.annotation.SynchRecord)")
    public void annotationPointcut() {

    }

    @Before("annotationPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        SynchRecord annotationLog = getAnnotationLog(joinPoint);
        if (null != annotationLog) {
            List<WeSynchRecord> weSynchRecords = iWeSynchRecordService.list(new LambdaQueryWrapper<WeSynchRecord>()
                    .eq(WeSynchRecord::getSynchType, annotationLog.synchType())
                    .between(WeSynchRecord::getSynchTime, DateUtils.getBeforeByMinute(linkWeChatConfig.getDataSynchInterval())
                            , DateUtils.getBeforeByHourTime(0)));
            if (CollectionUtil.isNotEmpty(weSynchRecords)) {
                throw new CustomException("由于企业微信开放平台的限制，" + linkWeChatConfig.getDataSynchInterval() + "分钟内不得重复同步操作",-1);
            }

        }
    }

    @After("annotationPointcut()")
    public void doAfter(JoinPoint joinPoint) {

        SynchRecord annotationLog = getAnnotationLog(joinPoint);

        if (null != annotationLog) {
            iWeSynchRecordService.save(
                    WeSynchRecord.builder()
                            .synchTime(new Date())
                            .synchType(annotationLog.synchType())
                            .build()
            );
        }
    }


    /**
     * 是否存在注解，如果存在就获取
     */
    private SynchRecord getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(SynchRecord.class);
        }
        return null;
    }


}
