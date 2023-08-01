package com.linkwechat.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.query.LinkMediaQuery;
import com.linkwechat.domain.side.WeChatItem;
import com.linkwechat.domain.side.WeChatSide;
import com.linkwechat.domain.side.dto.WeChatItemDto;
import com.linkwechat.domain.side.vo.WeChatSideVo;
import com.linkwechat.mapper.WeChatItemMapper;
import com.linkwechat.mapper.WeChatSideMapper;
import com.linkwechat.service.IWeChatItemService;
import com.linkwechat.service.IWeMaterialService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 聊天工具侧边栏
 *
 * @author kewen
 */
@Service
public class WeChatItemServiceImpl extends ServiceImpl<WeChatItemMapper, WeChatItem> implements IWeChatItemService {


    @Autowired
    private IWeMaterialService weMaterialService;

    @Autowired
    private WeChatSideMapper weChatSideMapper;

    @Override
    public void checkItems(WeChatItemDto chatItemDto) {
        List<Long> materialIds = new ArrayList<>();
        if ("0".equals(chatItemDto.getCheckAll())) {
            List<WeMaterial> weMaterials = weMaterialService.findWeMaterials(LinkMediaQuery.builder().mediaType(chatItemDto.getMediaType()).build());
            if (CollectionUtils.isNotEmpty(weMaterials)) {
                materialIds = weMaterials.stream().map(WeMaterial::getId).collect(Collectors.toList());
            }
        } else {
            materialIds = chatItemDto.getMaterialIds();
        }
        List<Long> finalMaterialIds = materialIds;
        Optional.ofNullable(materialIds).ifPresent(
                s -> {
                    List<WeChatItem> items = new ArrayList<>();
                    finalMaterialIds.forEach(materialId -> {
                        WeChatItem item = new WeChatItem();
                        item.setId(SnowFlakeUtil.nextId());
                        item.setMaterialId(materialId);
                        item.setSideId(chatItemDto.getSideId());
                        items.add(item);
                    });
                    this.baseMapper.dropItem(chatItemDto.getSideId());
                    if (CollectionUtils.isNotEmpty(items)) {
                        this.baseMapper.addItem(items);
                    }
                    checkItemsTotal(chatItemDto, finalMaterialIds);
                }
        );
    }

    @Override
    public List<WeChatSideVo> chatItems(Long sideId, String keyword, String mediaType, String userId) {
        return this.baseMapper.findChatItems(sideId, keyword, mediaType, userId);
    }


    /**
     * 更新该次选择的素材数
     *
     * @param chatItemDto      侧边栏信息
     * @param finalMaterialIds 素材id列表
     */
    private void checkItemsTotal(WeChatItemDto chatItemDto, List<Long> finalMaterialIds) {
        WeChatSide side = new WeChatSide();
        side.setId(chatItemDto.getSideId());
        side.setTotal(finalMaterialIds.size());
        weChatSideMapper.updateById(side);
    }
}

