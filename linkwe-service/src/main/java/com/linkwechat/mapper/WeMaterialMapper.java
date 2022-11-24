package com.linkwechat.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.query.ContentDetailQuery;
import com.linkwechat.domain.material.query.LinkMediaQuery;
import com.linkwechat.domain.material.vo.ContentDataDetailVo;
import com.linkwechat.domain.material.vo.WeMaterialAnalyseVo;
import com.linkwechat.domain.material.vo.WeMaterialNewVo;
import com.linkwechat.domain.material.vo.WeMaterialVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author leejoker
 * @date 2022/3/29 22:46
 */
public interface WeMaterialMapper extends BaseMapper<WeMaterial> {
    /**
     * 查询素材列表
     *
     * @param categoryId 类目id
     * @param search     搜索值
     * @param mediaType  0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本
     * @return {@link WeMaterial}s
     */
    @DataScope(type = "2", value = @DataColumn(alias = "wm", name = "create_by_id", userid = "user_id"))
    List<WeMaterial> findWeMaterials(@Param("categoryId") String categoryId, @Param("search") String search,
                                     @Param("mediaType") String mediaType, @Param("status") Integer status);


    /**
     * 根据id列表获取素材Vo列表
     *
     * @param ids 素材id列表
     * @return 结果
     */
    List<WeMaterialVo> findMaterialVoListByIds(Long[] ids);

    /**
     * 查询素材列表
     *
     * @param query
     * @return
     */
    List<WeMaterialNewVo> selectListByLkQuery(LinkMediaQuery query);

    List<WeMaterialAnalyseVo> getWeMaterialAnalyseTop(ContentDetailQuery contentDetailQuery);

    /**
     * 获取话术素材的列表
     *
     * @param contentDetailQuery
     * @return
     */
    List<WeMaterialAnalyseVo> selectMaterialsByTalkId(ContentDetailQuery contentDetailQuery);

    List<ContentDataDetailVo> getWeMaterialDataCountByTalkId(ContentDetailQuery contentDetailQuery);

    List<WeMaterial> getWeMaterialListByTlpId(Long tlpId);

    List<WeMaterial> getWeMaterialListByTalkId(Long talkId);


}
