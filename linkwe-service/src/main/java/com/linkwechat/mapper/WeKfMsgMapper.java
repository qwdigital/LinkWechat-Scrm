package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.linkwechat.domain.kf.query.WeKfRecordQuery;
import com.linkwechat.domain.kf.vo.WeKfRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeKfMsg;

/**
 * 客服消息表(WeKfMsg)
 *
 * @author danmo
 * @since 2022-04-15 15:53:36
 */
@Repository()
@Mapper
public interface WeKfMsgMapper extends BaseMapper<WeKfMsg> {

    /**
     * 会话记录详情
     * @param query
     * @return
     */
    List<WeKfRecordVo> getRecordDetail(WeKfRecordQuery query);
}

