package com.linkwechat.wecom.domain.query.qr;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.wecom.domain.WeMessageTemplate;
import com.linkwechat.wecom.domain.WeQrCode;
import com.linkwechat.wecom.domain.dto.WeExternalContactDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;

/**
 * @author danmo
 * @description 活码新增入参
 * @date 2021/11/7 13:49
 **/
@ApiModel
@Data
public class WeQrAddQuery {

    @ApiModelProperty("名称")
    private String qrName;

    @ApiModelProperty("分组id")
    private Long qrGroupId;

    @ApiModelProperty("是否自动添加 1-是 0-否")
    private Integer qrAutoAdd;

    @ApiModelProperty("标签id列表")
    private List<String> qrTags;

    @ApiModelProperty("活码类型  1-单人, 2-多人")
    private Integer qrType;

    @ApiModelProperty("排班类型  1-全天 2-自动")
    private Integer qrRuleType;

    @ApiModelProperty("活码员工列表")
    private List<WeQrUserInfoQuery> qrUserInfos;

    @ApiModelProperty("欢迎语素材列表")
    private List<WeMessageTemplate> attachments;

    @ApiModelProperty(value = "渠道",hidden = true)
    private static String state;

    static {
        Snowflake snowflake = IdUtil.getSnowflake(RandomUtil.randomLong(6), RandomUtil.randomInt(6));
        state = snowflake.nextIdStr();
    }


    /**
     * 企微接口参数生成方法
     *
     * @return 企微接口参数实体类
     */
    public WeExternalContactDto.WeContactWay getWeContactWay() {

        WeExternalContactDto.WeContactWay weContactWay = new WeExternalContactDto.WeContactWay();
        //根据类型生成相应的活码
        weContactWay.setType(this.qrType);
        weContactWay.setScene(WeConstans.QR_CODE_EMPLE_CODE_SCENE);
        weContactWay.setSkip_verify(BooleanUtils.toBoolean(this.qrAutoAdd));
        weContactWay.setState(WeConstans.WE_QR_CODE_PREFIX + "_" + state);
        if (CollectionUtil.isNotEmpty(qrUserInfos)) {
            //员工列表
            String[] userIdArr = qrUserInfos.stream().filter(item ->
                    ObjectUtil.equal(1, item.getType())).map(WeQrUserInfoQuery::getUserIds).toArray(String[]::new);
            if (userIdArr.length > 0) {
                weContactWay.setUser(userIdArr);
            }
            //部门列表
            Long[] partyArr = qrUserInfos.stream().filter(item ->
                    ObjectUtil.equal(1, item.getType())).map(WeQrUserInfoQuery::getPartys).toArray(Long[]::new);
            if (partyArr.length > 0) {
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
        weQrCode.setName(this.qrName);
        weQrCode.setAutoAdd(this.qrAutoAdd);
        weQrCode.setGroupId(this.getQrGroupId());
        weQrCode.setType(this.qrType);
        weQrCode.setRuleType(this.qrRuleType);
        weQrCode.setState(state);
        weQrCode.setConfigId(configId);
        weQrCode.setQrCode(qrCode);
        return weQrCode;
    }

}
