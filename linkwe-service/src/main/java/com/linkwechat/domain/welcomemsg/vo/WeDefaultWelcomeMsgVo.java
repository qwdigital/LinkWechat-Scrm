package com.linkwechat.domain.welcomemsg.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.linkwechat.domain.media.WeMessageTemplate;
import lombok.Data;

import java.util.List;

@Data
public class WeDefaultWelcomeMsgVo {
    /**
     * 素材新增编辑传入(欢迎语和附件)
     */
    @TableField(exist = false)
    private List<WeMessageTemplate> attachments;

}
