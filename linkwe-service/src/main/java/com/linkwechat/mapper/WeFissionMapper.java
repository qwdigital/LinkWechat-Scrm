package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.domain.fission.WeFission;
import com.linkwechat.domain.fission.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author robin
* @description 针对表【we_fission(裂变（任务宝,群裂变）)】的数据库操作Mapper
* @createDate 2023-03-14 14:07:21
* @Entity com.linkwechat.WeFission
*/
public interface WeFissionMapper extends BaseMapper<WeFission> {

    /**
     * 裂变列表
     * @param wrapper
     * @return
     */
    @DataScope(type = "2", value = @DataColumn(alias = "wf", name = "create_by_id", userid = "user_id"))
    List<WeFission> findWeFissions(@Param(Constants.WRAPPER) Wrapper wrapper);


    /**
     * 裂变头部汇总统计
     * @param fissionId
     * @return
     */
    WeFissionTabVo findWeFissionTab(@Param("fissionId") Long fissionId);


    /**
     * 数据趋势
     * @param weFission
     * @return
     */
    List<WeFissionTrendVo> findWeFissionTrend(@Param("weFission") WeFission weFission);


    /**
     * 数据报表
     * @param weFission
     * @return
     */
    List<WeFissionDataReportVo> findWeFissionDataReport(@Param("weFission") WeFission weFission);


    /**
     * 群裂变-裂变明细
     * @param customerName
     * @param weUserId
     * @return
     */
    List<WeGroupFissionDetailVo> findWeGroupFissionDetail(@Param("customerName") String customerName, @Param("weUserId") String weUserId,@Param("chatId") String chatId);


    /**
     * 裂变明细sub
     * @param fissionDetailId
     * @return
     */
    List<WeFissionDetailSubVo> findWeFissionDetailSub(@Param("fissionDetailId") Long fissionDetailId);

}




