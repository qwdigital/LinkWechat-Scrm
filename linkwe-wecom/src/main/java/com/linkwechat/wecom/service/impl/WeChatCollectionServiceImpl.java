package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.exception.wecom.WeComException;
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
    public boolean addCollection(Long materialId, String userId) {
        QueryWrapper<WeChatCollection> wrapper = new QueryWrapper<>();
        wrapper.eq("material_id",materialId).eq("user_id",userId);
        WeChatCollection queryCollection = weChatCollectionMapper.selectOne(wrapper);
        if(null!=queryCollection){
            return false;
        }
        WeChatCollection chatCollection=new WeChatCollection();
        chatCollection.setCollectionId(SnowFlakeUtil.nextId());
        chatCollection.setMaterialId(materialId);
        chatCollection.setUserId(userId);
        weChatCollectionMapper.addCollection(chatCollection);
        return true;
    }

    @Override
    public int cancleCollection(Long materialId, String userId) {
        return weChatCollectionMapper.dropCollection(materialId,userId);
    }

    @Override
    public List<WeChatSideVo> collections(String userId,String keyword) {
        return weChatCollectionMapper.findCollections(userId,keyword);
    }

}
