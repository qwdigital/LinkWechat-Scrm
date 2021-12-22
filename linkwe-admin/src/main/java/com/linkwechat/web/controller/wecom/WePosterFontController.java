package com.linkwechat.web.controller.wecom;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.wecom.domain.WePosterFont;
import com.linkwechat.wecom.service.IWePosterFontService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ws
 */
@RestController
@RequestMapping(value = "wecom/posterFont/")
@Api(description = "海报字体")
public class WePosterFontController extends BaseController {


    @Resource
    private IWePosterFontService wePosterFontService;

    @PostMapping(value = "posterFont")
    @ApiOperation("创建海报字体")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult insertPosterFont(@RequestBody WePosterFont posterFont) {
        posterFont.setMediaType(MediaType.POSTER_FONT.getType());
        wePosterFontService.save(posterFont);
        return AjaxResult.success("创建成功");
    }


    @PutMapping(value = "posterFont")
    @ApiOperation("修改海报字体")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult updatePosterFont(@RequestBody WePosterFont posterFont) {
        if (posterFont.getId() == null) {
            return AjaxResult.error("id为空");
        }
        posterFont.setMediaType(null);
        wePosterFontService.saveOrUpdate(posterFont);
        return AjaxResult.success("修改成功");
    }

    @GetMapping(value = "posterFontList")
    @ApiOperation("列表查询海报字体")
    public AjaxResult selectPosterFontList() {
        List<WePosterFont> fontList = wePosterFontService.lambdaQuery()
                .eq(WePosterFont::getDelFlag, 0)
                .orderByDesc(WePosterFont::getOrder)
                .orderByDesc(WePosterFont::getCreateTime)
                .list();
        return AjaxResult.success(fontList);
    }

    @GetMapping(value = "posterFontPage")
    @ApiOperation("分页查询海报字体")
    public AjaxResult selectPosterFontPage() {
        startPage();
        List<WePosterFont> fontList = wePosterFontService.lambdaQuery()
                .eq(WePosterFont::getDelFlag, 0)
                .orderByDesc(WePosterFont::getOrder)
                .orderByDesc(WePosterFont::getCreateTime)
                .list();
        return AjaxResult.success(getDataTable(fontList));
    }

    @DeleteMapping(value = "posterFont/{id}")
    @ApiOperation("删除海报字体")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult deletePosterFont(@PathVariable Long id) {
        wePosterFontService.update(
                Wrappers.lambdaUpdate(WePosterFont.class).set(WePosterFont::getDelFlag, 1).eq(WePosterFont::getId, id)
        );
        return AjaxResult.success("删除成功");
    }


}
