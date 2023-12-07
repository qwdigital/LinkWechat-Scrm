package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSea;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaStatisticVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 线索公海 Mapper 接口
 * </p>
 *
 * @author WangYX
 * @since 2023-04-03
 */
@Mapper
public interface WeLeadsSeaMapper extends BaseMapper<WeLeadsSea> {

    /**
     * 总领取量
     *
     * @param seaId     公海Id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param weUserId  企微员工Id
     * @return {@link List<WeLeadsSeaStatisticVO>}
     * @author WangYX
     * @date 2023/08/17 16:33
     */
    List<WeLeadsSeaStatisticVO> allReceiveNum(@Param("seaId") Long seaId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("weUserId") String weUserId);

    /**
     * 当日领取量
     *
     * @param seaId     公海Id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param weUserId  企微员工Id
     * @return {@link List<WeLeadsSeaStatisticVO>}
     * @author WangYX
     * @date 2023/08/17 17:20
     */
    List<WeLeadsSeaStatisticVO> todayReceiveNum(@Param("seaId") Long seaId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("weUserId") String weUserId);

    /**
     * 总跟进量
     *
     * @param seaId     公海Id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param weUserId  企微员工Id
     * @return {@link List<WeLeadsSeaStatisticVO>}
     * @author WangYX
     * @date 2023/08/17 18:12
     */
    List<WeLeadsSeaStatisticVO> allFollowerNum(@Param("seaId") Long seaId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("weUserId") String weUserId);

    /**
     * 当日跟进量
     *
     * @param seaId     公海Id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param weUserId  企微员工Id
     * @return {@link List<WeLeadsSeaStatisticVO>}
     * @author WangYX
     * @date 2023/08/17 17:20
     */
    List<WeLeadsSeaStatisticVO> todayFollowNum(@Param("seaId") Long seaId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("weUserId") String weUserId);


    /**
     * 获取总跟进量
     *
     * @param seaId 公海Id
     * @return {@link Integer}
     * @author WangYX
     * @date 2023/08/18 10:11
     */
    Integer getAllFollowNum(Long seaId);


}
