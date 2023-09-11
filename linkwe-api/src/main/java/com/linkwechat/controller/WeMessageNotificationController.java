package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.message.MessageReadEnum;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.message.entity.WeMessageNotification;
import com.linkwechat.domain.message.vo.WeMessageNotificationVo;
import com.linkwechat.service.IWeMessageNotificationService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 消息通知
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/20 14:40
 */
@Api(tags = "消息通知")
@RestController
@RequestMapping("/message/notification")
public class WeMessageNotificationController extends BaseController {

    @Resource
    private IWeMessageNotificationService weMessageNotificationService;

    /**
     * 消息通知未读数量
     *
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/20 14:46
     */
    @GetMapping("/num")
    public AjaxResult num() {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        LambdaQueryWrapper<WeMessageNotification> queryWrapper = Wrappers.lambdaQuery(WeMessageNotification.class);
        queryWrapper.eq(WeMessageNotification::getWeUserId, sysUser.getWeUserId());
        queryWrapper.eq(WeMessageNotification::getIsRead, MessageReadEnum.UN_READ.getCode());
        queryWrapper.eq(WeMessageNotification::getDelFlag, Constants.COMMON_STATE);
        int count = weMessageNotificationService.count(queryWrapper);
        return AjaxResult.success(count);
    }

    /**
     * 消息通知未读列表
     *
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/07/20 14:45
     */
    @GetMapping("/unread/list")
    public TableDataInfo unreadList() {
        startPage();
        LambdaQueryWrapper<WeMessageNotification> queryWrapper = Wrappers.lambdaQuery(WeMessageNotification.class);
        queryWrapper.eq(WeMessageNotification::getWeUserId, SecurityUtils.getLoginUser().getSysUser().getWeUserId());
        queryWrapper.eq(WeMessageNotification::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.orderByDesc(WeMessageNotification::getCreateTime);
        List<WeMessageNotification> list = weMessageNotificationService.list(queryWrapper);
        TableDataInfo dataTable = getDataTable(list);
        List<WeMessageNotificationVo> vos = BeanUtil.copyToList(list, WeMessageNotificationVo.class);
        dataTable.setRows(vos);
        return dataTable;
    }

    /**
     * 消息通知已读列表
     *
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/07/20 14:45
     */
    @GetMapping("/read/list")
    public TableDataInfo readList() {
        startPage();
        LambdaQueryWrapper<WeMessageNotification> queryWrapper = Wrappers.lambdaQuery(WeMessageNotification.class);
        queryWrapper.eq(WeMessageNotification::getWeUserId, SecurityUtils.getLoginUser().getSysUser().getWeUserId());
        queryWrapper.eq(WeMessageNotification::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.orderByDesc(WeMessageNotification::getCreateTime);
        List<WeMessageNotification> list = weMessageNotificationService.list(queryWrapper);
        TableDataInfo dataTable = getDataTable(list);
        List<WeMessageNotificationVo> vos = BeanUtil.copyToList(list, WeMessageNotificationVo.class);
        dataTable.setRows(vos);
        return dataTable;
    }

    /**
     * 已读
     *
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/20 14:45
     */
    @PostMapping("/read")
    public AjaxResult read() {
        String weUserId = SecurityUtils.getLoginUser().getSysUser().getWeUserId();
        LambdaUpdateWrapper<WeMessageNotification> wrapper = Wrappers.lambdaUpdate(WeMessageNotification.class);
        wrapper.set(WeMessageNotification::getIsRead, MessageReadEnum.READ.getCode());
        wrapper.set(WeMessageNotification::getUpdateTime, new Date());
        wrapper.eq(WeMessageNotification::getWeUserId, weUserId);
        weMessageNotificationService.update(wrapper);
        return AjaxResult.success();
    }
}

