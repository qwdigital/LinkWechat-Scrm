package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformExternalUserIdQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformUserIdQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeUnionidTransformExternalUserIdQuery;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformCorpVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformExternalUserIdVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformUserIdVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeUnionidTransformExternalUserIdVO;
import com.linkwechat.fallback.QwCorpFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author sxw
 * @description 企微企业接口
 * @date 2022/3/13 20:54
 **/
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwCorpFallbackFactory.class)
public interface QwCorpClient {

    /**
     * corpid的转换
     *
     * @param query corpid
     * @return
     */
    @PostMapping("/corp/transformCorpId")
    public AjaxResult<WeTransformCorpVO> transformCorpId(@RequestBody WeBaseQuery query);

    /**
     * userid的转换
     *
     * @param query corpid
     * @return
     */
    @PostMapping("/corp/transformUserId")
    public AjaxResult<WeTransformUserIdVO> transformUserId(@RequestBody WeTransformUserIdQuery query);

    /**
     * eid的转换
     *
     * @param query corpid
     * @return
     */
    @PostMapping("/corp/transformExternalUserId")
    public AjaxResult<WeTransformExternalUserIdVO> transformExternalUserId(@RequestBody WeTransformExternalUserIdQuery query);

    /**
     * unionid转换为第三方external_userid
     *
     * @param query
     * @return {@link AjaxResult<  WeUnionidTransformExternalUserIdVO >}
     * @author WangYX
     * @date 2022/10/26 16:40
     */
    @PostMapping("/corp/unionidTransformExternalUserId")
    public AjaxResult<WeUnionidTransformExternalUserIdVO> unionidTransformExteralUserId(@RequestBody WeUnionidTransformExternalUserIdQuery query);
}
