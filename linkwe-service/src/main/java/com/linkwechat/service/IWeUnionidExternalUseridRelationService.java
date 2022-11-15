package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeUnionidExternalUseridRelation;

/**
 * 轨迹素材隐私政策客户授权
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/26 15:53
 */
public interface IWeUnionidExternalUseridRelationService extends IService<WeUnionidExternalUseridRelation> {

    /**
     * 通过openid和unionid获取数据
     *
     * @param openid
     * @param unionid
     * @return
     */
    public WeUnionidExternalUseridRelation get(String openid, String unionid);


}
