package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.protobuf.ServiceException;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeProduct;
import com.linkwechat.domain.WeProductOrder;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.product.order.query.WeProductOrderQuery;
import com.linkwechat.domain.product.order.vo.WeProductOrderWareVo;
import com.linkwechat.domain.product.product.query.WeAddProductQuery;
import com.linkwechat.domain.product.product.query.WeProductLineChartQuery;
import com.linkwechat.domain.product.product.query.WeProductQuery;
import com.linkwechat.domain.product.product.vo.WeProductListVo;
import com.linkwechat.domain.product.product.vo.WeProductStatisticsVo;
import com.linkwechat.domain.product.product.vo.WeProductVo;
import com.linkwechat.domain.product.product.vo.WeUserOrderTop5Vo;
import com.linkwechat.domain.product.refund.vo.WeProductOrderRefundVo;
import com.linkwechat.domain.wecom.query.product.QwAddProductQuery;
import com.linkwechat.domain.wecom.query.product.QwProductListQuery;
import com.linkwechat.domain.wecom.query.product.QwProductQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwAddProductVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductListVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductVo;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.fegin.QwProductAlbumClient;
import com.linkwechat.mapper.WeProductMapper;
import com.linkwechat.mapper.WeProductOrderMapper;
import com.linkwechat.service.IWeMaterialService;
import com.linkwechat.service.IWeProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * 商品信息表(WeProduct)
 *
 * @author danmo
 * @since 2022-09-30 11:36:06
 */
@Slf4j
@Service
public class WeProductServiceImpl extends ServiceImpl<WeProductMapper, WeProduct> implements IWeProductService {

    @Resource
    private QwProductAlbumClient qwProductAlbumClient;

    @Autowired
    private IWeMaterialService iWeMaterialService;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private WeProductOrderMapper weProductOrderMapper;

    @Override
    public void addProduct(WeAddProductQuery query) throws ServiceException {

        List<WeMessageTemplate> accessory = new ArrayList<>();
        //封面
        String picture = query.getPicture();
        WeMessageTemplate weMessageTemplate = new WeMessageTemplate();
        weMessageTemplate.setMsgType(MessageType.IMAGE.getMessageType());
        weMessageTemplate.setPicUrl(picture);
        if (StringUtils.isEmpty(weMessageTemplate.getMediaId())) {
            String mediaId = iWeMaterialService.uploadAttachmentMaterial(weMessageTemplate.getPicUrl(), MediaType.IMAGE.getMediaType(), 2, SnowFlakeUtil.nextId().toString()).getMediaId();
            if (StringUtils.isNotEmpty(mediaId)) {
                weMessageTemplate.setMediaId(mediaId);
                accessory.add(weMessageTemplate);
            } else {
                throw new WeComException(12003, "获取附件素材ID失败");
            }
        }

        //图片处理
        if (StringUtils.isNotBlank(query.getAttachments())) {
            String[] split = query.getAttachments().split(",");
            if (split.length > 8) {
                throw new ServiceException("最多添加八张详情！");
            }
            //获取附件媒体ID
            for (String img : split) {
                WeMessageTemplate messageTemplate = new WeMessageTemplate();
                messageTemplate.setMsgType(MessageType.IMAGE.getMessageType());
                messageTemplate.setPicUrl(img);
                if (StringUtils.isEmpty(messageTemplate.getMediaId())) {
                    String mediaId = iWeMaterialService.uploadAttachmentMaterial(messageTemplate.getPicUrl(), MediaType.IMAGE.getMediaType(), 2, SnowFlakeUtil.nextId().toString()).getMediaId();
                    if (StringUtils.isNotEmpty(mediaId)) {
                        messageTemplate.setMediaId(mediaId);
                    } else {
                        throw new WeComException(12003, "获取附件素材ID失败");
                    }
                }
                accessory.add(messageTemplate);
            }
        }
        query.setAccessory(accessory);
        query.setProductSn(generateProductSn());

        QwAddProductQuery productQuery = query.convert2Qw();
        //商品描述处理
        String productSn = "商品编码：" + productQuery.getProduct_sn() + "\n";
        String productDesc = "商品描述：" + productQuery.getDescription();
        productQuery.setDescription(productSn + productDesc);

        QwAddProductVo productResult = qwProductAlbumClient.addProduct(productQuery).getData();
        if (Objects.isNull(productResult)) {
            throw new WeComException(12001, "新增商品失败");
        }
        if (!Objects.equals(productResult.getErrCode(), WeErrorCodeEnum.ERROR_CODE_0.getErrorCode())) {
            throw new WeComException(12002, "新增商品失败");
        }

        WeProduct product = new WeProduct();
        product.setProductId(productResult.getProductId());
        product.setPicture(query.getPicture());
        product.setPrice(productQuery.getPrice().toString());
        product.setDescribe(query.getDescribe());
        product.setProductSn(query.getProductSn());
        product.setAttachments(query.getAttachments());
        save(product);
    }

