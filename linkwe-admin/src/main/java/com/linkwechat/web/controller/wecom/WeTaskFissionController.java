package com.linkwechat.web.controller.wecom;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.config.CosConfig;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUploadUtils;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeTaskFission;
import com.linkwechat.wecom.domain.dto.WeChatUserDTO;
import com.linkwechat.wecom.domain.dto.WeTaskFissionPosterDTO;
import com.linkwechat.wecom.domain.query.WeTaskFissionStatisticQO;
import com.linkwechat.wecom.domain.vo.WeTaskFissionStatisticVO;
import com.linkwechat.wecom.service.IWeTaskFissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


/**
 * 任务宝Controller
 *
 * @author leejoker
 * @date 2021-01-20
 */
@Api("任务宝Controller")
@RestController
@RequestMapping("/wecom/fission")
public class WeTaskFissionController extends BaseController {
    @Autowired
    private IWeTaskFissionService weTaskFissionService;
    @Autowired
    private CosConfig cosConfig;

    /**
     * 查询任务宝列表
     */
    @ApiOperation(value = "查询任务宝列表", httpMethod = "GET")
    @PreAuthorize("@ss.hasPermi('wecom:fission:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeTaskFission weTaskFission) {
        startPage();
        List<WeTaskFission> list = weTaskFissionService.selectWeTaskFissionList(weTaskFission);
        return getDataTable(list);
    }

