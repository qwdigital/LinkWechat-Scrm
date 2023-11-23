package com.linkwechat.domain.kf.vo;

import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.groupchat.vo.WeGroupSimpleVo;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import lombok.Data;

import java.util.List;

/**
 * 客服升级服务
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/08/25 11:31
 */
@Data
public class WeKfUpgradeServiceConfigVO {

    /**
     * 员工集合
     */
    private List<SysUserVo> userList;

    /**
     * 群集合
     */
    private List<WeGroupSimpleVo> groupList;

}
