package com.linkwechat.domain.shortlink.query;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.qr.WeQrCode;
import com.linkwechat.domain.qr.query.WeQrUserInfoQuery;
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 短链新增入参
 * @date 2022/12/19 13:49
 **/
@ApiModel
@Data
public class WeShortLinkAddQuery {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id",hidden = true)
    private Long id;


    /**
     * 跳转类型 1-微信 2-其他
     */
    @ApiModelProperty(value = "跳转类型 1-微信 2-其他")
    @NotNull(message = "跳转类型不能为空")
    private Integer jumpType;


    /**
     * 推广类型 1-公众号 2-个人微信 3-企业微信 4-小程序
     */
    @ApiModelProperty(value = "推广类型 1-公众号 2-个人微信 3-企业微信 4-小程序")
    @NotNull(message = "推广类型不能为空")
    private Integer extensionType;


    /**
     * 1-文章 2-二维码
     */
    @ApiModelProperty(value = "1-文章 2-二维码")
    private Integer touchType;


    /**
     * 短链名称
     */
    @ApiModelProperty(value = "短链名称")
    @NotNull(message = "短链名称不能为空")
    private String shortLinkName;


    /**
     * 长链接
     */
    @ApiModelProperty(value = "长链接")
    private String longLink;


    /**
     * 业务类型 1-公众号 2-微信 3-微信群 4-员工活码 5-群活码 6-门店活码 7-小程序
     */
    @ApiModelProperty(value = "业务类型 1-公众号 2-微信 3-微信群 4-员工活码 5-群活码 6-门店活码 7-小程序")
    @NotNull(message = "业务类型不能为空")
    private Integer type;


    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;


    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String describe;


    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;


    /**
     * 二维码ID
     */
    @ApiModelProperty(value = "二维码ID")
    private String qrCodeId;


    /**
     * 二维码地址
     */
    @ApiModelProperty(value = "二维码地址")
    private String qrCode;


    /**
     * 小程序或公众号ID
     */
    @ApiModelProperty(value = "小程序或公众号ID")
    private String appId;


    /**
     * 小程序密钥
     */
    @ApiModelProperty(value = "小程序密钥")
    private String secret;


    /**
     * 校验url是否有效
     * @param url
     * @return
     */
    public Boolean isValidUrl(String url){
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            return false;
        }
        if (uri.getHost() == null) {
            return false;
        }
        if (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https")) {
            return true;
        }
        return false;
    }
}
