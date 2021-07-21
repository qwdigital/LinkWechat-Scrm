package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeChatCollection;
import com.linkwechat.wecom.domain.vo.WeChatSideVo;

import java.util.List;

/**
 * 聊天工具 侧边栏栏 素材收藏
 *
 * @author kwen
 */
public interface IWeChatCollectionService extends IService<WeChatCollection> {

    /**
     * 添加收藏
     *
     * @param materialId 素材id
     * @param userId 用户id
     * @return 结果
     */
    public boolean addCollection( Long materialId, String userId);

    /**
     * 取消收藏
     *
     * @param materialId 素材id
     * @param userId 用户id
     * @return 结果
     */
    public int cancleCollection( Long materialId, String userId);

    /**
     * 收藏列表
     *
     * @param userId 用户id
     * @return {@link WeChatSideVo}s
     */
    public List<WeChatSideVo> collections(String userId,String keyword);

}