    @Override
    public void updateProduct(Long id, WeAddProductQuery query) throws ServiceException {
        WeProduct product = getById(id);

        List<WeMessageTemplate> accessory = new ArrayList<>();

        //封面
        String picture = query.getPicture();
        WeMessageTemplate messageTemplate = new WeMessageTemplate();
        messageTemplate.setMsgType(MessageType.IMAGE.getMessageType());
        messageTemplate.setPicUrl(picture);
        if (StringUtils.isEmpty(messageTemplate.getMediaId())) {
            String mediaId = iWeMaterialService.uploadAttachmentMaterial(messageTemplate.getPicUrl(), MediaType.IMAGE.getMediaType(), 2, SnowFlakeUtil.nextId().toString()).getMediaId();
            if (StringUtils.isNotEmpty(mediaId)) {
                messageTemplate.setMediaId(mediaId);
                accessory.add(messageTemplate);
            } else {
                throw new WeComException(12003, "获取附件素材ID失败");
            }
        }
        //商品图片
        if (StringUtils.isNotBlank(query.getAttachments())) {
            String[] split = query.getAttachments().split(",");
            if (split.length > 8) {
                throw new ServiceException("最多添加八张详情！");
            }
            //获取附件媒体ID
            for (String img : split) {
                WeMessageTemplate weMessageTemplate = new WeMessageTemplate();
                weMessageTemplate.setMsgType(MessageType.IMAGE.getMessageType());
                weMessageTemplate.setPicUrl(img);
                if (StringUtils.isEmpty(weMessageTemplate.getMediaId())) {
                    String mediaId = iWeMaterialService.uploadAttachmentMaterial(weMessageTemplate.getPicUrl(), MediaType.IMAGE.getMediaType(), 2, SnowFlakeUtil.nextId().toString()).getMediaId();
                    if (StringUtils.isNotEmpty(mediaId)) {
                        weMessageTemplate.setMediaId(mediaId);
                    } else {
                        throw new WeComException(12003, "获取附件素材ID失败");
                    }
                }
                accessory.add(weMessageTemplate);
            }
            query.setAccessory(accessory);
        }

        QwAddProductQuery productQuery = query.convert2Qw();
        productQuery.setProduct_id(product.getProductId());
        if (StringUtils.isNotBlank(query.getDescribe())) {
            //商品描述处理
            String productSn = "商品编码：" + product.getProductSn() + "\n";
            String productDesc = "商品描述：" + productQuery.getDescription();
            productQuery.setDescription(productSn + productDesc);
        }

        WeResultVo productResult = qwProductAlbumClient.updateProductAlbum(productQuery).getData();
        if (Objects.isNull(productResult)) {
            throw new WeComException(12011, "修改商品失败");
        }
        if (!Objects.equals(productResult.getErrCode(), WeErrorCodeEnum.ERROR_CODE_0.getErrorCode())) {
            throw new WeComException(12012, "修改商品失败");
        }
        product.setPicture(query.getPicture());
        product.setPrice(productQuery.getPrice().toString());
        product.setDescribe(query.getDescribe());
        product.setAttachments(query.getAttachments());
        updateById(product);
    }

    @Override
    public void delProduct(List<Long> ids) {
        List<WeProduct> weProducts = listByIds(ids);
        for (WeProduct weProduct : weProducts) {
            QwProductQuery query = new QwProductQuery();
            query.setProduct_id(weProduct.getProductId());
            WeResultVo weResult = qwProductAlbumClient.delProductAlbum(query).getData();
            if (Objects.isNull(weResult)) {
                throw new WeComException(12021, "删除商品失败");
            }
            if (!Objects.equals(weResult.getErrCode(), WeErrorCodeEnum.ERROR_CODE_0.getErrorCode())) {
                throw new WeComException(12022, "删除商品失败");
            }
            weProduct.setDelFlag(1);
            updateById(weProduct);
        }
    }

