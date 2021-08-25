package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.WeSensitive;
import com.linkwechat.wecom.domain.query.WeSensitiveHitQuery;
import com.linkwechat.wecom.domain.vo.WeChatContactSensitiveMsgVO;
import com.linkwechat.wecom.service.IWeSensitiveService;
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
@RequestMapping("/wecom/sensitive")
@Api(tags = "敏感词管理")
public class WeSensitiveController extends BaseController {
    @Autowired
    private IWeSensitiveService weSensitiveService;

    /**
     * 查询敏感词设置列表
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:sensitive:list')")
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
    //   @PreAuthorize("@ss.hasPermi('wecom:sensitive:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询敏感词详情",httpMethod = "GET")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weSensitiveService.selectWeSensitiveById(id));
    }

    /**
     * 新增敏感词设置
     */
    //    @PreAuthorize("@ss.hasPermi('wecom:sensitive:add')")
    @Log(title = "敏感词设置", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "添加敏感词",httpMethod = "POST")
    public AjaxResult add(@Valid @RequestBody WeSensitive weSensitive) {
        return toAjax(weSensitiveService.insertWeSensitive(weSensitive));
    }

    /**
     * 修改敏感词设置
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:sensitive:edit')")
    @Log(title = "敏感词设置", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改敏感词",httpMethod = "PUT")
    public AjaxResult edit(@Valid @RequestBody WeSensitive weSensitive) {
        Long id = weSensitive.getId();
        WeSensitive originData = weSensitiveService.selectWeSensitiveById(id);
        if (originData == null) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "数据不存在");
        }
        return toAjax(weSensitiveService.updateWeSensitive(weSensitive));
    }

    /**
     * 删除敏感词设置
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:sensitive:remove')")
    @Log(title = "敏感词设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除敏感词",httpMethod = "DELETE")
    public AjaxResult remove(@PathVariable("ids") String ids) {
        String[] id = ids.split(",");
        Long[] idArray = new Long[id.length];
        Arrays.stream(id).map(Long::parseLong).collect(Collectors.toList()).toArray(idArray);
        return toAjax(weSensitiveService.destroyWeSensitiveByIds(idArray));
    }

    /**
     * 敏感词命中查询
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:sensitivehit:list')")
    @GetMapping("/hit/list")
    @ApiOperation(value = "敏感词命中查询",httpMethod = "GET")
    public TableDataInfo<List<WeChatContactSensitiveMsgVO>> hitList(WeSensitiveHitQuery query) {
        return getDataTable(weSensitiveService.getHitSensitiveList(query));
    }
}
