package com.linkwechat.wecom.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @description: 企业部门
 * @author: HaoN
 * @create: 2020-08-27 15:54
 **/
@Data
public class WeDepartMentDtoDto extends WeResultDto {



    private List<DeartMentDto> deartMents;


    @Data
    class DeartMentDto{

        private String name;

        private String name_en;

        private Long parentid;

        private Long order;

    }

}
