package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.domain.msgtlp.query.WeMsgTlpQuery;
import com.linkwechat.domain.msgtlp.vo.WeMsgTlpVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeMsgTlp;

/**
 * 欢迎语模板表(WeMsgTlp)
 *
 * @author danmo
 * @since 2022-03-28 10:21:23
 */
@Repository()
@Mapper
public interface WeMsgTlpMapper extends BaseMapper<WeMsgTlp> {



    List<WeMsgTlpVo> getList(@Param("query") WeMsgTlpQuery query, @Param("userIds") List<String> userIds);
}

