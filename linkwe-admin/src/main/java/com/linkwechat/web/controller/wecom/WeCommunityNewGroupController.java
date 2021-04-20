package com.linkwechat.web.controller.wecom;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.wecom.domain.WeEmpleCode;
import com.linkwechat.wecom.domain.dto.WeCommunityNewGroupDto;
import com.linkwechat.wecom.domain.vo.WeCommunityNewGroupVo;
import com.linkwechat.wecom.service.IWeCommunityNewGroupService;
import com.linkwechat.wecom.service.IWeEmpleCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 社群运营 新客自动拉群 Controller
 *
 * @author kewen
 * @date 2021-02-19
 */
@Api(description = "新客自动拉群 Controller")
@RestController
@RequestMapping(value = "/wecom/communityNewGroup")
public class WeCommunityNewGroupController extends BaseController {

    @Autowired
    private IWeCommunityNewGroupService weCommunityNewGroupService;

    @Autowired
    private IWeEmpleCodeService weEmpleCodeService;

    /**
     * 新增新客自动拉群
     */
    @ApiOperation(value = "新增新客自动拉群", httpMethod = "POST")
    //   @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:add')")
    @Log(title = "新客自动拉群", businessType = BusinessType.INSERT)
    @PostMapping("/")
    public AjaxResult add(@RequestBody @Validated WeCommunityNewGroupDto communityNewGroupDto) {
        return toAjax(weCommunityNewGroupService.add(communityNewGroupDto));
    }

    /**
     * 单个下载
     *
     * @param id       待下载员工活码
     * @param request  请求
     * @param response 响应
     */
    @ApiOperation(value = "员工活码下载", httpMethod = "GET")
    //   @PreAuthorize("@ss.hasPermi('wecom:code:download')")
    @Log(title = "员工活码下载", businessType = BusinessType.OTHER)
    @GetMapping("/download")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        Optional<WeCommunityNewGroupVo> communityNewGroupVo = weCommunityNewGroupService.selectWeCommunityNewGroupById(Long.valueOf(id));
        communityNewGroupVo.ifPresent(e -> {
            try {
                WeEmpleCode empleCode = weEmpleCodeService.selectWeEmpleCodeById(e.getEmplCodeId());
                FileUtils.downloadFile(empleCode.getQrCode(), response.getOutputStream());
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        });
        return;
    }

    /**
     * 员工活码批量下载
     *
     * @param ids      新客自动拉群ids
     * @param request  请求
     * @param response 输出
     */
    @ApiOperation(value = "员工活码批量下载", httpMethod = "GET")
    //   @PreAuthorize("@ss.hasPermi('wecom:code:downloadBatch')")
    @Log(title = "员工活码批量下载", businessType = BusinessType.OTHER)
    @GetMapping("/downloadBatch")
    public void downloadBatch(Long[] ids, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<WeCommunityNewGroupVo> weCommunityNewGroupVos = weCommunityNewGroupService.selectWeCommunityNewGroupByIds(Arrays.asList(ids));
            //zip输出流
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            if (CollectionUtil.isNotEmpty(weCommunityNewGroupVos)) {
                for (WeCommunityNewGroupVo communityNewGroupVo : weCommunityNewGroupVos) {
                    // 获取改新客拉群对应员工活码的二维码
                    WeEmpleCode empleCode = weEmpleCodeService.selectWeEmpleCodeById(communityNewGroupVo.getEmplCodeId());
                    String qrCode = empleCode.getQrCode();
                    if (StringUtils.isEmpty(qrCode)) {
                        continue;
                    }
                    URL url = new URL(qrCode);
                    // 二维码名称使用场景
                    String fileName = empleCode.getScenario() + ".png";
                    zos.putNextEntry(new ZipEntry(fileName));
                    InputStream fis = url.openConnection().getInputStream();
                    byte[] buffer = new byte[1024];
                    int r = 0;
                    while ((r = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, r);
                    }
                    fis.close();
                }
            }
            //关闭zip输出流
            zos.flush();
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询新客自动拉群列表
     */
    @ApiOperation(value = "查询新客自动拉群列表", httpMethod = "GET")
    //    @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:list')")
    @GetMapping("/list")
    public TableDataInfo<List<WeCommunityNewGroupVo>> list(
            @RequestParam(value = "emplCodeName", required = false) String emplCodeName,
            @RequestParam(value = "createBy", required = false) String createBy,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        startPage();
        List<WeCommunityNewGroupVo> communityNewGroupVos = weCommunityNewGroupService.selectWeCommunityNewGroupList(emplCodeName, createBy, beginTime, endTime);
        return getDataTable(communityNewGroupVos);
    }

    /**
     * 获取新客自动拉群详细信息
     */
    @ApiOperation(value = "获取新客自动拉群详细信息", httpMethod = "GET")
    //   @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult<WeCommunityNewGroupVo> getInfo(@PathVariable("id") @ApiParam("主键ID") String id) {
        Optional<WeCommunityNewGroupVo> communityNewGroupVo = weCommunityNewGroupService.selectWeCommunityNewGroupById(Long.valueOf(id));
        if (communityNewGroupVo.isPresent()) {
            return AjaxResult.success(communityNewGroupVo);
        }
        return AjaxResult.error(HttpStatus.NOT_FOUND, "新客拉群信息不存在");
    }

    /**
     * 修改新客自动拉群
     */
    @ApiOperation(value = "修改新客自动拉群", httpMethod = "PUT")
    //   @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:edit')")
    @Log(title = "新客自动拉群", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable("id") String id, @RequestBody @Validated WeCommunityNewGroupDto communityNewGroupDto) {
        return toAjax(weCommunityNewGroupService.updateWeCommunityNewGroup(Long.valueOf(id), communityNewGroupDto));
    }

    /**
     * 删除新客自动拉群
     */
    @ApiOperation(value = "删除新客自动拉群", httpMethod = "DELETE")
    //   @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:remove')")
    @Log(title = "新客自动拉群", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(weCommunityNewGroupService.batchRemoveWeCommunityNewGroupByIds(Arrays.asList(ids)));
    }

}

