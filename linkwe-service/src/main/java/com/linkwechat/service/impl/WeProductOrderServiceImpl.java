package com.linkwechat.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.annotation.SynchRecord;
import com.linkwechat.common.constant.ProductOrderConstants;
import com.linkwechat.common.constant.SynchRecordConstants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.TrajectorySceneType;
import com.linkwechat.common.enums.TrajectoryType;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    @Resource
    private WeCustomerTrajectoryMapper weCustomerTrajectoryMapper;

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
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_SPTC_ORDER)
    public void orderSync() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(), rabbitMQSettingConfig.getWeProductOrderQu(), loginUser.getCorpId());
    }

    @Override
    public void orderSyncExecute(String corpId) {
        synchronized (WeProductOrderServiceImpl.class) {
            WeGetBillListQuery query = new WeGetBillListQuery();
            query.setCorpid(corpId);
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
                                //保存或更新订单
                                insertOrUpdateOrder(bill);
                            }
                        }
                        //迭代请求数据
                        if (StringUtils.isNotBlank(cursor)) {
                            query.setCursor(cursor);
                            getBillList(query);
                        }
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
    public void insertOrUpdateOrder(WeGetBillListVo.Bill bill) {
        String outTradeNo = bill.getOutTradeNo();
        LambdaQueryWrapper<WeProductOrder> orderQuery = new LambdaQueryWrapper<>();
        orderQuery.eq(WeProductOrder::getOrderNo, outTradeNo);
        orderQuery.eq(WeProductOrder::getDelFlag, 0);
        WeProductOrder order = weProductOrderMapper.selectOne(orderQuery);
        if (ObjectUtil.isNotEmpty(order)) {
            //修改订单数据
            updateProductOrder(bill, order);
        } else {
            //保存订单数据
            saveProductOrder(bill);
        }
    }

    /**
     * 修改订单数据
     *
     * @param bill
     */
    private void updateProductOrder(WeGetBillListVo.Bill bill, WeProductOrder productOrder) {

        //修改订单数据
        LambdaUpdateWrapper<WeProductOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(!bill.getTradeState().equals(productOrder.getOrderState()), WeProductOrder::getOrderState, bill.getTradeState());
        updateWrapper.set(!bill.getTotalRefundFee().equals(productOrder.getTotalRefundFee()), WeProductOrder::getTotalRefundFee, bill.getTotalRefundFee());
        updateWrapper.set(!bill.getContactInfo().getName().equals(productOrder.getContact()), WeProductOrder::getContact, bill.getContactInfo().getName());
        updateWrapper.set(!bill.getContactInfo().getPhone().equals(productOrder.getPhone()), WeProductOrder::getPhone, bill.getContactInfo().getPhone());
        updateWrapper.set(!bill.getContactInfo().getAddress().equals(productOrder.getAddress()), WeProductOrder::getAddress, bill.getContactInfo().getAddress());
        updateWrapper.set(WeProductOrder::getUpdateTime, new Date());
        updateWrapper.eq(WeProductOrder::getId, productOrder.getId());
        weProductOrderMapper.update(null, updateWrapper);

        //更新退款操作
        LambdaQueryWrapper<WeProductOrderRefund> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeProductOrderRefund::getOrderNo, bill.getOutTradeNo());
        queryWrapper.eq(WeProductOrderRefund::getDelFlag, 0);
        List<WeProductOrderRefund> weProductOrderRefunds = weProductOrderRefundMapper.selectList(queryWrapper);
        Map<String, WeProductOrderRefund> collect = null;
        if (weProductOrderRefunds != null && weProductOrderRefunds.size() > 0) {
            collect = weProductOrderRefunds.stream().collect(Collectors.toMap(WeProductOrderRefund::getRefundNo, Function.identity()));
        }
        List<WeGetBillListVo.Refund> refundList = bill.getRefundList();
        if (collect == null) {
            for (WeGetBillListVo.Refund refund : refundList) {
                WeProductOrderRefund weProductOrderRefund = new WeProductOrderRefund();
                weProductOrderRefund.setOrderNo(bill.getOutTradeNo());
                weProductOrderRefund.setRefundNo(refund.getOutRefundNo());
                weProductOrderRefund.setRefundUserId(refund.getRefundUserid());
                if (StringUtils.isNotBlank(productOrder.getWeUserName())) {
                    weProductOrderRefund.setRefundUserName(productOrder.getWeUserName());
                }
                weProductOrderRefund.setRemark(refund.getRefundComment());
                LocalDateTime of = LocalDateTime.ofInstant(Instant.ofEpochMilli(refund.getRefundReqtime() * 1000L), ZoneId.systemDefault());
                weProductOrderRefund.setRefundTime(of);
                weProductOrderRefund.setRefundState(refund.getRefundStatus());
                weProductOrderRefund.setRefundFee(refund.getRefundFee().toString());
                weProductOrderRefund.setCreateTime(LocalDateTime.now());
                weProductOrderRefund.setDelFlag(0);
                weProductOrderRefundMapper.insert(weProductOrderRefund);
            }
        } else {
            for (WeGetBillListVo.Refund refund : refundList) {
                WeProductOrderRefund orderRefund = collect.get(refund.getOutRefundNo());
                if (ObjectUtil.isEmpty(orderRefund)) {
                    WeProductOrderRefund weProductOrderRefund = new WeProductOrderRefund();
                    weProductOrderRefund.setOrderNo(bill.getOutTradeNo());
                    weProductOrderRefund.setRefundNo(refund.getOutRefundNo());
                    weProductOrderRefund.setRefundUserId(refund.getRefundUserid());
                    if (StringUtils.isNotBlank(productOrder.getWeUserName())) {
                        weProductOrderRefund.setRefundUserName(productOrder.getWeUserName());
                    }
                    weProductOrderRefund.setRemark(refund.getRefundComment());
                    LocalDateTime of = LocalDateTime.ofInstant(Instant.ofEpochMilli(refund.getRefundReqtime() * 1000L), ZoneId.systemDefault());
                    weProductOrderRefund.setRefundTime(of);
                    weProductOrderRefund.setRefundState(refund.getRefundStatus());
                    weProductOrderRefund.setRefundFee(refund.getRefundFee().toString());
                    weProductOrderRefund.setCreateTime(LocalDateTime.now());
                    weProductOrderRefund.setDelFlag(0);
                    weProductOrderRefundMapper.insert(weProductOrderRefund);
                }
            }
        }

        if (!bill.getTotalRefundFee().equals(productOrder.getTotalRefundFee())) {
            //更新退款总金额
            BigDecimal subtract = BigDecimal.valueOf(bill.getTotalRefundFee()).subtract(new BigDecimal(productOrder.getTotalRefundFee()));
            String todayRefundFeeStr = (String) redisTemplate.opsForValue().get(ProductOrderConstants.PRODUCT_ANALYZE_ORDER_REFUND_FEE);
            BigDecimal refundFee = new BigDecimal(todayRefundFeeStr).add(subtract);
            redisTemplate.opsForValue().set(ProductOrderConstants.PRODUCT_ANALYZE_ORDER_REFUND_FEE, refundFee.toString());
        }
    }


    /**
     * 保存订单数据
     *
     * @param bill
     */
    private void saveProductOrder(WeGetBillListVo.Bill bill) {
        WeProductOrder weProductOrder = new WeProductOrder();
        //订单基本信息
        weProductOrder.setMchNo(bill.getTransactionId());
        weProductOrder.setOrderState(bill.getTradeState());
        weProductOrder.setPayTime(DateUtil.date(bill.getPayTime() * 1000L));
        weProductOrder.setOrderNo(bill.getOutTradeNo());
        weProductOrder.setPaymentType(bill.getPaymentType());

        //商品信息
        WeProduct weProduct = null;
        List<WeGetBillListVo.Commodity> commodityList = bill.getCommodityList();
        if (commodityList != null && commodityList.size() > 0) {
            WeGetBillListVo.Commodity commodity = commodityList.get(0);
            String description = commodity.getDescription();
            //商品部包含商品编码，直接跳过，不入库
            if (!description.contains("商品编码：")) {
                return;
            }
            Integer amount = commodity.getAmount();
            String[] split = description.split("\\\n");
            String productSn = split[0].replace("商品编码：", "");
            LambdaQueryWrapper<WeProduct> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WeProduct::getProductSn, productSn);
            weProduct = weProductMapper.selectOne(queryWrapper);
            if (ObjectUtil.isEmpty(weProduct)) {
                return;
            } else {
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
        WeCustomer weCustomer = null;
        if (weCustomers != null && weCustomers.size() > 0) {
            weCustomer = weCustomers.get(0);
            weProductOrder.setExternalName(weCustomer.getCustomerName());
            weProductOrder.setExternalAvatar(weCustomer.getAvatar());
            weProductOrder.setExternalType(weCustomer.getCustomerType() == null ? 1 : weCustomer.getCustomerType());
        }

        //收款人企业内账号userid
        weProductOrder.setWeUserId(bill.getPayeeUserid());
        AjaxResult<SysUser> info = qwSysUserClient.getInfo(bill.getPayeeUserid());
        SysUser data = null;
        if (info.getCode() == 200) {
            data = info.getData();
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
        WeGetBillListVo.Contact contactInfo = bill.getContactInfo();
        if (ObjectUtil.isNotEmpty(contactInfo)) {
            weProductOrder.setContact(contactInfo.getName());
            weProductOrder.setPhone(contactInfo.getPhone());
            weProductOrder.setAddress(contactInfo.getAddress());
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
                if (StringUtils.isNotBlank(weProductOrder.getWeUserName())) {
                    weProductOrderRefund.setRefundUserName(weProductOrder.getWeUserName());
                }
                weProductOrderRefund.setRemark(refund.getRefundComment());
                LocalDateTime of = LocalDateTime.ofInstant(Instant.ofEpochMilli(refund.getRefundReqtime() * 1000L), ZoneId.systemDefault());
                weProductOrderRefund.setRefundTime(of);
                weProductOrderRefund.setRefundState(refund.getRefundStatus());
                weProductOrderRefund.setRefundFee(refund.getRefundFee().toString());
                refundTotalFee = refundTotalFee + refund.getRefundFee();
                weProductOrderRefund.setCreateTime(LocalDateTime.now());
                weProductOrderRefund.setDelFlag(0);
                weProductOrderRefundMapper.insert(weProductOrderRefund);
            }
        }
        weProductOrder.setCreateTime(new Date());
        weProductOrder.setUpdateTime(new Date());
        weProductOrder.setDelFlag(0);
        //保存订单
        int insert = weProductOrderMapper.insert(weProductOrder);

        //添加客户轨迹
        insertCustomerTrajectory(weCustomer, data, weProduct);

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
    }

    /**
     * 添加客户轨迹
     */
    private void insertCustomerTrajectory(WeCustomer weCustomer, SysUser user, WeProduct product) {
        //其中有一个为null，则不处理
        if (ObjectUtil.isEmpty(weCustomer) || ObjectUtil.isEmpty(user) || ObjectUtil.isEmpty(product)) {
            return;
        }
        log.info("添加客户轨迹");
        WeCustomerTrajectory weCustomerTrajectory = new WeCustomerTrajectory();
        //轨迹类型:(1:客户动态;2:员工动态;3:互动动态4:跟进动态5:客群动态)
        weCustomerTrajectory.setTrajectoryType(TrajectoryType.TRAJECTORY_TYPE_XXDT.getType());
        //轨迹场景类型，详细描述，见TrajectorySceneType
        weCustomerTrajectory.setTrajectorySceneType(TrajectorySceneType.TRAJECTORY_TITLE_BUY_GOODS.getType());
        //操作人类型:1:客户;2:员工;
        weCustomerTrajectory.setOperatorType(1);
        //操作人id
        weCustomerTrajectory.setOperatorId(weCustomer.getExternalUserid());
        //操作人姓名
        weCustomerTrajectory.setOperatorName(weCustomer.getCustomerName());
        //被操作对象类型:1:客户;2:员工:3:客群
        weCustomerTrajectory.setOperatoredObjectType(2);
        //被操作对象的id
        weCustomerTrajectory.setOperatoredObjectId(user.getWeUserId());
        //被操作对象名称
        weCustomerTrajectory.setOperatoredObjectName(user.getUserName());
        //客户id或群id，查询字段冗余,档该id不存在的时候代表
        weCustomerTrajectory.setExternalUseridOrChatid(weCustomer.getExternalUserid());
        //员工id，查询字段冗余
        weCustomerTrajectory.setWeUserId(user.getWeUserId());
        //动作
        weCustomerTrajectory.setAction(TrajectorySceneType.TRAJECTORY_TITLE_BUY_GOODS.getName());
        //标题
        weCustomerTrajectory.setTitle(TrajectoryType.TRAJECTORY_TYPE_XXDT.getName());
        //文案内容,整体内容
        String format = String.format(TrajectorySceneType.TRAJECTORY_TITLE_BUY_GOODS.getMsgTpl(), weCustomer.getCustomerName(), user.getUserName(), product.getProductSn());
        weCustomerTrajectory.setContent(format);
        weCustomerTrajectoryMapper.insert(weCustomerTrajectory);
    }


}
