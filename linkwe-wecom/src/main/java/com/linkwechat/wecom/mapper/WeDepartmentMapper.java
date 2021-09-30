package com.linkwechat.wecom.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeDepartment;
import org.apache.ibatis.annotations.Param;

/**
 * 企业微信组织架构相关Mapper接口
 * 
 * @author ruoyi
 * @date 2020-09-01
 */
public interface WeDepartmentMapper extends BaseMapper<WeDepartment>
{
    /**
     *  批量新增或更新
     * @param weDepartmentList
     * @return
     */
    public int insertBatch(@Param("weDepartmentList") List<WeDepartment> weDepartmentList);
}
