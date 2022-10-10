package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeSensitive;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 敏感词设置表(WeSensitive)
 *
 * @author danmo
 * @since 2022-06-10 10:38:45
 */
@Repository()
@Mapper
public interface WeSensitiveMapper extends BaseMapper<WeSensitive> {
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

    public int batchUpdateWeSensitive(List<WeSensitive> weSensitiveList);

}

