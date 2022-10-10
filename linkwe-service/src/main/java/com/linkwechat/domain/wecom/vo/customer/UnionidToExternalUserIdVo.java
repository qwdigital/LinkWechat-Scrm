package com.linkwechat.domain.wecom.vo.customer;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UnionidToExternalUserIdVo extends WeResultVo {


    private List<UnionIdToExternalUserIdList> externalUseridInfo;


    @Data
    public static class UnionIdToExternalUserIdList{
        /**
         * 所属企业id
         */
        private String corpId;

        /**
         * 外部联系人id
         */
        private String externalUserId;
    }
}
