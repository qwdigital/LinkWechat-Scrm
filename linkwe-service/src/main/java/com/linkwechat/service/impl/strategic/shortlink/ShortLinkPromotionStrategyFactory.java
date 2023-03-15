package com.linkwechat.service.impl.strategic.shortlink;

import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.service.AbstractGroupMsgSendTaskService;

/**
 * 短链策略工厂
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/08 18:13
 */
public class ShortLinkPromotionStrategyFactory {

    /**
     * 获取推广方式
     *
     * @param type 0群发客户，1群发客户群，2群发朋友圈，3应用消息
     * @return
     */
    public static PromotionType getPromotionType(Integer type) {
        assert type != null;
        if (type.equals(0)) {
            return  SpringUtils.getBean("clientPromotion", PromotionType.class);
        } else if (type.equals(1)) {
            return  SpringUtils.getBean("groupPromotion", PromotionType.class);
        } else if (type.equals(2)) {
            return  SpringUtils.getBean("momentsPromotion", PromotionType.class);
        } else if (type.equals(3)) {
            return  SpringUtils.getBean("appMsgPromotion", PromotionType.class);
        } else {
            return null;
        }
    }


}
