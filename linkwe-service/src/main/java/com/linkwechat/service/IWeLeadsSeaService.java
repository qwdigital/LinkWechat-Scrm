package com.linkwechat.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSea;
import com.linkwechat.domain.leads.sea.query.WeLeadsSeaSaveRequest;
import com.linkwechat.domain.leads.sea.query.WeLeadsSeaUpdateRequest;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaDataDetailVO;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaEmployeeRankVO;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaTrendVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 线索公海 服务类
 * </p>
 *
 * @author WangYX
 * @since 2023-04-03
 */
public interface IWeLeadsSeaService extends IService<WeLeadsSea> {

    /**
     * 保存数据
     *
     * @param request 新增请求参数
     * @return {@link Long}
     * @author WangYX
     * @date 2023/07/10 18:07
     */
    Long add(WeLeadsSeaSaveRequest request);


    /**
     * 修改
     *
     * @param request
     * @author WangYX
     * @date 2023/07/10 18:19
     */
    void update(WeLeadsSeaUpdateRequest request);


    /**
     * 验证当前用户是否有此公海的数据访问权限
     *
     * @param seaId 公海Id
     * @return {@link Boolean}
     * @author WangYX
     * @date 2023/07/17 11:03
     */
    Boolean checkSeaById(Long seaId);


    /**
     * 分公海线索统计
     *
     * @param seaId 线索Id
     * @return {@link Map<String, Object>}
     * @author WangYX
     * @date 2023/07/17 13:43
     */
    Map<String, Object> getCounts(Long seaId);

    /**
     * 数据趋势
     *
     * @param seaId     公海Id
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param weUserId  企微用户Id
     * @return {@link List<WeLeadsSeaTrendVO>}
     * @author WangYX
     * @date 2023/07/17 14:07
     */
    List<WeLeadsSeaTrendVO> seaLeadsTrend(Long seaId, String beginTime, String endTime, String weUserId);

    /**
     * 跟进员工Top5
     *
     * @param seaId     公海Id
     * @param beginTime 开始时间 yyyy-MM-dd
     * @param endTime   结束时间 yyyy-MM-dd
     * @return {@link List< WeLeadsSeaEmployeeRankVO>}
     * @author WangYX
     * @date 2023/07/17 15:35
     */
    List<WeLeadsSeaEmployeeRankVO> getCustomerRank(Long seaId, String beginTime, String endTime);

    /**
     * 数据明细
     *
     * @param seaId     公海Id
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param weUserId  跟进人Id
     * @return {@link List<WeLeadsSeaDataDetailVO>}
     * @author WangYX
     * @date 2023/07/17 16:29
     */
    List<WeLeadsSeaDataDetailVO> getSeaDataDetail(Long seaId, String beginTime, String endTime, String weUserId);

    /**
     * 数据明细
     *
     * @param seaId     公海Id
     * @param weUserId  跟进人Id
     * @param dateTimes 日期集合
     * @return {@link List<WeLeadsSeaDataDetailVO>}
     * @author WangYX
     * @date 2023/07/17 16:29
     */
    public List<WeLeadsSeaDataDetailVO> getSeaDataDetail(Long seaId, String weUserId, List<DateTime> dateTimes);

}
