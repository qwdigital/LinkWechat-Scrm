package com.linkwechat.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.vo.WeMaterialVo;
import com.linkwechat.service.IWeMaterialService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 素材中心
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/27 16:12
 */
@RestController
@RequestMapping("/material")
public class WeMaterialController {

    @Resource
    private IWeMaterialService weMaterialService;


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
