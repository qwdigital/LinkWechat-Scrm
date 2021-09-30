package com.linkwechat.wecom.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeSensitiveAct;
import com.linkwechat.wecom.domain.WeSensitiveActHit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/1/13 8:46
 */
public interface IWeSensitiveActHitService extends IService<WeSensitiveActHit> {
    /**
     * 查询敏感行为记录
     *
     * @param id 敏感行为记录ID
     * @return 敏感行为
     */
    public WeSensitiveActHit selectWeSensitiveActHitById(Long id);

    /**
     * 查询敏感行为记录列表
     *
     * @param weSensitiveActHit 敏感行为记录
     * @return 敏感行为记录
     */
    public List<WeSensitiveActHit> selectWeSensitiveActHitList(WeSensitiveActHit weSensitiveActHit);

    /**
     * 新增敏感行为记录
     *
     * @param weSensitiveActHit 敏感行为记录
     * @return 结果
     */
    public boolean insertWeSensitiveActHit(WeSensitiveActHit weSensitiveActHit);

    /**
     * 过滤敏感行为，并保存
     *
     * @param chatJson
     */
    @Transactional
    void hitWeSensitiveAct(JSONObject chatJson);

    /**
     * 获取敏感行为类型
     *
     * @param msgType
     * @return
     */
    public WeSensitiveAct getSensitiveActType(String msgType);
}
