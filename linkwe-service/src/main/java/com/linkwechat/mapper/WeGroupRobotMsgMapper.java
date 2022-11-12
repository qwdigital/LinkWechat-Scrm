package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeGroupRobotMsg;

/**
 * 群机器人消息表(WeGroupRobotMsg)
 *
 * @author danmo
 * @since 2022-11-08 16:06:01
 */
@Repository()
@Mapper
public interface WeGroupRobotMsgMapper extends BaseMapper<WeGroupRobotMsg> {


}

