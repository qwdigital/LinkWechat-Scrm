package com.linkwechat.wecom.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeChatSide;
import com.linkwechat.wecom.mapper.WeChatSideMapper;
import com.linkwechat.wecom.service.IWeChatSideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 聊天工具侧边栏
 * @author kewen
 */
@Service
public class WeChatSideServiceImpl extends ServiceImpl<WeChatSideMapper, WeChatSide> implements IWeChatSideService {

    @Autowired
    private WeChatSideMapper weChatSideMapper;

    @Override
    public List<WeChatSide> chatSides(String h5) {
        return weChatSideMapper.selectWeChatSides(h5);
    }

    @Override
    public int updateWeChatSide(WeChatSide weChatSide) {
        return weChatSideMapper.updateWeChatSideById(weChatSide);
    }

}
