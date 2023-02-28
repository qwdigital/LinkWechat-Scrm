package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.linkwechat.domain.live.WeLive;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author robin
* @description 针对表【we_live(直播主表)】的数据库操作Mapper
* @createDate 2022-10-26 22:40:45
* @Entity generator.domain.WeLive
*/
public interface WeLiveMapper extends BaseMapper<WeLive> {


    List<WeLive> findLives(@Param(Constants.WRAPPER) Wrapper wrapper);


}
