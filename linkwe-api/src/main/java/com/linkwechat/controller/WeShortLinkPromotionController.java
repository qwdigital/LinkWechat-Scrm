package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.domain.*;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.query.WePosterQuery;
import com.linkwechat.domain.material.vo.WeMaterialVo;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
import com.linkwechat.domain.shortlink.vo.WeShortLinkListVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkPromotionGetVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkPromotionTemplateClientVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkPromotionVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 短链推广
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/07 15:27
 */
@Api(tags = "短链推广")
@RestController
@RequestMapping("/short/link/promotion")
public class WeShortLinkPromotionController extends BaseController {

    @Resource
    private IWeShortLinkPromotionService weShortLinkPromotionService;
    @Resource
    private IWeMaterialService weMaterialService;
    @Resource
    private IWeShortLinkService weShortLinkService;
    @Resource
    private IWeShortLinkPromotionTemplateClientService weShortLinkPromotionTemplateClientService;
    @Resource
    private QwSysUserClient qwSysUserClient;
    @Resource
    private IWeTagService weTagService;
    @Resource
    private IWeShortLinkPromotionAttachmentService weShortLinkPromotionAttachmentService;


    /**
     * 短链推广列表
     *
     * @param query 查询条件
     * @return
     */
    @ApiOperation(value = "短链推广列表", httpMethod = "GET")
    @Log(title = "短链推广列表", businessType = BusinessType.SELECT)
    @GetMapping("/list")
    public TableDataInfo<WeShortLinkPromotionVo> list(WeShortLinkPromotionQuery query) {
        startPage();
        List<WeShortLinkPromotionVo> list = weShortLinkPromotionService.selectList(query);
        return getDataTable(list);
    }

    /**
     * 新增短链推广
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "新增短链推广", httpMethod = "POST")
    @Log(title = "新增短链推广", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody WeShortLinkPromotionAddQuery query) {
        try {
            Long id = weShortLinkPromotionService.add(query);
            return AjaxResult.success(id);
        } catch (IOException e) {
            logger.error("新建短链推广失败：{}", e.getMessage());
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    /**
     * 暂存短链推广
     *
     * @param query
     * @return
     */
    @Log(title = "暂存短链推广", businessType = BusinessType.INSERT)
    @ApiOperation(value = "暂存短链推广", httpMethod = "POST")
    @PostMapping("/ts")
    public AjaxResult temporaryStorage(@Validated @RequestBody WeShortLinkPromotionAddQuery query) {
        try {
            Long id = weShortLinkPromotionService.ts(query);
            return AjaxResult.success(id);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("暂存推广短链失败：{}", e.getMessage());
            return AjaxResult.error();
        }
    }

    /**
     * 修改短链推广
     *
     * @param query
     * @return
     */
    @Log(title = "修改短链推广", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改", httpMethod = "PUT")
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody WeShortLinkPromotionUpdateQuery query) {
        weShortLinkPromotionService.edit(query);
        return AjaxResult.success();
    }

    /**
     * 删除短链推广
     *
     * @param id
     * @return
     */
    @Log(title = "删除短链推广", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除", httpMethod = "DELETE")
    @PutMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        LambdaUpdateWrapper<WeShortLinkPromotion> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(WeShortLinkPromotion::getId, id);
        updateWrapper.set(WeShortLinkPromotion::getDelFlag, 1);
        boolean update = weShortLinkPromotionService.update(updateWrapper);
        return AjaxResult.success();
    }

