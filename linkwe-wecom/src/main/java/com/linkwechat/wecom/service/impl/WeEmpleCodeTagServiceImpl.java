package com.linkwechat.wecom.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeEmpleCodeTagMapper;
import com.linkwechat.wecom.domain.WeEmpleCodeTag;
import com.linkwechat.wecom.service.IWeEmpleCodeTagService;

/**
 * 员工活码标签Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@Service
public class WeEmpleCodeTagServiceImpl extends ServiceImpl<WeEmpleCodeTagMapper,WeEmpleCodeTag> implements IWeEmpleCodeTagService
{
    @Autowired
    private WeEmpleCodeTagMapper weEmpleCodeTagMapper;

    /**
     * 查询员工活码标签
     *
     * @param id 员工活码标签ID
     * @return 员工活码标签
     */
    @Override
    public WeEmpleCodeTag selectWeEmpleCodeTagById(Long id)
    {
        return weEmpleCodeTagMapper.selectWeEmpleCodeTagById(id);
    }

    /**
     * 查询员工活码标签列表
     *
     * @param empleCodeId 员工活码id
     * @return 员工活码标签
     */
    @Override
    public List<WeEmpleCodeTag> selectWeEmpleCodeTagListById(Long empleCodeId)
    {
        return weEmpleCodeTagMapper.selectWeEmpleCodeTagListById(empleCodeId);
    }

    @Override
    public List<WeEmpleCodeTag> selectWeEmpleCodeTagListByIds(List<Long> empleCodeIdList) {
        return weEmpleCodeTagMapper.selectWeEmpleCodeTagListByIds(empleCodeIdList);
    }

    /**
     * 新增员工活码标签
     *
     * @param weEmpleCodeTag 员工活码标签
     * @return 结果
     */
    @Override
    public int insertWeEmpleCodeTag(WeEmpleCodeTag weEmpleCodeTag)
    {
        return weEmpleCodeTagMapper.insertWeEmpleCodeTag(weEmpleCodeTag);
    }

    /**
     * 修改员工活码标签
     *
     * @param weEmpleCodeTag 员工活码标签
     * @return 结果
     */
    @Override
    public int updateWeEmpleCodeTag(WeEmpleCodeTag weEmpleCodeTag)
    {
        return weEmpleCodeTagMapper.updateWeEmpleCodeTag(weEmpleCodeTag);
    }

    /**
     * 批量删除员工活码标签
     *
     * @param ids 需要删除的员工活码标签ID
     * @return 结果
     */
    @Override
    public int deleteWeEmpleCodeTagByIds(Long[] ids)
    {
        return weEmpleCodeTagMapper.deleteWeEmpleCodeTagByIds(ids);
    }

    /**
     * 删除员工活码标签信息
     *
     * @param id 员工活码标签ID
     * @return 结果
     */
    @Override
    public int deleteWeEmpleCodeTagById(Long id)
    {
        return weEmpleCodeTagMapper.deleteWeEmpleCodeTagById(id);
    }


    /**
     * 批量保存标签
     * @param weEmpleCodeTags
     * @return
     */
    @Override
    public int batchInsetWeEmpleCodeTag(List<WeEmpleCodeTag> weEmpleCodeTags) {
        return weEmpleCodeTagMapper.batchInsetWeEmpleCodeTag(weEmpleCodeTags);
    }




    /**
     * 批量逻辑删除
     * @param ids
     * @return
     */
    @Override
    public int batchRemoveWeEmpleCodeTagIds(List<Long>  ids) {
        return weEmpleCodeTagMapper.batchRemoveWeEmpleCodeTagIds(ids);
    }
}
