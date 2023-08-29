package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.constant.SubstituteCustomerOrderCataloguePropertyConstants;
import com.linkwechat.domain.substitute.customer.order.entity.WeSubstituteCustomerOrder;
import com.linkwechat.domain.substitute.customer.order.entity.WeSubstituteCustomerOrderCatalogueProperty;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderAddRequest;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderRequest;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderUpdateRequest;
import com.linkwechat.domain.substitute.customer.order.vo.WeSubstituteCustomerOrderCataloguePropertyVO;
import com.linkwechat.domain.substitute.customer.order.vo.WeSubstituteCustomerOrderCataloguePropertyValueVO;
import com.linkwechat.domain.substitute.customer.order.vo.WeSubstituteCustomerOrderCatalogueVO;
import com.linkwechat.domain.substitute.customer.order.vo.WeSubstituteCustomerOrderVO;
import com.linkwechat.mapper.WeSubstituteCustomerOrderMapper;
import com.linkwechat.service.IWeSubstituteCustomerOrderCataloguePropertyService;
import com.linkwechat.service.IWeSubstituteCustomerOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 代客下单-订单 服务实现类
 * </p>
 *
 * @author WangYX
 * @since 2023-08-03
 */
@Service
public class WeSubstituteCustomerOrderServiceImpl extends ServiceImpl<WeSubstituteCustomerOrderMapper, WeSubstituteCustomerOrder> implements IWeSubstituteCustomerOrderService {

    @Resource
    private IWeSubstituteCustomerOrderCataloguePropertyService weSubstituteCustomerOrderCataloguePropertyService;

    @Override
    public List<WeSubstituteCustomerOrderVO> selectList(WeSubstituteCustomerOrderRequest request) {
        return this.baseMapper.list(request);
    }

    @Override
    public List<WeSubstituteCustomerOrderCatalogueVO> get(Long id) {
        WeSubstituteCustomerOrder order = this.baseMapper.selectById(id);
        if (BeanUtil.isEmpty(order)) {
            return null;
        }
        List<WeSubstituteCustomerOrderCatalogueVO> catalogues = weSubstituteCustomerOrderCataloguePropertyService.properties();
        setActualValue(catalogues, order);
        return catalogues;
    }

    @Override
    public Long add(WeSubstituteCustomerOrderAddRequest request) {
        WeSubstituteCustomerOrder order = BeanUtil.copyProperties(request, WeSubstituteCustomerOrder.class);
        List<WeSubstituteCustomerOrderCataloguePropertyValueVO> customs = request.getCustoms();

        //订单属性
        LambdaQueryWrapper<WeSubstituteCustomerOrderCatalogueProperty> queryWrapper = Wrappers.lambdaQuery(WeSubstituteCustomerOrderCatalogueProperty.class);
        queryWrapper.eq(WeSubstituteCustomerOrderCatalogueProperty::getDelFlag, Constants.COMMON_STATE);
        List<WeSubstituteCustomerOrderCatalogueProperty> list = weSubstituteCustomerOrderCataloguePropertyService.list(queryWrapper);

        //判断订单属性是否必填
        judgePropertyRequired(list, order, customs);

        order.setId(IdUtil.getSnowflakeNextId());
        order.setDelFlag(Constants.COMMON_STATE);
        if (CollectionUtil.isNotEmpty(customs)) {
            order.setProperties(JSONObject.toJSONString(customs));
        }

        this.baseMapper.insert(order);
        return order.getId();
    }

    @Override
    public void update(WeSubstituteCustomerOrderUpdateRequest request) {
        WeSubstituteCustomerOrder order = BeanUtil.copyProperties(request, WeSubstituteCustomerOrder.class);
        List<WeSubstituteCustomerOrderCataloguePropertyValueVO> customs = request.getCustoms();

        //订单属性
        LambdaQueryWrapper<WeSubstituteCustomerOrderCatalogueProperty> queryWrapper = Wrappers.lambdaQuery(WeSubstituteCustomerOrderCatalogueProperty.class);
        queryWrapper.eq(WeSubstituteCustomerOrderCatalogueProperty::getDelFlag, Constants.COMMON_STATE);
        List<WeSubstituteCustomerOrderCatalogueProperty> list = weSubstituteCustomerOrderCataloguePropertyService.list(queryWrapper);

        //判断订单属性是否必填
        judgePropertyRequired(list, order, customs);

        if (CollectionUtil.isNotEmpty(customs)) {
            order.setProperties(JSONObject.toJSONString(customs));
        }
        this.baseMapper.updateById(order);
    }

