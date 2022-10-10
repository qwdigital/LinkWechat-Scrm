package com.linkwechat.domain.wecom.vo.user.tag;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.user.WeUserDetailVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 成员标签
 * @date 2021/12/7 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeUserTagDetailVo extends WeResultVo {
    /**
     * 标签名
     */
    private String tagName;

    /**
     * 标签中包含的成员列表
     */
    private List<WeUserDetailVo> userList;

    /**
     * 标签中包含的部门id列表
     */
    private List<Long> partyList;
}
