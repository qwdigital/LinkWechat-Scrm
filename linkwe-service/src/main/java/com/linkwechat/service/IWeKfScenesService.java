package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeKfScenes;
import com.linkwechat.domain.kf.query.WeAddKfScenesQuery;
import com.linkwechat.domain.kf.query.WeEditKfScenesQuery;
import com.linkwechat.domain.kf.query.WeKfScenesQuery;
import com.linkwechat.domain.kf.vo.WeKfScenesListVo;

import java.util.List;

/**
 * 客服场景信息表(WeKfScenes)
 *
 * @author danmo
 * @since 2022-04-15 15:53:38
 */
public interface IWeKfScenesService extends IService<WeKfScenes> {
    /**
     * 通过客服id查询场景数据
     * @param kfId
     * @return
     */
    List<WeKfScenes> getScenesByKfId(Long kfId);

    /**
     * 新增场景
     * @param query
     */
    void addKfScenes(WeAddKfScenesQuery query);

    /**
     * 删除场景值
     * @param ids
     */
    void delKfScenes(List<Long> ids);

    /**
     * 修改场景
     * @param query
     */
    void editKfScenes(WeEditKfScenesQuery query);

    /**
     * 场景管理列表
     * @param query
     */
    List<WeKfScenesListVo> getScenesList(WeKfScenesQuery query);
} 