    @Override
    public WeProductVo getProduct(Long id) {
        WeProduct product = getById(id);
        if (Objects.isNull(product)) {
            throw new WeComException("未找到相关商品");
        }
        WeProductVo productVo = new WeProductVo();
        productVo.setId(product.getId());
        productVo.setProductSn(product.getProductSn());
        BigDecimal divide = new BigDecimal(product.getPrice()).divide(BigDecimal.valueOf(100L));
        productVo.setPrice(divide.toString());


        productVo.setDescribe(product.getDescribe());
        productVo.setPicture(product.getPicture());
        productVo.setAttachments(product.getAttachments());
        return productVo;
    }

    @Override
    public List<WeProductListVo> productList(WeProductQuery query) {
        List<WeProductListVo> list = this.baseMapper.queryProductList(query);
        return list;
    }


    @Override
    public void syncProductList() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(), rabbitMQSettingConfig.getWeProductRk(), JSONObject.toJSONString(loginUser));
    }

    @Override
    public void syncProductListHandle(String msg) {
        LoginUser loginUser = JSONObject.parseObject(msg, LoginUser.class);
        List<WeProduct> weProducts = new LinkedList<>();
        QwProductListQuery query = new QwProductListQuery();
        QwProductListVo qwProductList = getQwProductList(query);
        log.info("企微商品图册数据：{}", JSONObject.toJSONString(qwProductList));
    }

    private QwProductListVo getQwProductList(QwProductListQuery query) {
        QwProductListVo productList = qwProductAlbumClient.getProductAlbumList(query).getData();
        if (productList != null && productList.getNextCursor() != null) {
            query.setCursor(productList.getNextCursor());
            QwProductListVo productChildList = getQwProductList(query);
            productList.getProductList().addAll(productChildList.getProductList());
        }
        return productList;
    }

    @Override
    public WeProductStatisticsVo statistics(Long productId) {
        WeProductStatisticsVo statistics = this.baseMapper.statistics(productId);
        BigDecimal totalFee = new BigDecimal(statistics.getOrderFee());
        BigDecimal refundFee = new BigDecimal(statistics.getRefundFee());
        BigDecimal bigDecimal = BigDecimal.valueOf(100L);
        statistics.setOrderFee(totalFee.divide(bigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        statistics.setRefundFee(refundFee.divide(bigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        statistics.setNetIncome(totalFee.subtract(refundFee).divide(bigDecimal).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        return statistics;
    }

    @Override
    public Map<String, Object> lineChart(WeProductLineChartQuery query) {
        String type = query.getType();
        Date startTime = null;
        Date endTime = null;
        if (!type.equals("customization")) {
            if (type.equals("week")) {
                startTime = DateUtil.offsetWeek(new Date(), -1);
            } else if (type.equals("month")) {
                startTime = DateUtil.offsetMonth(new Date(), -1);
            }
            endTime = new Date();
        } else {
            startTime = DateUtil.parse(query.getStartDate(), "yyyy-MM-dd");
            endTime = DateUtil.parse(query.getEndDate(), "yyyy-MM-dd");
        }

        List<DateTime> timeList = DateUtil.rangeToList(startTime, endTime, DateField.DAY_OF_YEAR);

        //折线图X轴数据
        String[] xAxisArray = new String[timeList.size()];
        //订单总额(元)
        String[] totalFeeArray = new String[timeList.size()];
        //退款总额(元)
        String[] refundFeeArray = new String[timeList.size()];
        //净收入
        String[] netIncomeArray = new String[timeList.size()];

        //查询该时间段之内所有的订单
        WeProductOrderQuery weProductOrderQuery = new WeProductOrderQuery();
        weProductOrderQuery.setProductId(query.getProductId());
        weProductOrderQuery.setBeginTime(DateUtil.format(startTime, "yyyy-MM-dd"));
        weProductOrderQuery.setEndTime(DateUtil.format(endTime, "yyyy-MM-dd"));
        List<WeProductOrderWareVo> list = weProductOrderMapper.list(weProductOrderQuery);
        Map<String, List<WeProductOrderWareVo>> collect = list.stream().collect(Collectors.groupingBy(o -> DateUtil.format(o.getPayTime(), "yyyy-MM-dd")));

        for (int i = 0; i < timeList.size(); i++) {
            String dateStr = timeList.get(i).toDateStr();
            xAxisArray[i] = dateStr;
            List<WeProductOrderWareVo> weProductOrderVos = collect.get(dateStr);
            if (weProductOrderVos != null && weProductOrderVos.size() > 0) {
                double totalFee = weProductOrderVos.stream().flatMapToDouble(o -> DoubleStream.of(Double.valueOf(o.getTotalFee()))).sum();
                List<WeProductOrderRefundVo> refundVos = weProductOrderVos.stream().flatMap(o -> o.getRefunds().stream()).collect(Collectors.toList());
                double refundFee = refundVos.stream().filter(o -> o.getRefundState().equals(2)).flatMapToDouble(o -> DoubleStream.of(Double.valueOf(o.getRefundFee()))).sum();
                BigDecimal bigDecimal = BigDecimal.valueOf(totalFee).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal bigDecimal1 = BigDecimal.valueOf(refundFee).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                totalFeeArray[i] = bigDecimal.toString();
                refundFeeArray[i] = bigDecimal1.toString();
                netIncomeArray[i] = bigDecimal.subtract(bigDecimal1).toString();
            } else {
                totalFeeArray[i] = "0";
                refundFeeArray[i] = "0";
                netIncomeArray[i] = "0";
            }
        }

        Map<String, Object> totalFeeData = new HashMap<>();
        totalFeeData.put("data", totalFeeArray);
        totalFeeData.put("name", "订单金额（元）");

        Map<String, Object> refundFeeData = new HashMap<>();
        refundFeeData.put("data", refundFeeArray);
        refundFeeData.put("name", "退款金额（元）");

        Map<String, Object> netIncomeData = new HashMap<>();
        netIncomeData.put("data", netIncomeArray);
        netIncomeData.put("name", "净收入（元）");

        Map<String, Object> xAxisData = new HashMap<>();
        xAxisData.put("data", xAxisArray);
        Map<String, Map<String, Object>> xAxisDataMap = new HashMap<>();
        xAxisDataMap.put("xAxis", xAxisData);

        List series = new ArrayList();
        series.add(totalFeeData);
        series.add(refundFeeData);
        series.add(netIncomeData);

        Map<String, Object> seriesMap = new HashMap<>();
        seriesMap.put("series", list);

        Map<String, Object> legendData = new HashMap<>();
        String[] legendArray = new String[]{"订单金额（元）", "退款金额（元）", "净收入（元）"};
        legendData.put("data", legendArray);

        Map map = new HashMap();
        map.put("legend", legendData);
        map.put("xAxis", xAxisData);
        map.put("series", series);
        return map;
    }

    @Override
    public List<WeUserOrderTop5Vo> userOrderTop5(Long productId) {
        //获取商品对应的数据
        LambdaQueryWrapper<WeProductOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(WeProductOrder::getId, WeProductOrder::getWeUserId, WeProductOrder::getWeUserName, WeProductOrder::getTotalFee);
        queryWrapper.eq(WeProductOrder::getProductId, productId);
        queryWrapper.eq(WeProductOrder::getDelFlag, 0);
        List<WeProductOrder> weProductOrders = weProductOrderMapper.selectList(queryWrapper);
        //返回结果
        List<WeUserOrderTop5Vo> result = new ArrayList<>();
        //遍历订单
        if (weProductOrders != null && weProductOrders.size() > 0) {
            Map<String, List<WeProductOrder>> collect = weProductOrders.stream().collect(Collectors.groupingBy(WeProductOrder::getWeUserId));
            if (collect != null && collect.size() > 0) {
                Iterator<Map.Entry<String, List<WeProductOrder>>> iterator = collect.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, List<WeProductOrder>> next = iterator.next();
                    //构建返回结果
                    WeUserOrderTop5Vo weUserOrderTop5Vo = new WeUserOrderTop5Vo();
                    weUserOrderTop5Vo.setWeUserId(next.getKey());
                    weUserOrderTop5Vo.setWeUserName(next.getValue().get(0).getWeUserName());
                    weUserOrderTop5Vo.setOrderNum(next.getValue().size());
                    double sum = next.getValue().stream().flatMapToDouble(o -> DoubleStream.of(Double.valueOf(o.getTotalFee()))).sum();
                    weUserOrderTop5Vo.setTotalFee(BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(100L)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    result.add(weUserOrderTop5Vo);
                }
            }
        }
        result.sort(Comparator.comparing(WeUserOrderTop5Vo::getTotalFee).reversed());
        if (result.size() > 5) {
            result.subList(0, 5);
        }
        return result;
    }

    /**
     * 生成商品编号
     *
     * @return
     */
    private synchronized String generateProductSn() {
        String prefix = "QP";
        String time = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        String randomCode = RandomUtil.randomNumbers(4);
        return String.format("%s%s%s", prefix, time, randomCode);
    }
}
