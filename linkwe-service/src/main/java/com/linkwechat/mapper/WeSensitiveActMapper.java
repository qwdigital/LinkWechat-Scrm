package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeSensitiveAct;

/**
 * 敏感行为表(WeSensitiveAct)
 *
 * @author danmo
 * @since 2022-06-10 10:38:46
 */
@Repository()
@Mapper
public interface WeSensitiveActMapper extends BaseMapper<WeSensitiveAct> {


}

