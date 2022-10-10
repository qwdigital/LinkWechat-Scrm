package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeGroupMessageList;

/**
 * 群发消息列表(WeGroupMessageList)
 *
 * @author danmo
 * @since 2022-04-06 22:29:03
 */
@Repository()
@Mapper
public interface WeGroupMessageListMapper extends BaseMapper<WeGroupMessageList> {


}

