package com.linkwechat.scheduler.task;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.ProductOrderConstants;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeProductDayStatistics;
import com.linkwechat.domain.WeProductStatistics;
import com.linkwechat.service.IWeProductDayStatisticsService;
import com.linkwechat.service.IWeProductStatisticsService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品订单统计数据
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/22 17:18
 */
@Slf4j
@Component
public class WeProductOrderStatisticsDataTask {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private IWeProductStatisticsService weProductStatisticsService;
    @Resource
    private IWeProductDayStatisticsService weProductDayStatisticsService;

    /**
     * 商品订单统计数据: 每天凌晨执行
     *
     */
    @XxlJob("weProductOrderStatisticsDataTask")
    @Transactional(rollbackFor = Exception.class)
    public void execute() {
        String params = XxlJobHelper.getJobParam();
        //今天订单总数
        Integer todayOrderNum = (Integer) redisTemplate.opsForValue().get(ProductOrderConstants.PRODUCT_ANALYZE_ORDER_NUMBER);
        //今天订单总额：分
        String todayTotalFeeStr = (String) redisTemplate.opsForValue().get(ProductOrderConstants.PRODUCT_ANALYZE_ORDER_TOTAL_FEE);
        //今天退款总额：分
        String todayRefundFeeStr = (String) redisTemplate.opsForValue().get(ProductOrderConstants.PRODUCT_ANALYZE_ORDER_REFUND_FEE);

        todayOrderNum = todayOrderNum == null ? 0 : todayOrderNum;
        todayTotalFeeStr = StringUtils.isBlank(todayTotalFeeStr) ? "0" : todayTotalFeeStr;
        todayRefundFeeStr = StringUtils.isBlank(todayRefundFeeStr) ? "0" : todayRefundFeeStr;

        //每日商品订单数据
        LambdaQueryWrapper<WeProductDayStatistics> query = new LambdaQueryWrapper<>();
        query.eq(WeProductDayStatistics::getDelFlag, 0);
        query.apply("date_format(create_time,'%Y-%m-%d') = date_format(now(),'%Y-%m-%d')");
        WeProductDayStatistics weProductDayStatistics = weProductDayStatisticsService.getOne(query);
        if (ObjectUtil.isNotEmpty(weProductDayStatistics)) {
            //修改数据
            weProductDayStatistics.setDayOrderTotalNum(weProductDayStatistics.getDayOrderTotalNum() + todayOrderNum);

            BigDecimal dayTotalFee = new BigDecimal(weProductDayStatistics.getDayOrderTotalFee()).add(new BigDecimal(todayTotalFeeStr));
            weProductDayStatistics.setDayOrderTotalFee(dayTotalFee.toString());

            BigDecimal dayRefundFee = new BigDecimal(weProductDayStatistics.getDayRefundTotalFee()).add(new BigDecimal(todayRefundFeeStr));
            weProductDayStatistics.setDayRefundTotalFee(dayRefundFee.toString());

            BigDecimal subtract = new BigDecimal(weProductDayStatistics.getDayOrderTotalFee()).subtract(new BigDecimal(weProductDayStatistics.getDayRefundTotalFee()));
            weProductDayStatistics.setDayNetIncome(subtract.toString());
            weProductDayStatistics.setUpdateTime(LocalDateTime.now());
            weProductDayStatisticsService.updateById(weProductDayStatistics);
        } else {
            //添加新数据
            weProductDayStatistics = new WeProductDayStatistics();
            weProductDayStatistics.setDayOrderTotalNum(todayOrderNum);
            weProductDayStatistics.setDayOrderTotalFee(todayTotalFeeStr);
            weProductDayStatistics.setDayRefundTotalFee(todayRefundFeeStr);
            BigDecimal subtract = new BigDecimal(weProductDayStatistics.getDayOrderTotalFee()).subtract(new BigDecimal(weProductDayStatistics.getDayRefundTotalFee()));
            weProductDayStatistics.setDayNetIncome(subtract.toString());
            weProductDayStatistics.setCreateTime(LocalDateTime.now());
            weProductDayStatistics.setUpdateTime(LocalDateTime.now());
            weProductDayStatisticsService.save(weProductDayStatistics);
        }

        //总的数据
        LambdaQueryWrapper<WeProductStatistics> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeProductStatistics::getDelFlag, 0);
        List<WeProductStatistics> list = weProductStatisticsService.list(queryWrapper);
        if (list != null && list.size() > 0) {
            //修改数据
            WeProductStatistics weProductStatistics = list.get(0);
            weProductStatistics.setOrderTotalNum(weProductStatistics.getOrderTotalNum() + todayOrderNum);

            BigDecimal dayTotalFee = new BigDecimal(weProductStatistics.getOrderTotalFee()).add(new BigDecimal(todayTotalFeeStr));
            weProductStatistics.setOrderTotalFee(dayTotalFee.toString());

            BigDecimal dayRefundFee = new BigDecimal(weProductStatistics.getRefundTotalFee()).add(new BigDecimal(todayRefundFeeStr));
            weProductStatistics.setRefundTotalFee(dayRefundFee.toString());

            BigDecimal subtract = new BigDecimal(weProductDayStatistics.getDayOrderTotalFee()).subtract(new BigDecimal(weProductDayStatistics.getDayRefundTotalFee()));
            weProductStatistics.setNetIncome(subtract.toString());
            weProductStatistics.setUpdateTime(LocalDateTime.now());
            weProductStatistics.setCreateTime(LocalDateTime.now());
            weProductStatisticsService.updateById(weProductStatistics);
        } else {
            //保存数据
            WeProductStatistics weProductStatistics = new WeProductStatistics();
            weProductStatistics.setOrderTotalNum(todayOrderNum);
            weProductStatistics.setOrderTotalFee(todayTotalFeeStr);
            weProductStatistics.setRefundTotalFee(todayRefundFeeStr);
            BigDecimal subtract = new BigDecimal(weProductStatistics.getOrderTotalFee()).subtract(new BigDecimal(weProductStatistics.getRefundTotalFee()));
            weProductStatistics.setNetIncome(subtract.toString());
            weProductStatistics.setCreateTime(LocalDateTime.now());
            weProductStatisticsService.save(weProductStatistics);
        }

        //统计完成之后，清空昨天的数据
        //今天订单总数
        redisTemplate.opsForValue().set(ProductOrderConstants.PRODUCT_ANALYZE_ORDER_NUMBER, 0);
        //今天订单总额：分
        redisTemplate.opsForValue().set(ProductOrderConstants.PRODUCT_ANALYZE_ORDER_TOTAL_FEE, "0");
        //今天退款总额：分
        redisTemplate.opsForValue().set(ProductOrderConstants.PRODUCT_ANALYZE_ORDER_REFUND_FEE, "0");
    }
}
