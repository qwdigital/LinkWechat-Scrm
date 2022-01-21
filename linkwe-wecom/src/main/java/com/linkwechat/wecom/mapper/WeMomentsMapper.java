package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeMoments;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeMomentsMapper extends BaseMapper<WeMoments> {

    List<WeMoments>  findMoments(WeMoments weMoments);

    WeMoments findMomentsDetail(@Param("momentId") String momentId);

    void removePushLwPush();
}
