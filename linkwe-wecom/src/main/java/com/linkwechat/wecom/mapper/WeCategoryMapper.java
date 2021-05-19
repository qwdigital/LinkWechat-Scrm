package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeCategoryMapper extends BaseMapper<WeCategory> {
    void deleteWeCategoryById(Long[] ids);
}
