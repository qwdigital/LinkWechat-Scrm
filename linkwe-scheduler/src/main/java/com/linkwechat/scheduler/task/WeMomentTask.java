package com.linkwechat.scheduler.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeMoments;
import com.linkwechat.domain.moments.dto.MomentsCreateResultDto;
import com.linkwechat.domain.moments.dto.MomentsListDetailParamDto;
import com.linkwechat.domain.moments.dto.MomentsListDetailResultDto;
import com.linkwechat.fegin.QwMomentsClient;
import com.linkwechat.service.IWeMomentsService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 朋友圈相关定时任务
 *
 * @author danmo
 * @date 2023/04/18 11:22
 **/

@Component
@Slf4j
public class WeMomentTask {


    @Resource
    private QwMomentsClient qwMomentsClient;

    @Autowired
    private IWeMomentsService weMomentsService;

    /**
     * 同步30天数据
     */
    public final Integer TIME_TYPE_THIRTY = 1;

    /**
     * 同步前一天数据
     */
    public final Integer PARAMS_TYPE_YESTERDAY = 2;

    /**
     * 同步自定义时间
     */
    public final Integer PARAMS_TYPE_CUSTOMIZE = 3;

    /**
     * 获取任务创建结果
     *
     * @param params
     */
    @XxlJob("weMomentResultTask")
    public ReturnT<String> weMomentResultHandle(String params) {

        XxlJobHelper.log("朋友圈获取任务创建结果>>>>>>>>>>>>>>>>>>> params:{}", params);

        ArrayList<Integer> status = ListUtil.toList(1, 2);

        //查询未创建及创建中的任务
        List<WeMoments> weMoments = weMomentsService.list(new LambdaQueryWrapper<WeMoments>().in(WeMoments::getStatus, status).eq(WeMoments::getDelFlag, 0));

        if (CollectionUtil.isNotEmpty(weMoments)) {
            for (WeMoments weMoment : weMoments) {
                if (Objects.isNull(weMoment.getJobId())) {
                    continue;
                }
                try {
                    MomentsCreateResultDto momentsCreateResult = qwMomentsClient.getMomentTaskResult(weMoment.getJobId()).getData();
                    if (Objects.nonNull(momentsCreateResult) && ObjectUtil.equal(momentsCreateResult.getErrCode(), 0)) {
                        weMoment.setStatus(momentsCreateResult.getStatus());
                        if (ObjectUtil.equal(3, momentsCreateResult.getStatus())) {
                            weMoment.setMomentId(momentsCreateResult.getResult().getMomentId());
                            //无效员工列表
                            MomentsCreateResultDto.WeMomentSendVo invalidSenderList = momentsCreateResult.getResult().getInvalidSenderList();
                            log.info("朋友圈获取任务创建结果>>>>>>>>>>>> 无效员工列表 senderList:{}", JSONObject.toJSONString(invalidSenderList));
                            //无效标签列表
                            MomentsCreateResultDto.WeMomentCustomerVo invalidExternalContactList = momentsCreateResult.getResult().getInvalidExternalContactList();
                            log.info("朋友圈获取任务创建结果>>>>>>>>>>>> 无效标签列表 tagList:{}", JSONObject.toJSONString(invalidExternalContactList));
                            weMomentsService.updateById(weMoment);
                        }
                    }
                } catch (Exception e) {
                    log.error("朋友圈获取任务创建结果>>>>>>>>>>>> 调用企微接口失败：jobId:{}", weMoment.getJobId(), e);
                }
            }
        }
        return ReturnT.SUCCESS;
    }


    /**
     * 朋友圈定时拉取任务
     *
     * @param params 入参
     */
    @XxlJob("weMomentPullTask")
    public ReturnT<String> weMomentPullHandle(String params) {
        XxlJobHelper.log("朋友圈定时拉取任务>>>>>>>>>>>>>>>>>>> params:{}", params);

        Long startTime = DateUtil.beginOfDay(new Date()).getTime() / 1000;
        Long endTime = DateUtil.date().getTime() / 1000;

        WeMomentPullQuery query = new WeMomentPullQuery();
        if (StringUtils.isNotEmpty(params)) {
            query = JSONObject.parseObject(params, WeMomentPullQuery.class);
        }
        //设置同步起始时间
        if (ObjectUtil.equal(TIME_TYPE_THIRTY, query.getType())) {
            startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -30)).getTime() / 1000;
            endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
        } else if (ObjectUtil.equal(PARAMS_TYPE_YESTERDAY, query.getType())) {
            startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
            endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -1)).getTime() / 1000;
        } else if (ObjectUtil.equal(PARAMS_TYPE_CUSTOMIZE, query.getType())) {
            startTime = query.getStartTime();
            endTime = query.getEndTime();
        }

        List<MomentsListDetailResultDto.Moment> moments = new ArrayList<>();
        MomentsListDetailParamDto detailParamDto = MomentsListDetailParamDto.builder().start_time(startTime).end_time(endTime).build();
        weMomentsService.getByMoment(null, moments, detailParamDto);
        weMomentsService.syncMentsDataHandle(moments);

        return ReturnT.SUCCESS;
    }

    @Data
    private static class WeMomentPullQuery {
        private Integer type = 2;
        private Long startTime;
        private Long endTime;
    }

}
