package com.linkwechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeSysFieldTemplate;
import java.util.List;

/**
* @author robin
* @description 针对表【we_sys_field_template(自定义表单字段模板表)】的数据库操作Service
* @createDate 2023-02-01 14:16:43
*/
public interface IWeSysFieldTemplateService extends IService<WeSysFieldTemplate> {


    /**
     * 获取模版列表
     * @param sysFieldTemplate
     * @return
     */
    List<WeSysFieldTemplate> findLists(WeSysFieldTemplate sysFieldTemplate);

    /**
     * 初始化动态表单所需基础字段
     */
    void initBaseSysField();


    /**
     * 保存字段模版
     * @param sysFieldTemplate
     */
    void addSysField(WeSysFieldTemplate sysFieldTemplate);


    /**
     * 更新字段模版
     * @param sysFieldTemplate
     */
    void updateSysField(WeSysFieldTemplate sysFieldTemplate);



}
