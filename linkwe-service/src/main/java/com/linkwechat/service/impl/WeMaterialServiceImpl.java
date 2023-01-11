package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.enums.CategoryMediaType;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.common.utils.img.ImageUtils;
import com.linkwechat.common.utils.img.NetFileUtils;
import com.linkwechat.domain.material.ao.*;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.entity.WeTalkMaterial;
import com.linkwechat.domain.material.query.ContentDetailQuery;
import com.linkwechat.domain.material.query.LinkMediaQuery;
import com.linkwechat.domain.material.vo.*;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msgtlp.entity.WeTlpMaterial;
import com.linkwechat.domain.wecom.query.media.WeGetMediaQuery;
import com.linkwechat.domain.wecom.query.media.WeMediaQuery;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.fegin.QwMediaClient;
import com.linkwechat.mapper.WeMaterialMapper;
import com.linkwechat.mapper.WeTalkMaterialMapper;
import com.linkwechat.mapper.WeTlpMaterialMapper;
import com.linkwechat.service.IWeContentSendRecordService;
import com.linkwechat.service.IWeContentViewRecordService;
import com.linkwechat.service.IWeMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author leejoker
 * @date 2022/3/29 22:49
 */
@Service
@Slf4j
public class WeMaterialServiceImpl extends ServiceImpl<WeMaterialMapper, WeMaterial> implements IWeMaterialService {

    @Resource
    private QwFileClient fileClient;
    @Resource
    private QwMediaClient mediaClient;

    @Resource
    private WeMaterialMapper weMaterialMapper;

    @Resource
    private IWeContentSendRecordService weContentSendRecordService;

    @Resource
    private IWeContentViewRecordService weContentViewRecordService;

    @Resource
    private WeTalkMaterialMapper weTalkMaterialMapper;

    @Resource
    private WeTlpMaterialMapper weTlpMaterialMapper;


    private static Font DEFAULT_FONT;

    private final static int TOP_MAX_SIZE = 5;
    private final static String FIELD_NAME_SEND = "sendTotalNum";
    private final static String FIELD_NAME_VIEW = "viewTotalNum";
    private final static String FIELD_NAME_VIEW_BY = "viewByTotalNum";

    static {
        try {
            DEFAULT_FONT = Font.createFont(Font.TRUETYPE_FONT,
                    WeMaterialServiceImpl.class.getResourceAsStream("/font/default.ttf"));
            log.info("字体文件读取成功");
        } catch (FontFormatException e) {
            e.printStackTrace();
            log.info("字体加载失败");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("字体文件读取失败");
        }
    }

    /**
     * 字体数据缓存
     */
    private final Map<Long, Font> FONT_MAP = new ConcurrentHashMap<>();


    @Override
    public WeMaterial findWeMaterialById(Long id) {

        return getById(id);
    }

    @Override
    public boolean deleteWeMaterialByIds(Long[] ids) {
        weTlpMaterialMapper.delete(new LambdaQueryWrapper<WeTlpMaterial>().in(WeTlpMaterial::getMaterialId, Arrays.asList(ids)));
        weTalkMaterialMapper.delete(new LambdaQueryWrapper<WeTalkMaterial>().in(WeTalkMaterial::getMaterialId, Arrays.asList(ids)));
        return new LambdaUpdateChainWrapper<>(this.baseMapper).set(WeMaterial::getDelFlag, 1)
                .in(WeMaterial::getId, Lists.newArrayList(ids)).update();
    }

