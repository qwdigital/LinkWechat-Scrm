package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSea;
import com.linkwechat.domain.leads.sea.query.WeLeadsSeaSaveRequest;
import com.linkwechat.domain.leads.sea.query.WeLeadsSeaUpdateRequest;
import com.linkwechat.domain.leads.sea.vo.SeaLeadsTrendVo;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaDataDetailVo;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaEmployeeRankVo;

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
     * @return
     */
    Boolean checkSeaById(Long seaId);


    /**
     * 总公海数据分析
     *
     * @return
     */
    Map<String, Object> getAllDataAnalysis();

    /**
     * 分公海线索统计
     *
     * @return
     */
    Map<String, Object> getCounts(Long seaId);

    /**
     * 数据趋势
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<SeaLeadsTrendVo> seaLeadsTrend(String seaId, String beginTime, String endTime);

    /**
     * 数据趋势
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<WeLeadsSeaEmployeeRankVo> getCustomerRank(String seaId, String beginTime, String endTime);

    /**
     * 数据明细
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<WeLeadsSeaDataDetailVo> getSeaDataDetail(String seaId, String beginTime, String endTime);


}
