package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.wecom.domain.WeChatCollection;
import com.linkwechat.wecom.domain.vo.WeChatSideVo;
import com.linkwechat.wecom.mapper.WeChatCollectionMapper;
import com.linkwechat.wecom.service.IWeChatCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 聊天工具 侧边栏栏 素材收藏
 *
 * @author kwen
 */
@Service
public class WeChatCollectionServiceImpl extends ServiceImpl<WeChatCollectionMapper, WeChatCollection> implements IWeChatCollectionService {

    @Autowired
    private WeChatCollectionMapper weChatCollectionMapper;


    @Override
    public int addCollection(Long materialId, Long userId) {
        WeChatCollection chatCollection=new WeChatCollection();
        chatCollection.setCollectionId(SnowFlakeUtil.nextId());
        chatCollection.setMaterialId(materialId);
        chatCollection.setUserId(userId);
        return weChatCollectionMapper.addCollection(chatCollection);
    }

    @Override
    public int cancleCollection(Long materialId, Long userId) {
        return weChatCollectionMapper.dropCollection(materialId,userId);
    }

    @Override
    public List<WeChatSideVo> collections(Long userId,String keyword) {
        return weChatCollectionMapper.findCollections(userId,keyword);
    }

}
