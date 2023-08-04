package com.linkwechat.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.domain.substitute.customer.order.entity.WeSubstituteCustomerOrderCatalogue;
import com.linkwechat.domain.substitute.customer.order.entity.WeSubstituteCustomerOrderCatalogueProperty;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderCatalogueMoveRequest;
import com.linkwechat.mapper.WeSubstituteCustomerOrderCatalogueMapper;
import com.linkwechat.mapper.WeSubstituteCustomerOrderCataloguePropertyMapper;
import com.linkwechat.service.IWeSubstituteCustomerOrderCatalogueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 代客下单字段分类 服务实现类
 * </p>
 *
 * @author WangYX
 * @since 2023-08-02
 */
@Service
public class WeSubstituteCustomerOrderCatalogueServiceImpl extends ServiceImpl<WeSubstituteCustomerOrderCatalogueMapper, WeSubstituteCustomerOrderCatalogue> implements IWeSubstituteCustomerOrderCatalogueService {

    @Resource
    private WeSubstituteCustomerOrderCataloguePropertyMapper weSubstituteCustomerOrderCataloguePropertyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        WeSubstituteCustomerOrderCatalogue one = this.baseMapper.selectById(id);
        if (BeanUtil.isEmpty(one)) {
            return;
        }
        if (one.getFixed().equals(1)) {
            throw new ServiceException("该分组不能删除！");
        }
        //删除分组
        LambdaUpdateWrapper<WeSubstituteCustomerOrderCatalogue> updateWrapper = Wrappers.lambdaUpdate(WeSubstituteCustomerOrderCatalogue.class);
        updateWrapper.eq(WeSubstituteCustomerOrderCatalogue::getId, id);
        updateWrapper.set(WeSubstituteCustomerOrderCatalogue::getDelFlag, Constants.DELETE_STATE);
        this.baseMapper.update(null, updateWrapper);

        //删除分组下的字段
        LambdaUpdateWrapper<WeSubstituteCustomerOrderCatalogueProperty> lambdaUpdate = Wrappers.lambdaUpdate(WeSubstituteCustomerOrderCatalogueProperty.class);
        lambdaUpdate.eq(WeSubstituteCustomerOrderCatalogueProperty::getCatalogueId, id);
        lambdaUpdate.set(WeSubstituteCustomerOrderCatalogueProperty::getDelFlag, Constants.DELETE_STATE);
        weSubstituteCustomerOrderCataloguePropertyMapper.update(null, lambdaUpdate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void move(WeSubstituteCustomerOrderCatalogueMoveRequest request) {
        WeSubstituteCustomerOrderCatalogue weSubstituteCustomerOrderCatalogue = null;
        //获取当前数据的下标
        LambdaQueryWrapper<WeSubstituteCustomerOrderCatalogue> queryWrapper = Wrappers.lambdaQuery(WeSubstituteCustomerOrderCatalogue.class);
        queryWrapper.eq(WeSubstituteCustomerOrderCatalogue::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.orderByAsc(WeSubstituteCustomerOrderCatalogue::getSort);
        List<WeSubstituteCustomerOrderCatalogue> list = this.baseMapper.selectList(queryWrapper);
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(request.getId())) {
                index = i;
                weSubstituteCustomerOrderCatalogue = list.get(i);
                break;
            }
        }
        if (index == -1 || BeanUtil.isEmpty(weSubstituteCustomerOrderCatalogue)) {
            return;
        }
        WeSubstituteCustomerOrderCatalogue one = null;
        //上移
        if (request.getDirection().equals(0)) {
            //下标小于0不处理
            if (index - 1 < 0) {
                return;
            }
            one = list.get(index - 1);
        }
        //下移
        if (request.getDirection().equals(1)) {
            //下标大于集合最大值不处理
            if (index + 1 > list.size()) {
                return;
            }
            one = list.get(index + 1);
        }
        if (BeanUtil.isNotEmpty(one)) {
            //交换排序数字
            LambdaUpdateWrapper<WeSubstituteCustomerOrderCatalogue> updateWrapper = Wrappers.lambdaUpdate(WeSubstituteCustomerOrderCatalogue.class);
            updateWrapper.eq(WeSubstituteCustomerOrderCatalogue::getId, one.getId());
            updateWrapper.set(WeSubstituteCustomerOrderCatalogue::getSort, weSubstituteCustomerOrderCatalogue.getSort());
            this.baseMapper.update(null, updateWrapper);
            updateWrapper.clear();
            updateWrapper.eq(WeSubstituteCustomerOrderCatalogue::getId, weSubstituteCustomerOrderCatalogue.getId());
            updateWrapper.set(WeSubstituteCustomerOrderCatalogue::getSort, one.getSort());
            this.baseMapper.update(null, updateWrapper);
        }
    }
}
