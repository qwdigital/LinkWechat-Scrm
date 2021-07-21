package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeChatCollection;
import com.linkwechat.wecom.domain.vo.WeChatSideVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 聊天工具 侧边栏栏 素材收藏
 *
 * @author kwen
 */
public interface WeChatCollectionMapper extends BaseMapper<WeChatCollection> {

    /**
     * 添加收藏
     *
     * @param chatCollection 收藏的素材信息
     * @return 结果
     */
    public int addCollection(WeChatCollection chatCollection);

    /**
     * 取消收藏
     *
     * @param materialId 素材id
     * @param userId 用户id
     * @return 结果
     */
    public int dropCollection(@Param("materialId") Long materialId,@Param("userId") String userId);

    /**
     * 通过素材id和用户id查询当前素材的收藏条数
     *
     * @param materialId 素材id
     * @param userId 用户id
     * @return 当前素材收藏的条数
     */
    public int countCollectionByMaterialIdAndUserId(@Param("materialId") Long materialId,@Param("userId") String userId);

    /**
     * 收藏列表
     *
     * @param userId 用户id
     * @return {@link WeChatSideVo}s
     */
    public List<WeChatSideVo> findCollections(@Param("userId") String userId,@Param("keyword") String keyword);

}
