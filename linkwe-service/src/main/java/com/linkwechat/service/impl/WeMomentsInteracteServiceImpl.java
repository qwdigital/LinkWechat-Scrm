package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.moments.entity.WeMomentsInteracte;
import com.linkwechat.mapper.WeMomentsInteracteMapper;
import com.linkwechat.service.WeMomentsInteracteService;
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
