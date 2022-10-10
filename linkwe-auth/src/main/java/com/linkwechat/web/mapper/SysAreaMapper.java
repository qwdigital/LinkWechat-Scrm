package com.linkwechat.web.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.web.domain.SysArea;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 行政区划(SysArea)
 *
 * @author danmo
 * @since 2022-06-27 11:00:59
 */

@Repository()
@Mapper
public interface SysAreaMapper extends BaseMapper<SysArea> {


}

