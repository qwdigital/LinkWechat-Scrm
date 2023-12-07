package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.domain.leads.template.entity.WeLeadsTemplateTableEntryContent;
import com.linkwechat.mapper.WeLeadsTemplateTableEntryContentMapper;
import com.linkwechat.service.IWeLeadsTemplateTableEntryContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 线索模版配置表项内容表
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/07 11:18
 */
@Slf4j
@Service
public class WeLeadsTemplateTableEntryContentServiceImpl extends ServiceImpl<WeLeadsTemplateTableEntryContentMapper, WeLeadsTemplateTableEntryContent> implements IWeLeadsTemplateTableEntryContentService {

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean removeByLeadsTemplateSettingsId(Long leadsTemplateSettingsId) {
        return remove(Wrappers.<WeLeadsTemplateTableEntryContent>lambdaUpdate().eq(WeLeadsTemplateTableEntryContent::getLeadsTemplateSettingsId, leadsTemplateSettingsId));
    }

    @Override
    public List<WeLeadsTemplateTableEntryContent> getByLeadsTemplateSettingsId(Long leadsTemplateSettingsId) {
        return list(Wrappers.<WeLeadsTemplateTableEntryContent>lambdaQuery()
                .eq(WeLeadsTemplateTableEntryContent::getLeadsTemplateSettingsId, leadsTemplateSettingsId)
                .eq(WeLeadsTemplateTableEntryContent::getDelFlag, Constants.COMMON_STATE)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatchTableEntryContent(Long settingsId, List<String> values) {
        List<WeLeadsTemplateTableEntryContent> tableEntryContentList = list(Wrappers.<WeLeadsTemplateTableEntryContent>lambdaQuery()
                .eq(WeLeadsTemplateTableEntryContent::getLeadsTemplateSettingsId, settingsId)
                .eq(WeLeadsTemplateTableEntryContent::getDelFlag, Constants.COMMON_STATE)
        );
        List<WeLeadsTemplateTableEntryContent> insertList = new ArrayList<>(values.size());
        List<Long> deleteIdList = new ArrayList<>(values.size());
        for (WeLeadsTemplateTableEntryContent tableEntryContent : tableEntryContentList) {
            String content = tableEntryContent.getContent();
            if (values.contains(content)) {
                values.remove(content);
            } else {
                deleteIdList.add(tableEntryContent.getId());
            }
        }
        for (String value : values) {
            WeLeadsTemplateTableEntryContent weTableEntryContent = new WeLeadsTemplateTableEntryContent();
            weTableEntryContent.setLeadsTemplateSettingsId(settingsId);
            weTableEntryContent.setContent(value);
            weTableEntryContent.setCreateTime(new Date());
            insertList.add(weTableEntryContent);
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            saveBatch(insertList);
        }
        if (CollectionUtils.isNotEmpty(deleteIdList)) {
            removeByIds(deleteIdList);
        }
    }
}
