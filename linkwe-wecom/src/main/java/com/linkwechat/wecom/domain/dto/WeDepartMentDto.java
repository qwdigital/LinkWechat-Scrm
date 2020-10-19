package com.linkwechat.wecom.domain.dto;

import cn.hutool.core.collection.CollectionUtil;
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



    private List<DeartMentDto> department=new ArrayList<>();

    public WeDepartMentDto(){

    }


    public WeDepartMentDto(WeDepartment weDepartment){

        DeartMentDto deartMentDto=new WeDepartMentDto().new DeartMentDto();
        BeanUtils.copyPropertiesignoreOther(weDepartment,deartMentDto);
        department.add(deartMentDto);
    }

    public List<WeDepartment> findWeDepartments(){
        List<WeDepartment> weDepartments=new ArrayList<>();
        if(CollectionUtil.isNotEmpty(this.department)){
            this.department.stream().forEach(k->{
                weDepartments.add(
                        WeDepartment.builder()
                                .id(k.getId())
                                .name(k.getName())
                                .parentId(k.getParentid())
                                .build()
                );
            });

        }
        return weDepartments;
    }



    @Data
    public class DeartMentDto{

        private Long id;

        private String name;

        private Long parentid;

    }



}
