package com.linkwechat.domain.wecom.query.qr;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author danmo
 * @Description 添加活码入参
 * @date 2021/12/2 16:11
 **/
@Data
@Builder
@AllArgsConstructor
public class WeAddWayQuery extends WeBaseQuery {

    public WeAddWayQuery() {

    }

    public WeAddWayQuery(Integer type, Integer scene, String state, String remark) {
        this.type = type;
        this.scene = scene;
        this.state = state;
        this.remark = remark;
    }

    /**
     * 企业联系方式的配置id
     */
    private String config_id;

    /**
     * 联系方式类型,1-单人, 2-多人
     */
    private Integer type;

    /**
     * 场景，1-在小程序中联系，2-通过二维码联系
     */
    private Integer scene;


    /**
     * 企业自定义的state参数，用于区分不同的添加渠道，在调用“获取外部联系人详情”时会返回该参数值，不超过30个字符
     */
    private String state;


    /**
     * 使用该联系方式的用户userID列表，在type为1时为必填，且只能有一个
     */
    private List<String> user;

    /**
     * 使用该联系方式的部门id列表，只在type为2时有效
     */
    private List<Long> party;


    /**
     * 外部客户添加时是否无需验证，默认为true
     */
    private Boolean skip_verify = true;


    /**
     * 样式
     */
    private Integer style = 1;

    /**
     * remark
     */
    private String remark;

    /**
     * 是否开启同一外部企业客户只能添加同一个员工，开启后，同一个企业的客户会优先添加到同一个跟进人  0-不开启 1-开启
     */
    private Boolean is_exclusive;
}
