package com.linkwechat.domain.wecom.vo.user;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 授权登录用户详情
 * @date 2021/12/7 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeLoginUserDetailVo extends WeResultVo {
    //用户所属企业的corpid
    private String corpid;
    //成员UserID
    private String userid;
    //成员姓名
    private String name;
    //性别
    private Integer gender;
    //头像url
    private String avatar;
    //员工个人二维码
    private String qr_code;
    private String mobile;
}
