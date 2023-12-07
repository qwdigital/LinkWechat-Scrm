package com.linkwechat.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.material.entity.WeContentTalk;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.entity.WeTalkMaterial;
import com.linkwechat.domain.material.query.WeTalkQuery;
import com.linkwechat.domain.material.vo.WeMaterialVo;
import com.linkwechat.domain.material.vo.talk.WeTalkVO;
import com.linkwechat.service.IWeContentTalkService;
import com.linkwechat.service.IWeMaterialService;
import com.linkwechat.service.IWeTalkMaterialService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 话术中心
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/27 16:37
 */
@RestController
@RequestMapping("/talk")
public class WeTalkController extends BaseController {

    @Resource
    private IWeContentTalkService weContentTalkService;
    @Resource
    private IWeTalkMaterialService weTalkMaterialService;
    @Resource
    private IWeMaterialService weMaterialService;

    /**
     * 话术列表
     *
     * @return
     */
    @ApiOperation("话术列表")
    @GetMapping("/list")
    private TableDataInfo list(WeTalkQuery weTalkQuery) {
        startPage();
        //获取话术列表信息
        LambdaQueryWrapper<WeContentTalk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(weTalkQuery.getCategoryId() != null, WeContentTalk::getCategoryId, weTalkQuery.getCategoryId());
        queryWrapper.eq(weTalkQuery.getTalkType() != null, WeContentTalk::getTalkType, weTalkQuery.getTalkType());
        queryWrapper.eq(WeContentTalk::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.like(StringUtils.isNotBlank(weTalkQuery.getTalkTitle()), WeContentTalk::getTalkTitle, weTalkQuery.getTalkTitle());
        queryWrapper.orderByDesc(WeContentTalk::getUpdateTime);
        List<WeContentTalk> list = weContentTalkService.list(queryWrapper);

        TableDataInfo dataTable = getDataTable(list);

        List<WeTalkVO> result = new ArrayList<>();
        //获取话术包含的素材
        if (list != null && list.size() > 0) {
            for (WeContentTalk weContentTalk : list) {
                //话术基本信息
                WeTalkVO weTalkVO = new WeTalkVO();
                weTalkVO.setId(weContentTalk.getId());
                weTalkVO.setCategoryId(weContentTalk.getCategoryId());
                weTalkVO.setTalkType(weContentTalk.getTalkType());
                weTalkVO.setTalkTitle(weContentTalk.getTalkTitle());

                //话术素材关联
                LambdaQueryWrapper<WeTalkMaterial> query = new LambdaQueryWrapper();
                query.eq(WeTalkMaterial::getTalkId, weContentTalk.getId());
                query.orderByAsc(WeTalkMaterial::getSort);
                List<WeTalkMaterial> weTalkMaterials = weTalkMaterialService.list(query);

                if (weTalkMaterials != null && weTalkMaterials.size() > 0) {
                    //获取素材
                    List<Long> collect = weTalkMaterials.stream().map(o -> o.getMaterialId()).collect(Collectors.toList());
                    List<WeMaterial> weMaterials = weMaterialService.listByIds(collect);
                    if (weMaterials != null && weMaterials.size() > 0) {
                        List<WeMaterialVo> weMaterialVos = new ArrayList<>();
                        weMaterials.forEach(o -> {
                            WeMaterialVo weMaterialVo = BeanUtil.copyProperties(o, WeMaterialVo.class);
                            weMaterialVos.add(weMaterialVo);
                        });
                        weTalkVO.setMaterials(weMaterialVos);
                    }
                }
                result.add(weTalkVO);
            }
        }

        dataTable.setRows(result);
        return dataTable;
    }

}
