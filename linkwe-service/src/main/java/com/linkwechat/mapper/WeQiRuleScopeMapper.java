package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeQiRuleScope;
import com.linkwechat.domain.qirule.vo.WeQiRuleScopeVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 质检规则范围表(WeQiRuleScope)
 *
 * @author danmo
 * @since 2023-05-05 16:57:31
 */
@Repository()
@Mapper
public interface WeQiRuleScopeMapper extends BaseMapper<WeQiRuleScope> {
}

