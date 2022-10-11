package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeKfWelcome;

/**
 * 客服欢迎语表(WeKfWelcome)
 *
 * @author danmo
 * @since 2022-04-15 15:53:39
 */
@Repository()
@Mapper
public interface WeKfWelcomeMapper extends BaseMapper<WeKfWelcome> {


}

