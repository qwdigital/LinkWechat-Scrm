package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeKfMsgCursor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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

