package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.material.entity.WeContentTalk;
import com.linkwechat.domain.material.vo.talk.WeContentTalkVo;

import java.util.List;

public interface IWeContentTalkService extends IService<WeContentTalk> {

    /**
     * 保存话术
     *
     * @param weContentTalk
     */
    void saveContentTalk(WeContentTalk weContentTalk);

    /**
     * 分页查询
     *
     * @param weContentTalk
     * @return
     */
    @Deprecated
    List<WeContentTalkVo> selectContentTalkPage(WeContentTalk weContentTalk);


    /**
     * 根据Id获取话术,不加租户Id
     *
     * @param id
     * @return
     */

    WeContentTalk getByIdWithOutTenantId(Long id);


    /**
     * 根据id批量删除
     * @param talkIds
     */
    void del(List<Long> talkIds);


    /**
     * 查询话术列表
     *
     * @param weContentTalk
     * @return
     */
    List<WeContentTalkVo> selectWeContentVoList(WeContentTalk weContentTalk);

}
