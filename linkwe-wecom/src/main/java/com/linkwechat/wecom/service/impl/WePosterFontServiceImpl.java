package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.html.EscapeUtil;
import com.linkwechat.common.utils.img.NetFileUtils;
import com.linkwechat.wecom.domain.WePosterFont;
import com.linkwechat.wecom.mapper.WePosterFontMapper;
import com.linkwechat.wecom.service.IWePosterFontService;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
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
@Log
public class WePosterFontServiceImpl extends ServiceImpl<WePosterFontMapper,WePosterFont> implements IWePosterFontService {

    private static Font DEFAULT_FONT;

    static {
        try {
            DEFAULT_FONT = Font.createFont(Font.TRUETYPE_FONT,WePosterFontServiceImpl.class.getResourceAsStream("/font/default.ttf"));
            log.info("字体文件读取成功");
        } catch (FontFormatException e) {
            e.printStackTrace();
            log.info("字体加载失败");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("字体文件读取失败");
        }
    }


    @Resource
    private WePosterFontMapper wePosterFontMapper;

    /**
     * 字体数据缓存
     */
    private final Map<Long, Font> FONT_MAP = new ConcurrentHashMap<>();


    @Override
    public Font getFont(Long id, Integer size,Integer fontStyle) {
        int fontType = Font.PLAIN;
        if(fontStyle.equals(1)){
            fontType = Font.BOLD;
        }else if(fontStyle.equals(2)){
            fontType = Font.ITALIC;
        }else if(fontStyle.equals(3)){
            fontType = Font.BOLD + Font.ITALIC;
        }
        if(id == null || id.equals(0L)){
            return DEFAULT_FONT.deriveFont(fontType,(float)size);
        }
        WePosterFont posterFont = this.getById(id);
        if(posterFont == null){
            throw new RuntimeException("字体id为："+id+"的字体不存在");
        }
        Font font;
        if((font=FONT_MAP.get(posterFont.getId())) != null){
            return font.deriveFont((float)size);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(NetFileUtils.getByteArrayOutputStream(NetFileUtils.getNetFile(posterFont.getFontUrl()),false).toByteArray());
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, byteArrayInputStream);
            FONT_MAP.put(posterFont.getId(),font);
            font = font.deriveFont(fontType,(float)size);
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
