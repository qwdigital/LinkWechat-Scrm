package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.img.NetFileUtils;
import com.linkwechat.wecom.domain.WePosterFont;
import com.linkwechat.wecom.mapper.WePosterFontMapper;
import com.linkwechat.wecom.service.IWePosterFontService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 海报字体
 * @author ws
 */
@Service
public class WePosterFontServiceImpl extends ServiceImpl<WePosterFontMapper,WePosterFont> implements IWePosterFontService {


    @Resource
    private WePosterFontMapper wePosterFontMapper;

    /**
     * 字体数据缓存
     */
    private final Map<Long, Font> FONT_MAP = new ConcurrentHashMap<>();


    /**
     * 获取字体
     *
     * @param id
     * @param size
     * @return
     */
    @Override
    public Font getFont(Long id, Integer size) {
        WePosterFont posterFont = this.getById(id);
        if(posterFont == null){
            throw new RuntimeException("字体id为："+id+"的字体不存在");
        }
        if(StringUtils.isBlank(posterFont.getFontUrl())){
            posterFont.setFontUrl("http://img.kometl.com/1.ttf");
        }
        Font font;
        if((font=FONT_MAP.get(posterFont.getId())) != null){
            return font.deriveFont((float)size);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(NetFileUtils.getByteArrayOutputStream(NetFileUtils.getNetFile(posterFont.getFontUrl()),false).toByteArray());
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, byteArrayInputStream);
            FONT_MAP.put(posterFont.getId(),font);
            font = font.deriveFont((float)size);
            return font;
        } catch (FontFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("字体格式错误");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
