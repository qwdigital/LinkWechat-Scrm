package com.linkwechat.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.groupcode.vo.WeGroupChatInfoVo;
import com.linkwechat.domain.groupcode.vo.WeGroupCodeCountTrendVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author leejoker
 * @date 2022/4/6 23:22
 */
public interface WeGroupCodeMapper extends BaseMapper<WeGroupCode> {


    /**
     * 获取每个群对应的退群，入群成员
     * @param chatIds
     * @parm state
     * @return
     */
    List<WeGroupChatInfoVo> findWeGroupChatInfoVo(@Param("chatIds") String chatIds,@Param("state") String state);


    /**
     * 获取指定活码加群退群指定时间段内相关数据
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    List<WeGroupCodeCountTrendVo> findWeGroupCodeCountTrend(@Param("state") String state,@Param("beginTime") String beginTime,@Param("endTime") String endTime);






    /**
     * 获取群活码列表
     * @param activityName
     * @param tagIds
     * @return
     */
    @DataScope(type = "2", value = @DataColumn(alias = "a", name = "create_by_id", userid = "user_id"))
    List<WeGroupCode> findWeGroupCodeList(@Param("activityName") String activityName,@Param("tagIds") String tagIds);
}
