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
     * 查询客户群活码
     *
     * @param id 客户群活码ID
     * @return 客户群活码
     */
    WeGroupCode selectWeGroupCodeById(Long id);

    /**
     * 根据群活码id查询实际码列表
     *
     * @param groupCodeId 群活码id
     * @return 结果
     */
    List<WeGroupCodeActual> selectActualListByGroupCodeId(Long groupCodeId);

    /**
     * 查询客户群活码列表
     *
     * @param weGroupCode 客户群活码
     * @return 客户群活码集合
     */
    List<WeGroupCode> selectWeGroupCodeList(WeGroupCode weGroupCode);

    /**
     * 根据群活码id查询群活码列表
     *
     * @param ids id列表
     * @return 结果
     */
    List<WeGroupCode> selectWeGroupCodeListByIds(List<String> ids);

    /**
     * 新增客户群活码
     *
     * @param weGroupCode 客户群活码
     * @return 结果
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
     * 检测活码名称是否唯一
     *
     * @param weGroupCode 活码对象
     * @return 结果
     */
    boolean checkActivityNameUnique(WeGroupCode weGroupCode);

    /**
     * 根据 uuid获取群活码
     *
     * @param uuid uuid
     * @return 结果
     */
    WeGroupCode getWeGroupByUuid(String uuid);

}
