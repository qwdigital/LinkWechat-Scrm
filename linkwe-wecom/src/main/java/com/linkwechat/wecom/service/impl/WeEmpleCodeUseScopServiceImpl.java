package com.linkwechat.wecom.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeEmpleCodeUseScopMapper;
import com.linkwechat.wecom.domain.WeEmpleCodeUseScop;
import com.linkwechat.wecom.service.IWeEmpleCodeUseScopService;

/**
 * 员工活码使用人Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@Service
public class WeEmpleCodeUseScopServiceImpl extends ServiceImpl<WeEmpleCodeUseScopMapper,WeEmpleCodeUseScop> implements IWeEmpleCodeUseScopService
{
    @Autowired
    private WeEmpleCodeUseScopMapper weEmpleCodeUseScopMapper;

    /**
     * 查询员工活码使用人
     *
     * @param id 员工活码使用人ID
     * @return 员工活码使用人
     */
    @Override
    public WeEmpleCodeUseScop selectWeEmpleCodeUseScopById(Long id)
    {
        return weEmpleCodeUseScopMapper.selectWeEmpleCodeUseScopById(id);
    }

    /**
     * 查询员工活码使用人列表
     *
     * @param empleCodeId
     * @return 员工活码使用人
     */
    @Override
    public List<WeEmpleCodeUseScop> selectWeEmpleCodeUseScopListById( Long empleCodeId)
    {
        return weEmpleCodeUseScopMapper.selectWeEmpleCodeUseScopListById(empleCodeId);
    }

    /**
     * 查询员工活码使用人列表(批量)
     * @param empleCodeIdList
     * @return
     */
    @Override
    public List<WeEmpleCodeUseScop> selectWeEmpleCodeUseScopListByIds(List<Long> empleCodeIdList) {
        return weEmpleCodeUseScopMapper.selectWeEmpleCodeUseScopListByIds(empleCodeIdList);
    }

    /**
     * 新增员工活码使用人
     *
     * @param weEmpleCodeUseScop 员工活码使用人
     * @return 结果
     */
    @Override
    public int insertWeEmpleCodeUseScop(WeEmpleCodeUseScop weEmpleCodeUseScop)
    {
        return weEmpleCodeUseScopMapper.insertWeEmpleCodeUseScop(weEmpleCodeUseScop);
    }

    /**
     * 修改员工活码使用人
     *
     * @param weEmpleCodeUseScop 员工活码使用人
     * @return 结果
     */
    @Override
    public int updateWeEmpleCodeUseScop(WeEmpleCodeUseScop weEmpleCodeUseScop)
    {
        return weEmpleCodeUseScopMapper.updateWeEmpleCodeUseScop(weEmpleCodeUseScop);
    }

    /**
     * 批量删除员工活码使用人
     *
     * @param ids 需要删除的员工活码使用人ID
     * @return 结果
     */
    @Override
    public int deleteWeEmpleCodeUseScopByIds(Long[] ids)
    {
        return weEmpleCodeUseScopMapper.deleteWeEmpleCodeUseScopByIds(ids);
    }

    /**
     * 删除员工活码使用人信息
     *
     * @param id 员工活码使用人ID
     * @return 结果
     */
    @Override
    public int deleteWeEmpleCodeUseScopById(Long id)
    {
        return weEmpleCodeUseScopMapper.deleteWeEmpleCodeUseScopById(id);
    }


    /**
     * 批量保存
     * @param weEmpleCodeUseScops
     * @return
     */
    @Override
    public int batchInsetWeEmpleCodeUseScop(List<WeEmpleCodeUseScop> weEmpleCodeUseScops) {
        return weEmpleCodeUseScopMapper.batchInsetWeEmpleCodeUseScop(weEmpleCodeUseScops);
    }

    /**
     * 批量逻辑删除
     * @param ids
     * @return
     */
    @Override
    public int batchRemoveWeEmpleCodeUseScopIds(List<Long> ids) {
        return weEmpleCodeUseScopMapper.batchRemoveWeEmpleCodeUseScopIds(ids);
    }
}
