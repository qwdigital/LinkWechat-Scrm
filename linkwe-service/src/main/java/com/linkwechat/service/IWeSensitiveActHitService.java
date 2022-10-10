package com.linkwechat.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeSensitiveActHit;

/**
 * 敏感行为记录表(WeSensitiveActHit)
 *
 * @author danmo
 * @since 2022-06-10 10:38:47
 */
public interface IWeSensitiveActHitService extends IService<WeSensitiveActHit> {

    void hitWeSensitiveAct(JSONObject jsonObject);
}
