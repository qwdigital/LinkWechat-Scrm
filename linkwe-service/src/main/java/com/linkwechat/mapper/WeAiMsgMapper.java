package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeAiMsg;

/**
 * ai助手消息表(WeAiMsg)
 *
 * @author makejava
 * @since 2023-12-01 15:12:13
 */
@Repository()
@Mapper
public interface WeAiMsgMapper extends BaseMapper<WeAiMsg> {


    List<WeAiMsg> getSessionList(@Param("userId") Long userId, @Param("content") String content);

    List<String> collectionMsgIdByQuery(@Param("userId") Long userId, @Param("content") String content);
    List<WeAiMsg> collectionList(@Param("msgIds") List<String> msgIds);

    Integer computeTodayToken();

}