    /**
     * 查询统计信息
     */
    @ApiOperation(value = "查询统计信息", httpMethod = "GET")
    @PreAuthorize("@ss.hasPermi('wecom:fission:stat')")
    @GetMapping("/stat")
    public AjaxResult statistics(WeTaskFissionStatisticQO weTaskFissionStatisticQO) throws ParseException {
        Date st;
        Date et = DateUtils.getNowDate();
        if (weTaskFissionStatisticQO.getSeven()) {
            st = DateUtils.addDays(et, -7);
        } else if (weTaskFissionStatisticQO.getThirty()) {
            st = DateUtils.addDays(et, -30);
        } else {
            if (StringUtils.isNotBlank(weTaskFissionStatisticQO.getBeginTime()) ^ StringUtils.isNotBlank(weTaskFissionStatisticQO.getEndTime())) {
                throw new WeComException("开始或结束时间不能为空");
            }
            st = DateUtils.parseDate(weTaskFissionStatisticQO.getBeginTime() + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            et = DateUtils.parseDate(weTaskFissionStatisticQO.getEndTime() + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }
        WeTaskFissionStatisticVO vo = weTaskFissionService.taskFissionStatistic(weTaskFissionStatisticQO.getTaskFissionId(), st, et);
        return AjaxResult.success(vo);
    }

    /**
     * 导出任务宝列表
     */
    @ApiOperation(value = "导出任务宝列表", httpMethod = "GET")
    @PreAuthorize("@ss.hasPermi('wecom:fission:export')")
    @Log(title = "任务宝", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeTaskFission weTaskFission) {
        List<WeTaskFission> list = weTaskFissionService.selectWeTaskFissionList(weTaskFission);
        ExcelUtil<WeTaskFission> util = new ExcelUtil<WeTaskFission>(WeTaskFission.class);
        return util.exportExcel(list, "fission");
    }

    /**
     * 获取任务宝详细信息
     */
    @ApiOperation(value = "获取任务宝详细信息", httpMethod = "GET")
    @PreAuthorize("@ss.hasPermi('wecom:fission:query')")
    @GetMapping(value = "/getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weTaskFissionService.selectWeTaskFissionById(id));
    }

    /**
     * 新增任务宝
     */
    @ApiOperation(value = "新增任务宝", httpMethod = "POST")
    @PreAuthorize("@ss.hasPermi('wecom:fission:add')")
    @Log(title = "任务宝", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody WeTaskFission weTaskFission) {
        Long fissionTaskId = weTaskFissionService.insertWeTaskFission(weTaskFission);
        if (fissionTaskId != null) {
            JSONObject json = new JSONObject();
            json.put("id", fissionTaskId);
            return AjaxResult.success(json.toJSONString());
        }
        return AjaxResult.error();
    }

    /**
     * 编辑任务宝
     */
    @ApiOperation(value = "编辑任务宝", httpMethod = "POST")
    @PreAuthorize("@ss.hasPermi('wecom:fission:edit')")
    @Log(title = "任务宝", businessType = BusinessType.INSERT)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody WeTaskFission weTaskFission) {
        if (ObjectUtils.isEmpty(weTaskFission.getId())) {
            return AjaxResult.error("数据id为空");
        }
        WeTaskFission fissionTask = weTaskFissionService.selectWeTaskFissionById(weTaskFission.getId());
        if (ObjectUtils.isEmpty(fissionTask)) {
            return AjaxResult.error("数据不存在");
        }
        CopyOptions options = CopyOptions.create();
        options.setIgnoreNullValue(true);
        BeanUtil.copyProperties(weTaskFission, fissionTask, options);
        Long id = weTaskFissionService.updateWeTaskFission(fissionTask);
        JSONObject json = new JSONObject();
        json.put("id", id);
        return AjaxResult.success(json.toJSONString());
    }

    /**
     * 删除任务宝
     */
    @ApiOperation(value = "删除任务宝", httpMethod = "DELETE")
    @PreAuthorize("@ss.hasPermi('wecom:fission:remove')")
    @Log(title = "任务宝", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(weTaskFissionService.deleteWeTaskFissionByIds(ids));
    }

    /**
     * 发送裂变任务
     */
    @ApiOperation(value = "发送裂变任务", httpMethod = "GET")
    @PreAuthorize("@ss.hasPermi('wecom:fission:send')")
    @Log(title = "发送裂变任务", businessType = BusinessType.OTHER)
    @GetMapping("/send/{id}")
    public AjaxResult send(@PathVariable Long id) {
        weTaskFissionService.sendWeTaskFission(id);
        return AjaxResult.success();
    }

    /**
     * 添加群裂变完成记录
     */
    @ApiOperation(value = "添加群裂变完成记录", httpMethod = "POST")
    @PreAuthorize("@ss.hasPermi('wecom:fission:complete')")
    @Log(title = "添加群裂变完成记录", businessType = BusinessType.OTHER)
    @PostMapping("/complete/{id}/records/{recordId}")
    public AjaxResult completeRecord(@PathVariable("id") Long id,
                                     @PathVariable("recordId") Long recordId,
                                     @RequestBody WeChatUserDTO weChatUserDTO) {
        WeTaskFission taskFission = weTaskFissionService.selectWeTaskFissionById(id);
        if (taskFission == null) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "数据不存在");
        }
        weTaskFissionService.completeFissionRecord(id, recordId, weChatUserDTO);
        return AjaxResult.success(taskFission.getFissQrcode());
    }

    /**
     * 生成带二维码的海报
     */
    @ApiOperation(value = "生成带二维码的海报", httpMethod = "POST")
    @PreAuthorize("@ss.hasPermi('wecom:fission:poster')")
    @Log(title = "生成带二维码的海报", businessType = BusinessType.OTHER)
    @PostMapping("/poster")
    public AjaxResult posterGenerate(@RequestBody WeTaskFissionPosterDTO weTaskFissionPosterDTO) {
        String posterUrl = weTaskFissionService.fissionPosterGenerate(weTaskFissionPosterDTO);
        JSONObject json = new JSONObject();
        json.put("posterUrl", posterUrl);
        return AjaxResult.success(json);
    }

    /**
     * 上传兑奖图片
     */
    @PreAuthorize("@ss.hasPermi('wechat:fission:upload')")
    @Log(title = "上传兑奖图片", businessType = BusinessType.OTHER)
    @PostMapping("/upload")
    @ApiOperation(value = "上传兑奖图片", httpMethod = "POST")
    public AjaxResult upload(@RequestParam(value = "file") MultipartFile file) throws IOException {
        String url = FileUploadUtils.upload2Cos(file, cosConfig);
        JSONObject json = new JSONObject();
        json.put("rewardImageUrl", url);
        return AjaxResult.success(json);
    }


    /**
     * 根据任务id和unionId获取添加客户列表
     */
    @ApiOperation(value = "根据任务id和unionId获取添加客户列表", httpMethod = "GET")
    @PreAuthorize("@ss.hasPermi('wecom:fission:getCustomerListById')")
    @Log(title = "根据任务id和unionId获取添加客户列表", businessType = BusinessType.OTHER)
    @GetMapping("/getCustomerListById/{unionId}/{fissionId}")
    public AjaxResult<List<WeCustomer>> getCustomerListById(@ApiParam("微信用户id") String unionId, @ApiParam("任务id") String fissionId) {
        return AjaxResult.success(weTaskFissionService.getCustomerListById(unionId, fissionId));
    }
}
