package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeCustomerLinkCount;
import com.linkwechat.domain.customer.vo.WeCustomerLinkCountTabVo;
import com.linkwechat.domain.customer.vo.WeCustomerLinkCountTableVo;
import com.linkwechat.domain.customer.vo.WeCustomerLinkCountTrendVo;
import com.linkwechat.service.IWeCustomerLinkCountService;
import com.linkwechat.mapper.WeCustomerLinkCountMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author robin
* @description 针对表【we_customer_link_count】的数据库操作Service实现
* @createDate 2023-07-26 14:51:19
*/
@Service
public class WeCustomerLinkCountServiceImpl extends ServiceImpl<WeCustomerLinkCountMapper, WeCustomerLinkCount>
    implements IWeCustomerLinkCountService {

    @Override
    public void synchWeCustomerLinkCount() {




    }

    @Override
    public List<WeCustomerLinkCountTrendVo> selectLinkCountTrend(String linkId, String beginTime, String endTime) {
        return this.baseMapper.selectLinkCountTrend(linkId,beginTime,endTime);
    }

    @Override
    public List<WeCustomerLinkCountTableVo> selectLinkCountTable(String linkId, String beginTime, String endTime) {
        return this.baseMapper.selectLinkCountTable(linkId,beginTime,endTime);
    }

    @Override
    public WeCustomerLinkCountTabVo selectLinkCountTab(String linkId) {
        return this.baseMapper.selectLinkCountTab(linkId);
    }
}




