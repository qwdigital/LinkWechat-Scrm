package com.linkwechat.domain.wecom.vo.living;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 直播返回对象
 * @date 2022年10月11日 16:09
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGetLivingShareInfoVo extends WeResultVo {

    /**
     * 直播id
     */
    @ApiModelProperty("直播id")
    private String livingId;

    /**
     * 直播id
     */
    @ApiModelProperty("观众的userid，观众为企业内部成员时返回")
    private String viewerUserId;

    /**
     * 观众的external_userid，观众为非企业内部成员时返回
     */
    @ApiModelProperty("观众的external_userid，观众为非企业内部成员时返回")
    private String viewerExternalUserId;

    /**
     * 邀请人的userid，邀请人为企业内部成员时返回（观众首次进入直播时，其使用的直播卡片/二维码所对应的分享人）
     */
    @ApiModelProperty("邀请人的userid，邀请人为企业内部成员时返回（观众首次进入直播时，其使用的直播卡片/二维码所对应的分享人）")
    private String invitorUserId;

    /**
     * 邀请人的external_userid，邀请人为非企业内部成员时返回 （观众首次进入直播时，其使用的直播卡片/二维码所对应的分享人）
     */
    @ApiModelProperty("邀请人的external_userid，邀请人为非企业内部成员时返回 （观众首次进入直播时，其使用的直播卡片/二维码所对应的分享人）")
    private String invitorExternalUserId;
}
