package com.linkwechat.wecom.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkwechat.wecom.domain.WeKeywordGroupTaskKeyword;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 社区运营 - 关键词拉群任务VO
 */
@Data
public class WeKeywordGroupTaskVo {

    /**
     * id
     */
    private Long taskId;

    /**
     * 任务名
     */
    private String taskName;

    /**
     * 群活码id
     */
    @JsonIgnore
    private Long groupCodeId;

    /**
     * 群活码信息
     */
    @TableField(exist = false)
    private WeGroupCodeVo groupCodeInfo;

    /**
     * 关键词
     */
    @TableField(exist = false)
    private List<WeKeywordGroupTaskKeyword> keywordList;

    /**
     * 实际群聊(仅群聊名称)
     */
    @TableField(exist = false)
    private List<String> groupNameList;

    /**
     * 加群引导语
     */
    private String welcomeMsg;

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
