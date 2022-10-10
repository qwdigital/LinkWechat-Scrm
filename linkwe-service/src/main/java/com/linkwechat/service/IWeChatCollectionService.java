package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeChatCollection;
import com.linkwechat.domain.side.vo.WeChatSideVo;

import java.util.List;

/**
 * 素材收藏表(WeChatCollection)
 *
 * @author danmo
 * @since 2022-05-25 17:56:59
 */
public interface IWeChatCollectionService extends IService<WeChatCollection> {

    List<WeChatSideVo> collections(Long userId, String keyword);

    void addCollection(Long materialId);

    void cancleCollection(Long materialId);
}
