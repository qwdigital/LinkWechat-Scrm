package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeEmpleCode;
import com.linkwechat.wecom.domain.dto.WeEmpleCodeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 员工活码Mapper接口
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@Mapper
public interface WeEmpleCodeMapper extends BaseMapper<WeEmpleCode>
{
    /**
     * 查询员工活码
     * 
     * @param id 员工活码ID
     * @return 员工活码
     */
    WeEmpleCode selectWeEmpleCodeById(Long id);

    /**
     * 批量查询员工活码根据id
     * @param ids
     * @return
     */
    List<WeEmpleCode> selectWeEmpleCodeByIds(@Param("ids") List<String> ids);

    /**
     * 查询员工活码列表
     * 
     * @param weEmpleCode 员工活码
     * @return 员工活码集合
     */
    List<WeEmpleCode> selectWeEmpleCodeList(WeEmpleCode weEmpleCode);

    /**
     * 新增员工活码
     * 
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    int insertWeEmpleCode(WeEmpleCode weEmpleCode);

    /**
     * 修改员工活码
     * 
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    int updateWeEmpleCode(WeEmpleCode weEmpleCode);

    /**
     * 删除员工活码
     * 
     * @param id 员工活码ID
     * @return 结果
     */
    int deleteWeEmpleCodeById(Long id);

    /**
     * 批量删除员工活码
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteWeEmpleCodeByIds(Long[] ids);


    /**
     * 批量逻辑删除员工活码
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int batchRemoveWeEmpleCodeIds(@Param("ids") List<String> ids);

    /**
     * 通过活动场景获取客户欢迎语
     * @param scenario 活动场景
     * @return
     */
    WeEmpleCodeDto selectWelcomeMsgByScenario(@Param("scenario") String scenario, @Param("userId") String userId);

    /**
     * 通过state定位员工活码
     * @param state state
     * @return 员工活码
     */
    WeEmpleCodeDto selectWelcomeMsgByState(@Param("state") String state);

    /**
     * 通过成员id 获取去成员活码
     * @param userId 成员id
     * @return
     */
    WeEmpleCode getQrcodeByUserId(String userId);

    /**
     * 递增扫码次数
     * @param state state
     */
    void updateScanTimesByState(String state);
}
