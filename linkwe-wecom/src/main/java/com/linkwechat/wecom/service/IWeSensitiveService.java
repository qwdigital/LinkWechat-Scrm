package com.linkwechat.wecom.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.linkwechat.wecom.domain.WeSensitive;
import com.linkwechat.wecom.domain.query.WeSensitiveHitQuery;

import java.util.List;

/**
 * 敏感词设置Service接口
 *
 * @author ruoyi
 * @date 2020-12-29
 */
public interface IWeSensitiveService {
    /**
     * 查询敏感词设置
     *
     * @param id 敏感词设置ID
     * @return 敏感词设置
     */
    public WeSensitive selectWeSensitiveById(Long id);

    /**
     * 查询敏感词设置列表
     *
     * @param weSensitive 敏感词设置
     * @return 敏感词设置集合
     */
    public List<WeSensitive> selectWeSensitiveList(WeSensitive weSensitive);

    /**
     * 新增敏感词设置
     *
     * @param weSensitive 敏感词设置
     * @return 结果
     */
    public int insertWeSensitive(WeSensitive weSensitive);

    /**
     * 修改敏感词设置
     *
     * @param weSensitive 敏感词设置
     * @return 结果
     */
    public int updateWeSensitive(WeSensitive weSensitive);

    /**
     * 批量删除敏感词设置
     *
     * @param ids 需要删除的敏感词设置ID
     * @return 结果
     */
    public int deleteWeSensitiveByIds(Long[] ids);

    /**
     * 批量删除敏感词设置信息
     *
     * @param ids 敏感词设置ID
     * @return 结果
     */
    public int destroyWeSensitiveByIds(Long[] ids);

    /**
     * 敏感词命中
     *
     * @param entityList
     */
    public void hitSensitive(List<JSONObject> entityList);

    public PageInfo<JSONObject> getHitSensitiveList(WeSensitiveHitQuery weSensitiveHitQuery);
}
