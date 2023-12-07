package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.config.mybatis.LwBaseMapper;
import com.linkwechat.domain.moments.entity.WeMomentsEstimateCustomer;
import com.linkwechat.domain.moments.query.WeMomentsStatisticCustomerRecordRequest;
import com.linkwechat.domain.moments.vo.WeMomentsEstimateCustomerVO;

import java.util.List;

/**
 * 预估朋友圈可见客户 Mapper 接口
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/03 10:13
 */
public interface WeMomentsEstimateCustomerMapper extends LwBaseMapper<WeMomentsEstimateCustomer> {

    /**
     * 获取预估客户数据
     *
     * @param request 朋友圈统计-客户记录列表请求参数
     * @return {@link List <WeMomentsEstimateCustomer>}
     * @author WangYX
     * @date 2023/07/03 10:54
     */
    List<WeMomentsEstimateCustomerVO> getEstimateCustomer(WeMomentsStatisticCustomerRecordRequest request);

}
