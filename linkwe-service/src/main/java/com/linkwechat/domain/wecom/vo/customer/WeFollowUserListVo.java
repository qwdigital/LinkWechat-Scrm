package com.linkwechat.domain.wecom.vo.customer;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 配置了客户联系功能的成员userid列表
 * @date 2021/12/2 15:57
 **/
@Data
public class WeFollowUserListVo extends WeResultVo {
    /**
     * 成员userid列表
     */
    private List<String> followUser;
}
