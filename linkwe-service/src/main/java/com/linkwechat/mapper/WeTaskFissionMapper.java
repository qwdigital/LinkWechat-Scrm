package com.linkwechat.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeTaskFission;
import com.linkwechat.domain.taskfission.vo.WeTaskFissionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务宝表(WeTaskFission)
 *
 * @author danmo
 * @since 2022-06-28 13:48:53
 */
@Repository()
@Mapper
public interface WeTaskFissionMapper extends BaseMapper<WeTaskFission> {

    /**
     * 查询任务宝列表
     *
     * @param weTaskFission 任务宝
     * @return 任务宝集合
     */
    public List<WeTaskFission> selectWeTaskFissionList(WeTaskFission weTaskFission);

    WeTaskFissionVo selectWeTaskFissionById(@Param("id") Long id);

    
    void updateExpiredWeTaskFission();
}

