package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeMaterial;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.vo.WeMaterialFileVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 素材service
 *
 * @author KEWEN
 * @date 2020-10-08
 */
public interface IWeMaterialService {

    /**
     * 上传素材信息
     *
     * @param file 文件
     * @param type 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file)
     * @return {@link WeMaterialFileVO}
     */
    WeMaterialFileVO uploadWeMaterialFile(MultipartFile file, String type);

    /**
     * 添加素材信息
     *
     * @param WeMaterial 素材信息
     * @return
     */
    int insertWeMaterial(WeMaterial WeMaterial);

    /**
     * 删除素材信息
     *
     * @param id 主键id
     * @return {@link int}
     */
    int deleteWeMaterialById(Long id);

    /**
     * 批量删除
     *
     * @param ids id列表
     * @return {@link int}
     */
    int deleteWeMaterialByIds(Long[] ids);

    /**
     * 更新素材信息
     *
     * @param WeMaterial
     * @return
     */
    int updateWeMaterial(WeMaterial WeMaterial);

    /**
     * 查询素材详细信息
     *
     * @param id id
     * @return {@link WeMaterial}
     */
    WeMaterial findWeMaterialById(Long id);

    /**
     * 查询素材列表
     *
     * @param categoryId 类目id
     * @param search     搜索值
     * @return {@link WeMaterial}s
     */
    List<WeMaterial> findWeMaterials(String categoryId, String search,String mediaType);

    /**
     * 更换分组
     *
     * @param categoryId 类目id
     * @param materials  素材id
     * @return int
     */
    void resetCategory(String categoryId, String materials);

    /**
     * 上传企微临时素材
     * @param url 素材路径
     * @param type 素材类型
     * @return
     */
    public WeMediaDto uploadTemporaryMaterial(String url, String type);


    /**
     * 上传企微临时素材
     * @param url 素材路径
     * @param type 素材类型
     * @param name 素材名称
     * @return
     */
    public WeMediaDto uploadTemporaryMaterial(String url, String type,String name);

}
