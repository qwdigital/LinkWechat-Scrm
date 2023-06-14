package com.linkwechat.scheduler.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.moments.dto.MomentsListDetailParamDto;
import com.linkwechat.domain.moments.dto.MomentsListDetailResultDto;
import com.linkwechat.fegin.QwMomentsClient;
import com.linkwechat.service.IWeMomentsTaskService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Resource
    private IWeMomentsTaskService weMomentsTaskService;

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
     * 朋友圈定时拉取任务
     */
    @XxlJob("weMomentPullTask")
    public void weMomentPullHandle() {
        //获取页面传递的参数
        String params = XxlJobHelper.getJobParam();

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
        weMomentsTaskService.getByMoment(null, moments, detailParamDto);
        weMomentsTaskService.syncMomentsDataHandle(moments);
    }

    @Data
    private static class WeMomentPullQuery {
        private Integer type = 2;
        private Long startTime;
        private Long endTime;
    }

}
