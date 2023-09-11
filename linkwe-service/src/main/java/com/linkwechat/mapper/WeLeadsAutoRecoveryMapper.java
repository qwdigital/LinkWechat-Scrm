package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.leads.leads.entity.WeLeadsAutoRecovery;
import org.apache.ibatis.annotations.Mapper;

/**
 * 线索自动回收
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 10:51
 */
@Mapper
public interface WeLeadsAutoRecoveryMapper extends BaseMapper<WeLeadsAutoRecovery> {
}
