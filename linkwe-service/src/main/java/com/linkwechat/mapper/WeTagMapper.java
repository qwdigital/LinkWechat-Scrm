package com.linkwechat.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeTagMapper extends BaseMapper<WeTag> {

   @SqlParser(filter=true)
   void batchAddOrUpdate(@Param("weTags") List<WeTag> weTags);

}
