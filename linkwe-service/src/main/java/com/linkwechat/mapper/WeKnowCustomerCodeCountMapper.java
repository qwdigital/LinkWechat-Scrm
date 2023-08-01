package com.linkwechat.mapper;


import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.know.WeKnowCustomerCodeCount;
import com.linkwechat.domain.know.vo.WeKnowCustomerCountTabVo;
import com.linkwechat.domain.know.vo.WeKnowCustomerCountTrendOrTableVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author robin
* @description 针对表【we_know_customer_code_count(识客码统计相关)】的数据库操作Mapper
* @createDate 2023-01-09 17:13:50
* @Entity generator.domain.WeKnowCustomerCodeCount
*/
public interface WeKnowCustomerCodeCountMapper extends BaseMapper<WeKnowCustomerCodeCount> {


    /**
     * 获取详情统计头部tab
     * @param state
     * @param knowCustomerId
     * @return
     */
    WeKnowCustomerCountTabVo findWeKnowCustomerCountDetailTab(@Param("state") String state,@Param("knowCustomerId") Long knowCustomerId);


    /**
     * 获取识客码详情统计折线图数据
     * @param state
     * @param knowCustomerId
     * @param beginTime
     * @param endTime
     * @return
     */
    @SqlParser(filter = true)
    List<WeKnowCustomerCountTrendOrTableVo> findWeKnowCustomerCountTrend(@Param("state") String state, @Param("knowCustomerId") Long knowCustomerId, @Param("beginTime") String beginTime, @Param("endTime") String endTime);


    /**
     * 按时间分组统计识客码相关数据
     * @param state
     * @param knowCustomerId
     * @return
     */
    List<WeKnowCustomerCountTrendOrTableVo> findWeKnowCustomerCounTtable(@Param("state") String state,@Param("knowCustomerId") Long knowCustomerId);

}
