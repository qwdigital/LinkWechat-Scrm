package com.linkwechat.wecom.domain.dto;

import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.domain.WeDepartment;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 企业部门
 * @author: HaoN
 * @create: 2020-08-27 15:54
 **/
@Data
public class WeDepartMentDto extends WeResultDto {



    private List<DeartMentDto> deartMents=new ArrayList<>();

    public WeDepartMentDto(){

    }


    public WeDepartMentDto(WeDepartment weDepartment){

        WeDepartMentDto.DeartMentDto deartMentDto=new WeDepartMentDto().new DeartMentDto();
        BeanUtils.copyPropertiesignoreOther(weDepartment,deartMentDto);
        deartMents.add(deartMentDto);
    }



    @Data
    public class DeartMentDto{

        private Long id;

        private String name;

        private Long parentid;

    }



}
