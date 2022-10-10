package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeTaskFissionStaff;

/**
 * 裂变任务员工列表(WeTaskFissionStaff)
 *
 * @author danmo
 * @since 2022-06-28 13:48:55
 */
@Repository()
@Mapper
public interface WeTaskFissionStaffMapper extends BaseMapper<WeTaskFissionStaff> {


}

