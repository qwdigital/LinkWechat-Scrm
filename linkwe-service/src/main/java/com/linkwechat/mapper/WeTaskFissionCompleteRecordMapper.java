package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeTaskFissionCompleteRecord;

/**
 * 裂变任务完成记录(WeTaskFissionCompleteRecord)
 *
 * @author danmo
 * @since 2022-07-19 10:21:08
 */
@Repository()
@Mapper
public interface WeTaskFissionCompleteRecordMapper extends BaseMapper<WeTaskFissionCompleteRecord> {


}

