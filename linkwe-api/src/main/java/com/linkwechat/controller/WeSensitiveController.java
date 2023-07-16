package com.linkwechat.controller;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.domain.WeSensitive;
import com.linkwechat.domain.msgaudit.query.WeSensitiveHitQuery;
import com.linkwechat.domain.msgaudit.vo.WeChatContactSensitiveMsgVo;
import com.linkwechat.service.IWeSensitiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 敏感词Controller
 *
 * @author ruoyi
 * @date 2020-12-29
 */
@RestController
@RequestMapping("/sensitive")
@Api(tags = "敏感词管理")
public class WeSensitiveController extends BaseController {
    @Autowired
    private IWeSensitiveService weSensitiveService;

    /**
     * 查询敏感词设置列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询敏感词列表",httpMethod = "GET")
    public TableDataInfo<List<WeSensitive>> list(WeSensitive weSensitive) {
        startPage();
        List<WeSensitive> list = weSensitiveService.selectWeSensitiveList(weSensitive);
        return getDataTable(list);
    }

    /**
     * 获取敏感词设置详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询敏感词详情",httpMethod = "GET")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weSensitiveService.selectWeSensitiveById(id));
    }

    /**
     * 新增敏感词设置
     */
    @Log(title = "敏感词设置", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "添加敏感词",httpMethod = "POST")
    public AjaxResult add(@Valid @RequestBody WeSensitive weSensitive) {
        weSensitiveService.insertWeSensitive(weSensitive);
        return AjaxResult.success();
    }

    /**
     * 修改敏感词设置
     */
    @Log(title = "敏感词设置", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改敏感词",httpMethod = "PUT")
    public AjaxResult edit(@Valid @RequestBody WeSensitive weSensitive) {
        WeSensitive originData = weSensitiveService.selectWeSensitiveById(weSensitive.getId());
        if (originData == null) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "数据不存在");
        }
        weSensitiveService.updateWeSensitive(weSensitive);
        return AjaxResult.success();
    }

    /**
     * 删除敏感词设置
     */
    @Log(title = "敏感词设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除敏感词",httpMethod = "DELETE")
    public AjaxResult remove(@PathVariable("ids") String ids) {
        String[] id = ids.split(",");
        Long[] idArray = new Long[id.length];
        Arrays.stream(id).map(Long::parseLong).collect(Collectors.toList()).toArray(idArray);
        weSensitiveService.destroyWeSensitiveByIds(idArray);
        return AjaxResult.success();
    }

    /**
     * 敏感词命中查询
     */
    @GetMapping("/hit/list")
    @ApiOperation(value = "敏感词命中查询",httpMethod = "GET")
    public TableDataInfo<WeChatContactSensitiveMsgVo> hitList(WeSensitiveHitQuery query) {
        startPage();
        return getDataTable(weSensitiveService.getHitSensitiveList(query));
    }


    /**
     * 启用或者关闭敏感词
     * @param weSensitive
     * @return
     */
    @PostMapping("/startOrClose")
    public AjaxResult startOrClose(@RequestBody WeSensitive weSensitive){
        weSensitiveService.updateById(weSensitive);
        return AjaxResult.success();
    }
}
