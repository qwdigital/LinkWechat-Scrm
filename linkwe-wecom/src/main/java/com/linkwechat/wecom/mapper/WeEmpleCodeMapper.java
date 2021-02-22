package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeEmpleCode;
import com.linkwechat.wecom.domain.dto.WeEmpleCodeDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工活码Mapper接口
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
public interface WeEmpleCodeMapper extends BaseMapper<WeEmpleCode>
{
    /**
     * 查询员工活码
     * 
     * @param id 员工活码ID
     * @return 员工活码
     */
    public WeEmpleCode selectWeEmpleCodeById(Long id);

    /**
     * 批量查询员工活码根据id
     * @param ids
     * @return
     */
    public List<WeEmpleCode> selectWeEmpleCodeByIds(@Param("ids") List<String> ids);

    /**
     * 查询员工活码列表
     * 
     * @param weEmpleCode 员工活码
     * @return 员工活码集合
     */
    public List<WeEmpleCode> selectWeEmpleCodeList(WeEmpleCode weEmpleCode);

    /**
     * 新增员工活码
     * 
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    public int insertWeEmpleCode(WeEmpleCode weEmpleCode);

    /**
     * 修改员工活码
     * 
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    public int updateWeEmpleCode(WeEmpleCode weEmpleCode);

    /**
     * 删除员工活码
     * 
     * @param id 员工活码ID
     * @return 结果
     */
    public int deleteWeEmpleCodeById(Long id);

    /**
     * 批量删除员工活码
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeEmpleCodeByIds(Long[] ids);


    /**
     * 批量逻辑删除员工活码
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int batchRemoveWeEmpleCodeIds(@Param("ids") List<String> ids);

    /**
     * 通过活动场景获取客户欢迎语
     * @param activityScene 活动场景
     * @return
     */
    public WeEmpleCodeDto selectWelcomeMsgByActivityScene(@Param("activityScene") String activityScene, @Param("userId") String userId);

    /**
     * 通过成员id 获取去成员活码
     * @param userId 成员id
     * @return
     */
    WeEmpleCode getQrcodeByUserId(String userId);
}
