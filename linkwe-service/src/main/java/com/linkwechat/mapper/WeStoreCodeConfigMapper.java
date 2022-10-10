package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.storecode.entity.WeStoreCodeConfig;
import org.apache.ibatis.annotations.Param;

public interface WeStoreCodeConfigMapper extends BaseMapper<WeStoreCodeConfig> {

    
    WeStoreCodeConfig getWeStoreCodeConfig(@Param("storeCodeType") Integer storeCodeType);

}
