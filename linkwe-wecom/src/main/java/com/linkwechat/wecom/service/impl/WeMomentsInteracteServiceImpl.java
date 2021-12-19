package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeMomentsInteracte;
import com.linkwechat.wecom.mapper.WeMomentsInteracteMapper;
import com.linkwechat.wecom.service.WeMomentsInteracteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeMomentsInteracteServiceImpl extends ServiceImpl<WeMomentsInteracteMapper, WeMomentsInteracte>
implements WeMomentsInteracteService {

    @Override
    public void batchAddOrUpdate(List<WeMomentsInteracte> weMomentsInteracteList) {
         this.baseMapper.batchAddOrUpdate(weMomentsInteracteList);
    }
}
