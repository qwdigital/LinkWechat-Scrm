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

    WeMaterial generateSimpleImg(WePoster poster);

    /**
     * 生成海报
     *
     * @author WangYX
     * @date 2022/11/04 16:58
     * @version 1.0.0
     */
    FileEntity createPoster(PurePoster purePoster);

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
     * 获取素材信息,不携带租户Id查询
     *
     * @param id
     * @return
     */
    WeMaterial getMaterialByIdWithOutTenantId(Long id);


    /**
     * 消息模版图片转化获取MediaId
     * @param messageTemplates
     * @return
     */
    List<WeMessageTemplate> msgTplToMediaId(List<WeMessageTemplate> messageTemplates);


    List<WeMaterial> getWeMaterialListByTalkId(Long talkId);



    /**
     * 获取话术素材的列表
     *
     * @param contentDetailQuery
     * @return
     */
    List<WeMaterialAnalyseVo> selectMaterialsByTalkId(ContentDetailQuery contentDetailQuery);
}
