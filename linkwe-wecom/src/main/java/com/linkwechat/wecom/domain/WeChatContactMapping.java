package com.linkwechat.wecom.domain;

import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 聊天关系映射对象 we_chat_contact_mapping
 * 
 * @author ruoyi
 * @date 2020-12-27
 */
@Data
public class WeChatContactMapping extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 发送人id */
    @Excel(name = "发送人id")
    private String fromId;

    /** 发送人头像 */
    @Excel(name = "发送人头像")
    private String fromAvatar;

    /** 发送人姓名 */
    @Excel(name = "发送人姓名")
    private String fromName;

    /** 发送人性别 0-未知 1-男性 2-女性 */
    @Excel(name = "发送人性别 0-未知 1-男性 2-女性")
    private Integer fromGender;

    /** 接收人头像 */
    @Excel(name = "接收人头像")
    private String reviceAvatar;

    /** 接收人姓名 */
    @Excel(name = "接收人姓名")
    private String reviceName;

    /** 接收人性别 0-未知 1-男性 2-女性 */
    @Excel(name = "接收人性别 0-未知 1-男性 2-女性")
    private Integer reviceGender;

    /** 接收人id */
    @Excel(name = "接收人id")
    private String reviceId;

    /** 群聊id */
    @Excel(name = "群聊id")
    private String roomId;

    /** 是否为客户 0-成员 1-客户 */
    @Excel(name = "是否为客户 0-成员 1-客户")
    private Integer isCustom;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("fromId", getFromId())
            .append("reviceAvatar", getReviceAvatar())
            .append("reviceName", getReviceName())
            .append("reviceGender", getReviceGender())
            .append("reviceId", getReviceId())
            .append("roomId", getRoomId())
            .append("isCustom", getIsCustom())
            .toString();
    }
}
