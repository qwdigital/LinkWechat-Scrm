package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeEmpleCode;

import java.util.List;

/**
 * 员工活码Service接口
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
public interface IWeEmpleCodeService  extends IService<WeEmpleCode>
{
//    /**
//     * 查询员工活码
//     *
//     * @param id 员工活码ID
//     * @return 员工活码
//     */
//    public WeEmpleCode selectWeEmpleCodeById(Long id);
//
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
    public void insertWeEmpleCode(WeEmpleCode weEmpleCode);

    /**
     * 修改员工活码
     * 
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    public void updateWeEmpleCode(WeEmpleCode weEmpleCode);

//    /**
//     * 批量删除员工活码
//     *
//     * @param ids 需要删除的员工活码ID
//     * @return 结果
//     */
//    public int deleteWeEmpleCodeByIds(Long[] ids);
//
//    /**
//     * 删除员工活码信息
//     *
//     * @param id 员工活码ID
//     * @return 结果
//     */
//    public int deleteWeEmpleCodeById(Long id);
//
//
//
//    /**
//     * 批量逻辑删除员工活码
//     *
//     * @param ids 需要删除的数据ID
//     * @return 结果
//     */
//    public int batchRemoveWeEmpleCodeIds(List<String> ids);



}
