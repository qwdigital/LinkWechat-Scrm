package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.domain.WeKfServicer;
import com.linkwechat.domain.kf.WeKfUser;
import com.linkwechat.domain.kf.query.WeKfServicerListQuery;
import com.linkwechat.domain.kf.vo.WeKfServicerListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客服接待人员表(WeKfServicer)
 *
 * @author danmo
 * @since 2022-04-15 15:53:38
 */
@Repository()
@Mapper
public interface WeKfServicerMapper extends BaseMapper<WeKfServicer> {
    /**
     * 根据客服id查询接待人员
     * @param kfId 客服id
     * @return
     */
    List<WeKfUser> getServicerByKfId(@Param("kfId") Long kfId);

    /**
     * 客服接待人员列表
     * @param query
     * @return
     */
    @DataScope(type = "2", value = @DataColumn(alias = "wks", name = "user_id", userid = "we_user_id"))
    List<WeKfServicerListVo> getKfServicerList(WeKfServicerListQuery query);

    List<WeKfUser> getKfUserIdList(@Param("kfId") Long kfId);
}

