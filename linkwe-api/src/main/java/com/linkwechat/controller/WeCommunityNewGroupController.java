package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import com.linkwechat.domain.community.WeEmpleCode;
import com.linkwechat.domain.community.query.WeCommunityNewGroupQuery;
import com.linkwechat.domain.community.vo.WeCommunityNewGroupVo;
import com.linkwechat.service.IWeCommunityNewGroupService;
import com.linkwechat.service.IWeEmpleCodeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 社区运营新客自动拉群
 */
@RestController
@RequestMapping(value = "/communityNewGroup")
public class WeCommunityNewGroupController extends BaseController {

    @Autowired
    private IWeCommunityNewGroupService iWeCommunityNewGroupService;


    /**
     * 新增新客自动拉群
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody @Validated WeCommunityNewGroupQuery weCommunityNewGroupQuery) {

        iWeCommunityNewGroupService.add(weCommunityNewGroupQuery);
        return AjaxResult.success();
    }



    /**
     * 单个下载
     *z
     * @param id       待下载员工活码
     * @param response 响应
     */
    @Log(title = "员工活码下载", businessType = BusinessType.OTHER)
    @GetMapping("/download")
    public void download(String id, HttpServletResponse response) throws IOException {
        WeCommunityNewGroup weCommunityNewGroup = iWeCommunityNewGroupService.getById(id);

        if(null != weCommunityNewGroup){
            FileUtils.downloadFile(weCommunityNewGroup.getEmplCodeUrl(), response.getOutputStream());
        }
    }


    /**
     * 员工活码批量下载
     *
     * @param ids      新客自动拉群ids
     * @param response 输出
     */
    @Log(title = "员工活码批量下载", businessType = BusinessType.OTHER)
    @GetMapping("/downloadBatch")
    public void downloadBatch(Long[] ids, HttpServletResponse response) throws IOException {
        List<WeCommunityNewGroup> weCommunityNewGroups = iWeCommunityNewGroupService.listByIds(ListUtil.toList(ids));

        if(CollectionUtil.isNotEmpty(weCommunityNewGroups)){

            List<FileUtils.FileEntity> fileList=new ArrayList<>();

            weCommunityNewGroups.stream().forEach(k->{
                fileList.add(
                        FileUtils.FileEntity.builder()
                                .fileName(k.getCodeName())
                                .url(k.getEmplCodeUrl())
                                .suffix(".jpg")
                                .build()
                );
            });
            FileUtils.batchDownloadFile(fileList, response.getOutputStream());

        }
    }

    /**
     * 查询新客自动拉群列表
     */
    @GetMapping("/list")
    public TableDataInfo<List<WeCommunityNewGroup>> list(WeCommunityNewGroup weCommunityNewGroup) {
        startPage();
        List<WeCommunityNewGroup> communityNewGroupVos = iWeCommunityNewGroupService.selectWeCommunityNewGroupList(weCommunityNewGroup);
        return getDataTable(communityNewGroupVos);
    }


    /**
     * 获取新客自动拉群详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult<WeCommunityNewGroup> findWeCommunityNewGroupById(@PathVariable("id") String id) {
        WeCommunityNewGroup weCommunityNewGroup
                = iWeCommunityNewGroupService.findWeCommunityNewGroupById(id);

        return AjaxResult.success(weCommunityNewGroup);
    }

    /**
     * 修改新客自动拉群
     */
    @Log(title = "新客自动拉群", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody WeCommunityNewGroupQuery weCommunityNewGroupQuery) {
        iWeCommunityNewGroupService.updateWeCommunityNewGroup(weCommunityNewGroupQuery);
        return AjaxResult.success();
    }

    /**
     * 删除新客自动拉群
     */
    @Log(title = "新客自动拉群", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        iWeCommunityNewGroupService.removeByIds(ListUtil.toList(ids));

        return AjaxResult.success();
    }

}
