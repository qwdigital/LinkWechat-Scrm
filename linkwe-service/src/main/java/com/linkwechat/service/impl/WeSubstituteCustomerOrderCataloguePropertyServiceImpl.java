package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.domain.substitute.customer.order.entity.WeSubstituteCustomerOrderCatalogue;
import com.linkwechat.domain.substitute.customer.order.entity.WeSubstituteCustomerOrderCatalogueProperty;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderCataloguePropertyMoveRequest;
import com.linkwechat.domain.substitute.customer.order.vo.WeSubstituteCustomerOrderCataloguePropertyVO;
import com.linkwechat.domain.substitute.customer.order.vo.WeSubstituteCustomerOrderCatalogueVO;
import com.linkwechat.mapper.WeSubstituteCustomerOrderCatalogueMapper;
import com.linkwechat.mapper.WeSubstituteCustomerOrderCataloguePropertyMapper;
import com.linkwechat.service.IWeSubstituteCustomerOrderCataloguePropertyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private WeSubstituteCustomerOrderCatalogueMapper weSubstituteCustomerOrderCatalogueMapper;

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

    @Override
    public List<WeSubstituteCustomerOrderCatalogueVO> properties() {
        LambdaQueryWrapper<WeSubstituteCustomerOrderCatalogue> queryWrapper = Wrappers.lambdaQuery(WeSubstituteCustomerOrderCatalogue.class);
        queryWrapper.eq(WeSubstituteCustomerOrderCatalogue::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.orderByAsc(WeSubstituteCustomerOrderCatalogue::getSort);
        List<WeSubstituteCustomerOrderCatalogue> list = weSubstituteCustomerOrderCatalogueMapper.selectList(queryWrapper);
        List<WeSubstituteCustomerOrderCatalogueVO> vos = BeanUtil.copyToList(list, WeSubstituteCustomerOrderCatalogueVO.class);

        LambdaQueryWrapper<WeSubstituteCustomerOrderCatalogueProperty> lambdaQuery = Wrappers.lambdaQuery(WeSubstituteCustomerOrderCatalogueProperty.class);
        lambdaQuery.eq(WeSubstituteCustomerOrderCatalogueProperty::getDelFlag, Constants.COMMON_STATE);
        lambdaQuery.orderByAsc(WeSubstituteCustomerOrderCatalogueProperty::getSort);
        List<WeSubstituteCustomerOrderCatalogueProperty> properties = this.baseMapper.selectList(lambdaQuery);
        Map<Long, List<WeSubstituteCustomerOrderCatalogueProperty>> map = properties.stream().collect(Collectors.groupingBy(WeSubstituteCustomerOrderCatalogueProperty::getCatalogueId));

        vos.forEach(i -> {
            List<WeSubstituteCustomerOrderCatalogueProperty> item = map.get(i.getId());
            i.setProperties(BeanUtil.copyToList(item, WeSubstituteCustomerOrderCataloguePropertyVO.class));
        });

        return vos;
    }
}
