package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeEmpleCode;
import com.linkwechat.wecom.domain.WeEmpleCodeUseScop;
import com.linkwechat.wecom.domain.dto.WeEmpleCodeDto;
import com.linkwechat.wecom.domain.dto.WeExternalContactDto;

import java.util.List;

/**
 * 员工活码Service接口
 *
 * @author ruoyi
 * @date 2020-10-04
 */
public interface IWeEmpleCodeService  extends IService<WeEmpleCode>
{
    /**
     * 查询员工活码
     *
     * @param id 员工活码ID
     * @return 员工活码
     */
    WeEmpleCode selectWeEmpleCodeById(Long id);

    /**
     * 批量查询员工活码
     * @param ids 员工活码ID
     * @return
     */
    List<WeEmpleCode> selectWeEmpleCodeByIds(List<String> ids);

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
    void insertWeEmpleCode(WeEmpleCode weEmpleCode);

    /**
     * 修改员工活码
     *
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    void updateWeEmpleCode(WeEmpleCode weEmpleCode);

//    /**
//     * 批量删除员工活码
//     *
//     * @param ids 需要删除的员工活码ID
//     * @return 结果
//     */
//    public int deleteWeEmpleCodeByIds(Long[] ids);
//
    /**
     * 删除员工活码信息
     *
     * @param id 员工活码ID
     * @return 结果
     */
    public int deleteWeEmpleCodeById(Long id);
//
//
//
    /**
     * 批量逻辑删除员工活码
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int batchRemoveWeEmpleCodeIds(List<String> ids);

    /**
     * 通过活动场景获取客户欢迎语
     * @param scenario 活动场景
     * @param userId 成员id
     * @return
     */
    public WeEmpleCodeDto selectWelcomeMsgByScenario(String scenario, String userId);

    /**
     * 通过state定位员工活码
     * @param state state
     * @return 员工活码
     */
    WeEmpleCodeDto selectWelcomeMsgByState(String state);

    /**
     * 批量新增员工活码
     *
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    public void insertWeEmpleCodeBatch(WeEmpleCode weEmpleCode);

    /**
     * 获取员工二维码
     * @param userIds 员工id
     * @param departmentIds 部门id
     */
    public WeExternalContactDto getQrcode(String userIds, String departmentIds);
    /**
     * 获取员工二维码
     * @param userIdArr 员工id
     * @param departmentIdArr 部门id
     */
    public WeExternalContactDto getQrcode(String[] userIdArr, Long[] departmentIdArr);

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
