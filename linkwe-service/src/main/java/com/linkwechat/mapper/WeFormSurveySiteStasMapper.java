package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeFormSurveySiteStas;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 智能表单站点统计
 *
 * @author danmo
 * @since 2022-09-20 18:02:57
 */
@Repository()
@Mapper
public interface WeFormSurveySiteStasMapper extends BaseMapper<WeFormSurveySiteStas> {


}

