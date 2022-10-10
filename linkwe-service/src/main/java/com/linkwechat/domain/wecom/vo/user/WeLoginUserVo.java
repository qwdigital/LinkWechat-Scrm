package com.linkwechat.domain.wecom.vo.user;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 成员列表
 * @date 2021/12/7 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeLoginUserVo extends WeResultVo {

    /**
     * 用户所属企业的corpid
     */
    private String CorpId;
    /**
     * 成员UserID
     */
    private String UserId;

    /**
     * 成员openUserId
     */
    private String openUserId;

    /**
     * 手机设备号(由企业微信在安装时随机生成，删除重装会改变，升级不受影响)
     */
    private String DeviceId;

    /**
     * 外部联系人id，当且仅当用户是企业的客户，且跟进人在应用的可见范围内时返回
     */
    private String externalUserId;

    /**
     * 非企业成员的标识，对当前企业唯一
     */
    private String OpenId;

    /**
     * 成员票据，最大为512字节。scope为snsapi_userinfo或snsapi_privateinfo，且用户在应用可见范围之内时返回此参数
     */
    private String userTicket;

}
