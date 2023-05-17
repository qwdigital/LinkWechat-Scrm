package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.domain.WeMoments;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeMomentsMapper extends BaseMapper<WeMoments> {

    @DataScope(type = "2", value = @DataColumn(alias = "wm", name = "create_by_id", userid = "user_id"))
    List<WeMoments> findMoments(WeMoments weMoments);

    WeMoments findMomentsDetail(@Param("id") Long id);

    void removePushLwPush();
}
