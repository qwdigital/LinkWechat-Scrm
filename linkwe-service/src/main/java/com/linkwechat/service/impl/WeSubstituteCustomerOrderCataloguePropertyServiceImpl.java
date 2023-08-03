package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.substitute.customer.order.entity.WeSubstituteCustomerOrderCatalogueProperty;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderCataloguePropertyMoveRequest;
import com.linkwechat.mapper.WeSubstituteCustomerOrderCataloguePropertyMapper;
import com.linkwechat.service.IWeSubstituteCustomerOrderCataloguePropertyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 代客下单分类字段 服务实现类
 * </p>
 *
 * @author WangYX
 * @since 2023-08-03
 */
@Service
public class WeSubstituteCustomerOrderCataloguePropertyServiceImpl extends ServiceImpl<WeSubstituteCustomerOrderCataloguePropertyMapper, WeSubstituteCustomerOrderCatalogueProperty> implements IWeSubstituteCustomerOrderCataloguePropertyService {

    @Override
    public void move(WeSubstituteCustomerOrderCataloguePropertyMoveRequest request) {
        LambdaQueryWrapper<WeSubstituteCustomerOrderCatalogueProperty> queryWrapper = Wrappers.lambdaQuery(WeSubstituteCustomerOrderCatalogueProperty.class);
        queryWrapper.eq(WeSubstituteCustomerOrderCatalogueProperty::getCatalogueId, request.getCatalogueId());
        queryWrapper.orderByAsc(WeSubstituteCustomerOrderCatalogueProperty::getSort);
        List<WeSubstituteCustomerOrderCatalogueProperty> list = this.baseMapper.selectList(queryWrapper);
        WeSubstituteCustomerOrderCatalogueProperty property = null;
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(request.getId())) {
                index = i;
                property = list.get(i);
                break;
            }
        }

        if (index == -1 || property == null) {
            return;
        }
        WeSubstituteCustomerOrderCatalogueProperty one = null;
        //上移
        if (request.getDirection().equals(0)) {
            if (index - 1 < 0) {
                return;
            }
            one = list.get(index - 1);
        }
        //下移
        if (request.getDirection().equals(1)) {
            if (index + 1 > list.size()) {
                return;
            }
            one = list.get(index + 1);
        }
        if (BeanUtil.isNotEmpty(one)) {
            //交换排序数字
            LambdaUpdateWrapper<WeSubstituteCustomerOrderCatalogueProperty> updateWrapper = Wrappers.lambdaUpdate(WeSubstituteCustomerOrderCatalogueProperty.class);
            updateWrapper.eq(WeSubstituteCustomerOrderCatalogueProperty::getId, one.getId());
            updateWrapper.set(WeSubstituteCustomerOrderCatalogueProperty::getSort, property.getSort());
            this.baseMapper.update(null, updateWrapper);
            updateWrapper.clear();
            updateWrapper.eq(WeSubstituteCustomerOrderCatalogueProperty::getId, property.getId());
            updateWrapper.set(WeSubstituteCustomerOrderCatalogueProperty::getSort, one.getSort());
            this.baseMapper.update(null, updateWrapper);
        }

    }
}
