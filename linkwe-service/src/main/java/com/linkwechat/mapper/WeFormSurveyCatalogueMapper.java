package com.linkwechat.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeFormSurveyCatalogue;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 问卷-目录列表(WeFormSurveyCatalogue)
 *
 * @author danmo
 * @since 2022-09-20 18:02:56
 */
@Repository()
@Mapper
public interface WeFormSurveyCatalogueMapper extends BaseMapper<WeFormSurveyCatalogue> {

    /**
     * 此注解忽略了MybatisPlus的多租户拦截
     */
    @InterceptorIgnore(tenantLine = "true")
    WeFormSurveyCatalogue getWeFormSurveyCatalogueById(Long id);

    /**
     * 获取所有的问卷，跳过租户拦截判断
     *
     * @return
     */
//    @InterceptorIgnore(tenantLine = "true")
    List<WeFormSurveyCatalogue> getListIgnoreTenantId();

    @InterceptorIgnore(tenantLine = "true")
    void updateByIdIgnoreTenantId(WeFormSurveyCatalogue weFormSurveyCatalogue);

}

