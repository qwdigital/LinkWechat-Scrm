package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.domain.WeKfCustomerStat;
import com.linkwechat.domain.WeKfPool;
import com.linkwechat.domain.WeKfUserStat;
import com.linkwechat.domain.kf.query.WeKfRecordQuery;
import com.linkwechat.domain.kf.vo.WeKfRecordListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客服接待池表(WeKfPool)
 *
 * @author danmo
 * @since 2022-04-15 15:53:37
 */
@Repository()
@Mapper
public interface WeKfPoolMapper extends BaseMapper<WeKfPool> {

    @DataScope(type = "2", value = @DataColumn(alias = "record", name = "user_id", userid = "we_user_id"))
    List<WeKfRecordListVo> getRecordList(WeKfRecordQuery query);

    /**
     * 获取客服客户统计
     * @param dateTime 日期
     */
    WeKfCustomerStat getKfCustomerStat(@Param("dateTime") String dateTime);

    /**
     * 客服员工统计
     * @param dateTime
     * @return
     */
    List<WeKfUserStat> getKfUserStat(@Param("dateTime") String dateTime);
}

