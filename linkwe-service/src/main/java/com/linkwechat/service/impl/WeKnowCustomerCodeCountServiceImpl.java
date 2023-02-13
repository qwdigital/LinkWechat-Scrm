package com.linkwechat.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.know.WeKnowCustomerCodeCount;
import com.linkwechat.domain.know.vo.WeKnowCustomerCountTabVo;
import com.linkwechat.domain.know.vo.WeKnowCustomerCountTrendOrTableVo;
import com.linkwechat.mapper.WeKnowCustomerCodeCountMapper;
import com.linkwechat.service.IWeKnowCustomerCodeCountService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author robin
* @description 针对表【we_know_customer_code_count(识客码统计相关)】的数据库操作Service实现
* @createDate 2023-01-09 17:13:50
*/
@Service
public class WeKnowCustomerCodeCountServiceImpl extends ServiceImpl<WeKnowCustomerCodeCountMapper, WeKnowCustomerCodeCount>
implements IWeKnowCustomerCodeCountService {

    @Override
    public WeKnowCustomerCountTabVo findWeKnowCustomerCountDetailTab(String state, Long knowCustomerId) {
        return this.baseMapper.findWeKnowCustomerCountDetailTab(state,knowCustomerId);
    }

    @Override
    public List<WeKnowCustomerCountTrendOrTableVo> findWeKnowCustomerCountTrend(String state, Long knowCustomerId, String beginTime, String endTime) {
        return this.baseMapper.findWeKnowCustomerCountTrend(state,knowCustomerId,beginTime,endTime);
    }

    @Override
    public List<WeKnowCustomerCountTrendOrTableVo> findWeKnowCustomerCounTtable(String state, Long knowCustomerId) {
        return this.baseMapper.findWeKnowCustomerCounTtable(state,knowCustomerId);
    }
}
