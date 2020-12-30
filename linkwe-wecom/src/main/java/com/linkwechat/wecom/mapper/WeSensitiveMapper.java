package com.linkwechat.wecom.mapper;


import com.linkwechat.wecom.domain.WeSensitive;

import java.util.List;

/**
 * 敏感词设置Mapper接口
 *
 * @author ruoyi
 * @date 2020-12-29
 */
public interface WeSensitiveMapper {
    /**
     * 查询敏感词设置
     *
     * @param id 敏感词设置ID
     * @return 敏感词设置
     */
    public WeSensitive selectWeSensitiveById(Long id);

    public List<WeSensitive> selectWeSensitiveByIds(Long[] ids);

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

    public int batchUpdateWeSensitive(List<WeSensitive> weSensitiveList);

    /**
     * 删除敏感词设置
     *
     * @param id 敏感词设置ID
     * @return 结果
     */
    public int deleteWeSensitiveById(Long id);

    /**
     * 批量删除敏感词设置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeSensitiveByIds(Long[] ids);
}
