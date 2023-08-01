package com.linkwechat.factory;


import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.TagSynchEnum;
import com.linkwechat.common.enums.WelcomeMsgTypeEnum;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;

/**
 * @author danmo
 * @description 事件类型策略接口
 * @date 2021/1/20 22:00
 **/
public abstract class WeEventStrategy {
    protected  final String tag = TagSynchEnum.TAG_TYPE.getType();
    protected  final String tagGroup = TagSynchEnum.GROUP_TAG_TYPE.getType();

    public abstract void eventHandle(WeBackBaseVo message);


    protected boolean isFission(String str) {
        return str.startsWith(WelcomeMsgTypeEnum.FISSION_PREFIX.getType());
    }

}
