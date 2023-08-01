package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeKfWelcome;
import com.linkwechat.domain.kf.WeKfWelcomeInfo;

import java.util.List;

/**
 * 客服欢迎语表(WeKfWelcome)
 *
 * @author danmo
 * @since 2022-04-15 15:53:39
 */
public interface IWeKfWelcomeService extends IService<WeKfWelcome> {

    /**
     *  通过客服id获取欢迎语信息
     * @param kfId
     * @return
     */
    List<WeKfWelcomeInfo> getWelcomeByKfId(Long kfId);

    /**
     * 通过客服id删除欢迎语信息
     * @param kfId
     */
    void delWelcomByKfId(Long kfId);
} 
