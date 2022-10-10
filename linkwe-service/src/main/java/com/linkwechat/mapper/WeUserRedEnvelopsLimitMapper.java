package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.envelopes.WeUserRedEnvelopsLimit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeUserRedEnvelopsLimitMapper extends BaseMapper<WeUserRedEnvelopsLimit> {

    List<WeUserRedEnvelopsLimit> findLimitUserRedEnvelops(@Param("userId") String userId);

}
