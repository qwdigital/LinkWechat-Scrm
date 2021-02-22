package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeEmpleCodeUseScop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工活码使用人Mapper接口
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
public interface WeEmpleCodeUseScopMapper extends BaseMapper<WeEmpleCodeUseScop>
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
     * @param empleCodeId 员工活码id
     * @return 员工活码使用人集合
     */
    public List<WeEmpleCodeUseScop> selectWeEmpleCodeUseScopListById(@Param("empleCodeId") Long empleCodeId);

    /**
     * 查询员工活码使用人列表(批量)
     * @param empleCodeIdList
     * @return
     */
    List<WeEmpleCodeUseScop> selectWeEmpleCodeUseScopListByIds(@Param("empleCodeIdList") List<Long> empleCodeIdList);

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
     * 删除员工活码使用人
     * 
     * @param id 员工活码使用人ID
     * @return 结果
     */
    public int deleteWeEmpleCodeUseScopById(Long id);

    /**
     * 批量删除员工活码使用人
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeEmpleCodeUseScopByIds(Long[] ids);

    /**
     * 批量保存
     * @param weEmpleCodeUseScops
     * @return
     */
    public int batchInsetWeEmpleCodeUseScop(@Param("weEmpleCodeUseScops") List<WeEmpleCodeUseScop> weEmpleCodeUseScops);


    /**
     * 批量物理删除
     * @param ids
     * @return
     */
    public int batchRemoveWeEmpleCodeUseScopIds(List<Long> ids);


}