    @Override
    public WeMaterialFileVo uploadWeMaterialFile(MultipartFile file, String type) {
        if (null == file) {
            throw new WeComException("文件为空！");
        }
        try {
            //上传临时素材
//            Optional<MediaType> mediaType = MediaType.of(type);
//            if (!mediaType.isPresent()) {
//                throw new WeComException("媒体类型出错！");
//            }
            //构造返回结果
            AjaxResult<FileEntity> result = fileClient.upload(file);
            if (result != null && result.getData() != null) {
                FileEntity entity = result.getData();
                return WeMaterialFileVo.builder().materialUrl(entity.getUrl()).materialName(entity.getName()).build();
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new WeComException(e.getMessage());
        }
        return null;
    }

    @Override
    public void resetCategory(String categoryId, String materials) {
        List<String> materialList = Arrays.asList(
                StringUtils.splitByWholeSeparatorPreserveAllTokens(materials, WeConstans.COMMA));
        if (CollectionUtil.isNotEmpty(materialList)) {
            Set<Long> ids = materialList.stream().map(Long::parseLong).collect(Collectors.toSet());
            Map<Long, WeMaterial> map = findWeMaterialByIds(ids).stream()
                    .collect(Collectors.toMap(WeMaterial::getId, Function.identity()));
            new LambdaUpdateChainWrapper<>(this.baseMapper).set(WeMaterial::getCategoryId, categoryId)
                    .in(WeMaterial::getId, map.keySet()).update();
            ids.removeAll(map.keySet());
            if (CollectionUtils.isNotEmpty(ids)) {
                List<WeMaterial> list = ids.stream()
                        .map(id -> WeMaterial.builder().categoryId(Long.parseLong(categoryId)).id(id).build())
                        .collect(Collectors.toList());
                saveBatch(list);
            }
        }
    }

    @Override
    public WeMediaVo uploadTemporaryMaterial(String url, String type, String name) {
        return mediaClient.upload(new WeMediaQuery(type, url, name)).getData();
    }

    @Override
    public WeMediaVo uploadWebhookMaterial(String key,String url, String type, String name) {
        return mediaClient.webhookUpload(new WeMediaQuery(key, type, url, name)).getData();
    }


    /**
     * 生成海报图片地址(合成图片+文字)
     *
     * @param poster
     * @return
     */
    @Override
    public WeMaterial generateSimpleImg(WePoster poster) {
        if (CollectionUtils.isEmpty(poster.getPosterSubassemblyList())) {
            return generateMaterialFromPoster(poster);
        }

        Set<String> existFontId = new HashSet<>();
        Map<String, Font> fontMap = generateFontMap(poster.getPosterSubassemblyList(), existFontId);

        Map<String, NetFileUtils.FileCallable> fileCallableMap = poster.getPosterSubassemblyList()
                .stream()
                .map(WePosterSubassembly::getImgPath)
                .filter(org.apache.commons.lang3.StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toMap(s -> s, NetFileUtils::getNetFile));

        if (org.springframework.util.CollectionUtils.isEmpty(fileCallableMap)) {
            fileCallableMap = new HashMap<>();
        }

        fileCallableMap.put(poster.getBackgroundImgPath(), NetFileUtils.getNetFile(poster.getBackgroundImgPath()));

        Map<String, BufferedImage> bufferedImageMap = fileCallableMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, stringFileCallableEntry -> {
                    try {
                        ByteArrayOutputStream byteArrayOutputStream = NetFileUtils.getByteArrayOutputStream(
                                stringFileCallableEntry.getValue(), false);
                        BufferedImage read = ImageIO.read(
                                new ByteArrayInputStream(Objects.requireNonNull(byteArrayOutputStream).toByteArray()));

                        return ImageUtils.copyBufferedImage(read,
                                BufferedImage.TYPE_INT_ARGB);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("图片读取错误");
                    }
                }));

        BufferedImage backgroundImg = bufferedImageMap.get(poster.getBackgroundImgPath());
        poster.setWidth(backgroundImg.getWidth());
        poster.setHeight(backgroundImg.getHeight());

