package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.moments.entity.WeMomentsInteracte;

import java.util.List;

public interface WeMomentsInteracteService extends IService<WeMomentsInteracte> {
    void batchAddOrUpdate(List<WeMomentsInteracte> weMomentsInteracteList);
}
