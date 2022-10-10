package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.groupcode.entity.WeGroupCodeTagRel;
import com.linkwechat.domain.groupcode.query.WeMakeGroupCodeTagQuery;

public interface IWeGroupCodeTagRelService extends IService<WeGroupCodeTagRel> {

    /**
     * 客群活码标签编辑
     * @param query
     */
    void makeGroupCodeTag(WeMakeGroupCodeTagQuery query);

}
