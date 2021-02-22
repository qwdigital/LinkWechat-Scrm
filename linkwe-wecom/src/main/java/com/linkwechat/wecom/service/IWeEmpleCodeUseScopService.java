package com.linkwechat.wecom.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeEmpleCodeUseScop;

/**
 * 员工活码使用人Service接口
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
public interface IWeEmpleCodeUseScopService extends IService<WeEmpleCodeUseScop>
{
    /**
     * 查询员工活码使用人
     *
     * @param id 员工活码使用人ID
     * @return 员工活码使用人
     */
    public WeEmpleCodeUseScop selectWeEmpleCodeUseScopById(Long id);

    /**
     * 查询员工活码使用人列表
     *
     * @param empleCodeId
     * @return 员工活码使用人集合
     */
    public List<WeEmpleCodeUseScop> selectWeEmpleCodeUseScopListById(Long empleCodeId);

    /**
     * 查询员工活码使用人列表(批量)
     * @param empleCodeIdList
     * @return
     */
    public List<WeEmpleCodeUseScop> selectWeEmpleCodeUseScopListByIds(List<Long> empleCodeIdList);


    /**
     * 新增员工活码使用人
     *
     * @param weEmpleCodeUseScop 员工活码使用人
     * @return 结果
     */
    public int insertWeEmpleCodeUseScop(WeEmpleCodeUseScop weEmpleCodeUseScop);

    /**
     * 修改员工活码使用人
     *
     * @param weEmpleCodeUseScop 员工活码使用人
     * @return 结果
     */
    public int updateWeEmpleCodeUseScop(WeEmpleCodeUseScop weEmpleCodeUseScop);

    /**
     * 批量删除员工活码使用人
     *
     * @param ids 需要删除的员工活码使用人ID
     * @return 结果
     */
    public int deleteWeEmpleCodeUseScopByIds(Long[] ids);

    /**
     * 删除员工活码使用人信息
     *
     * @param id 员工活码使用人ID
     * @return 结果
     */
    public int deleteWeEmpleCodeUseScopById(Long id);

    /**
     * 批量保存
     * @param weEmpleCodeUseScops
     * @return
     */
    public int batchInsetWeEmpleCodeUseScop(List<WeEmpleCodeUseScop> weEmpleCodeUseScops);


    /**
     * 批量物理删除
     * @param ids
     * @return
     */
    public int batchRemoveWeEmpleCodeUseScopIds(List<Long> ids);
}
