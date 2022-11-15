package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeKfMsgCursor;

/**
 * 客服消息偏移量表(WeKfMsgCursor)
 *
 * @author danmo
 * @since 2022-04-15 15:53:37
 */
@Repository()
@Mapper
public interface WeKfMsgCursorMapper extends BaseMapper<WeKfMsgCursor> {


}

