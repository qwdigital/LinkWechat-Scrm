package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.wecom.domain.WeEmpleCode;
import com.linkwechat.wecom.domain.WeFlowerCustomerRel;
import com.linkwechat.wecom.service.IWeEmpleCodeService;
import com.linkwechat.wecom.service.IWeFlowerCustomerRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工活码Controller
 *
 * @author ruoyi
 * @date 2020-10-04
 */
@RestController
@RequestMapping("/wecom/code")
public class WeEmpleCodeController extends BaseController {
    @Autowired
    private IWeEmpleCodeService weEmpleCodeService;


    @Autowired
    private IWeFlowerCustomerRelService weFlowerCustomerRelService;

    /**
     * 查询员工活码列表
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:code:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeEmpleCode weEmpleCode) {
        startPage();
        List<WeEmpleCode> list = weEmpleCodeService.selectWeEmpleCodeList(weEmpleCode);
        return getDataTable(list);
    }


    /**
     * 获取员工活码详细信息
     */
    //  @PreAuthorize("@ss.hasPermi('wecom:code:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weEmpleCodeService.selectWeEmpleCodeById(id));
    }

    /**
     * 新增员工活码
     */
    //  @PreAuthorize("@ss.hasPermi('wecom:code:add')")
    @Log(title = "员工活码", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody @Validated WeEmpleCode weEmpleCode) {
        try {
            weEmpleCodeService.insertWeEmpleCode(weEmpleCode);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof WeComException){
                return AjaxResult.error(e.getMessage());
            }else {
                return AjaxResult.error("请求接口异常！");
            }
        }

    }

    /**
     * 批量新增员工活码
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:code:batchAdd')")
    @Log(title = "批量新增员工活码", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public AjaxResult batchAdd(@RequestBody @Validated WeEmpleCode weEmpleCode) {
        try {
            weEmpleCodeService.insertWeEmpleCodeBatch(weEmpleCode);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof WeComException){
                return AjaxResult.error(e.getMessage());
            }else {
                return AjaxResult.error("请求接口异常！");
            }
        }
    }

    /**
     * 修改员工活码
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:code:edit')")
    @Log(title = "员工活码", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult edit(@RequestBody WeEmpleCode weEmpleCode) {
        weEmpleCodeService.updateWeEmpleCode(weEmpleCode);

        return AjaxResult.success();
    }

    /**
     * 删除员工活码
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:code:remove')")
    @Log(title = "员工活码", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable String ids) {
        List<String> idList = Arrays.stream(StringUtils.split(ids, ",")).collect(Collectors.toList());
        return toAjax(weEmpleCodeService.batchRemoveWeEmpleCodeIds(idList));
    }


    /**
     * 获取员工二维码
     * @param userIds 员工id
     * @param departmentIds 部门id
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:code:qrcode')")
    @Log(title = "获取员工二维码", businessType = BusinessType.DELETE)
    @GetMapping("/getQrcode")
    public AjaxResult getQrcode(String userIds, String departmentIds) {
        return AjaxResult.success(weEmpleCodeService.getQrcode(userIds,departmentIds));
    }

    /**
     * 员工活码批量下载
     *
     * @param ids 员工活码ids
     * @param request 请求
     * @param response 输出
     * @throws Exception
     */
    //  @PreAuthorize("@ss.hasPermi('wecom:code:downloadBatch')")
    @Log(title = "员工活码批量下载", businessType = BusinessType.OTHER)
    @GetMapping("/downloadBatch")
    public void downloadBatch(String ids, HttpServletRequest request, HttpServletResponse response){
        List<Map<String, String>> fileList = Arrays
                .stream(Optional.ofNullable(ids).orElse("").split(","))
                .filter(StringUtils::isNotEmpty)
                .map(id -> {
                    WeEmpleCode code = weEmpleCodeService.getById(id);
                    Map<String, String> fileMap = new HashMap<>();
                    fileMap.put("fileName", code.getUseUserName() + "-" + code.getScenario()+".jpg");
                    fileMap.put("url", code.getQrCode());
                    return fileMap;})
                .collect(Collectors.toList());
        try {
            FileUtils.batchDownloadFile(fileList, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  @PreAuthorize("@ss.hasPermi('wecom:code:download')")
    @Log(title = "员工活码下载", businessType = BusinessType.OTHER)
    @GetMapping("/download")
    public void download(String id, HttpServletRequest request, HttpServletResponse response){
        WeEmpleCode weEmpleCode = weEmpleCodeService.selectWeEmpleCodeById(Long.valueOf(id));
        if (StringUtils.isEmpty(weEmpleCode.getQrCode())){
            throw new CustomException("活码不存在");
        }else {
            try {
                FileUtils.downloadFile(weEmpleCode.getQrCode(), response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 成员添加客户统计
     * @return
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:code:getUserAddCustomerStat')")
    @Log(title = "成员添加客户统计", businessType = BusinessType.OTHER)
    @GetMapping("/getUserAddCustomerStat")
    public AjaxResult getUserAddCustomerStat(WeFlowerCustomerRel weFlowerCustomerRel){
        return AjaxResult.success(weFlowerCustomerRelService.getUserAddCustomerStat(weFlowerCustomerRel));
    }
}
