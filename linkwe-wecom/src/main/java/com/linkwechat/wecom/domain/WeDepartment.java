package com.linkwechat.wecom.domain;


import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.domain.dto.WeDepartMentDto;
import lombok.Data;

/**
 * 企业微信组织架构相关对象 we_department
 * 
 * @author ruoyi
 * @date 2020-09-01
 */
@Data
public class WeDepartment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id= SnowFlakeUtil.nextId();


    /** 部门名称 */
    private String name;


    /** 父节点的id */
    private Long parentId;



    /** 系统通讯录部门转化为企业微信通讯录部门的dto对象 */
    public WeDepartMentDto.DeartMentDto transformDeartMentDto(WeDepartment weDepartment){

        WeDepartMentDto.DeartMentDto deartMentDto=new WeDepartMentDto().new DeartMentDto();
        BeanUtils.copyPropertiesignoreOther(weDepartment,deartMentDto);

        return deartMentDto;

    }

    /** 企业微信通讯录部门的dto对象转化为系统通讯录部门对象 */
    public  static  WeDepartment transformWeDepartment(WeDepartMentDto.DeartMentDto deartMentDto){

        WeDepartment weDepartment=new WeDepartment();
        BeanUtils.copyPropertiesignoreOther(deartMentDto,weDepartment);

        return weDepartment;
    }


}
