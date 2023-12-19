package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeKeyWordGroupSub;

import java.util.List;

/**
* @author robin
* @description 针对表【we_key_word_group_sub(关键词群子表)】的数据库操作Service
* @createDate 2023-12-19 12:33:29
*/
public interface IWeKeyWordGroupSubService extends IService<WeKeyWordGroupSub> {

    /**
     * 新建关键词群
     * @param weKeyWordGroupSub
     */
    void createWeKeyWordGroup(WeKeyWordGroupSub weKeyWordGroupSub);


    /**
     * 更新关键词群
     * @param weKeyWordGroupSub
     */
    void updateWeKeyWordGroup(WeKeyWordGroupSub weKeyWordGroupSub);


    /**
     * 通过主键批量删除关键词群
     * @param keyWordId
     */
    void batchRemoveWeKeyWordGroupByIds(List<Long> keyWordId);


    /**
     * 通过keyWordId删除
     * @param keyWordId
     */
    void batchRemoveWeKeyWordGroupByKeyWordId(Long keyWordId);
}
