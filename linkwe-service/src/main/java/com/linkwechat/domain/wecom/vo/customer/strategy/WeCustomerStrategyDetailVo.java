package com.linkwechat.domain.wecom.vo.customer.strategy;

import com.linkwechat.domain.wecom.entity.customer.strategy.WeCustomerStrategyPrivilegeEntity;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 规则组详情
 * @date 2021/12/2 23:44
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeCustomerStrategyDetailVo extends WeResultVo {

    private StrategyDetail strategy;

    @Data
    public static class StrategyDetail{
        /**
         * 规则组id
         */
        private Integer strategyId;

        /**
         * 父规则组id， 如果当前规则组没父规则组，则为0
         */
        private Integer parentId;

        /**
         * 规则组名称
         */
        private String strategyName;

        /**
         * 规则组创建时间戳
         */
        private Long createTime;

        /**
         * 规则组管理员userid列表
         */
        private List<String> adminList;

        /**
         * 基础权限
         */
        private WeCustomerStrategyPrivilegeEntity privilege;
    }
}
