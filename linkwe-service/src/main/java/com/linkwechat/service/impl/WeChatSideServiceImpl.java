package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.side.WeChatSide;
import com.linkwechat.mapper.WeChatSideMapper;
import com.linkwechat.service.IWeChatSideService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 聊天工具侧边栏
 * @author kewen
 */
@Service
public class WeChatSideServiceImpl extends ServiceImpl<WeChatSideMapper, WeChatSide> implements IWeChatSideService {



    @Override
    public List<WeChatSide> chatSides(String h5) {
        return this.baseMapper.selectWeChatSides(h5);
    }

    @Override
    public int updateWeChatSide(WeChatSide weChatSide) {
        return this.baseMapper.updateWeChatSideById(weChatSide);
    }

}
