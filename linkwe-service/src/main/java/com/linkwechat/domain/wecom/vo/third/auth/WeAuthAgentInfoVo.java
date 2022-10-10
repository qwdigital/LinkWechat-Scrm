package com.linkwechat.domain.wecom.vo.third.auth;

import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 授权信息
 * @date 2022/3/4 11:34
 **/
@Data
public class WeAuthAgentInfoVo {

    private String agentId;

    private String name;

    private String squareLogoUrl;

    private String roundLogoUrl;

    private String appId;

    private Integer authMode;

    private Integer isCustomizedApp;

    private Privilege privilege;

    @Data
    public class Privilege {
        private Integer level;

        private List<Long> allowParty;

        private List<Integer> allowTag;

        private List<String> allowUser;

        private List<Integer> extraParty;

        private List<String> extraUser;

        private List<Integer> extraTag;
    }
}