    /**
     * 设置订单属性实际值
     *
     * @param catalogues 订单自定义属性
     * @param order      订单详情
     * @return
     * @author WangYX
     * @date 2023/08/07 16:52
     */
    private void setActualValue(List<WeSubstituteCustomerOrderCatalogueVO> catalogues, WeSubstituteCustomerOrder order) {
        for (WeSubstituteCustomerOrderCatalogueVO catalogue : catalogues) {
            List<WeSubstituteCustomerOrderCataloguePropertyVO> properties = catalogue.getProperties();
            if (CollectionUtil.isEmpty(properties)) {
                continue;
            }

            //固定字段写入值
            List<WeSubstituteCustomerOrderCataloguePropertyVO> fixedProperties = properties.stream().filter(i -> i.getFixed().equals(1)).collect(Collectors.toList());
            for (WeSubstituteCustomerOrderCataloguePropertyVO property : fixedProperties) {
                switch (property.getCode()) {
                    case "productName":
                        property.setActualValue(order.getProductName());
                        break;
                    case "productUrl":
                        property.setActualValue(order.getProductUrl());
                        break;
                    case "productUnitPrice":
                        property.setActualValue(order.getProductUnitPrice());
                        break;
                    case "amount":
                        property.setActualValue(order.getAmount());
                        break;
                    case "purchaser":
                        property.setActualValue(order.getPurchaser());
                        break;
                    case "phone":
                        property.setActualValue(order.getPhone());
                        break;
                    case "source":
                        property.setActualValue(order.getSource());
                        break;
                    case "orderTime":
                        if (BeanUtil.isNotEmpty(order.getOrderTime())) {
                            String format = null;
                            if (property.getToTime().equals(0)) {
                                format = DateUtil.format(order.getOrderTime(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
                            } else {
                                format = DateUtil.format(order.getOrderTime(), DatePattern.NORM_DATETIME_PATTERN);
                            }
                            property.setActualValue(format);
                        }
                        break;
                    case "deptId":
                        property.setActualValue(order.getDeptId());
                        break;
                    case "userId":
                        property.setActualValue(order.getUserId());
                        break;
                    case "orderStatus":
                        property.setActualValue(order.getOrderStatus());
                        break;
                    case "totalPrice":
                        property.setActualValue(order.getTotalPrice());
                        break;
                    case "discount":
                        property.setActualValue(order.getDiscount());
                    case "discountAmount":
                        property.setActualValue(order.getDiscountAmount());
                        break;
                    case "actualPayment":
                        property.setActualValue(order.getActualPayment());
                        break;
                    case "returnedMoneyType":
                        property.setActualValue(order.getReturnedMoneyType());
                        break;
                    case "returnedMoney":
                        property.setActualValue(order.getReturnedMoney());
                        break;
                    case "returnedDate":
                        if (BeanUtil.isNotEmpty(order.getReturnedDate())) {
                            String format = null;
                            if (property.getToTime().equals(0)) {
                                format = DateUtil.format(order.getReturnedDate(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
                            } else {
                                format = DateUtil.format(order.getReturnedDate(), DatePattern.NORM_DATETIME_PATTERN);
                            }
                            property.setActualValue(format);
                        }
                        break;
                    case "payer":
                        property.setActualValue(order.getPayer());
                        break;
                    case "returnedReceipt":
                        property.setActualValue(order.getReturnedReceipt());
                        break;
                    default:
                        break;
                }
            }

            //自定义字段写入值
            List<WeSubstituteCustomerOrderCataloguePropertyVO> unFixedProperties = properties.stream().filter(i -> i.getFixed().equals(0)).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(unFixedProperties)) {
                continue;
            }
            if (StrUtil.isBlank(order.getProperties())) {
                continue;
            }
            List<WeSubstituteCustomerOrderCataloguePropertyValueVO> values = JSONObject.parseArray(order.getProperties(), WeSubstituteCustomerOrderCataloguePropertyValueVO.class);
            unFixedProperties.forEach(i -> {
                Optional<WeSubstituteCustomerOrderCataloguePropertyValueVO> first = values.stream().filter(j -> i.getId().equals(j.getId())).findFirst();
                first.ifPresent(k -> i.setActualValue(k.getValue()));
            });
        }
    }

    /**
     * 判断订单属性是否必填
     *
     * @param list    订单字段属性
     * @param order   订单固定项
     * @param customs 订单自定义
     * @author WangYX
     * @date 2023/08/07 17:36
     */
    private void judgePropertyRequired(List<WeSubstituteCustomerOrderCatalogueProperty> list, WeSubstituteCustomerOrder order, List<WeSubstituteCustomerOrderCataloguePropertyValueVO> customs) {
        //固定字段判断
        List<WeSubstituteCustomerOrderCatalogueProperty> fixedList = list.stream().filter(i -> i.getFixed().equals(1)).collect(Collectors.toList());
        for (WeSubstituteCustomerOrderCatalogueProperty property : fixedList) {
            switch (property.getCode()) {
                case SubstituteCustomerOrderCataloguePropertyConstants.PRODUCT_NAME:
                    required(property, order.getProductName());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.PRODUCT_URL:
                    required(property, order.getProductUrl());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.PRODUCT_UNIT_PRICE:
                    required(property, order.getProductUnitPrice());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.AMOUNT:
                    required(property, order.getAmount());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.PURCHASE:
                    required(property, order.getPurchaser());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.PHONE:
                    required(property, order.getPhone());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.SOURCE:
                    required(property, order.getSource());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.ORDER_TIME:
                    required(property, order.getOrderTime());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.DEPT_ID:
                    required(property, order.getDeptId());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.USER_ID:
                    required(property, order.getUserId());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.ORDER_STATUS:
                    required(property, order.getOrderStatus());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.TOTAL_PRICE:
                    required(property, order.getTotalPrice());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.DISCOUNT:
                    required(property, order.getDiscount());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.DISCOUNT_AMOUNT:
                    required(property, order.getDiscountAmount());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.ACTUAL_PAYMENT:
                    required(property, order.getActualPayment());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.RETURNED_MONEY_TYPE:
                    required(property, order.getReturnedMoneyType());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.RETURNED_MONEY:
                    required(property, order.getReturnedMoney());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.RETURNED_DATE:
                    required(property, order.getReturnedDate());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.PAYER:
                    required(property, order.getPayer());
                    break;
                case SubstituteCustomerOrderCataloguePropertyConstants.RETURNED_RECEIPT:
                    required(property, order.getReturnedReceipt());
                    break;
                default:
                    break;
            }
        }
        //自定义字段判断
        List<WeSubstituteCustomerOrderCatalogueProperty> unFixedList = list.stream().filter(i -> i.getFixed().equals(0)).collect(Collectors.toList());
        for (WeSubstituteCustomerOrderCatalogueProperty property : unFixedList) {
            Optional<WeSubstituteCustomerOrderCataloguePropertyValueVO> first = customs.stream().filter(i -> i.getId().equals(property.getId())).findFirst();
            first.ifPresent(i -> required(property, i.getValue()));
        }
    }

    /**
     * 判断是否必填
     *
     * @param property    字段属性
     * @param actualValue 实际值
     * @return
     * @author WangYX
     * @date 2023/08/07 17:57
     */
    private void required(WeSubstituteCustomerOrderCatalogueProperty property, Object actualValue) {
        if (property.getRequired().equals(1)) {
            if (BeanUtil.isEmpty(actualValue)) {
                throw new ServiceException(property.getName() + "必填");
            }
        }
    }


}
