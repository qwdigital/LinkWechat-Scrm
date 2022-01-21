package com.linkwechat.wecom.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class WeAppDto extends WeResultDto{



    private List<Agent> agentlist;


    @Data
    public class Agent{
        //企业应用id
        private String agentid;
        //企业应用名称
        private String name;
        //企业应用方形头像url
        private String  square_logo_url;
    }




}
