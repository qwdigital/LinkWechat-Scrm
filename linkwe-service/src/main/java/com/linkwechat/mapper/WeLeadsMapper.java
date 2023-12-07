package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.query.WeLeadsBaseRequest;
import com.linkwechat.domain.leads.leads.vo.WeLeadsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 线索 Mapper 接口
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:43
 */
@Mapper
public interface WeLeadsMapper extends BaseMapper<WeLeads> {

    /**
     * 移动端-公海线索列表
     *
     * @param name  线索姓名
     * @param seaId 公海Id
     * @return {@link List < WeLeads>}
     * @author WangYX
     * @date 2023/07/18 9:59
     */
    List<WeLeadsVO> seaLeadsList(@Param("name") String name, @Param("seaId") Long seaId);

    /**
     * 线索列表
     *
     * @param request 请求参数
     * @return {@link List<WeLeadsVO>}
     * @author WangYX
     * @date 2023/08/16 17:37
     */
    List<WeLeadsVO> leadsList(WeLeadsBaseRequest request);

}
