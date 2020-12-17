package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.file.FileUploadUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.common.utils.img.ImageUtils;
import com.linkwechat.common.utils.img.NetFileUtils;
import com.linkwechat.wecom.domain.WePoster;
import com.linkwechat.wecom.domain.WePosterFont;
import com.linkwechat.wecom.domain.WePosterSubassembly;
import com.linkwechat.wecom.mapper.WePosterMapper;
import com.linkwechat.wecom.service.IWePosterFontService;
import com.linkwechat.wecom.service.IWePosterService;
import com.linkwechat.wecom.service.IWePosterSubassemblyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 海报字体
 *
 * @author ws
 */
@Service
public class WePosterServiceImpl extends ServiceImpl<WePosterMapper, WePoster> implements IWePosterService {

    @Resource
    private WePosterMapper wePosterMapper;

    @Resource
    private IWePosterSubassemblyService posterSubassemblyService;

    @Resource
    private IWePosterFontService posterFontService;


    /**
     * 查询一条
     *
     * @param id
     * @return
     */
    @Override
    public WePoster selectOne(Long id) {
        WePoster poster = this.lambdaQuery()
                .eq(WePoster::getId, id)
                .eq(WePoster::getDelFlag, 1)
                .getEntity();
        if (poster == null) {
            return null;
        }
        poster.setPosterSubassemblyList(posterSubassemblyService.lambdaQuery().eq(WePosterSubassembly::getPosterId, poster.getId()).list());
        if (!CollectionUtils.isEmpty(poster.getPosterSubassemblyList())) {

            List<Long> fontIdList = poster.getPosterSubassemblyList().stream().filter(wePosterSubassembly -> wePosterSubassembly.getFontId() != null).map(WePosterSubassembly::getFontId).distinct().collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(fontIdList)) {
                List<WePosterFont> fontList = posterFontService.lambdaQuery().in(WePosterFont::getId, fontIdList).list();
                if (!CollectionUtils.isEmpty(fontList)) {
                    Map<Long, WePosterFont> fontMap = fontList.stream().collect(Collectors.toMap(WePosterFont::getId, f -> f));
                    poster.getPosterSubassemblyList().stream().filter(wePosterSubassembly -> wePosterSubassembly.getFontId() != null).forEach(wePosterSubassembly -> {
                        WePosterFont font = fontMap.get(wePosterSubassembly.getFontId());
                        if (font != null) {
                            wePosterSubassembly.setFont(font);
                        }
                    });
                }
            }

        }

        return poster;
    }

    /**
     * 生成海报图片地址
     *
     * @param poster
     * @return
     */
    @Override
    public String generateSimpleImg(WePoster poster) {
        Map<Long, Font> fontMap = poster.getPosterSubassemblyList().stream().filter(wePosterSubassembly -> wePosterSubassembly.getFontId() != null)
                .collect(Collectors.toMap(WePosterSubassembly::getFontId, wePosterSubassembly -> posterFontService.getFont(wePosterSubassembly.getFontId(), wePosterSubassembly.getFontSize())));
        Map<String, NetFileUtils.FileCallable> fileCallableMap = poster.getPosterSubassemblyList().stream().filter(wePosterSubassembly -> StringUtils.isNotBlank(wePosterSubassembly.getImgPath())).distinct().collect(Collectors.toMap(WePosterSubassembly::getImgPath, wePosterSubassembly -> {
            return NetFileUtils.getNetFile(wePosterSubassembly.getImgPath());
        }));
        if (CollectionUtils.isEmpty(fileCallableMap)) {
            fileCallableMap = new HashMap<>();
        }
        fileCallableMap.put(poster.getBackgroundImgPath(), NetFileUtils.getNetFile(poster.getBackgroundImgPath()));
        Map<String, BufferedImage> bufferedImageMap = fileCallableMap.entrySet().stream().collect(Collectors.toMap(stringFileCallableEntry -> stringFileCallableEntry.getKey(), stringFileCallableEntry -> {
            try {
                return ImageIO.read(new ByteArrayInputStream(NetFileUtils.getByteArrayOutputStream(stringFileCallableEntry.getValue(), false).toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("图片读取错误");
            }
        }));
        BufferedImage backgroundImg = bufferedImageMap.get(poster.getBackgroundImgPath());
        poster.getPosterSubassemblyList().forEach(wePosterSubassembly -> {
            if (wePosterSubassembly.getFontId() != null) {
                Font font = fontMap.get(wePosterSubassembly.getFontId());
                FontMetrics fontMetrics = ImageUtils.getFontMetrics(font);
                Color color;
                if (StringUtils.isNotBlank(wePosterSubassembly.getFontColor())) {
                    color = ImageUtils.getColor(wePosterSubassembly.getFontColor());
                } else {
                    color = Color.BLACK;
                }
                List<ImageUtils.LineText> lineTextList = ImageUtils.splitContext(wePosterSubassembly.getContent(), fontMetrics, wePosterSubassembly.getLeft(), wePosterSubassembly.getTop(), wePosterSubassembly.getWidth(), wePosterSubassembly.getHeight(), wePosterSubassembly.getFontTextAlign());
                lineTextList.forEach(lineText -> {
                    ImageUtils.writeFontBufferedImage(backgroundImg, lineText.getText(), lineText.getPointX(), lineText.getPointY(), font, color);
                });


            } else {
                BufferedImage bufferedImage = bufferedImageMap.get(wePosterSubassembly.getImgPath());
                bufferedImage = ImageUtils.fixedDimensionBufferedImage(bufferedImage, BufferedImage.TYPE_INT_RGB, wePosterSubassembly.getWidth(), wePosterSubassembly.getHeight());
                ImageUtils.mergeBufferedImage(backgroundImg, bufferedImage, wePosterSubassembly.getLeft(), wePosterSubassembly.getTop());
            }

        });
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(backgroundImg, "jpg", byteArrayOutputStream);
            ImageUtils.byteToFile(byteArrayOutputStream.toByteArray(),"测试"+System.currentTimeMillis(),"jpg","D:/网页/");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("图片生成错误");
        }

        return null;
    }
}
