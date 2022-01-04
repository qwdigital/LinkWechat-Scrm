package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.wecom.domain.WeMaterial;
import com.linkwechat.wecom.domain.WePoster;

import java.util.List;

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
     * 列表查询海报
     * @param categoryId
     * @param name
     * @return
     */
    public List<WePoster> list(Long categoryId, String name);


    /**
     * 生成海报图片地址
     * @param poster
     * @return
     */
    public String generateSimpleImg(WePoster poster);


    List<WeMaterial> findWePosterToWeMaterial(String categoryId,String name,Integer status);

}
