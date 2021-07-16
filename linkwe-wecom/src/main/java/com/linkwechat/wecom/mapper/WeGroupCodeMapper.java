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
public interface WeGroupCodeMapper extends BaseMapper<WeGroupCode> {
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

    /**
     * 通过员工活码获取群活码，用于新客自动拉群。
     * @param state 员工活码state
     * @return 群活码URL
     */
    String selectGroupCodeUrlByEmplCodeState(String state);
}
