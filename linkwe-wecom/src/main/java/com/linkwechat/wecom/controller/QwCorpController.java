package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformExternalUserIdQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformUserIdQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeUnionidTransformExternalUserIdQuery;
import com.linkwechat.domain.wecom.query.user.WeAddUserQuery;
import com.linkwechat.domain.wecom.query.user.WeUserListQuery;
import com.linkwechat.domain.wecom.query.user.WeUserQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformCorpVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformExternalUserIdVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformUserIdVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeUnionidTransformExternalUserIdVO;
import com.linkwechat.domain.wecom.vo.user.WeLoginUserVo;
import com.linkwechat.domain.wecom.vo.user.WeUserDetailVo;
import com.linkwechat.domain.wecom.vo.user.WeUserListVo;
import com.linkwechat.wecom.service.IQwCorpService;
import com.linkwechat.wecom.service.IQwUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author danmo
 * @description 企微企业接口
 * @date 2022/3/13 21:01
 **/
@Slf4j
@RestController
@RequestMapping("corp")
public class QwCorpController {

    @Autowired
    private IQwCorpService qwCorpService;

    /**
     * corpid的转换
     *
     * @param query corpid
     * @return
     */
    @PostMapping("/transformCorpId")
    public AjaxResult<WeTransformCorpVO> transformCorpId(@RequestBody WeBaseQuery query) {
        WeTransformCorpVO weTransformCorp = qwCorpService.transformCorpId(query);
        return AjaxResult.success(weTransformCorp);
    }

    /**
     * userid的转换
     *
     * @param query corpid
     * @return
     */
    @PostMapping("/transformUserId")
    public AjaxResult<WeTransformUserIdVO> transformUserId(@RequestBody WeTransformUserIdQuery query) {
        WeTransformUserIdVO weTransformUserId = qwCorpService.transformUserId(query);
        return AjaxResult.success(weTransformUserId);
    }

    /**
     * eid的转换
     *
     * @param query corpid
     * @return
     */
    @PostMapping("/transformExternalUserId")
    public AjaxResult<WeTransformExternalUserIdVO> transformExternalUserId(@RequestBody WeTransformExternalUserIdQuery query) {
        WeTransformExternalUserIdVO transformExternalUserId = qwCorpService.transformExternalUserId(query);
        return AjaxResult.success(transformExternalUserId);
    }

    /**
     * unionid转换为第三方external_userid
     *
     * @param query
     * @return {@link AjaxResult<  WeUnionidTransformExternalUserIdVO >}
     * @author WangYX
     * @date 2022/10/26 16:37
     */
    @PostMapping("/unionidTransformExternalUserId")
    public AjaxResult<WeUnionidTransformExternalUserIdVO> unionidTransformExternalUserId(@RequestBody WeUnionidTransformExternalUserIdQuery query) {
        WeUnionidTransformExternalUserIdVO result = qwCorpService.unionidTransformExternalUserId(query);
        return AjaxResult.success(result);
    }

}
