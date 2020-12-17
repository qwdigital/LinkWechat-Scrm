package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WePosterSubassembly;
import com.linkwechat.wecom.mapper.WePosterSubassemblyMapper;
import com.linkwechat.wecom.service.IWePosterSubassemblyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 海报字体
 * @author ws
 */
@Service
public class WePosterSubassemblyServiceImpl extends ServiceImpl<WePosterSubassemblyMapper, WePosterSubassembly> implements IWePosterSubassemblyService {

    @Resource
    private WePosterSubassemblyMapper wePosterSubassemblyMapper;

}
