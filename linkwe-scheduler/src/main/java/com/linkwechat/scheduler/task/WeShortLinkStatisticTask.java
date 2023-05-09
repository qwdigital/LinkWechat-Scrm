package com.linkwechat.scheduler.task;

import cn.hutool.core.date.DateUtil;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.domain.WeShortLink;
import com.linkwechat.domain.WeShortLinkStat;
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
 * @description 短链统计
 * @date 2023/1/09 14:39
 **/
@Slf4j
@Component
public class WeShortLinkStatisticTask {
    @Autowired
    private RedisService redisService;

    @Autowired
    private IWeShortLinkStatService weShortLinkStatService;

    @Autowired
    private IWeShortLinkService weShortLinkService;

    @XxlJob("weShortLinkStatisticTask")
    public void shortLinkStatisticHandle() {
        String params = XxlJobHelper.getJobParam();
        Map<Long, WeShortLinkStat> map = new HashMap<>();
        log.info("短链统计--------------------------start");
        Collection<String> keys = redisService.keys(WeConstans.WE_SHORT_LINK_KEY + "*");
        for (String key : keys) {
            String shortUrl = key.substring(key.lastIndexOf(":") + 1);
            long shortLinkId = Base62NumUtil.decode(shortUrl);
            WeShortLink weShortLink = weShortLinkService.getById(shortLinkId);
            if(Objects.isNull(weShortLink)){
                continue;
            }
            WeShortLinkStat shortLinkStat = map.get(shortLinkId);
            if (Objects.isNull(shortLinkStat)) {
                shortLinkStat = new WeShortLinkStat();
                shortLinkStat.setDateTime(DateUtil.parseDate(DateUtil.today()));
            }
            shortLinkStat.setShortId(shortLinkId);

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
        boolean saveBatch = weShortLinkStatService.saveBatch(new ArrayList<>(map.values()));
        if(saveBatch){
            redisService.deleteObject(keys);
        }
        log.info("短链统计--------------------------end");
    }
}
