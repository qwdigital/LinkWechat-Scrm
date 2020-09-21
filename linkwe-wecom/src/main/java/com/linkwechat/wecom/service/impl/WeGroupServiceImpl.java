package com.linkwechat.wecom.service.impl;

import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.mapper.WeGroupMapper;
import com.linkwechat.wecom.service.IWeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/22 0022 0:03
 */
@Service
public class WeGroupServiceImpl implements IWeGroupService {
    @Autowired
    private WeGroupMapper weGroupMapper;

    public WeGroup selectWeGroupById(Long id) {
        return this.weGroupMapper.selectWeGroupById(id);
    }

    public List<WeGroup> selectWeGroupList(WeGroup weGroup) {
        return this.weGroupMapper.selectWeGroupList(weGroup);
    }

    public int insertWeGroup(WeGroup weGroup) {
        weGroup.setCreateTime(DateUtils.getNowDate());
        return this.weGroupMapper.insertWeGroup(weGroup);
    }

    public int updateWeGroup(WeGroup weGroup) {
        return this.weGroupMapper.updateWeGroup(weGroup);
    }

    public int deleteWeGroupByIds(Long[] ids) {
        return this.weGroupMapper.deleteWeGroupByIds(ids);
    }

    public int deleteWeGroupById(Long id) {
        return this.weGroupMapper.deleteWeGroupById(id);
    }
}
