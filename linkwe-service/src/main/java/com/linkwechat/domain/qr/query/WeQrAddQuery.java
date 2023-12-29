package com.linkwechat.domain.qr.query;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.WelcomeMsgTypeEnum;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.qr.WeQrCode;
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 活码新增入参
 * @date 2021/11/7 13:49
 **/
@ApiModel
@Data
public class WeQrAddQuery {

    @ApiModelProperty("活码Id")
    private Long qrId;

    @ApiModelProperty("活码Id")
    private String configId;

    @ApiModelProperty("名称")
    @NotEmpty(message = "活码名称不能为空")
    private String qrName;

    @ApiModelProperty("分组id")
    @NotNull(message = "分组ID不能为空")
    private Long qrGroupId;

    @ApiModelProperty("是否自动添加 1-是 0-否 默认是 1")
    private Integer qrAutoAdd = 1;

    @ApiModelProperty("标签id列表")
    private List<String> qrTags;

    @ApiModelProperty("活码类型  1-单人, 2-多人")
    @NotNull(message = "活码类型不能为空")
    private Integer qrType;

    @ApiModelProperty("排班类型  1-全天 2-自动")
    private Integer qrRuleType;

    @ApiModelProperty("排班方式  1-轮询 2-顺序 3-随机")
    private Integer qrRuleMode;

    @ApiModelProperty("开启备用员工 1-是 0-否 默认是 0")
    private Integer openSpareUser = 0;

    @ApiModelProperty("是否开启同一外部企业客户只能添加同一个员工，开启后，同一个企业的客户会优先添加到同一个跟进人  0-不开启 1-开启")
    private Integer isExclusive = 0;


    @ApiModelProperty("活码员工列表")
    private List<WeQrUserInfoQuery> qrUserInfos;

    @ApiModelProperty("欢迎语素材列表")
    @NotNull(message = "欢迎语素材列表不能为空")
    private List<WeMessageTemplate> attachments;

    @ApiModelProperty(value = "渠道",hidden = true)
    private String state;

    /**
     * 欢迎语开关
     */
    @ApiModelProperty("欢迎语开关 1-不发送欢迎语，2-发送欢迎语")
    private Integer qrWelcomeOpen;


    /**
     * 是否优先员工欢迎语
     */
    @ApiModelProperty("是否优先员工欢迎语 0-否，1-是（仅欢迎语开关为2是生效）")
    private Integer qrPriorityUserWelcome;

    /**
     * 企微接口参数生成方法
     *
     * @return 企微接口参数实体类
     */
    public WeAddWayQuery getWeContactWay() {
//        WeAddWayQuery weContactWay = new WeAddWayQuery();
//        //根据类型生成相应的活码
//        if(this.qrId == null){
//            Snowflake snowflake = IdUtil.getSnowflake(RandomUtil.randomLong(6), RandomUtil.randomInt(6));
//            this.state = WelcomeMsgTypeEnum.WE_QR_CODE_PREFIX.getType() + snowflake.nextIdStr();
//            weContactWay.setState(state);
//        }
//        weContactWay.setIs_exclusive(BooleanUtils.toBoolean(this.isExclusive));
//        weContactWay.setConfig_id(this.configId);
//        weContactWay.setType(this.qrType);
//        weContactWay.setScene(WeConstans.QR_CODE_EMPLE_CODE_SCENE);
//        weContactWay.setSkip_verify(BooleanUtils.toBoolean(this.qrAutoAdd));
//        if (CollectionUtil.isNotEmpty(qrUserInfos)) {
//            //员工列表
//            List<String> userIdArr = qrUserInfos.stream().map(WeQrUserInfoQuery::getUserIds)
//                    .filter(CollectionUtil::isNotEmpty).flatMap(Collection::stream).collect(Collectors.toList());
//            if (CollectionUtil.isNotEmpty(userIdArr)) {
//                weContactWay.setUser(userIdArr);
//            }
//            //部门列表
//            List<Long> partyArr = qrUserInfos.stream().map(WeQrUserInfoQuery::getPartys)
//                    .filter(CollectionUtil::isNotEmpty).flatMap(Collection::stream).collect(Collectors.toList());
//            if (CollectionUtil.isNotEmpty(partyArr)) {
//                weContactWay.setParty(partyArr);
//            }
//        }
//        return weContactWay;
        return  getWeContactWayByState(WelcomeMsgTypeEnum.WE_QR_CODE_PREFIX.getType() + IdUtil.getSnowflake(RandomUtil.randomLong(6), RandomUtil.randomInt(6)).nextIdStr());
    }


    /**
     * 设置渠道活码
     * @param channelState
     * @return
     */
    public WeAddWayQuery getWeContactWayByState(String channelState){
        WeAddWayQuery weContactWay = new WeAddWayQuery();
        //根据类型生成相应的活码
        if(this.qrId == null){
            weContactWay.setState(channelState);
        }
        weContactWay.setIs_exclusive(BooleanUtils.toBoolean(this.isExclusive));
        weContactWay.setConfig_id(this.configId);
        weContactWay.setType(this.qrType);
        weContactWay.setScene(WeConstans.QR_CODE_EMPLE_CODE_SCENE);
        weContactWay.setSkip_verify(BooleanUtils.toBoolean(this.qrAutoAdd));
        if (CollectionUtil.isNotEmpty(qrUserInfos)) {
            //员工列表
            List<String> userIdArr = qrUserInfos.stream().map(WeQrUserInfoQuery::getUserIds)
                    .filter(CollectionUtil::isNotEmpty).flatMap(Collection::stream).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(userIdArr)) {
                weContactWay.setUser(userIdArr);
            }
            //部门列表
            List<Long> partyArr = qrUserInfos.stream().map(WeQrUserInfoQuery::getPartys)
                    .filter(CollectionUtil::isNotEmpty).flatMap(Collection::stream).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(partyArr)) {
                weContactWay.setParty(partyArr);
            }
        }
        return weContactWay;
    }

    /**
     * 获取数据表实体
     * @return
     * @param configId 二维码配置ID
     * @param qrCode 二维码链接
     */
    public WeQrCode getWeQrCodeEntity(String configId, String qrCode){
        WeQrCode weQrCode = new WeQrCode();
        weQrCode.setId(this.qrId);
        weQrCode.setName(this.qrName);
        weQrCode.setAutoAdd(this.qrAutoAdd);
        weQrCode.setGroupId(this.getQrGroupId());
        weQrCode.setType(this.qrType);
        weQrCode.setRuleType(this.qrRuleType);
        weQrCode.setRuleMode(this.qrRuleMode);
        weQrCode.setOpenSpareUser(this.openSpareUser);
        weQrCode.setIsExclusive(this.isExclusive);
        if(this.qrId == null){
            weQrCode.setState(state);
        }
        weQrCode.setConfigId(configId);
        weQrCode.setQrCode(qrCode);
        weQrCode.setQrWelcomeOpen(this.qrWelcomeOpen);
        weQrCode.setQrPriorityUserWelcome(this.qrPriorityUserWelcome);
        return weQrCode;
    }

}
