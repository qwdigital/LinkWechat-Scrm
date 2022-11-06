package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.agent.vo.LwAgentListVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeAgentInfo;

/**
 * 应用信息表(WeAgentInfo)
 *
 * @author danmo
 * @since 2022-11-04 17:06:33
 */
@Repository()
@Mapper
public interface WeAgentInfoMapper extends BaseMapper<WeAgentInfo> {


    List<LwAgentListVo> getList();
}