    /**
     * 批量删除短链推广
     *
     * @param ids
     * @return
     */
    @Log(title = "删除短链推广", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除", httpMethod = "DELETE")
    @PutMapping("/batch/delete")
    public AjaxResult batchDelete(List<Long> ids) {
        LambdaUpdateWrapper<WeShortLinkPromotion> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.in(WeShortLinkPromotion::getId, ids);
        updateWrapper.set(WeShortLinkPromotion::getDelFlag, 1);
        boolean update = weShortLinkPromotionService.update(updateWrapper);
        return AjaxResult.success();
    }

    /**
     * 获取包含占位码组件的海报
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取包含占位码组件的海报", httpMethod = "GET")
    @Log(title = "获取包含占位码组件的海报", businessType = BusinessType.SELECT)
    @GetMapping("/poster/list")
    public TableDataInfo<WeMaterialVo> posterList(WePosterQuery query) {
        startPage();
        LambdaQueryWrapper<WeMaterial> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(WeMaterial::getId, WeMaterial::getMaterialUrl, WeMaterial::getMaterialName);
        //只取带有占位码组件的海报
        queryWrapper.eq(WeMaterial::getPosterQrType, 3);
        queryWrapper.eq(WeMaterial::getDelFlag, 0);
        queryWrapper.like(StrUtil.isNotBlank(query.getTitle()), WeMaterial::getMaterialName, query.getTitle());
        List<WeMaterial> list = weMaterialService.list(queryWrapper);
        List<WeMaterialVo> weMaterialVos = BeanUtil.copyToList(list, WeMaterialVo.class);
        return getDataTable(weMaterialVos);
    }

    @ApiOperation(value = "推广", httpMethod = "GET")
    @GetMapping("/spread")
    public AjaxResult promotion() {

        return AjaxResult.success();
    }

    /**
     * 获取短链推广
     */
    @ApiOperation(value = "获取短链推广", httpMethod = "GET")
    @Log(title = "获取短链推广", businessType = BusinessType.SELECT)
    @GetMapping("/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {
        WeShortLinkPromotion weShortLinkPromotion = weShortLinkPromotionService.getById(id);
        WeShortLinkPromotionGetVo result = Optional.ofNullable(weShortLinkPromotion).map(i -> {
            WeShortLinkPromotionGetVo vo = BeanUtil.copyProperties(weShortLinkPromotion, WeShortLinkPromotionGetVo.class);
            WeShortLink weShortLink = weShortLinkService.getById(weShortLinkPromotion.getShortLinkId());
            //短链详情
            WeShortLinkListVo weShortLinkListVo = BeanUtil.copyProperties(weShortLink, WeShortLinkListVo.class);
            vo.setShortLink(weShortLinkListVo);
            //任务状态: 0待推广 1推广中 2已结束 3暂存中
            if (i.getTaskStatus() != 3) {
                //推广信息
                Integer type = i.getType();
                switch (type) {
                    case 0:
                        LambdaQueryWrapper<WeShortLinkPromotionTemplateClient> queryWrapper = Wrappers.lambdaQuery();
                        queryWrapper.eq(WeShortLinkPromotionTemplateClient::getPromotionId, i.getId());
                        queryWrapper.eq(WeShortLinkPromotionTemplateClient::getDelFlag, 0);
                        WeShortLinkPromotionTemplateClient one = weShortLinkPromotionTemplateClientService.getOne(queryWrapper);
                        WeShortLinkPromotionTemplateClientVo clientVo = BeanUtil.copyProperties(one, WeShortLinkPromotionTemplateClientVo.class);
                        //群发客户分类：0全部客户 1部分客户
                        if (one.getType() == 1) {
                            SysUserQuery sysUserQuery = new SysUserQuery();
                            List<String> list = Arrays.asList(one.getUserIds().split(","));
                            sysUserQuery.setWeUserIds(list);
                            AjaxResult<List<SysUserVo>> weUsersResult = qwSysUserClient.getUserListByWeUserIds(sysUserQuery);
                            if (weUsersResult.getCode() == 200) {
                                List<SysUserVo> data = weUsersResult.getData();
                                Map<String, String> map = new HashMap<>(list.size());
                                data.stream().forEach(o -> map.put(o.getWeUserId(), o.getUserName()));
                                clientVo.setUser(map);
                            }
                        }
                        //标签
                        if (StrUtil.isNotBlank(one.getLabelIds())) {
                            LambdaQueryWrapper<WeTag> tagQueryWrapper = Wrappers.lambdaQuery();
                            tagQueryWrapper.eq(WeTag::getDelFlag, 0);
                            tagQueryWrapper.in(WeTag::getTagId, one.getLabelIds().split(","));
                            List<WeTag> list = weTagService.list(tagQueryWrapper);
                            Map<String, String> map = new HashMap<>(list.size());
                            list.stream().forEach(o -> map.put(o.getTagId(), o.getName()));
                            clientVo.setLabels(map);
                        }
                        //附件
                        LambdaQueryWrapper<WeShortLinkPromotionAttachment> attachmentQueryWrapper = Wrappers.lambdaQuery();
                        attachmentQueryWrapper.eq(WeShortLinkPromotionAttachment::getTemplateType, 0);
                        attachmentQueryWrapper.eq(WeShortLinkPromotionAttachment::getTemplateId, one.getId());
                        attachmentQueryWrapper.eq(WeShortLinkPromotionAttachment::getDelFlag, 0);
                        List<WeShortLinkPromotionAttachment> list = weShortLinkPromotionAttachmentService.list(attachmentQueryWrapper);
                        if (list != null && list.size() > 0) {
                            List<WeMessageTemplate> collect = list.stream().map(o -> BeanUtil.copyProperties(o, WeMessageTemplate.class)).collect(Collectors.toList());
                            clientVo.setAttachmentsList(collect);
                        }

                        vo.setClient(clientVo);
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }
            }

            return vo;
        }).orElse(null);
        return AjaxResult.success(result);
    }


}

