package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.wecom.domain.WeChatItem;
import com.linkwechat.wecom.domain.WeChatSide;
import com.linkwechat.wecom.domain.WeMaterial;
import com.linkwechat.wecom.domain.dto.WeChatItemDto;
import com.linkwechat.wecom.domain.vo.WeChatSideVo;
import com.linkwechat.wecom.mapper.WeChatItemMapper;
import com.linkwechat.wecom.mapper.WeChatSideMapper;
import com.linkwechat.wecom.service.IWeChatItemService;
import com.linkwechat.wecom.service.IWeMaterialService;
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
    private WeChatItemMapper weChatItemMapper;

    @Autowired
    private IWeMaterialService weMaterialService;

    @Autowired
    private WeChatSideMapper weChatSideMapper;

    @Override
    public int checkItems(WeChatItemDto chatItemDto) {
        List<Long> materialIds = new ArrayList<>();
        if ("0".equals(chatItemDto.getCheckAll())) {
            List<WeMaterial> weMaterials = weMaterialService.findWeMaterials(null, null, chatItemDto.getMediaType());
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
                        item.setItemId(SnowFlakeUtil.nextId());
                        item.setMaterialId(materialId);
                        item.setSideId(chatItemDto.getSideId());
                        items.add(item);
                    });
                    weChatItemMapper.dropItem(chatItemDto.getSideId());
                    if (CollectionUtils.isNotEmpty(items)) {
                        weChatItemMapper.addItem(items);
                    }
                    checkItemsTotal(chatItemDto, finalMaterialIds);
                }
        );
        return 1;
    }

    @Override
    public List<WeChatSideVo> chatItems(Long sideId, String keyword, String mediaType, String userId) {
        return weChatItemMapper.findChatItems(sideId, keyword, mediaType, userId);
    }

    @Override
    public List<WeChatSideVo> findChatPostsItems(Long sideId, String keyword, String mediaType, String userId) {
        return weChatItemMapper.findChatPostsItems(sideId,keyword,mediaType,userId);
    }

    /**
     * 更新该次选择的素材数
     *
     * @param chatItemDto      侧边栏信息
     * @param finalMaterialIds 素材id列表
     */
    private void checkItemsTotal(WeChatItemDto chatItemDto, List<Long> finalMaterialIds) {
        WeChatSide side = new WeChatSide();
        side.setSideId(chatItemDto.getSideId());
        side.setTotal(finalMaterialIds.size());
        weChatSideMapper.updateWeChatSideById(side);
    }
}
