package com.linkwechat.wecom.aspectj;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.annotation.SynchRecord;
import com.linkwechat.wecom.domain.WeSynchRecord;
import com.linkwechat.wecom.service.IWeSynchRecordService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;


/**
 * 回调接口调用记录
 */
@Aspect
@Component
@SuppressWarnings({"unused"})
public class SynchRecordAop {

    @Autowired
    private IWeSynchRecordService iWeSynchRecordService;


    @Pointcut("@annotation(com.linkwechat.wecom.annotation.SynchRecord)")
    public void annotationPointcut() {

    }

    @Around("annotationPointcut()")
    public void doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        SynchRecord annotationLog = getAnnotationLog(joinPoint);
        if(null != annotationLog){
            List<WeSynchRecord> weSynchRecords = iWeSynchRecordService.list(new LambdaQueryWrapper<WeSynchRecord>()
                    .apply("date_format (synch_time,'%Y-%m-%d HH:mm:ss') >= date_format('" + DateUtils.getBeforeByHourTime(2) + "','%Y-%m-%d HH:mm:ss')")
                    .apply("date_format (synch_time,'%Y-%m-%d HH:mm:ss') <= date_format('" + DateUtils.getBeforeByHourTime(0) + "','%Y-%m-%d HH:mm:ss')"));
            if(CollectionUtil.isEmpty(weSynchRecords)||weSynchRecords.size()<=1){
                 joinPoint.proceed();
            }

        }
    }

    @After("annotationPointcut()")
    public void doAfter(JoinPoint joinPoint) throws Throwable {

        SynchRecord annotationLog = getAnnotationLog(joinPoint);

        if(null != annotationLog){
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
    private SynchRecord getAnnotationLog(JoinPoint joinPoint) throws Exception
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(SynchRecord.class);
        }
        return null;
    }





}
