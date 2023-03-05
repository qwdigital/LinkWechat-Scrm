package com.linkwechat.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.material.entity.WeContentTalk;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.query.WeContentViewRecordQuery;
import com.linkwechat.domain.material.vo.MaterialAddViewVo;
import com.linkwechat.domain.material.vo.WeMaterialVo;
import com.linkwechat.service.IWeContentTalkService;
import com.linkwechat.service.IWeContentViewRecordService;
import com.linkwechat.service.IWeMaterialService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 素材中心
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/27 16:12
 */
@Slf4j
@RestController
@RequestMapping("/material")
public class WeMaterialController {

    @Resource
    private IWeMaterialService weMaterialService;

    @Resource
    private IWeContentViewRecordService weContentViewRecordService;

//    @Resource
//    private IWeContentTalkService weContentTalkService;


    @ApiOperation("保存查看详情")
    @PostMapping("/action/addView")
    public AjaxResult addView(@RequestBody MaterialAddViewVo addViewVo) {

//        String sendUserId = request.getParameter("sendUserId");
//        String openid = request.getParameter("openid");
//        String unionid = request.getParameter("unionid");

        //如果其中有一个为null，该条数据不处理
//        if (StringUtils.isEmpty(addViewVo.getSendUserId()) || StringUtils.isEmpty(addViewVo.getOpenid()) || StringUtils.isEmpty(addViewVo.getUnionid()) || "null".equals(openid) || "null".equals(unionid)) {
//            return AjaxResult.success();
//        }
//
//        String talkId = request.getParameter("talkId");
//        String contentId = request.getParameter("contentId");
//        String viewWatchTime = request.getParameter("viewWatchTime");
//        String resourceType = request.getParameter("resourceType");
//
//        log.info("保存素材查看详情：会话Id：{},素材Id：{}，发送者：{}，查看者openid：{}，查看者unionid：{},查看时长：{},数据类型：{}", talkId, contentId, sendUserId, openid, unionid, viewWatchTime, resourceType);

        WeContentViewRecordQuery query = new WeContentViewRecordQuery();
//        if (StringUtils.isNotEmpty(addViewVo.getTalkId())) {
//            WeContentTalk byId = weContentTalkService.getById(talkId);
//            query.setTalkId(addViewVo.getTalkId());
//        }
        query.setTalkId(addViewVo.getTalkId());
        query.setContentId(addViewVo.getContentId());
        query.setOpenid(addViewVo.getOpenid());
        query.setUnionid(addViewVo.getUnionid());
        query.setSendUserId(addViewVo.getSendUserId());
        query.setViewTime(new Date());
        query.setViewWatchTime(addViewVo.getViewWatchTime());
        weContentViewRecordService.addView(query);
        return AjaxResult.success();
    }


    /**
     * 根据Id获取素材
     *
     * @param id
     * @return
     */
    @ApiOperation("根据Id获取素材")
    @GetMapping("/get/{id}")
    public AjaxResult<WeMaterialVo> get(@PathVariable("id") Long id) {
        WeMaterial weMaterial = weMaterialService.getById(id);
        WeMaterialVo weMaterialVo = null;
        if (ObjectUtil.isNotEmpty(weMaterial)) {
            weMaterialVo = BeanUtil.copyProperties(weMaterial, WeMaterialVo.class);
            weMaterialVo.setResourceType(String.valueOf(weMaterial.getModuleType()));
        }
        return AjaxResult.success(weMaterialVo);
    }
}
