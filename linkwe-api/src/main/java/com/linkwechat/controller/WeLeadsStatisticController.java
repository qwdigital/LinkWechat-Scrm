package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.LeadsCenterConstants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.leads.leads.LeadsStatusEnum;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.query.WeLeadsUserStatisticRequest;
import com.linkwechat.domain.leads.leads.vo.WeLeadsDataTrendVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsFollowerVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsImportRecordVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsUserStatisticVO;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecord;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 线索统计
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/19 10:08
 */
@Api(tags = "线索统计")
@RestController
@RequestMapping("/leads/statistic")
public class WeLeadsStatisticController extends BaseController {

    @Resource
    private IWeLeadsService weLeadsService;
    @Resource
    private QwSysUserClient qwSysUserClient;
    @Resource
    private IWeLeadsFollowerService weLeadsFollowerService;
    @Resource
    private IWeLeadsStatisticService weLeadsStatisticService;
    @Resource
    private IWeLeadsFollowRecordService weLeadsFollowRecordService;
    @Resource
    private IWeLeadsImportRecordService weLeadsImportRecordService;


    /**
     * 统计
     *
     * @param
     * @return {@link AjaxResult<Map<String,Object>>}
     * @author WangYX
     * @date 2023/07/19 10:11
     */
    @ApiOperation("统计")
    @GetMapping("")
    public AjaxResult<Map<String, Object>> statistic() {
        return AjaxResult.success(weLeadsStatisticService.statistic());
    }

    /**
     * 数据趋势
     *
     * @param beginTime 开始时间 yyyy-MM-dd
     * @param endTime   结束时间 yyyy-MM-dd
     * @return {@link AjaxResult<List<WeLeadsDataTrendVO>>}
     * @author WangYX
     * @date 2023/07/19 11:05
     */
    @ApiOperation("数据趋势")
    @GetMapping("/data/trend")
    public AjaxResult<List<WeLeadsDataTrendVO>> dataTrend(@RequestParam("beginTime") String beginTime, @RequestParam("endTime") String endTime) {
        return AjaxResult.success(weLeadsStatisticService.dataTrend(beginTime, endTime));
    }

    /**
     * 线索转化Top5
     *
     * @param beginTime 开始时间 yyyy-MM-dd
     * @param endTime   结束时间 yyyy-MM-dd
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/19 14:48
     */
    @ApiOperation("线索转化top5")
    @GetMapping("/conversion/top")
    public AjaxResult conversionTop5(@RequestParam("beginTime") String beginTime, @RequestParam("endTime") String endTime) {
        return AjaxResult.success(weLeadsStatisticService.conversionTop5(beginTime, endTime));
    }

    /**
     * 员工线索跟进top5
     *
     * @param beginTime 开始时间 yyyy-MM-dd
     * @param endTime   结束时间 yyyy-MM-dd
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/19 16:13
     */
    @ApiOperation("线索日跟进top5")
    @GetMapping("/follow/top")
    public AjaxResult followTop5(@RequestParam("beginTime") String beginTime, @RequestParam("endTime") String endTime) {
        return AjaxResult.success(weLeadsStatisticService.userFollowTop5(beginTime, endTime));
    }

    /**
     * 线索导入记录
     *
     * @param name 导入表格名称
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/07/19 10:18
     */
    @ApiOperation("导入记录")
    @GetMapping("/import/record")
    public TableDataInfo importRecord(String name) {
        startPage();
        List<WeLeadsImportRecordVO> records = weLeadsImportRecordService.importRecord(StringUtils.queryReplace(name));
        TableDataInfo dataTable = getDataTable(records);

        getImportRecordData(records);
        return dataTable;
    }

