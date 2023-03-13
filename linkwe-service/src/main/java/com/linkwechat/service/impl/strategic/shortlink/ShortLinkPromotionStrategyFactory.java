package com.linkwechat.service.impl.strategic.shortlink;

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
     * @param type 0群发客户，1群发客户群，2群发朋友圈，4应用消息
     * @return
     */
    public static PromotionType getPromotionType(Integer type) {
        assert type != null;
        if (type.equals(1)) {
            return new ClientPromotion();
        } else if (type.equals(2)) {
            return new GroupPromotion();
        } else if (type.equals(3)) {
            return new MomentsPromotion();
        } else if (type.equals(4)) {
            return null;
        } else {
            return null;
        }

    }


}
