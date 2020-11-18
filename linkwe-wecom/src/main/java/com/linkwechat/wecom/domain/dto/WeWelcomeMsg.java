package com.linkwechat.wecom.domain.dto;

import lombok.Builder;
import lombok.Data;
import me.chanjar.weixin.cp.bean.external.msg.Image;
import me.chanjar.weixin.cp.bean.external.msg.Link;
import me.chanjar.weixin.cp.bean.external.msg.MiniProgram;
import me.chanjar.weixin.cp.bean.external.msg.Text;

/**
 * @author admin
 * @date 2020-11-18
 */
@Data
@Builder
public class WeWelcomeMsg {
    private String welcome_code;

    private Text text;

    private Image image;

    private Link link;

    private MiniProgram miniprogram;
}
