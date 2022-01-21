package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeMomentsInteracte;
import java.util.List;

public interface WeMomentsInteracteService extends IService<WeMomentsInteracte> {

    void batchAddOrUpdate(List<WeMomentsInteracte> weMomentsInteracteList);
}
