package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeSensitiveActHit;

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
}
