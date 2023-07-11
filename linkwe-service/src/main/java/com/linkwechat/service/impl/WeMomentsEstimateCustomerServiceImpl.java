package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.moments.entity.WeMomentsEstimateCustomer;
import com.linkwechat.domain.moments.query.WeMomentsStatisticCustomerRecordRequest;
import com.linkwechat.domain.moments.vo.WeMomentsEstimateCustomerVO;
import com.linkwechat.mapper.WeMomentsEstimateCustomerMapper;
import com.linkwechat.service.IWeMomentsEstimateCustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预估朋友圈可见客户 服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/03 10:15
 */
@Service
public class WeMomentsEstimateCustomerServiceImpl extends ServiceImpl<WeMomentsEstimateCustomerMapper, WeMomentsEstimateCustomer> implements IWeMomentsEstimateCustomerService {

    @Override
    public List<WeMomentsEstimateCustomerVO> getEstimateCustomer(WeMomentsStatisticCustomerRecordRequest request) {
        return this.baseMapper.getEstimateCustomer(request);
    }
}
