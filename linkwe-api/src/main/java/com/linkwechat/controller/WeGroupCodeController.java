package com.linkwechat.controller;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.groupcode.vo.WeGroupChatInfoVo;
import com.linkwechat.domain.groupcode.vo.WeGroupCodeCountTrendVo;
import com.linkwechat.service.IWeGroupCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 客户群活码Controller
 *
 * @author ruoyi
 * @date 2020-10-07
 */
@Slf4j
@RestController
@RequestMapping("/groupCode")
public class WeGroupCodeController extends BaseController {

    @Autowired
    private IWeGroupCodeService groupCodeService;



    /**
     * 查询客户群活码列表
     */
    @GetMapping("/list")
    public TableDataInfo<WeGroupCode> list(WeGroupCode weGroupCode) {
        startPage();
        List<WeGroupCode> list = groupCodeService.selectWeGroupCodeList(weGroupCode);
        return getDataTable(list);
    }


    /**
     * 获取客群详情
     * @param groupCodeId
     * @return
     */
    @GetMapping("/findWeGroupCodeById/{groupCodeId}")
    public AjaxResult<WeGroupCode> findWeGroupCodeById(@PathVariable Long groupCodeId){

        return AjaxResult.success(
                groupCodeService.findWeGroupCodeById(groupCodeId)
        );
    }


    /**
     * 获取群活码详情
     * @param id
     * @return
     */
    @GetMapping("/getActualCode/{id}")
    public AjaxResult<WeGroupCode> getWeGroupCode(@PathVariable String id){
        return AjaxResult.success(
                groupCodeService.getById(id)
        );
    }

    /**
     * 批量下载群活码
     */
    @Log(title = "群活码批量下载", businessType = BusinessType.OTHER)
    @GetMapping("/downloadBatch")
    public void downloadBatch(String ids, HttpServletResponse response) {

        // 构建文件信息列表
        List<FileUtils.FileEntity> fileList = Arrays.stream(Optional.ofNullable(ids).orElse("").split(","))
                .filter(StringUtils::isNotEmpty).map(id -> {
                    WeGroupCode code = groupCodeService.getById(id);
                    return FileUtils.FileEntity.builder()
                            .fileName(code.getActivityName())
                            .url(code.getCodeUrl())
                            .suffix(".jpg")
                            .build();
                }).collect(Collectors.toList());
        try {
            FileUtils.batchDownloadFile(fileList, response.getOutputStream());
        } catch (IOException e) {
            log.error("群活码批量下载失败:"+e.getMessage());
            throw new CustomException("群活码批量下载失败");
        }
    }

    /**
     * 下载群活码
     */
    @Log(title = "群活码下载", businessType = BusinessType.OTHER)
    @GetMapping("/download")
    public void download(String id, HttpServletResponse response) {
        WeGroupCode weGroupCode = groupCodeService.getById(Long.valueOf(id));
        try {
            FileUtils.downloadFile(weGroupCode.getCodeUrl(), response.getOutputStream());
        } catch (IOException e) {
            log.error("群活码下载失败:"+e.getMessage());
            throw new CustomException("群活码下载失败");
        }
    }


    /**
     * 新增客户群活码
     */
    @Log(title = "客户群活码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult<WeGroupCode> add(@Validated @RequestBody WeGroupCode weGroupCode) {
        //校验活码名称
//       if(groupCodeService.count(new LambdaQueryWrapper<WeGroupCode>()
//               .eq(WeGroupCode::getActivityName,weGroupCode.getActivityName()))>0){
//           return AjaxResult.error(
//                   "活码名称已存在"
//           );
//       }

        groupCodeService.insertWeGroupCode(weGroupCode);
        return AjaxResult.success(
                weGroupCode
        );
    }

    /**
     * 修改客户群活码
     */
    @Log(title = "客户群活码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult<WeGroupCode> edit(@Validated @RequestBody WeGroupCode weGroupCode) {

//        if(!groupCodeService.getById(weGroupCode.getId())
//                .getActivityName().equals(weGroupCode.getActivityName())){
//            if(groupCodeService.count(new LambdaQueryWrapper<WeGroupCode>()
//                    .eq(WeGroupCode::getActivityName,weGroupCode.getActivityName()))>0){
//                return AjaxResult.error(HttpStatus.NOT_DATA_DUPLICATION,
//                        "活码名称已存在"
//                );
//            }
//        }

        return AjaxResult.success(
                groupCodeService
                        .updateWeGroupCode(weGroupCode)
        );
    }

    /**
     * 删除客户群活码
     */
    @Log(title = "客户群活码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult batchRemove(@PathVariable Long[] ids) {
        groupCodeService.batchRemoveByIds(Arrays.asList(ids));
        return AjaxResult.success();
    }


    /**
     * 获取指定活码下,群相关信息
     * @return
     */
    @GetMapping("/findWeGroupChatInfos/{groupId}")
    public AjaxResult<WeGroupChatInfoVo> findWeGroupChatInfoVos(@PathVariable Long groupId){
        return AjaxResult.success(
                groupCodeService.findWeGroupChatInfoVos(groupId)
        );
    }


    /**
     *  获取指定活码加群退群指定时间段内相关数据
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    @GetMapping("/findWeGroupCodeCountTrend")
    public AjaxResult<WeGroupCodeCountTrendVo> findWeGroupCodeCountTrend(String state, String beginTime, String endTime) throws ParseException {

        return AjaxResult.success(
                groupCodeService.findWeGroupCodeCountTrend(state,beginTime,endTime)
        );
    }




}
