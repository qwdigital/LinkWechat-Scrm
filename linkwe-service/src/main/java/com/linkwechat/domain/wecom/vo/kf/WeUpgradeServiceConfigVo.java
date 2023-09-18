package com.linkwechat.domain.wecom.vo.kf;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 专员服务配置范围
 * @date 2021/12/13 16:51
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeUpgradeServiceConfigVo extends WeResultVo {

    /**
     * 专员服务配置范围
     */
    private MemberRange memberRange;

    /**
     * 客户群配置范围
     */
    private GroupChatRange groupChatRange;


    @Data
    public static class MemberRange{
        /**
         * 专员userid列表
         */
        private List<String> userIdList;

        /**
         * 专员部门列表
         */
        private List<Integer> departmentIdList;
    }

    @Data
    public static class GroupChatRange{
        /**
         * 客户群列表
         */
        private List<String> chatIdList;
    }
}
