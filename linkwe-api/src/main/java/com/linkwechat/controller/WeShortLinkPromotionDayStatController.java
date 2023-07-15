package com.linkwechat.controller;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.domain.WeShortLinkPromotionDayStat;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionStatisticQuery;
import com.linkwechat.service.IWeShortLinkPromotionDayStatService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 短链推广每日统计 前端控制器
 * </p>
 *
 * @author WangYX
 * @since 2023-03-07
 */
@RestController
@RequestMapping("/short/link/promotion/day/stat")
public class WeShortLinkPromotionDayStatController extends BaseController {

    @Resource
    private RedisService redisService;
    @Resource
    private IWeShortLinkPromotionDayStatService weShortLinkPromotionDayStatService;


    /**
     * 统计
     *
     * @param promotionId 短链推广Id
     * @return
     */
    @GetMapping(value = {"/statistics/{promotionId}"})
    public AjaxResult statistics(@PathVariable("promotionId") Long promotionId) {

        String encode = Base62NumUtil.encode(promotionId);
        //今日PV数
        Integer tpv = redisService.getCacheObject(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.PV + encode);
        tpv = tpv == null ? 0 : tpv;
        //今日UV数
        Long tuv = redisService.hyperLogLogCount(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.UV + encode);
        //今日打开小程序数
        Integer topen = redisService.getCacheObject(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.OPEN_APPLET + encode);
        topen = topen == null ? 0 : topen;

        LambdaQueryWrapper<WeShortLinkPromotionDayStat> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeShortLinkPromotionDayStat::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionDayStat::getDelFlag, 0);
        List<WeShortLinkPromotionDayStat> list = weShortLinkPromotionDayStatService.list(queryWrapper);
        Map<String, Integer> result = new HashMap<>(9);
        result.put("pv", 0);
        result.put("uv", 0);
        result.put("open", 0);
        result.put("todayPv", tpv);
        result.put("comparedYesterdayPv", 0);
        result.put("todayUv", tuv.intValue());
        result.put("comparedYesterdayUv", 0);
        result.put("todayOpen", topen);
        result.put("comparedYesterdayOpen", 0);
        if (list != null && list.size() > 0) {
            //总数
            int pv = list.stream().mapToInt(WeShortLinkPromotionDayStat::getPvNum).sum();
            int uv = list.stream().mapToInt(WeShortLinkPromotionDayStat::getUvNum).sum();
            int open = list.stream().mapToInt(WeShortLinkPromotionDayStat::getOpenNum).sum();
            result.put("pv", pv + tpv);
            result.put("uv", uv + tuv.intValue());
            result.put("open", open + topen);

            //当天
            result.put("todayPv", tpv);
            result.put("todayUv", tuv.intValue());
            result.put("todayOpen", topen);

            //昨天
            String format = DateUtil.format(DateUtil.yesterday(), "yyyy-MM:dd");
            Integer finalTpv = tpv;
            Integer finalTopen = topen;
            list.stream().filter(i -> i.getStatDay().equals(format)).findFirst().ifPresent(yesData -> {
                Integer pvNum = yesData.getPvNum();
                Integer uvNum = yesData.getUvNum();
                Integer openNum = yesData.getOpenNum();
                result.put("comparedYesterdayPv", finalTpv - pvNum);
                result.put("comparedYesterdayUv", tuv.intValue() - uvNum);
                result.put("comparedYesterdayOpen", finalTopen - openNum);
            });
        } else {
            result.put("pv", tpv);
            result.put("uv", tuv.intValue());
            result.put("open", topen);
        }
        return AjaxResult.success(result);
    }

    /**
     * 折线图
     *
     * @return
     */
    @GetMapping("/statistics/line")
    public AjaxResult getLine(@Validated WeShortLinkPromotionStatisticQuery query) {

        if (StrUtil.isBlank(query.getBeginTime()) || StrUtil.isBlank(query.getEndTime())) {
            DateTime dateTime = DateUtil.offsetDay(new Date(), -6);
            String format = DateUtil.formatDate(dateTime);
            query.setBeginTime(format);
            query.setEndTime(DateUtil.today());
        }

        List<Integer> pvList = new LinkedList<>();
        List<Integer> uvList = new LinkedList<>();
        List<Integer> openList = new LinkedList<>();
        List<DateTime> timeList = DateUtil.rangeToList(DateUtil.parseDate(query.getBeginTime()), DateUtil.parseDate(query.getEndTime()), DateField.DAY_OF_YEAR);
        List<String> dateList = timeList.stream().map(DateTime::toDateStr).collect(Collectors.toList());

        String encode = Base62NumUtil.encode(query.getPromotionId());

        //今日PV数
        Integer tpv = redisService.getCacheObject(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.PV + encode);
        tpv = tpv == null ? 0 : tpv;
        //今日UV数
        Long tuv = redisService.hyperLogLogCount(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.UV + encode);
        //今日打开小程序数
        Integer topen = redisService.getCacheObject(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.OPEN_APPLET + encode);
        topen = topen == null ? 0 : topen;

        LambdaQueryWrapper<WeShortLinkPromotionDayStat> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeShortLinkPromotionDayStat::getPromotionId, query.getPromotionId());
        queryWrapper.eq(WeShortLinkPromotionDayStat::getDelFlag, 0);
        queryWrapper.between(WeShortLinkPromotionDayStat::getStatDay, query.getBeginTime(), query.getEndTime());
        List<WeShortLinkPromotionDayStat> list = weShortLinkPromotionDayStatService.list(queryWrapper);
        for (String date : dateList) {
            if (date.equals(DateUtil.today())) {
                pvList.add(tpv);
                uvList.add(tuv.intValue());
                openList.add(topen);
                break;
            }
            Optional<WeShortLinkPromotionDayStat> first = list.stream().filter(i -> i.getStatDay().equals(date)).findFirst();
            if (first.isPresent()) {
                WeShortLinkPromotionDayStat weShortLinkPromotionDayStat = first.get();
                pvList.add(weShortLinkPromotionDayStat.getPvNum());
                uvList.add(weShortLinkPromotionDayStat.getUvNum());
                openList.add(weShortLinkPromotionDayStat.getOpenNum());
            } else {
                pvList.add(0);
                uvList.add(0);
                openList.add(0);
            }
        }

        Map<String, Object> yData = new HashMap(3);
        yData.put("pv", pvList);
        yData.put("uv", uvList);
        yData.put("open", openList);

        Map<String, Object> result = new HashMap<>(2);
        result.put("xAxis", dateList);
        result.put("yAxis", yData);
        return AjaxResult.success(result);
    }
}

