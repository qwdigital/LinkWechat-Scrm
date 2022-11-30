package com.linkwechat.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.ProductOrderConstants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.*;
import com.linkwechat.domain.product.order.query.WeProductOrderQuery;
import com.linkwechat.domain.product.order.vo.WeProductOrderWareVo;
import com.linkwechat.domain.wecom.query.merchant.WeGetBillListQuery;
import com.linkwechat.domain.wecom.vo.merchant.WeGetBillListVo;
import com.linkwechat.fegin.QwMerchantClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.*;
import com.linkwechat.service.IWeProductOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * 商品订单表
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/21 18:20
 */
@Slf4j
@Service
public class WeProductOrderServiceImpl extends ServiceImpl<WeProductOrderMapper, WeProductOrder> implements IWeProductOrderService {

    @Resource
    private WeProductOrderMapper weProductOrderMapper;
    @Resource
    private WeCorpAccountMapper weCorpAccountMapper;
    @Resource
    private QwSysUserClient qwSysUserClient;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RabbitMQSettingConfig rabbitMQSettingConfig;
    @Resource
    private QwMerchantClient qwMerchantClient;
    @Resource
    private WeCustomerMapper weCustomerMapper;
    @Resource
    private WeProductOrderRefundMapper weProductOrderRefundMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private WeProductMapper weProductMapper;

    /**
     * 获取对外收款记录的游标
     */
    private static String cursor;

    @Override
    public List<WeProductOrderWareVo> list(WeProductOrderQuery query) {
        List<WeProductOrderWareVo> list = weProductOrderMapper.list(query);
        return list;
    }

