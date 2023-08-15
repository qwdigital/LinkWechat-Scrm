package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeCustomerLinkCount;
import com.linkwechat.domain.customer.vo.WeCustomerLinkCountTabVo;
import com.linkwechat.domain.customer.vo.WeCustomerLinkCountTableVo;
import com.linkwechat.domain.customer.vo.WeCustomerLinkCountTrendVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author robin
* @description 针对表【we_customer_link_count】的数据库操作Mapper
* @createDate 2023-07-26 14:51:19
* @Entity com.linkwechat.WeCustomerLinkCount
*/
public interface WeCustomerLinkCountMapper extends BaseMapper<WeCustomerLinkCount> {


    /**
     * 获取折线图统计数据
     * @param linkId
     * @param beginTime
     * @param endTime
     * @return
     */
    List<WeCustomerLinkCountTrendVo> selectLinkCountTrend(@Param("linkId") String linkId,@Param("beginTime") String beginTime,@Param("endTime") String endTime);


    /**
     * 获取表格统计数据
     * @param linkId
     * @param beginTime
     * @param endTime
     * @return
     */
    List<WeCustomerLinkCountTableVo> selectLinkCountTable(@Param("linkId") String linkId,@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    /**
     * 获取头部统计数据
     * @param linkId
     * @return
     */
    WeCustomerLinkCountTabVo selectLinkCountTab(@Param("linkId") String linkId);


    /**
     * 批量新增或更新
     * @param weCustomerLinkCounts
     */
    void batchAddOrUpdate(@Param("weCustomerLinkCounts") List<WeCustomerLinkCount> weCustomerLinkCounts);

}




