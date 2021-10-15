package com.linkwechat.wecom.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeGroupCode;
import com.linkwechat.wecom.domain.WeGroupCodeActual;
import org.springframework.web.multipart.MultipartFile;

/**
 * 客户群活码Service接口
 *
 * @author ruoyi
 * @date 2020-10-07
 */
public interface IWeGroupCodeService extends IService<WeGroupCode> {

    /**
     * 根据群活码id查询实际码列表
     *
     * @param groupCodeId 群活码id
     * @return 结果
     */
    List<WeGroupCodeActual> selectActualList(Long groupCodeId);

    /**
     * 查询客户群活码列表
     *
     * @param weGroupCode 客户群活码
     * @return 客户群活码集合
     */
    List<WeGroupCode> selectWeGroupCodeList(WeGroupCode weGroupCode);

    /**
     * 新增客户群活码
     *
     * @param weGroupCode 客户群活码
     */
    void insertWeGroupCode(WeGroupCode weGroupCode);

    /**
     * 修改客户群活码
     *
     * @param weGroupCode 客户群活码
     * @return 结果
     */
    int updateWeGroupCode(WeGroupCode weGroupCode);

    /**
     * 批量删除客户群活码
     *
     * @param ids 需要删除的客户群活码ID
     * @return 结果
     */
    int deleteWeGroupCodeByIds(Long[] ids);

    /**
     * 删除客户群活码信息
     *
     * @param id 客户群活码ID
     * @return 结果
     */
    int deleteWeGroupCodeById(Long id);

    /**
     * 检测活码名称是否被占用
     *
     * @param weGroupCode 活码对象
     * @return 结果
     */
    boolean isNameOccupied(WeGroupCode weGroupCode);

    /**
     * 通过员工活码获取群活码，用于新客自动拉群。
     *
     * @param state 员工活码state
     * @return 群活码URL
     */
    String selectGroupCodeUrlByEmplCodeState(String state);


    void countScanTimes(WeGroupCode groupCode);

}
