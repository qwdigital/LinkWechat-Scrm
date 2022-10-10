package com.linkwechat.domain.community.query;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 社区运营 - 群SOP
 */
@Data
public class WeGroupSopQuery {
    /**
     * 规则名
     */
    @Size(max = 64, message = "规则名称不可超过64字符")
    @NotBlank(message = "规则名不可为空")
    private String ruleName;

    /**
     * 群聊id列表
     */
    @NotNull(message = "未选择群聊")
    private List<String> chatIdList;

    /**
     * 内容标题
     */
    @Size(max = 64, message = "标题不可超过64字符")
    @NotBlank(message = "标题不可为空")
    private String title;

    /**
     * 消息内容
     */
    @Size(max = 255, message = "标题不可超过255字符")
    private String content;

    /**
     * 素材列表
     */
    private List<Long> materialIdList;

    /**
     * 上传的图片的URL列表
     */
    private List<String> picList;

    /**
     * 开始执行时间
     */
    @NotNull(message = "开始执行时间为空")
    private String startExeTime;

    /**
     * 结束执行时间
     */
    @NotNull(message = "结束时间为空")
    private String stopExeTime;
}