    @Override
    public void orderSync() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(), rabbitMQSettingConfig.getWeProductOrderQu(), JSONObject.toJSONString(loginUser));
    }

    @Override
    public void orderSyncExecute(String msg) {
        WeGetBillListQuery query = new WeGetBillListQuery();
        long beginTime = DateUtil.offset(DateUtil.date(), DateField.DAY_OF_YEAR, -1).getTime();
        query.setBeginTime(beginTime);
        long endTime = DateUtil.date().getTime();
        query.setEndTime(endTime);
        if (StringUtils.isNotBlank(cursor)) {
            query.setCursor(cursor);
        }
        query.setLimit(100);
        //处理订单
        getBillList(query);
    }

    /**
     * 处理订单
     *
     * @param query
     */
    private void getBillList(WeGetBillListQuery query) {
        AjaxResult<WeGetBillListVo> result = qwMerchantClient.getBillList(query);
        if (result.getCode() == 200) {
            WeGetBillListVo data = result.getData();
            if (ObjectUtil.isNotEmpty(data)) {
                if (data.getErrCode() == 0) {
                    cursor = data.getNextCursor();
                    List<WeGetBillListVo.Bill> billList = data.getBillList();
                    if (billList != null && billList.size() > 0) {
                        for (WeGetBillListVo.Bill bill : billList) {
                            //只处理商品图册收款
                            if (bill.getPaymentType().equals(3)) {
                                //保存订单
                                insertOrder(bill);
                            }
                        }
                        //迭代请求数据
                        if (StringUtils.isNotBlank(cursor)) {
                            query.setCursor(cursor);
                        }
                        getBillList(query);
                    }
                }
            }
        }
    }

    /**
     * 添加订单
     *
     * @param bill
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public WeProductOrder insertOrder(WeGetBillListVo.Bill bill) {
        WeProductOrder weProductOrder = new WeProductOrder();
        //订单基本信息
        weProductOrder.setMchNo(bill.getTransactionId());
        weProductOrder.setOrderState(bill.getTradeState());
        weProductOrder.setPayTime(DateUtil.date(bill.getPayTime()));
        weProductOrder.setOrderNo(bill.getOutTradeNo());

        //商品信息
        List<WeGetBillListVo.Commodity> commodityList = bill.getCommodityList();
        if (commodityList != null && commodityList.size() > 0) {
            WeGetBillListVo.Commodity commodity = commodityList.get(0);
            String description = commodity.getDescription();
            //商品部包含商品编码，直接跳过，不入库
            if (!description.contains("商品编码：")) {
                return null;
            }
            Integer amount = commodity.getAmount();
            String[] split = description.split("\\\n");
            String productSn = split[0].replace("商品编码：", "");
            LambdaQueryWrapper<WeProduct> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WeProduct::getProductSn, productSn);
            WeProduct weProduct = weProductMapper.selectOne(queryWrapper);
            if (ObjectUtil.isNotEmpty(weProduct)) {
                weProductOrder.setProductId(weProduct.getId());
            }
            weProductOrder.setProductNum(amount);
        }

        //订单总金额
        weProductOrder.setTotalFee(bill.getTotalFee().toString());

        //客户信息
        weProductOrder.setExternalUserid(bill.getExternalUserid());
        LambdaQueryWrapper<WeCustomer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeCustomer::getExternalUserid, bill.getExternalUserid());
        queryWrapper.eq(WeCustomer::getDelFlag, 0);
        List<WeCustomer> weCustomers = weCustomerMapper.selectList(queryWrapper);
        if (weCustomers != null && weCustomers.size() > 0) {
            WeCustomer weCustomer = weCustomers.get(0);
            weProductOrder.setExternalName(weCustomer.getCustomerName());
            weProductOrder.setExternalAvatar(weCustomer.getAvatar());
            weProductOrder.setExternalType(weCustomer.getCustomerType() == null ? 1 : weCustomer.getCustomerType());
        }

        //收款人企业内账号userid
        weProductOrder.setWeUserId(bill.getPayeeUserid());
        AjaxResult<SysUser> info = qwSysUserClient.getInfo(bill.getPayeeUserid());
        if (info.getCode() == 200) {
            SysUser data = info.getData();
            if (ObjectUtil.isNotEmpty(data)) {
                weProductOrder.setWeUserName(data.getUserName());
            }
        }

        //商户信息
        weProductOrder.setMchId(bill.getMchId());
        LambdaQueryWrapper<WeCorpAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WeCorpAccount::getMerChantNumber, bill.getMchId());
        wrapper.eq(WeCorpAccount::getDelFlag, 0);
        WeCorpAccount weCorpAccount = weCorpAccountMapper.selectOne(wrapper);
        if (ObjectUtil.isNotEmpty(weCorpAccount)) {
            weProductOrder.setMchName(weCorpAccount.getCompanyName());
        }

        //订单地址信息
        WeGetBillListVo.Payer payerInfo = bill.getPayerInfo();
        if (ObjectUtil.isNotEmpty(payerInfo)) {
            weProductOrder.setContact(payerInfo.getName());
            weProductOrder.setPhone(payerInfo.getPhone());
            weProductOrder.setAddress(payerInfo.getAddress());
        }

        //退款信息
        Integer refundTotalFee = 0;
        List<WeGetBillListVo.Refund> refundList = bill.getRefundList();
        weProductOrder.setTotalRefundFee(bill.getTotalRefundFee().toString());
        if (refundList != null && refundList.size() > 0) {
            for (WeGetBillListVo.Refund refund : refundList) {
                WeProductOrderRefund weProductOrderRefund = new WeProductOrderRefund();
                weProductOrderRefund.setOrderNo(bill.getOutTradeNo());
                weProductOrderRefund.setRefundNo(refund.getOutRefundNo());
                weProductOrderRefund.setRefundUserId(refund.getRefundUserid());
                weProductOrderRefund.setRemark(refund.getRefundComment());
                LocalDateTime of = LocalDateTime.ofInstant(Instant.ofEpochMilli(refund.getRefundReqtime()), ZoneId.systemDefault());
                weProductOrderRefund.setRefundTime(of);
                weProductOrderRefund.setRefundState(refund.getRefundStatus());
                weProductOrderRefund.setRefundFee(refund.getRefundFee().toString());
                refundTotalFee = refundTotalFee + refund.getRefundFee();
                weProductOrderRefund.setCreateTime(LocalDateTime.now());
                weProductOrderRefundMapper.insert(weProductOrderRefund);
            }
        }
        weProductOrder.setCreateTime(new Date());
        weProductOrder.setUpdateTime(new Date());
        weProductOrder.setDelFlag(0);
        //保存订单
        int insert = weProductOrderMapper.insert(weProductOrder);

        //订单总数
        redisTemplate.opsForValue().increment(ProductOrderConstants.PRODUCT_ANALYZE_ORDER_NUMBER);
        //订单总金额
        String todayTotalFeeStr = (String) redisTemplate.opsForValue().get(ProductOrderConstants.PRODUCT_ANALYZE_ORDER_TOTAL_FEE);
        BigDecimal totalFee = new BigDecimal(todayTotalFeeStr).add(BigDecimal.valueOf(bill.getTotalFee()));
        redisTemplate.opsForValue().set(ProductOrderConstants.PRODUCT_ANALYZE_ORDER_TOTAL_FEE, totalFee.toString());
        //退款总金额
        String todayRefundFeeStr = (String) redisTemplate.opsForValue().get(ProductOrderConstants.PRODUCT_ANALYZE_ORDER_REFUND_FEE);
        BigDecimal refundFee = new BigDecimal(todayRefundFeeStr).add(BigDecimal.valueOf(refundTotalFee));
        redisTemplate.opsForValue().set(ProductOrderConstants.PRODUCT_ANALYZE_ORDER_REFUND_FEE, refundFee.toString());
        return weProductOrder;
    }

}
