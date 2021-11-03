package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;


/**
 * 欢迎语模板对象 we_msg_tlp
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@Data
public class WeMsgTlp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id= SnowFlakeUtil.nextId();

    /** 欢迎语 */
    private String welcomeMsg;

    /** 素材的id */
    private Long mediaId;

    /** 0:正常;1:删除; */
    private Integer delFlag=new Integer(0);

    /** 欢迎语模板类型:1:员工欢迎语;2:部门员工欢迎语;3:客户群欢迎语 */
    private Long welcomeMsgTplType;

    /** 欢迎语模板使用范围 */
    private List<WeMsgTlpScope> weMsgTlpScopes;

    private String materialUrl;



}
