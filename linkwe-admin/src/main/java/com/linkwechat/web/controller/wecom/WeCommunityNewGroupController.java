package com.linkwechat.web.controller.wecom;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.wecom.domain.WeCommunityNewGroup;
import com.linkwechat.wecom.domain.WeEmpleCode;
import com.linkwechat.wecom.domain.dto.WeCommunityNewGroupDto;
import com.linkwechat.wecom.domain.vo.WeCommunityNewGroupVo;
import com.linkwechat.wecom.service.IWeCommunityNewGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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
import java.util.stream.Collectors;
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

    /**
     * 查询新客自动拉群列表
     */
    @ApiOperation(value = "查询新客自动拉群列表", httpMethod = "GET")
    @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:list')")
    @GetMapping("/list")
    public TableDataInfo<List<WeCommunityNewGroupVo>> list(@RequestParam(value = "empleCodeName", required = false) String empleCodeName
            , @RequestParam(value = "createBy", required = false) String createBy
            , @RequestParam(value = "beginTime", required = false) String beginTime
            , @RequestParam(value = "endTime", required = false) String endTime) {
        startPage();
        List<WeCommunityNewGroupVo> communityNewGroupVos = weCommunityNewGroupService.selectWeCommunityNewGroupList(empleCodeName, createBy, beginTime, endTime);
        return getDataTable(communityNewGroupVos);
    }

    /**
     * 获取新客自动拉群详细信息
     */
    @ApiOperation(value = "获取新客自动拉群详细信息", httpMethod = "GET")
    @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:query')")
    @GetMapping(value = "/{newGroupId}")
    public AjaxResult<WeCommunityNewGroupVo> getInfo(@PathVariable("newGroupId") @ApiParam("主键ID") String newGroupId) {
        return AjaxResult.success(weCommunityNewGroupService.selectWeCommunityNewGroupById(new Long(newGroupId)));
    }

    /**
     * 修改新客自动拉群
     */
    @ApiOperation(value = "修改新客自动拉群", httpMethod = "PUT")
    @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:edit')")
    @Log(title = "新客自动拉群", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult edit(@RequestBody @Validated WeCommunityNewGroupDto communityNewGroupDto) {
        weCommunityNewGroupService.updateWeCommunityNewGroup(communityNewGroupDto);

        return AjaxResult.success();
    }

    /**
     * 删除新客自动拉群
     */
    @ApiOperation(value = "删除新客自动拉群", httpMethod = "DELETE")
    @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:remove')")
    @Log(title = "新客自动拉群", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable String ids) {
        List<String> idList = Arrays.stream(StringUtils.split(ids, ",")).collect(Collectors.toList());
        return toAjax(weCommunityNewGroupService.batchRemoveWeCommunityNewGroupIds(idList));
    }


    /**
     * 新增新客自动拉群
     */
    @ApiOperation(value = "新增新客自动拉群", httpMethod = "POST")
    @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:add')")
    @Log(title = "新客自动拉群", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody @Validated WeCommunityNewGroupDto communityNewGroupDto) {
        try {
            weCommunityNewGroupService.add(communityNewGroupDto);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof WeComException) {
                return AjaxResult.error(e.getMessage());
            } else {
                return AjaxResult.error("请求接口异常！");
            }
        }

    }


    /**
     * 员工活码批量下载
     *
     * @param ids      新客自动拉群ids
     * @param request  请求
     * @param response 输出
     * @throws Exception
     */
    @ApiOperation(value = "员工活码批量下载", httpMethod = "GET")
    @PreAuthorize("@ss.hasPermi('wecom:code:downloadBatch')")
    @Log(title = "员工活码批量下载", businessType = BusinessType.OTHER)
    @GetMapping("/downloadBatch")
    public void downloadBatch(String ids, HttpServletRequest request, HttpServletResponse response) {
        List<String> idList = Arrays.stream(Optional.ofNullable(ids).orElse("").split(","))
                .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        try {

            List<WeCommunityNewGroupVo> weCommunityNewGroupVos = weCommunityNewGroupService.selectWeCommunityNewGroupByIds(idList);

            //zip输出流
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            if (CollectionUtil.isNotEmpty(weCommunityNewGroupVos)) {
                for (WeCommunityNewGroupVo communityNewGroupVo : weCommunityNewGroupVos) {
                    String qrCode = communityNewGroupVo.getQrCode();
                    if (StringUtils.isEmpty(qrCode)) {
                        continue;
                    }
                    URL url = new URL(qrCode);
                    //每个二维码名称
                    String fileName = communityNewGroupVo.getEmpleCodeName() + communityNewGroupVo.getActivityScene() + ".jpg";
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "员工活码下载", httpMethod = "GET")
    @PreAuthorize("@ss.hasPermi('wecom:code:download')")
    @Log(title = "员工活码下载", businessType = BusinessType.OTHER)
    @GetMapping("/download")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        WeCommunityNewGroupVo communityNewGroupVo = weCommunityNewGroupService.selectWeCommunityNewGroupById(Long.valueOf(id));
        if (StringUtils.isEmpty(communityNewGroupVo.getQrCode())) {
            return;
        } else {
            try {
                FileUtils.downloadFile(communityNewGroupVo.getQrCode(), response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
