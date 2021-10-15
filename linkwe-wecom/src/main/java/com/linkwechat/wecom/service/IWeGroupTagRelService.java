package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeGroupTagRel;
import com.linkwechat.wecom.domain.vo.WeMakeGroupTagVo;


public interface IWeGroupTagRelService extends IService<WeGroupTagRel> {

    void makeGroupTag(WeMakeGroupTagVo weMakeGroupTagVo);

}
