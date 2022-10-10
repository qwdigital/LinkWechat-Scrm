package com.linkwechat.domain.storecode.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.qr.WeQrAttachments;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 门店活码配置(导购码,门店群活码)
 */
@Data
@TableName("we_store_code_config")
public class WeStoreCodeConfig extends BaseEntity {

    //主键
    @TableId
    private Long id;

    //超范围提示
    private String outOfRangeTip;

    //加群欢迎语
    private String welcomeMsg;

    //方圆多少公里
    private String raidus;

    //客服活码状态(0:启用;1:关闭)
    private Integer codeState;


//    //客服活码url
//    private String codeUrl;

    //客户标签id,多个使用逗号隔开
    private String tagIds;

    //客户标签名,多个使用逗号隔开
    private String tagNames;

    //门店码类型(1:门店导购码;2:门店群活码)
    private Integer storeCodeType;

    //客服名称活码url
    private String customerServiceUrl;

    //客服名称
    private String customerServiceName;

    //客服id
    private String customerServiceId;

    //configId
    private String configId;


    //导购或群对应唯一地址
    private String storeCodeConfigUrl;

    //导购或群对应唯一二维码
    private String storeCodeConfigQr;

    //欢迎语素材新增编辑传入
    @TableField(exist = false)
    private List<WeMessageTemplate> attachments;

    //欢迎语素材返回展示
    @TableField(exist = false)
    private List<WeQrAttachments> weQrAttachments;

    @TableLogic
    private Integer delFlag;

    //新增或更新:true新增;false更新
    @TableField(exist = false)
    private Boolean addOrUpdate=true;

    //渠道id
    private String state;
}
