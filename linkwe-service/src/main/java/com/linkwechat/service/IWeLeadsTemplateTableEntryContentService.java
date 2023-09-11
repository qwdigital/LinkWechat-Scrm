package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.template.entity.WeLeadsTemplateTableEntryContent;

import java.util.List;

/**
 * 线索模版配置表项内容
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/07 11:18
 */
public interface IWeLeadsTemplateTableEntryContentService extends IService<WeLeadsTemplateTableEntryContent> {

    /**
     * 移除表项内容
     *
     * @param leadsTemplateSettingsId 模板Id
     * @return
     */
    boolean removeByLeadsTemplateSettingsId(Long leadsTemplateSettingsId);

    List<WeLeadsTemplateTableEntryContent> getByLeadsTemplateSettingsId(Long leadsTemplateSettingsId);

    /**
     * 保存模版表项，注意：该方法会将之前存在的表项全部删除再新增
     *
     * @param settingsId 模版id
     * @param values     值列表
     * @return 是否成功
     */
    void saveBatchTableEntryContent(Long settingsId, List<String> values);
}
