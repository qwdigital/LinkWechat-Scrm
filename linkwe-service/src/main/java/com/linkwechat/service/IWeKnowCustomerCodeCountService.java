package com.linkwechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.know.WeKnowCustomerCodeCount;
import com.linkwechat.domain.know.vo.WeKnowCustomerCountTabVo;
import com.linkwechat.domain.know.vo.WeKnowCustomerCountTrendOrTableVo;
import java.util.List;

/**
* @author robin
* @description 针对表【we_know_customer_code_count(识客码统计相关)】的数据库操作Service
* @createDate 2023-01-09 17:13:50
*/
public interface IWeKnowCustomerCodeCountService extends IService<WeKnowCustomerCodeCount> {

    /**
     * 获取详情统计头部tab
     * @param state
     * @param knowCustomerId
     * @return
     */
    WeKnowCustomerCountTabVo findWeKnowCustomerCountDetailTab(String state,Long knowCustomerId);


    /**
     * 获取识客码详情统计折线图数据
     * @param state
     * @param knowCustomerId
     * @param beginTime
     * @param endTime
     * @return
     */
    List<WeKnowCustomerCountTrendOrTableVo> findWeKnowCustomerCountTrend(String state,Long knowCustomerId,String beginTime,String endTime);


    /**
     * 按时间分组统计识客码相关数据
     * @param state
     * @param knowCustomerId
     * @return
     */
    List<WeKnowCustomerCountTrendOrTableVo> findWeKnowCustomerCounTtable(String state,Long knowCustomerId);

}
