package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeTaskFissionRecord;
import com.linkwechat.domain.taskfission.vo.WeTaskFissionProgressVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 裂变任务记录(WeTaskFissionRecord)
 *
 * @author danmo
 * @since 2022-06-28 13:48:54
 */
@Repository()
@Mapper
public interface WeTaskFissionRecordMapper extends BaseMapper<WeTaskFissionRecord> {


    WeTaskFissionProgressVo getTaskProgress(@Param("taskId") Long taskId, @Param("unionId") String unionId);
}

