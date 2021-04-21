package com.linkwechat.wecom.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.WeGroupCode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 客户群活码Mapper接口
 * 
 * @author ruoyi
 * @date 2020-10-07
 */
@Mapper
@Repository
public interface WeGroupCodeMapper  extends BaseMapper<WeGroupCode>
{
    /**
     * 查询客户群活码
     * 
     * @param id 客户群活码ID
     * @return 客户群活码
     */
    WeGroupCode selectWeGroupCodeById(Long id);

    /**
     * 查询客户群活码列表
     * 
     * @param weGroupCode 客户群活码
     * @return 客户群活码集合
     */
    List<WeGroupCode> selectWeGroupCodeList(WeGroupCode weGroupCode);

    /**
     * 根据群活码id查询群活码列表
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
    int insertWeGroupCode(WeGroupCode weGroupCode);

    /**
     * 修改客户群活码
     * 
     * @param weGroupCode 客户群活码
     * @return 结果
     */
    int updateWeGroupCode(WeGroupCode weGroupCode);

    /**
     * 删除客户群活码
     * 
     * @param id 客户群活码ID
     * @return 结果
     */
    int deleteWeGroupCodeById(Long id);

    /**
     * 批量删除客户群活码
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteWeGroupCodeByIds(Long[] ids);

    /**
     * 检测活码名称是否唯一
     * @param activityName 活码名称
     * @return 结果
     */
    int checkActivityNameUnique(String activityName);

    /**
     * 根据 uuid获取群活码
     *
     * @param uuid uuid
     * @return 结果
     */
    WeGroupCode getWeGroupByUuid(String uuid);

    /**
     * 根据群活码id获取对应所有群聊信息
     * @param groupCodeId 群活码id
     * @return 结果
     */
    List<WeGroup> selectWeGroupListByGroupCodeId(Long groupCodeId);

    /**
     * 获取群活码的总扫码次数
     * @param groupCodeId 群活码id
     * @return 总扫码次数
     */
    int selectScanTimesByGroupCodeId(Long groupCodeId);
}
