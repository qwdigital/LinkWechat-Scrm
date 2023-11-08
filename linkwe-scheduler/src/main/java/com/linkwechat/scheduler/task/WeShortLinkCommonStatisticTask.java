package com.linkwechat.scheduler.task;

import cn.hutool.core.date.DateUtil;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.domain.WeCommonLinkStat;
import com.linkwechat.domain.WeShortLink;
import com.linkwechat.domain.WeShortLinkStat;
import com.linkwechat.service.IWeCommonLinkStatService;
import com.linkwechat.service.IWeShortLinkService;
import com.linkwechat.service.IWeShortLinkStatService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author danmo
 * @description 短链通用统计
 * @date 2023/1/09 14:39
 **/
@Slf4j
@Component
public class WeShortLinkCommonStatisticTask {
    @Autowired
    private RedisService redisService;

    @Autowired
    private IWeCommonLinkStatService weCommonLinkStatService;

    @Autowired
    private IWeShortLinkService weShortLinkService;

    @XxlJob("weCommonLinkStatisticTask")
    public void commonLinkStatisticTask() {
        String params = XxlJobHelper.getJobParam();
        Map<Long, WeCommonLinkStat> map = new HashMap<>();
        log.info("短链通用统计--------------------------start");
        Collection<String> keys = redisService.keys(WeConstans.WE_SHORT_LINK_COMMON_KEY + "*");
        for (String key : keys) {

            String[] split = key.split(":");
            String shortUrl = split[split.length -1];
            String prefix = split[split.length -2];
            long shortLinkId = Base62NumUtil.decode(shortUrl);
            WeCommonLinkStat shortLinkStat = map.get(shortLinkId);
            if (Objects.isNull(shortLinkStat)) {
                shortLinkStat = new WeCommonLinkStat();
                shortLinkStat.setDateTime(DateUtil.parseDate(DateUtil.today()));
            }
            shortLinkStat.setShortId(shortLinkId);
            shortLinkStat.setType(prefix);

            if (key.contains(WeConstans.PV)) {
                Integer pvNum = redisService.getCacheObject(key);
                shortLinkStat.setPvNum(pvNum);
            } else if (key.contains(WeConstans.UV)) {
                Long uvNum = redisService.hyperLogLogCount(key);
                shortLinkStat.setUvNum(uvNum.intValue());
            } else if (key.contains(WeConstans.OPEN_APPLET)) {
                Integer openNum = redisService.getCacheObject(key);
                shortLinkStat.setOpenNum(openNum);
            }
            map.put(shortLinkId, shortLinkStat);
        }
        boolean saveBatch = weCommonLinkStatService.saveBatch(new ArrayList<>(map.values()));
        if(saveBatch){
            redisService.deleteObject(keys);
        }
        log.info("短链通用统计--------------------------end");
    }
}
