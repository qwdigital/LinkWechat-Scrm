package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.material.entity.WeContentTalk;
import com.linkwechat.domain.material.vo.talk.WeContentTalkVo;

import java.util.List;

public interface WeContentTalkMapper extends BaseMapper<WeContentTalk> {

    /**
     * 查询话术列表
     *
     * @param weContentTalk
     * @return
     */
    List<WeContentTalkVo> selectWeContentVoList(WeContentTalk weContentTalk);


    /**
     * 根据Id获取话术,不加租户Id
     *
     * @param id
     * @return
     */

    WeContentTalk getByIdWithOutTenantId(Long id);
}
