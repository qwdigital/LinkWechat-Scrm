package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WePosterFont;

import java.awt.*;

/**
 * 海报
 * @author ws
 */
public interface IWePosterFontService extends IService<WePosterFont> {

    /**
     * 获取字体
     * @param id 字体id
     * @param size 字体大小
     * @param fontStyle 字体类型 0 通常 1 粗体 2 斜体
     * @return
     */
    public Font getFont(Long id,Integer size,Integer fontStyle);

}
