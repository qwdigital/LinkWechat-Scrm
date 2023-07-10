package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.domain.material.ao.PurePoster;
import com.linkwechat.domain.material.ao.WePoster;
import com.linkwechat.domain.material.ao.WePosterFontAO;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.query.ContentDetailQuery;
import com.linkwechat.domain.material.query.LinkMediaQuery;
import com.linkwechat.domain.material.vo.*;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author leejoker
 * @date 2022/3/29 22:47
 */
public interface IWeMaterialService extends IService<WeMaterial> {

    WeMaterial findWeMaterialById(Long id);

    boolean deleteWeMaterialByIds(Long[] ids);

    /**
     * 上传素材信息
     *
     * @param file 文件
     * @param type 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file)
     * @return {@link WeMaterialFileVo}
     */
    WeMaterialFileVo uploadWeMaterialFile(MultipartFile file, String type);

    void resetCategory(String categoryId, String materials);

    WeMediaVo uploadTemporaryMaterial(String url, String type, String name);

    WeMediaVo uploadWebhookMaterial(String key, String url, String type, String name);


//    /**
//     *  构建海报图片(旧)
//     * @param poster
//     * @return
//     */
//    WeMaterial generateSimpleImg(WePoster poster);


    /**
     *  构建海报图片升级（新）
     * @param poster
     * @return
     */
    WeMaterial builderSimpleImg(WePoster poster) throws Exception;


    /**
     * 构建海报（基于https://gitee.com/dromara/image-combiner#https://gitee.com/dromara/image-combiner/issues/I4FVGB）
     * @param purePoster
     * @return
     */
    FileEntity builderPoster(PurePoster purePoster) throws Exception;


//    /**
//     * 生成海报
//     *
//     * @author WangYX
//     * @date 2022/11/04 16:58
//     * @version 1.0.0
//     */
//    FileEntity createPoster(PurePoster purePoster);

    List<WePosterFontAO> getFontList(BaseEntity query);

    /**
     * 上传企微图片素材
     *
     * @param file
     * @return
     */
    WeMediaVo uploadImg(MultipartFile file);

    /**
     * 素材列表
     *
     * @param query
     * @return
     */
    List<WeMaterial> findWeMaterials(LinkMediaQuery query);

    /**
     * 获取附件mediaid
     *
     * @param url
     * @param type
     * @param attachmentType 1：朋友圈；2:商品图册
     * @param name
     * @return
     */
    WeMediaVo uploadAttachmentMaterial(String url, String type, Integer attachmentType, String name);


    /**
     * 下载服务器素材
     *
     * @param media_id
     * @param fileType
     * @param extentType
     * @return
     */
    String mediaGet(String media_id, String fileType, String extentType);

    /**
     * 获取素材
     *
     * @param mediaId 素材id
     * @return
     */
    WeMediaVo getMediaToResponse(String mediaId);

    /**
     * 查询素材列表
     *
     * @param query
     * @return
     */
    List<WeMaterialNewVo> selectListByLkQuery(LinkMediaQuery query);

    /**
     * 数据统计
     *
     * @param contentDetailQuery
     * @return
     */
    WeContentCountVo getWeMaterialCount(ContentDetailQuery contentDetailQuery);


    List<ContentDataDetailVo> getWeMaterialDataCount(ContentDetailQuery contentDetailQuery);

    /**
     * 触达分析 Top5
     *
     * @param contentDetailQuery
     * @return
     */
    List<WeMaterialAnalyseVo> getWeMaterialAnalyseTop(ContentDetailQuery contentDetailQuery);

    List<WeMaterialAnalyseVo> getWeMaterialAnalyseVoListByQuery(ContentDetailQuery contentDetailQuery);


    List<Long> addOrUpdate(WeMaterial material);




    /**
     * 消息模版图片转化获取MediaId
     * @param messageTemplates
     * @return
     */
    List<WeMessageTemplate> msgTplToMediaId(List<WeMessageTemplate> messageTemplates);


    /**
     * 消息模版图片转化获取MediaId(根据CategoryMediaType中的类型)
     * @param messageTemplates
     * @return
     */
    List<WeMessageTemplate> msgTplToMediaIdByCategoryMediaType(List<WeMessageTemplate> messageTemplates);


    List<WeMaterial> getWeMaterialListByTalkId(Long talkId);



    /**
     * 获取话术素材的列表
     *
     * @param contentDetailQuery
     * @return
     */
    List<WeMaterialAnalyseVo> selectMaterialsByTalkId(ContentDetailQuery contentDetailQuery);

    /**
     * 导出文本素材的模板
     *
     * @param
     * @return
     * @author WangYX
     * @date 2022/12/16 12:29
     */
    void importTemplate() throws IOException;

    /**
     * 文本素材数据导入
     *
     * @param file
     * @return
     * @author WangYX
     * @date 2022/12/16 12:31
     */
    String importData(MultipartFile file) throws IOException;


    /**
     * 替换海报中占位符,生成实际二维码
     * @param actualCodeUrl
     * @param posterId
     * @return
     */
    WeMaterial builderPosterWeMaterial(String actualCodeUrl,Long posterId) throws Exception;




}
