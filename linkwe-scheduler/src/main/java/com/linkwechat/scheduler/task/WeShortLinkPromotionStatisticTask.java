package com.linkwechat.scheduler.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.domain.WeShortLinkPromotion;
import com.linkwechat.domain.WeShortLinkPromotionDayStat;
import com.linkwechat.service.IWeShortLinkPromotionDayStatService;
import com.linkwechat.service.IWeShortLinkPromotionService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 短链推广统计
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/17 16:28
 */
@Slf4j
@Component
public class WeShortLinkPromotionStatisticTask {
    @Resource
    private RedisService redisService;
    @Resource
    private IWeShortLinkPromotionService weShortLinkPromotionService;
    @Resource
    private IWeShortLinkPromotionDayStatService weShortLinkPromotionDayStatService;

    @XxlJob("shortLinkPromotionStatisticTask")
    public void shortLinkPromotionStatisticHandle() {
        Map<Long, WeShortLinkPromotionDayStat> map = new HashMap<>();

        log.info("短链推广统计--------------------------start");
        Collection<String> keys = redisService.keys(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + "*");
        for (String key : keys) {
            String promotionIdKey = key.substring(key.lastIndexOf(":") + 1);
            long promotionId = Base62NumUtil.decode(promotionIdKey);
            WeShortLinkPromotion weShortLinkPromotion = weShortLinkPromotionService.getById(promotionId);
            if (Objects.isNull(weShortLinkPromotion)) {
                continue;
            }
            WeShortLinkPromotionDayStat weShortLinkPromotionDayStat = map.get(promotionId);
            if (Objects.isNull(weShortLinkPromotionDayStat)) {
                weShortLinkPromotionDayStat = new WeShortLinkPromotionDayStat();
                DateTime dateTime = DateUtil.parseDate(DateUtil.today());
                String format = DateUtil.format(dateTime, "yyyy-MM-dd");
                weShortLinkPromotionDayStat.setStatDay(format);
            }
            weShortLinkPromotionDayStat.setPromotionId(promotionId);

            if (key.contains(WeConstans.PV)) {
                Integer pvNum = redisService.getCacheObject(key);
                weShortLinkPromotionDayStat.setPvNum(pvNum);
            } else if (key.contains(WeConstans.UV)) {
                Long uvNum = redisService.hyperLogLogCount(key);
                weShortLinkPromotionDayStat.setUvNum(uvNum.intValue());
            } else if (key.contains(WeConstans.OPEN_APPLET)) {
                Integer openNum = redisService.getCacheObject(key);
                weShortLinkPromotionDayStat.setOpenNum(openNum);
            }
            map.put(promotionId, weShortLinkPromotionDayStat);
        }
        boolean saveBatch = weShortLinkPromotionDayStatService.saveBatch(new ArrayList<>(map.values()));
        if (saveBatch) {
            redisService.deleteObject(keys);
        }
        log.info("短链推广统计--------------------------end");
    }
}
