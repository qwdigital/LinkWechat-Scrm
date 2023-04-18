package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.fission.WeFissionInviterRecordSub;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author robin
* @description 针对表【we_fission_detail_sub(裂变明细子表)】的数据库操作Mapper
* @createDate 2023-03-14 14:07:21
* @Entity com.linkwechat.WeFissionDetailSub
*/
public interface WeFissionInviterRecordSubMapper extends BaseMapper<WeFissionInviterRecordSub> {
    void batchSaveOrUpdate(@Param("weFissionInviterRecordSubList") List<WeFissionInviterRecordSub> weFissionInviterRecordSubList);

}




