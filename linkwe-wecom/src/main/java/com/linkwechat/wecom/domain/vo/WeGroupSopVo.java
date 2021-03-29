package com.linkwechat.wecom.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.enums.CommunityTaskType;
import com.linkwechat.wecom.domain.WeGroup;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WeGroupSopVo {

    /**
     * 类型。该属性仅用于H5页面与老客标签建群混合列表的任务类型判断
     */
    @TableField(exist = false)
    private final Integer type = CommunityTaskType.SOP.getType();

    /**
     * id
     */
    private Long ruleId;

    /**
     * 规则名
     */
    private String ruleName;

    /**
     * 内容标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 素材列表
     */
    @TableField(exist = false)
    private List<WeMaterialVo> materialList;

    /**
     * 自上传图片列表
     */
    @TableField(exist = false)
    private List<String> picList;

    /**
     * 群聊
     */
    @TableField(exist = false)
    private List<WeGroup> groupList;

    /**
     * 执行人列表
     */
    @TableField(exist = false)
    private List<WeCommunityTaskEmplVo> scopeList;

    /**
     * 开始执行时间
     */
    private String startExeTime;

    /**
     * 结束执行时间
     */
    private String stopExeTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;
}
