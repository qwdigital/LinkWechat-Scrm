package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WePoster;

/**
 * 海报
 * @author ws
 */
public interface IWePosterService extends IService<WePoster> {

    /**
     * 查询一条
     * @param id
     * @return
     */
    public WePoster selectOne(Long id);


    /**
     * 生成海报图片地址
     * @param poster
     * @return
     */
    public String generateSimpleImg(WePoster poster);

}
