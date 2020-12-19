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
     * @param id
     * @param size
     * @return
     */
    public Font getFont(Long id,Integer size);

}
