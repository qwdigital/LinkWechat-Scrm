package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeLxQrScope;

/**
 * 拉新活码使用范围表(WeLxQrScope)
 *
 * @author danmo
 * @since 2023-03-07 15:06:04
 */
@Repository()
@Mapper
public interface WeLxQrScopeMapper extends BaseMapper<WeLxQrScope> {


}

