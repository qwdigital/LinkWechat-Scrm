package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecord;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaEmployeeRankVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 线索跟进记录 Mapper 接口
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 9:45
 */
@Mapper
public interface WeLeadsFollowRecordMapper extends BaseMapper<WeLeadsFollowRecord> {

    /**
     * 数据趋势
     *
     * @param seaId     公海Id
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param weUserId  企微员工Id
     * @return {@link List<WeLeadsFollowRecord>}
     * @author WangYX
     * @date 2023/07/17 15:09
     */
    List<WeLeadsFollowRecord> seaLeadsTrend(@Param("seaId") Long seaId,
                                            @Param("beginTime") String beginTime,
                                            @Param("endTime") String endTime,
                                            @Param("weUserId") String weUserId);

    /**
     * 跟进员工Top5
     *
     * @param seaId     公海Id
     * @param beginTime 开始时间 yyyy-MM-dd
     * @param endTime   结束时间 yyyy-MM-dd
     * @return {@link List<WeLeadsSeaEmployeeRankVO>}
     * @author WangYX
     * @date 2023/07/17 15:37
     */
    List<WeLeadsSeaEmployeeRankVO> getCustomerRank(@Param("seaId") Long seaId, @Param("beginTime") String beginTime, @Param("endTime") String endTime);


    /**
     * 查询跟进记录列表
     *
     * @param seaId        公海Id
     * @param createTime   跟进时间
     * @param weUserId     跟进人
     * @param recordStatus 记录状态
     * @return {@link List< WeLeadsFollowRecord>}
     * @author WangYX
     * @date 2023/07/17 16:44
     */
    List<WeLeadsFollowRecord> list(@Param("seaId") Long seaId,
                                   @Param("createTime") String createTime,
                                   @Param("weUserId") String weUserId,
                                   @Param("recordStatus") Integer recordStatus);


}
