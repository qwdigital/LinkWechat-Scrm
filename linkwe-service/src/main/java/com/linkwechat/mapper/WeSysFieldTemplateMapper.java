package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.linkwechat.domain.WeSysFieldTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author robin
* @description 针对表【we_sys_field_template(自定义表单字段模板表)】的数据库操作Mapper
* @createDate 2023-02-01 14:16:43
* @Entity generator.domain.WeSysFieldTemplate
*/
public interface WeSysFieldTemplateMapper extends BaseMapper<WeSysFieldTemplate> {


    List<WeSysFieldTemplate> findLists(@Param(Constants.WRAPPER) Wrapper<WeSysFieldTemplate> queryWrapper);


}