    /**
     * 获取导入额外的数据
     *
     * @param records 线索导入记录
     * @author WangYX
     * @date 2023/07/20 11:31
     */
    private void getImportRecordData(List<WeLeadsImportRecordVO> records) {
        if (CollectionUtil.isNotEmpty(records)) {
            List<Long> ids = records.stream().map(WeLeadsImportRecordVO::getId).collect(Collectors.toList());
            //获取线索
            LambdaQueryWrapper<WeLeads> queryWrapper = Wrappers.lambdaQuery(WeLeads.class);
            queryWrapper.select(WeLeads::getId, WeLeads::getLeadsStatus, WeLeads::getImportRecordId, WeLeads::getCustomerId);
            queryWrapper.in(WeLeads::getImportRecordId, ids);
            List<WeLeads> list = weLeadsService.list(queryWrapper);

            NumberFormat instance = NumberFormat.getPercentInstance();
            instance.setMaximumFractionDigits(2);

            for (WeLeadsImportRecordVO record : records) {
                //导入记录对应的线索集合
                List<WeLeads> collect = list.stream().filter(i -> i.getImportRecordId().equals(record.getId())).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(collect)) {
                    //转化成功的线索
                    List<WeLeads> conversionList = collect.stream().filter(i -> i.getLeadsStatus().equals(LeadsStatusEnum.VISIT.getCode())).collect(Collectors.toList());
                    //退回的线索
                    List<WeLeads> returnList = collect.stream().filter(i -> i.getLeadsStatus().equals(LeadsStatusEnum.RETURNED.getCode())).collect(Collectors.toList());
                    //已添加客户数
                    long customerNum = conversionList.stream().map(WeLeads::getCustomerId).distinct().count();

                    //所有线索的跟进次数
                    List<Long> leadsIds = collect.stream().map(WeLeads::getId).collect(Collectors.toList());
                    LambdaQueryWrapper<WeLeadsFollowRecord> wrapper = Wrappers.lambdaQuery(WeLeadsFollowRecord.class);
                    wrapper.eq(WeLeadsFollowRecord::getRecordStatus, 1);
                    wrapper.in(WeLeadsFollowRecord::getWeLeadsId, leadsIds);
                    int count = weLeadsFollowRecordService.count(wrapper);

                    record.setConversionRate(instance.format(conversionList.size() / (double) collect.size()));
                    record.setReturnRate(instance.format(returnList.size() / (double) collect.size()));
                    record.setCustomerNum((int) customerNum);
                    record.setFollowNum((int) count);
                }
            }
        }
    }

    /**
     * 导入记录导出
     *
     * @param name 导入表格名称
     * @author WangYX
     * @date 2023/07/20 11:29
     */
    @ApiOperation("导入记录导出")
    @GetMapping("/import/record/export")
    public void importRecordExport(String name) {
        List<WeLeadsImportRecordVO> records = weLeadsImportRecordService.importRecord(StringUtils.queryReplace(name));
        getImportRecordData(records);
        try {
            HttpServletResponse response = ServletUtils.getResponse();
            String fileName = DateUtil.today() + "-导入记录" + LeadsCenterConstants.XLSX_FILE_EXTENSION;
            String encode = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + encode);
            EasyExcel.write(response.getOutputStream(), WeLeadsImportRecordVO.class).sheet(0).doWrite(records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 员工统计
     *
     * @param request 请求数据
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/07/20 11:00
     */
    @ApiOperation("员工统计")
    @GetMapping("/user")
    public TableDataInfo userStatistic(WeLeadsUserStatisticRequest request) {

        Integer pageNum = ServletUtils.getParameterToInt("pageNum");
        Integer pageSize = ServletUtils.getParameterToInt("pageSize");
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;

        AjaxResult<List<SysUser>> result = qwSysUserClient.findAllSysUser(request.getUserIds(), null, request.getDeptIds());

        if (result.getCode() == HttpStatus.SUCCESS) {
            List<SysUser> data = result.getData();
            //移除admin
            Optional<SysUser> first = data.stream().filter(i -> i.getUserId().equals(1L)).findFirst();
            first.ifPresent(i -> data.remove(i));

            List<SysUser> sub = CollectionUtil.sub(data, (pageNum - 1) * pageSize, pageNum * pageSize);
            List<WeLeadsUserStatisticVO> vos = userStatistic(sub);
            TableDataInfo dataTable = getDataTable(vos);
            dataTable.setTotal(data.size());
            return dataTable;
        }
        return getDataTable(new ArrayList<>());
    }

    /**
     * 员工统计导出
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/07/20 11:45
     */
    @ApiOperation("员工统计导出")
    @GetMapping("/user/export")
    public void userStatisticExport(WeLeadsUserStatisticRequest request) {
        AjaxResult<List<SysUser>> result = qwSysUserClient.findAllSysUser(request.getUserIds(), null, request.getDeptIds());


        List<WeLeadsUserStatisticVO> vos = new ArrayList<>();
        if (result.getCode() == HttpStatus.SUCCESS) {
            List<SysUser> data = result.getData();
            //移除admin
            Optional<SysUser> first = data.stream().filter(i -> i.getUserId().equals(1L)).findFirst();
            first.ifPresent(i -> data.remove(i));

            vos = userStatistic(data);
        }
        try {
            HttpServletResponse response = ServletUtils.getResponse();
            String fileName = DateUtil.today() + "-员工统计" + LeadsCenterConstants.XLSX_FILE_EXTENSION;
            String encode = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + encode);
            EasyExcel.write(response.getOutputStream(), WeLeadsUserStatisticVO.class).sheet(0).doWrite(vos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 员工统计
     *
     * @param sub 员工集合
     * @return {@link List<WeLeadsUserStatisticVO>}
     * @author WangYX
     * @date 2023/07/20 11:00
     */
    public List<WeLeadsUserStatisticVO> userStatistic(List<SysUser> sub) {

        List<Long> userIds = sub.stream().map(SysUser::getUserId).collect(Collectors.toList());
        List<WeLeadsFollowerVO> vos = weLeadsFollowerService.userStatistic(userIds);

        NumberFormat instance = NumberFormat.getPercentInstance();
        instance.setMaximumFractionDigits(2);

        List<WeLeadsUserStatisticVO> result = new ArrayList<>();
        for (SysUser sysUser : sub) {
            //员工跟进的线索
            List<WeLeadsFollowerVO> list = vos.stream().filter(i -> i.getFollowerId().equals(sysUser.getUserId())).collect(Collectors.toList());
            //已转化线索数量
            long conversionList = list.stream().filter(i -> i.getFollowerStatus().equals(2)).count();
            //已退回线索数量
            long returnList = list.stream().filter(i -> i.getFollowerStatus().equals(3)).count();
            //客户数量
            long count = list.stream().filter(i -> i.getCustomerId() != null).distinct().count();

            WeLeadsUserStatisticVO vo = new WeLeadsUserStatisticVO();
            vo.setUserName(sysUser.getUserName());
            vo.setDeptName(sysUser.getDeptName());
            if (list.size() == 0) {
                vo.setConversionRate(instance.format(0));
                vo.setReturnRate(instance.format(0));
            } else {
                vo.setConversionRate(instance.format(conversionList / (double) list.size()));
                vo.setReturnRate(instance.format(returnList / (double) list.size()));
            }
            vo.setCustomerNum((int) count);

            //跟进总次数
            vo.setFollowNum(0);
            List<Long> ids = list.stream().map(WeLeadsFollowerVO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(ids)) {
                LambdaQueryWrapper<WeLeadsFollowRecord> wrapper = Wrappers.lambdaQuery(WeLeadsFollowRecord.class);
                wrapper.eq(WeLeadsFollowRecord::getRecordStatus, 1);
                wrapper.in(WeLeadsFollowRecord::getFollowUserId, ids);
                int followNum = weLeadsFollowRecordService.count(wrapper);
                vo.setFollowNum(followNum);
            }
            result.add(vo);
        }
        return result;
    }
}
