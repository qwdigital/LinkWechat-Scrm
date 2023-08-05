package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.material.dto.WeContentSendViewDto;
import com.linkwechat.domain.material.entity.WeContentSendRecord;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.query.ContentDetailQuery;
import com.linkwechat.domain.material.vo.ContentDataDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface WeContentSendRecordMapper extends BaseMapper<WeContentSendRecord> {

    ContentDataDetailVo getWeMaterialDataCount(ContentDetailQuery contentDetailQuery);

    List<WeContentSendViewDto> selectSendTotalNumGroupByContentId();


    List<ContentDataDetailVo> findContentDataDetailVo(@Param("contentId") Long contentId,@Param("talkId") Long talkId,@Param("startTime") String startTime,@Param("endTime") String endTime);

}
