package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.web.controller.common.CommonController;
import com.linkwechat.wecom.domain.WePosterFont;
import com.linkwechat.wecom.service.IWePosterFontService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "wecom/posterFont/")
public class WePosterFontController extends BaseController {


    @Resource
    private IWePosterFontService wePosterFontService;

    @PostMapping(value = "posterFont")
    public AjaxResult insertPosterFont(@RequestBody WePosterFont posterFont){
        posterFont.setId(SnowFlakeUtil.nextId());
        posterFont.setDelFlag(1);
        wePosterFontService.save(posterFont);
        return AjaxResult.success("创建成功");
    }


    @PutMapping(value = "posterFont")
    public AjaxResult updatePosterFont(@RequestBody WePosterFont posterFont){
        if(posterFont.getId() == null){
            return AjaxResult.error("id为空");
        }
        wePosterFontService.saveOrUpdate(posterFont);
        return AjaxResult.success("修改成功");
    }

    @GetMapping(value = "posterFontList")
    public AjaxResult selectPosterFontList(){
        List<WePosterFont> fontList = wePosterFontService.lambdaQuery()
                .eq(WePosterFont::getDelFlag,1)
                .orderByDesc(WePosterFont::getOrder)
                .orderByDesc(WePosterFont::getCreateTime)
                .list();
        return AjaxResult.success(fontList);
    }

    @GetMapping(value = "posterFontPage")
    public AjaxResult selectPosterFontPage(){
        startPage();
        List<WePosterFont> fontList = wePosterFontService.lambdaQuery()
                .eq(WePosterFont::getDelFlag,1)
                .orderByDesc(WePosterFont::getOrder)
                .orderByDesc(WePosterFont::getCreateTime)
                .list();
        return AjaxResult.success(getDataTable(fontList));
    }

    @DeleteMapping(value = "posterFont/{id}")
    public AjaxResult deletePosterFont(@PathVariable Long id){
        wePosterFontService.lambdaUpdate().set(WePosterFont::getDelFlag,0).eq(WePosterFont::getId,id);
        return AjaxResult.success("删除成功");
    }



}