        poster.getPosterSubassemblyList().forEach(wePosterSubassembly -> {
            if (wePosterSubassembly.getType().equals(1)) {
                Font font = fontMap.get(
                        wePosterSubassembly.getFontId() + "_" + wePosterSubassembly.getFontSize() + "_" + wePosterSubassembly.getFontStyle());
                FontMetrics fontMetrics = ImageUtils.getFontMetrics(font);
                Color color;
                if (org.apache.commons.lang3.StringUtils.isNotBlank(wePosterSubassembly.getFontColor())) {
                    color = ImageUtils.getColor(wePosterSubassembly.getFontColor(), wePosterSubassembly.getAlpha());
                } else {
                    color = Color.BLACK;
                }
                List<ImageUtils.LineText> lineTextList = ImageUtils.splitContext(wePosterSubassembly.getContent(),
                        fontMetrics, wePosterSubassembly.getLeft(), wePosterSubassembly.getTop(),
                        wePosterSubassembly.getWidth(), wePosterSubassembly.getHeight(),
                        wePosterSubassembly.getWordSpace(), wePosterSubassembly.getLineSpace(),
                        wePosterSubassembly.getFontTextAlign(), wePosterSubassembly.getVerticalType());
                lineTextList.forEach(lineText -> {
                    lineText.getCharTextList().forEach(charText -> {
                        ImageUtils.writeFontBufferedImage(backgroundImg, charText.getValue().toString(),
                                charText.getPointX(), charText.getPointY(), font, color);
                    });
                });
            } else {
                BufferedImage bufferedImage = bufferedImageMap.get(wePosterSubassembly.getImgPath());
                if (wePosterSubassembly.getAlpha() != null && wePosterSubassembly.getAlpha() >= 0) {
                    bufferedImage = ImageUtils.setBufferedImageAlpha(bufferedImage, wePosterSubassembly.getAlpha(),
                            BufferedImage.TYPE_INT_ARGB);
                }
                bufferedImage = ImageUtils.fixedDimensionBufferedImage(bufferedImage, BufferedImage.TYPE_INT_ARGB,
                        wePosterSubassembly.getWidth(), wePosterSubassembly.getHeight());
                if (wePosterSubassembly.getRotate() == null) {
                    ImageUtils.mergeBufferedImage(backgroundImg, bufferedImage, wePosterSubassembly.getLeft(),
                            wePosterSubassembly.getTop());
                } else {
                    int x = wePosterSubassembly.getLeft() + bufferedImage.getWidth() / 2;
                    int y = wePosterSubassembly.getTop() + bufferedImage.getHeight() / 2;
                    bufferedImage = ImageUtils.rotateImage(bufferedImage, wePosterSubassembly.getRotate());
                    x = x - bufferedImage.getWidth() / 2;
                    y = y - bufferedImage.getHeight() / 2;
                    ImageUtils.mergeBufferedImage(backgroundImg, bufferedImage, x, y);
                }
            }
        });

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(backgroundImg, "png", byteArrayOutputStream);
            NetFileUtils.StreamMultipartFile streamMultipartFile = new NetFileUtils.StreamMultipartFile(System.currentTimeMillis() + ".jpg", byteArrayOutputStream.toByteArray());
            byteArrayOutputStream.close();
            AjaxResult<FileEntity> result = fileClient.upload(streamMultipartFile);
            if (result != null) {
                poster.setSampleImgPath(result.getData().getUrl());
            }
            WeMaterial material = generateMaterialFromPoster(poster);
            material.setPosterSubassembly(JSONArray.toJSONString(poster.getPosterSubassemblyList()));
            return material;
        } catch (IOException e) {
            e.printStackTrace();
            throw new WeComException("图片生成错误");
        }
    }

    @Override
    public FileEntity createPoster(PurePoster purePoster) {
        //海报组件数组为空，直接返回图片
        if (CollectionUtils.isEmpty(purePoster.getPosterSubassemblyList())) {
            FileEntity sysFile = new FileEntity();
            sysFile.setName(FileUtils.getName(purePoster.getBackgroundImgPath()));
            sysFile.setUrl(purePoster.getBackgroundImgPath());
            return sysFile;
        }

        Set<String> existFontId = new HashSet<>();
        Map<String, Font> fontMap = generateFontMap(purePoster.getPosterSubassemblyList(), existFontId);
        Map<String, NetFileUtils.FileCallable> fileCallableMap = purePoster.getPosterSubassemblyList().stream().map(WePosterSubassembly::getImgPath).filter(org.apache.commons.lang3.StringUtils::isNotBlank)
                .distinct().collect(Collectors.toMap(s -> s, NetFileUtils::getNetFile));
        if (org.springframework.util.CollectionUtils.isEmpty(fileCallableMap)) {
            fileCallableMap = new HashMap<>();
        }
        Map<String, BufferedImage> bufferedImageMap = fileCallableMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, stringFileCallableEntry -> {
                    try {
                        ByteArrayOutputStream byteArrayOutputStream = NetFileUtils.getByteArrayOutputStream(stringFileCallableEntry.getValue(), false);
                        BufferedImage read = ImageIO.read(new ByteArrayInputStream(Objects.requireNonNull(byteArrayOutputStream).toByteArray()));
                        return ImageUtils.copyBufferedImage(read, BufferedImage.TYPE_INT_ARGB);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("图片读取错误");
                    }
                }));
        BufferedImage backgroundImg = bufferedImageMap.get(purePoster.getBackgroundImgPath());
        purePoster.setWidth(backgroundImg.getWidth());
        purePoster.setHeight(backgroundImg.getHeight());
        purePoster.getPosterSubassemblyList().forEach(wePosterSubassembly -> {
            if (wePosterSubassembly.getType().equals(1)) {
                Font font = fontMap.get(
                        wePosterSubassembly.getFontId() + "_" + wePosterSubassembly.getFontSize() + "_" + wePosterSubassembly.getFontStyle());
                FontMetrics fontMetrics = ImageUtils.getFontMetrics(font);
                Color color;
                if (org.apache.commons.lang3.StringUtils.isNotBlank(wePosterSubassembly.getFontColor())) {
                    color = ImageUtils.getColor(wePosterSubassembly.getFontColor(), wePosterSubassembly.getAlpha());
                } else {
                    color = Color.BLACK;
                }
                List<ImageUtils.LineText> lineTextList = ImageUtils.splitContext(wePosterSubassembly.getContent(),
                        fontMetrics, wePosterSubassembly.getLeft(), wePosterSubassembly.getTop(),
                        wePosterSubassembly.getWidth(), wePosterSubassembly.getHeight(),
                        wePosterSubassembly.getWordSpace(), wePosterSubassembly.getLineSpace(),
                        wePosterSubassembly.getFontTextAlign(), wePosterSubassembly.getVerticalType());
                lineTextList.forEach(lineText -> {
                    lineText.getCharTextList().forEach(charText -> {
                        ImageUtils.writeFontBufferedImage(backgroundImg, charText.getValue().toString(),
                                charText.getPointX(), charText.getPointY(), font, color);
                    });
                });
            } else {
                BufferedImage bufferedImage = bufferedImageMap.get(wePosterSubassembly.getImgPath());
                if (wePosterSubassembly.getAlpha() != null && wePosterSubassembly.getAlpha() >= 0) {
                    bufferedImage = ImageUtils.setBufferedImageAlpha(bufferedImage, wePosterSubassembly.getAlpha(),
                            BufferedImage.TYPE_INT_ARGB);
                }
                bufferedImage = ImageUtils.fixedDimensionBufferedImage(bufferedImage, BufferedImage.TYPE_INT_ARGB,
                        wePosterSubassembly.getWidth(), wePosterSubassembly.getHeight());
                if (wePosterSubassembly.getRotate() == null) {
                    ImageUtils.mergeBufferedImage(backgroundImg, bufferedImage, wePosterSubassembly.getLeft(),
                            wePosterSubassembly.getTop());
                } else {
                    int x = wePosterSubassembly.getLeft() + bufferedImage.getWidth() / 2;
                    int y = wePosterSubassembly.getTop() + bufferedImage.getHeight() / 2;
                    bufferedImage = ImageUtils.rotateImage(bufferedImage, wePosterSubassembly.getRotate());
                    x = x - bufferedImage.getWidth() / 2;
                    y = y - bufferedImage.getHeight() / 2;
                    ImageUtils.mergeBufferedImage(backgroundImg, bufferedImage, x, y);
                }
            }
        });
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(backgroundImg, "png", byteArrayOutputStream);
            NetFileUtils.StreamMultipartFile streamMultipartFile = new NetFileUtils.StreamMultipartFile(System.currentTimeMillis() + ".jpg", byteArrayOutputStream.toByteArray());
            byteArrayOutputStream.close();
            AjaxResult<FileEntity> result = fileClient.upload(streamMultipartFile);
            return result.getData();
        } catch (IOException e) {
            e.printStackTrace();
            throw new WeComException("图片生成错误");
        }
    }

    @Override
    @DataScope(type = "2", value = @DataColumn(alias = "we_material", name = "create_by_id", userid = "user_id"))
    public List<WePosterFontAO> getFontList(BaseEntity query) {
        List<WeMaterial> materials = lambdaQuery().eq(WeMaterial::getMediaType, MediaType.POSTER_FONT.getType())
                .apply("" + query.getParams().get("dataScope") + "").orderByDesc(WeMaterial::getFrontOrder)
                .orderByDesc(WeMaterial::getCreateTime).list();
        return materials.stream().map(m -> {
            WePosterFontAO ao = BeanUtil.copyProperties(m, WePosterFontAO.class);
            ao.setFontName(m.getMaterialName());
            ao.setFontUrl(m.getMaterialUrl());
            return ao;
        }).collect(Collectors.toList());
    }

    @Override
    public WeMediaVo uploadImg(MultipartFile file) {
        return mediaClient.uploadImg(new WeMediaQuery(file)).getData();
    }

    @Override
    @DataScope(type = "2", value = @DataColumn(alias = "we_material", name = "create_by_id", userid = "user_id"))
    public List<WeMaterial> findWeMaterials(LinkMediaQuery query) {
        LambdaQueryWrapper<WeMaterial> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(query.getCategoryId())) {
            wrapper.eq(WeMaterial::getCategoryId, query.getCategoryId());
        }
        if (StringUtils.isNotBlank(query.getSearch())) {
            wrapper.and(w -> w.like(WeMaterial::getMaterialName, query.getSearch()).or()
                    .like(WeMaterial::getContent, query.getSearch()));
        }
        if (StringUtils.isNotBlank(query.getMediaType())) {
            wrapper.eq(WeMaterial::getMediaType, query.getMediaType());
        }
        if (query.getStatus() != null) {
            wrapper.eq(WeMaterial::getMaterialStatus, query.getStatus());
        }

        if (StringUtils.isNotEmpty(query.getParams().get("dataScope").toString())) {
            wrapper.apply("" + query.getParams().get("dataScope").toString() + "");
        }

        return list(wrapper);
    }

    @Override
    public WeMediaVo uploadAttachmentMaterial(String url, String type, Integer attachmentType, String name) {
        WeMediaQuery weMediaQuery = new WeMediaQuery();
        weMediaQuery.setUrl(url);
        weMediaQuery.setType(type);
        weMediaQuery.setAttachmentType(attachmentType);
        weMediaQuery.setName(name);
        AjaxResult<WeMediaVo> result = mediaClient.uploadAttachment(weMediaQuery);
        return result != null && null !=result.getData() ? result.getData() : null;
    }

    /**
     * 下载服务器素材
     *
     * @param media_id
     * @param fileType
     * @return
     */
    @Override
    public String mediaGet(String media_id, String fileType, String extentType) {

        byte[] data = mediaClient.mediaGet(new WeMediaQuery(media_id)).getData();


        String fileName = String.valueOf(SnowFlakeUtil.nextId());
        MockMultipartFile mockMultipartFile = new MockMultipartFile(fileName, fileName + "." + extentType, "text/plain",
                data);

        try {
            WeMaterialFileVo weMaterialFileVO = this.uploadWeMaterialFile(mockMultipartFile, fileType);
            return weMaterialFileVO.getMaterialUrl();
        } catch (Exception e) {
            log.error("朋友圈资源获取失败:" + e.getMessage());
        }

        return null;
    }


    // ---------------------------------------- private functions ----------------------------------------------
    private WeMaterial generateMaterialFromPoster(WePoster poster) {
        poster.setBackgroundImgPath(poster.getBackgroundImgPath());
        WeMaterial m = BeanUtil.copyProperties(poster, WeMaterial.class);
        if (StringUtils.isBlank(poster.getSampleImgPath())) {
            m.setMaterialUrl(poster.getBackgroundImgPath());
        } else {
            m.setMaterialUrl(poster.getSampleImgPath());
        }
        m.setBackgroundImgUrl(poster.getBackgroundImgPath());
        m.setMaterialName(StringUtils.isBlank(poster.getTitle()) ? poster.getDigest() : poster.getTitle());
        return m;
    }

    private Map<String, Font> generateFontMap(List<WePosterSubassembly> list, Set<String> existFontId) {
        Map<String, Font> fontMap = list.stream()
                .filter(wePosterSubassembly -> wePosterSubassembly.getType().equals(1)).peek(wePosterSubassembly -> {
                    if (wePosterSubassembly.getFontId() == null) {
                        wePosterSubassembly.setFontId(0L);
                    }
                }).filter(wePosterSubassembly -> {
                    if (existFontId.contains(
                            wePosterSubassembly.getFontId() + "_" + wePosterSubassembly.getFontSize() + "_" + wePosterSubassembly.getFontStyle())) {
                        return false;
                    } else {
                        existFontId.add(
                                wePosterSubassembly.getFontId() + "_" + wePosterSubassembly.getFontSize() + "_" + wePosterSubassembly.getFontStyle());
                        return true;
                    }
                }).collect(Collectors.toMap(
                        wePosterSubassembly -> wePosterSubassembly.getFontId() + "_" + wePosterSubassembly.getFontSize() + "_" + wePosterSubassembly.getFontStyle(),
                        wePosterSubassembly -> getFont(wePosterSubassembly.getFontId(),
                                wePosterSubassembly.getFontSize(), wePosterSubassembly.getFontStyle())));
        return fontMap;
    }

    private List<WeMaterial> findWeMaterialByIds(Collection<Long> ids) {
        return new LambdaQueryChainWrapper<>(this.baseMapper).in(WeMaterial::getId, ids).eq(WeMaterial::getDelFlag, 0)
                .list();
    }

    private Font getFont(Long id, Integer fontSize, Integer fontStyle) {
        List<WeMaterial> materials = lambdaQuery().eq(WeMaterial::getMediaType, MediaType.POSTER_FONT.getType())
                .orderByDesc(WeMaterial::getFrontOrder).orderByDesc(WeMaterial::getCreateTime).eq(WeMaterial::getId, id)
                .list();
        WeMaterial fontMaterial = null;
        if (CollectionUtils.isNotEmpty(materials)) {
            fontMaterial = materials.get(0);
        }
        return getFont(fontMaterial, fontStyle, fontSize);
    }

    private Font getFont(WeMaterial fontMaterial, Integer fontStyle, Integer fontSize) {
        int fontType = Font.PLAIN;
        if (fontStyle.equals(1)) {
            fontType = Font.BOLD;
        } else if (fontStyle.equals(2)) {
            fontType = Font.ITALIC;
        } else if (fontStyle.equals(3)) {
            fontType = Font.BOLD + Font.ITALIC;
        }
        if (fontMaterial == null) {
            return DEFAULT_FONT.deriveFont(fontType, (float) fontSize);
        }
        Font font;
        if ((font = FONT_MAP.get(fontMaterial.getId())) != null) {
            return font.deriveFont((float) fontSize);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                NetFileUtils.getByteArrayOutputStream(NetFileUtils.getNetFile(fontMaterial.getMaterialUrl()), false)
                        .toByteArray());
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, byteArrayInputStream);
            FONT_MAP.put(fontMaterial.getId(), font);
            font = font.deriveFont(fontType, (float) fontSize);
            return font;
        } catch (FontFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("字体格式错误");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public WeMediaVo getMediaToResponse(String mediaId) {
        WeMediaVo weMediaVo = mediaClient.getMedia(new WeGetMediaQuery(mediaId)).getData();
        MockMultipartFile multipartFile = new MockMultipartFile(FileUtil.getPrefix(weMediaVo.getFileName()),
                weMediaVo.getFileName(), "text/plain", weMediaVo.getBytes());
        FileEntity data = fileClient.upload(multipartFile).getData();
        weMediaVo.setUrl(data.getUrl());
        return weMediaVo;
    }

    /**
     * 查询素材列表
     *
     * @param query
     * @return
     */
    @Override
    public List<WeMaterialNewVo> selectListByLkQuery(LinkMediaQuery query) {
        return weMaterialMapper.selectListByLkQuery(query);
    }

    @Override
    public WeContentCountVo getWeMaterialCount(ContentDetailQuery contentDetailQuery) {
        WeContentCountVo weContentCountVo = new WeContentCountVo();
        //发送次数统计
        weContentSendRecordService.setWeContentCountVoForSendRecord(contentDetailQuery, weContentCountVo);
        //查看次数统计
        weContentViewRecordService.setWeContentCountVoForViewRecord(contentDetailQuery, weContentCountVo);
        return weContentCountVo;
    }


    @Override
    public List<ContentDataDetailVo> getWeMaterialDataCount(ContentDetailQuery contentDetailQuery) {
        Integer detailsType = contentDetailQuery.getDetailsType();
        if (StringUtils.isNotBlank(contentDetailQuery.getBeginTime())) {
            contentDetailQuery.setBeginTime(DateUtils.initSqlBeginTime(contentDetailQuery.getBeginTime()));
        }
        if (StringUtils.isNotBlank(contentDetailQuery.getEndTime())) {
            contentDetailQuery.setEndTime(DateUtils.initSqlEndTime(contentDetailQuery.getEndTime()));
        }
        List<ContentDataDetailVo> result = new ArrayList<>();
        switch (detailsType) {
            case 1:
                //发送明细
                result = weContentSendRecordService.getSendDetail(contentDetailQuery);
                break;
            case 2:
                //查看明细
                result = weContentViewRecordService.getViewDetail(contentDetailQuery);
                result.forEach(contentDataDetailVo -> {
                    //秒数
                    Integer viewDuration = contentDataDetailVo.getViewDuration() / 1000;

                    //小时
                    Integer hour = viewDuration / 3600;
                    //剩余秒数
                    viewDuration = viewDuration % 3600;

                    //分钟
                    Integer minutes = viewDuration / 60;
                    //剩余秒数
                    viewDuration = viewDuration % 60;


                    StringBuffer sb = new StringBuffer();
                    sb.append(hour != 0L ? hour + "时" : "");
                    sb.append(minutes != 0L ? minutes + "分" : "");
                    sb.append(viewDuration != 0L ? viewDuration + "秒" : "");
                    contentDataDetailVo.setViewDurationCpt(sb.toString());
                });
                break;
        }
        return result;
    }

    /**
     * 触达分析 Top5
     *
     * @param contentDetailQuery
     * @return
     */
    @Override
    public List<WeMaterialAnalyseVo> getWeMaterialAnalyseTop(ContentDetailQuery contentDetailQuery) {
        Integer moduleType = contentDetailQuery.getModuleType();
        List<WeMaterialAnalyseVo> weMaterialAnalyseVoList = null;
        switch (moduleType) {
            case 1:
                //素材
                weMaterialAnalyseVoList = weMaterialMapper.getWeMaterialAnalyseTop(contentDetailQuery);
                break;
            case 2:
                //话术
                weMaterialAnalyseVoList = getWeMaterialAnalyseVoListByQuery(contentDetailQuery);
                break;
        }
        //数据排序，取前5
        return sortViewBy(weMaterialAnalyseVoList);
    }

    @Override
    public List<WeMaterialAnalyseVo> getWeMaterialAnalyseVoListByQuery(ContentDetailQuery contentDetailQuery) {
        //获取话术素材的列表
        List<WeMaterialAnalyseVo> materialAnalyseVoList = weMaterialMapper.selectMaterialsByTalkId(contentDetailQuery);
        //按照话术Id将话术素材进行分类
        Map<Long, List<WeMaterialAnalyseVo>> map = materialAnalyseVoList.stream().collect(Collectors.groupingBy(WeMaterialAnalyseVo::getTalkId));

        List<WeMaterialAnalyseVo> finalWeMaterialAnalyseVoList = new ArrayList<>();
        map.forEach((id, list) -> {
            WeMaterialAnalyseVo weMaterialAnalyseVo = new WeMaterialAnalyseVo();
            int sendTotalNum = list.stream().mapToInt(WeMaterialAnalyseVo::getSendTotalNum).sum();
            int viewTotalNum = list.stream().mapToInt(WeMaterialAnalyseVo::getViewTotalNum).sum();
            int viewByTotalNum = list.stream().mapToInt(WeMaterialAnalyseVo::getViewByTotalNum).sum();
            weMaterialAnalyseVo.setTalkTitle(list.get(0).getTalkTitle());
            weMaterialAnalyseVo.setId(id);
            weMaterialAnalyseVo.setMaterialNum(list.size());
            weMaterialAnalyseVo.setSendTotalNum(sendTotalNum);
            weMaterialAnalyseVo.setViewTotalNum(viewTotalNum);
            weMaterialAnalyseVo.setViewByTotalNum(viewByTotalNum);
            finalWeMaterialAnalyseVoList.add(weMaterialAnalyseVo);
        });
        return finalWeMaterialAnalyseVoList;
    }

    /**
     * 集合排序
     *
     * @param weMaterialAnalyseVoList 数据集合
     * @return {@link List< WeMaterialAnalyseVo>}
     * @author WangYX
     * @date 2022/10/21 15:45
     */
    private List<WeMaterialAnalyseVo> sortViewBy(List<WeMaterialAnalyseVo> weMaterialAnalyseVoList) {
        weMaterialAnalyseVoList.sort(Comparator.comparing(WeMaterialAnalyseVo::getViewByTotalNum).reversed()
                .thenComparing(WeMaterialAnalyseVo::getViewTotalNum).reversed()
                .thenComparing(WeMaterialAnalyseVo::getSendTotalNum).reversed());
        if (weMaterialAnalyseVoList.size() <= 5) {
            return weMaterialAnalyseVoList;
        }
        return weMaterialAnalyseVoList.subList(0, 5);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> addOrUpdate(WeMaterial material) {
        List<Long> ids = new LinkedList<>();
        if (Objects.equals(CategoryMediaType.IMAGE.getType().toString(), material.getMediaType())) {
            List<WeMaterialImgAo> weMaterialImgAoList = material.getWeMaterialImgAoList();
            if (ObjectUtil.isNotEmpty(weMaterialImgAoList)) {
                weMaterialImgAoList.forEach(weMaterialImgAo -> {
                    WeMaterial weMaterial = new WeMaterial();
                    BeanUtil.copyProperties(material, weMaterial);
                    weMaterial.setMaterialUrl(weMaterialImgAo.getMaterialUrl());
                    weMaterial.setMaterialName(weMaterialImgAo.getMaterialName());
                    saveOrUpdate(weMaterial);
                    ids.add(weMaterial.getId());
                });
                return ids;
            }
        }
        saveOrUpdate(material);
        ids.add(material.getId());
        return ids;
    }



    @Override
    public List<WeMessageTemplate> msgTplToMediaId(List<WeMessageTemplate> messageTemplates) {
        Optional.ofNullable(messageTemplates).orElseGet(ArrayList::new).forEach(messageTemplate -> {
            if (ObjectUtil.equal(MessageType.IMAGE.getMessageType(), messageTemplate.getMsgType())) {
                WeMediaVo weMedia = this.uploadTemporaryMaterial(messageTemplate.getPicUrl()
                        , MessageType.IMAGE.getMessageType()
                        , FileUtil.getName(messageTemplate.getPicUrl()));
                messageTemplate.setMediaId(weMedia.getMediaId());
            } else if (ObjectUtil.equal(MessageType.MINIPROGRAM.getMessageType(), messageTemplate.getMsgType())) {
                WeMediaVo weMedia = this.uploadTemporaryMaterial(messageTemplate.getPicUrl()
                        , MessageType.IMAGE.getMessageType()
                        , FileUtil.getName(messageTemplate.getPicUrl()));
                messageTemplate.setMediaId(weMedia.getMediaId());
            }
//            else if (ObjectUtil.equal(MessageType.VIDEO.getMessageType(), messageTemplate.getMsgType())) {
////                WeMediaVo weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
////                        , MessageType.IMAGE.getMessageType()
////                        , FileUtil.getName(messageTemplate.getMediaId()));
////                messageTemplate.setMediaId(weMedia.getMediaId());
//            } else if (ObjectUtil.equal(MessageType.FILE.getMessageType(), messageTemplate.getMsgType())) {
////                WeMediaVo weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
////                        , MessageType.IMAGE.getMessageType()
////                        , FileUtil.getName(messageTemplate.getMediaId()));
////                messageTemplate.setMediaId(weMedia.getMediaId());
//            }
        });
        return messageTemplates;
    }

    @Override
    public List<WeMaterial> getWeMaterialListByTalkId(Long talkId) {
        return this.baseMapper.getWeMaterialListByTalkId(talkId);
    }

    @Override
    public List<WeMaterialAnalyseVo> selectMaterialsByTalkId(ContentDetailQuery contentDetailQuery) {
        return this.baseMapper.selectMaterialsByTalkId(contentDetailQuery);
    }
}
