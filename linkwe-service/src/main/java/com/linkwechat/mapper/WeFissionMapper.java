package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.domain.fission.WeFission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author robin
* @description 针对表【we_fission(裂变（任务宝,群裂变）)】的数据库操作Mapper
* @createDate 2023-03-14 14:07:21
* @Entity com.linkwechat.WeFission
*/
public interface WeFissionMapper extends BaseMapper<WeFission> {

    @DataScope(type = "2", value = @DataColumn(alias = "wf", name = "create_by_id", userid = "user_id"))
    List<WeFission> findWeFissions(@Param(Constants.WRAPPER) Wrapper wrapper);

}




