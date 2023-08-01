package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.domain.WeKfScenes;
import com.linkwechat.domain.kf.query.WeKfScenesQuery;
import com.linkwechat.domain.kf.vo.WeKfScenesListVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客服场景信息表(WeKfScenes)
 *
 * @author danmo
 * @since 2022-04-15 15:53:38
 */
@Repository()
@Mapper
public interface WeKfScenesMapper extends BaseMapper<WeKfScenes> {

    @DataScope(type = "2", value = @DataColumn(alias = "wks", name = "create_by_id", userid = "user_id"))
    List<WeKfScenesListVo> getScenesList(WeKfScenesQuery query);
}

