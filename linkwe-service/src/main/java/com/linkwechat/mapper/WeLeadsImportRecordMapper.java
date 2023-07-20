package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.leads.leads.entity.WeLeadsImportRecord;
import com.linkwechat.domain.leads.leads.vo.WeLeadsImportRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 线索导入记录
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 9:45
 */
@Mapper
public interface WeLeadsImportRecordMapper extends BaseMapper<WeLeadsImportRecord> {

    /**
     * 线索导入记录
     *
     * @param name 导入表格名称
     * @return {@link List <  WeLeadsImportRecordVO >}
     * @author WangYX
     * @date 2023/07/19 16:57
     */
    List<WeLeadsImportRecordVO> importRecord(@Param("name") String name);

}
