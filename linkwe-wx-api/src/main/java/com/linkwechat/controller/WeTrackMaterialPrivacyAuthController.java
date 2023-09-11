package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.material.entity.WeTrackMaterialPrivacyAuth;
import com.linkwechat.domain.material.query.WeTrackMaterialPrivacyAuthQuery;
import com.linkwechat.service.IWeTrackMaterialPrivacyAuthService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 轨迹素材隐私政策客户授权
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/26 16:01
 */
@Slf4j
@RestController
@RequestMapping("/track/material/auth")
public class WeTrackMaterialPrivacyAuthController {

    @Resource
    private IWeTrackMaterialPrivacyAuthService weTrackMaterialPrivacyAuthService;

    @ApiOperation("通过微信openid获取授权信息")
    @GetMapping("/get")
    public AjaxResult<Integer> get(@RequestParam("openid") String openid) {
        LambdaQueryWrapper<WeTrackMaterialPrivacyAuth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeTrackMaterialPrivacyAuth::getOpenid, openid);
        queryWrapper.eq(WeTrackMaterialPrivacyAuth::getDelFlag, Constants.COMMON_STATE);
        List<WeTrackMaterialPrivacyAuth> one = weTrackMaterialPrivacyAuthService.list(queryWrapper);
        //是否授权(0否，1是)
        Integer isAuth = 0;
        if(CollectionUtil.isNotEmpty(one)){

            if (ObjectUtil.isNotNull(one)) {
                isAuth = one.stream().findFirst().get().getIsAuth();
            }
        }

        return AjaxResult.success(isAuth);
    }

    @ApiOperation("保存客户授权信息")
    @PostMapping("/save")
    public AjaxResult save(@RequestBody @Validated WeTrackMaterialPrivacyAuthQuery query) {
        LambdaQueryWrapper<WeTrackMaterialPrivacyAuth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeTrackMaterialPrivacyAuth::getOpenid, query.getOpenid());
        queryWrapper.eq(WeTrackMaterialPrivacyAuth::getUnionid, query.getUnionid());
        queryWrapper.eq(WeTrackMaterialPrivacyAuth::getDelFlag, Constants.COMMON_STATE);
        WeTrackMaterialPrivacyAuth one = weTrackMaterialPrivacyAuthService.getOne(queryWrapper);
        if (ObjectUtil.isNull(one)) {
            WeTrackMaterialPrivacyAuth weTrackMaterialPrivacyAuth = new WeTrackMaterialPrivacyAuth();
            weTrackMaterialPrivacyAuth.setOpenid(query.getOpenid());
            weTrackMaterialPrivacyAuth.setUnionid(query.getUnionid());
            weTrackMaterialPrivacyAuth.setAuthTime(new Date());
            weTrackMaterialPrivacyAuth.setCreateTime(new Date());
            weTrackMaterialPrivacyAuth.setDelFlag(Constants.COMMON_STATE);
            weTrackMaterialPrivacyAuthService.save(weTrackMaterialPrivacyAuth);
        }
        return AjaxResult.success();
    }
}
