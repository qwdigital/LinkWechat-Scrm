package com.linkwechat.wecom.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.wecom.domain.WeCommunityNewGroup;
import com.linkwechat.wecom.domain.WeGroupCodeActual;
import com.linkwechat.wecom.mapper.WeCommunityNewGroupMapper;
import com.linkwechat.wecom.mapper.WeGroupCodeActualMapper;
import com.linkwechat.wecom.service.IWeGroupCodeActualService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeGroupCodeMapper;
import com.linkwechat.wecom.domain.WeGroupCode;
import com.linkwechat.wecom.service.IWeGroupCodeService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客户群活码Service业务层处理
 *
 * @author ruoyi
 * @date 2020-10-07
 */
@Service
public class WeGroupCodeServiceImpl extends ServiceImpl<WeGroupCodeMapper, WeGroupCode> implements IWeGroupCodeService {

    private final IWeGroupCodeActualService iWeGroupCodeActualService;

    private final WeGroupCodeActualMapper actualCodeMapper;

    private final WeCommunityNewGroupMapper communityNewGroupMapper;

    public WeGroupCodeServiceImpl(IWeGroupCodeActualService iWeGroupCodeActualService, WeGroupCodeActualMapper actualCodeMapper, WeCommunityNewGroupMapper communityNewGroupMapper) {
        this.iWeGroupCodeActualService = iWeGroupCodeActualService;
        this.actualCodeMapper = actualCodeMapper;
        this.communityNewGroupMapper = communityNewGroupMapper;
    }

    /**
     * 根据群活码id查询实际码列表
     *
     * @param groupCodeId 群活码id
     * @return 结果
     */
    @Override
    public List<WeGroupCodeActual> selectActualList(Long groupCodeId) {
        return actualCodeMapper.selectActualList(groupCodeId);
    }

    /**
     * 查询客户群活码列表
     *
     * @param weGroupCode 客户群活码
     * @return 客户群活码
     */
    @Override
    public List<WeGroupCode> selectWeGroupCodeList(WeGroupCode weGroupCode) {
        List<WeGroupCode> weGroupCodeList = baseMapper.selectWeGroupCodeList(weGroupCode);
        for (WeGroupCode item : weGroupCodeList) {
            List<WeGroupCodeActual> actualList = actualCodeMapper.selectActualList(item.getId());
            item.setActualList(actualList);
        }
        return weGroupCodeList;
    }

    /**
     * 新增客户群活码
     *
     * @param weGroupCode 客户群活码
     */
    @Override
    public void insertWeGroupCode(WeGroupCode weGroupCode) {
        try {
            baseMapper.insertWeGroupCode(weGroupCode);
        } catch (Exception e) {
            throw new WeComException(e.getMessage());
        }

    }

    /**
     * 修改客户群活码
     *
     * @param weGroupCode 客户群活码
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public int updateWeGroupCode(WeGroupCode weGroupCode) {
        // 更新实际码
        List<WeGroupCodeActual> actualList = weGroupCode.getActualList();
        if (CollectionUtil.isNotEmpty(actualList)) {
            iWeGroupCodeActualService.updateBatchById(actualList);
        }
        return baseMapper.updateById(weGroupCode);
    }

    /**
     * 批量删除客户群活码
     *
     * @param ids 需要删除的客户群活码ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteWeGroupCodeByIds(Long[] ids) {
        // 删除新客拉群信息，防止出现新科拉群没有群活码可用
        LambdaUpdateWrapper<WeGroupCodeActual> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(WeGroupCodeActual::getGroupCodeId, Arrays.asList(ids)).set(WeGroupCodeActual::getDelFlag, 1);
        actualCodeMapper.update(null, updateWrapper);
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除客户群活码信息
     *
     * @param id 客户群活码ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public int deleteWeGroupCodeById(Long id) {
        // 需要删除新客拉群信息
        LambdaUpdateWrapper<WeCommunityNewGroup> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WeCommunityNewGroup::getGroupCodeId, id).set(WeCommunityNewGroup::getDelFlag, 1);
        communityNewGroupMapper.update(null, updateWrapper);
        // 删除群活码
        return baseMapper.deleteById(id);
    }

    /**
     * 检测活码名称是否被占用
     *
     * @param weGroupCode 活码对象
     * @return 结果
     */
    @Override
    public boolean isNameOccupied(WeGroupCode weGroupCode) {
        Long currentId = Optional.ofNullable(weGroupCode.getId()).orElse(-1L);
        LambdaQueryWrapper<WeGroupCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeGroupCode::getActivityName, weGroupCode.getActivityName()).eq(WeGroupCode::getDelFlag, 0);
        List<WeGroupCode> res = baseMapper.selectList(queryWrapper);
        return !res.isEmpty() && !currentId.equals(res.get(0).getId());
    }

    /**
     * 通过员工活码获取群活码，用于新客自动拉群。
     *
     * @param state 员工活码state
     * @return 群活码URL
     */
    @Override
    public String selectGroupCodeUrlByEmplCodeState(String state) {
        return baseMapper.selectGroupCodeUrlByEmplCodeState(state);
    }


    /**
     * 统计扫码次数
     * @param groupCode
     */
    @Override
    @Async
    public void countScanTimes(WeGroupCode groupCode) {

        if(groupCode !=null){
            groupCode.setTotalScanTimes(
                    groupCode.getTotalScanTimes()+1
            );
            this.updateById(groupCode);
        }



    }
}
