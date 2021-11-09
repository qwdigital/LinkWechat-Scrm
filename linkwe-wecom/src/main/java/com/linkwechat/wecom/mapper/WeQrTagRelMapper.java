package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.wecom.domain.WeQrTagRel;

/**
 * 活码标签关联表(WeQrTagRel)Mapper接口
 *
 * @author makejava
 * @since 2021-11-07 01:29:13
 */
@Repository()
@Mapper
public interface WeQrTagRelMapper extends BaseMapper<WeQrTagRel> {


}

