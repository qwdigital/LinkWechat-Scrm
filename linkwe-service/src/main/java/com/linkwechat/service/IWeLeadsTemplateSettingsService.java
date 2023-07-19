package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.template.entity.WeLeadsTemplateSettings;
import com.linkwechat.domain.leads.template.entity.WeLeadsTemplateTableEntryContent;
import com.linkwechat.domain.leads.template.query.WeLeadsTemplateSettingsRequest;
import com.linkwechat.domain.leads.template.query.WeTemplateSettingsReRankRequest;
import com.linkwechat.domain.leads.template.vo.WeLeadsTemplateSettingsVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 公海模版配置表(WeSeaLeadsTemplateSettings)表服务接口
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/07 10:04
 */
public interface IWeLeadsTemplateSettingsService extends IService<WeLeadsTemplateSettings> {

    /**
     * 全量查询
     *
     * @return 结果列表
     */
    List<WeLeadsTemplateSettingsVO> queryAll();

    /**
     * 新增
     *
     * @param request 前端入参
     * @return 是否成功
     */
    boolean saveLeadsTemplateSettings(WeLeadsTemplateSettingsRequest request);

    /**
     * 修改
     *
     * @param param 前端入参
     * @return 是否成功
     */
    boolean updateLeadsTemplateSettings(WeLeadsTemplateSettingsRequest param);

    /**
     * 删除
     *
     * @param id 表主键id
     * @return 是否成功
     */
    boolean deleteLeadsTemplateSettings(Long id);

    /**
     * 重排序
     *
     * @param paramList 前端入参
     * @return 是否成功
     */
    boolean reRank(List<WeTemplateSettingsReRankRequest> paramList);

    /**
     * 自动生成表项ID
     *
     * @return 返回一个自动生成的表项id
     */
    String autoGenerate();

}

