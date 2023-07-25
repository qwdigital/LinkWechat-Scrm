package com.linkwechat.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.annotation.RepeatSubmit;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.leads.leads.LeadsStatusEnum;
import com.linkwechat.common.enums.leads.record.FollowModeEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
import com.linkwechat.domain.leads.leads.vo.WeLeadsFollowerVO;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecord;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordAttachment;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordContent;
import com.linkwechat.domain.leads.record.query.WeLeadsAddFollowRequest;
import com.linkwechat.domain.leads.record.query.WeLeadsFollowRecordReplyRequest;
import com.linkwechat.domain.leads.record.query.WeLeadsFollowRecordRequest;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordAttachmentVO;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordContentVO;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordContentVO.Content;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordVO;
import com.linkwechat.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 线索跟进记录
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/18 11:14
 */
@Api(tags = "线索跟进记录相关")
@RestController
@RequestMapping("/leads/follow/record")
public class WeLeadsFollowRecordController extends BaseController {
    @Resource
    private IWeLeadsService weLeadsService;
    @Resource
    private IWeLeadsFollowerService weLeadsFollowerService;
    @Resource
    private IWeLeadsFollowRecordService weLeadsFollowRecordService;
    @Resource
    private IWeLeadsFollowRecordContentService weLeadsFollowRecordContentService;
    @Resource
    private IWeLeadsFollowRecordAttachmentService weLeadsFollowRecordAttachmentService;

    /**
     * 线索的跟进记录
     *
     * @author WangYX
     * @date 2023/07/12 14:38
     * @version 1.0.0
     */
    @ApiOperation(value = "线索的跟进记录")
    @GetMapping("/list")
    public TableDataInfo list(@Validated WeLeadsFollowRecordRequest request) {
        //获取跟进人信息
        LambdaQueryWrapper<WeLeadsFollower> queryWrapper = Wrappers.lambdaQuery(WeLeadsFollower.class);
        queryWrapper.eq(WeLeadsFollower::getLeadsId, request.getWeLeadsId());
        queryWrapper.eq(WeLeadsFollower::getFollowerId, request.getFollowUserId());
        queryWrapper.orderByDesc(WeLeadsFollower::getFollowerStartTime);
        queryWrapper.last("limit 1");
        List<WeLeadsFollower> list = weLeadsFollowerService.list(queryWrapper);
        Optional<WeLeadsFollower> first = list.stream().findFirst();
        if (!first.isPresent()) {
            return getDataTable(CollectionUtil.newArrayList());
        }
        startPage();
        LambdaQueryWrapper<WeLeadsFollowRecord> wrapper = Wrappers.lambdaQuery(WeLeadsFollowRecord.class);
        wrapper.eq(WeLeadsFollowRecord::getFollowUserId, first.get().getId());
        wrapper.orderByDesc(WeLeadsFollowRecord::getCreateTime);
        List<WeLeadsFollowRecord> records = weLeadsFollowRecordService.list(wrapper);

        TableDataInfo dataTable = getDataTable(records);
        List<WeLeadsFollowRecordVO> rows = BeanUtil.copyToList(records, WeLeadsFollowRecordVO.class);
        getContents(rows);
        dataTable.setRows(rows);

        return dataTable;
    }

    /**
     * 添加跟进
     *
     * @param request 添加跟进请求参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/18 15:55
     */
    @RepeatSubmit
    @ApiOperation(value = "添加跟进")
    @PostMapping(value = "/addFollow")
    public AjaxResult addFollow(@RequestBody @Validated WeLeadsAddFollowRequest request) {
        weLeadsFollowerService.addFollow(request);
        return AjaxResult.success(Boolean.TRUE);
    }

    /**
     * 获取跟进方式
     *
     * @param
     * @return {@link AjaxResult<Map<String,Integer>>}
     * @author WangYX
     * @date 2023/07/12 15:22
     */
    @ApiOperation(value = "获取跟进方式")
    @GetMapping(value = "/getFollowMode")
    public AjaxResult<Map<String, Integer>> getFollowMode() {
        Map<String, Integer> map = Arrays.stream(FollowModeEnum.values()).collect(Collectors.toMap(FollowModeEnum::getDesc, FollowModeEnum::getCode));
        return AjaxResult.success(map);
    }

    /**
     * 获取跟进人名单
     *
     * @param leadsId 线索Id
     * @return {@link AjaxResult<List<WeLeadsFollowerVO>>}
     * @author WangYX
     * @date 2023/07/12 15:01
     */
    @ApiOperation(value = "获取跟进人名单")
    @GetMapping(value = "/getFollowUpList/{leadsId}")
    public AjaxResult<List<WeLeadsFollowerVO>> getFollowUpList(@PathVariable("leadsId") Long leadsId) {
        return AjaxResult.success(weLeadsFollowerService.getFollowerList(leadsId));
    }

    /**
     * 构建跟进记录内容
     *
     * @param rows 列表返回记录
     * @author WangYX
     * @date 2023/07/18 17:01
     */
    private void getContents(List<WeLeadsFollowRecordVO> rows) {
        //跟进记录内容
        List<Long> recordIds = rows.stream().map(WeLeadsFollowRecordVO::getId).collect(Collectors.toList());
        LambdaQueryWrapper<WeLeadsFollowRecordContent> queryWrapper = Wrappers.lambdaQuery(WeLeadsFollowRecordContent.class).in(WeLeadsFollowRecordContent::getRecordId, recordIds);
        List<WeLeadsFollowRecordContent> list = weLeadsFollowRecordContentService.list(queryWrapper);
        //顶级父类的记录内容
        List<WeLeadsFollowRecordContent> parentList = list.stream().filter(i -> i.getParentId().equals(0L)).collect(Collectors.toList());
        Map<Long, List<WeLeadsFollowRecordContent>> contentMap = parentList.stream().collect(Collectors.groupingBy(WeLeadsFollowRecordContent::getRecordId));

        //跟进记录内容附件
        LambdaQueryWrapper<WeLeadsFollowRecordAttachment> lambdaQuery = Wrappers.lambdaQuery(WeLeadsFollowRecordAttachment.class);
        lambdaQuery.in(WeLeadsFollowRecordAttachment::getContentId, list.stream().map(WeLeadsFollowRecordContent::getId).collect(Collectors.toList()));
        List<WeLeadsFollowRecordAttachment> attachments = weLeadsFollowRecordAttachmentService.list(lambdaQuery);
        Map<Long, List<WeLeadsFollowRecordAttachment>> attachmentsMap = attachments.stream().collect(Collectors.groupingBy(WeLeadsFollowRecordAttachment::getContentId));

        for (WeLeadsFollowRecordVO row : rows) {
            //记录状态
            row.setRecordStatusFullName(LeadsStatusEnum.ofByCode(row.getRecordStatus()).getStatusCn());

            List<WeLeadsFollowRecordContentVO> vos = new ArrayList<>();

            List<WeLeadsFollowRecordContent> contents = contentMap.get(row.getId());
            if (CollectionUtil.isEmpty(contents)) {
                continue;
            }
            contents.sort(Comparator.comparing(WeLeadsFollowRecordContent::getRank));
            for (WeLeadsFollowRecordContent content : contents) {
                WeLeadsFollowRecordContentVO vo = new WeLeadsFollowRecordContentVO();
                vo.setId(content.getId());
                vo.setItemKey(content.getItemKey());

                List<Content> contentList = new ArrayList<>();
                contentList.add(getContent(content, attachmentsMap));
                //子类跟进内容
                List<WeLeadsFollowRecordContent> subContents = list.stream().filter(i -> i.getParentId().equals(content.getId())).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(subContents)) {
                    subContents.forEach(i -> contentList.add(getContent(i, attachmentsMap)));
                }
                vo.setItemValue(contentList);
                vos.add(vo);
            }
            row.setContents(vos);
        }
    }

    /**
     * 构建跟进内容
     *
     * @param content        跟进内容
     * @param attachmentsMap 跟进内容附件map
     * @return {@link Content}
     * @author WangYX
     * @date 2023/07/18 16:56
     */
    private Content getContent(WeLeadsFollowRecordContent content, Map<Long, List<WeLeadsFollowRecordAttachment>> attachmentsMap) {
        Content result = new Content();
        result.setContent(content.getItemValue());
        result.setCreateTime(content.getCreateTime());
        result.setIsAttachment(content.getAttachment());
        if (content.getAttachment().equals(1)) {
            List<WeLeadsFollowRecordAttachment> attachmentList = attachmentsMap.get(content.getId());
            if (CollectionUtil.isNotEmpty(attachmentList)) {
                List<WeLeadsFollowRecordAttachmentVO> weLeadsFollowRecordAttachmentVOS = BeanUtil.copyToList(attachmentList, WeLeadsFollowRecordAttachmentVO.class);
                result.setAttachments(weLeadsFollowRecordAttachmentVOS);
            }
        }
        result.setParentId(content.getParentId());
        result.setReplierFromId(content.getReplierFromId());
        result.setReplierFromWeUserId(content.getReplierFromWeUserId());
        result.setReplierFrom(content.getReplierFrom());
        result.setReplierToId(content.getReplierToId());
        result.setReplierToWeUserId(content.getReplierToWeUserId());
        result.setReplierTo(content.getReplierTo());
        return result;
    }

    /**
     * 跟进记录详情
     *
     * @param recordId 跟进记录Id
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/24 14:31
     */
    @ApiOperation(value = "跟进记录详情")
    @GetMapping("/{recordId}")
    public AjaxResult detail(@PathVariable("recordId") Long recordId) {
        return AjaxResult.success(weLeadsFollowRecordService.detail(recordId));
    }

    /**
     * 通过内容Id获取跟进记录详情
     *
     * @param contentId 内容Id
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/24 18:21
     */
    @ApiOperation(value = "通过内容Id获取跟进记录详情")
    @GetMapping("/content/{contentId}")
    public AjaxResult detailByContentId(@PathVariable("contentId") Long contentId) {
        WeLeadsFollowRecordContent content = weLeadsFollowRecordContentService.getById(contentId);
        if (BeanUtil.isEmpty(content)) {
            throw new ServiceException("内容Id不存在");
        }
        return AjaxResult.success(weLeadsFollowRecordService.detail(content.getRecordId()));
    }

    /**
     * 获取回复目标
     *
     * @param contentId 跟进记录内容Id
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/24 18:43
     */
    @ApiOperation(value = "获取回复目标")
    @GetMapping("/replay/target")
    public AjaxResult getReplyTarget(Long contentId) {
        WeLeadsFollowRecordContent content = weLeadsFollowRecordContentService.getById(contentId);
        if (content.getParentId().equals(0L)) {
            Long recordId = content.getRecordId();
            WeLeadsFollowRecord weLeadsFollowRecord = weLeadsFollowRecordService.getById(recordId);
            WeLeads weLeads = weLeadsService.getById(weLeadsFollowRecord.getWeLeadsId());
            if (BeanUtil.isNotEmpty(weLeads)) {
                Map<String, Object> map = new HashedMap(3);
                map.put("userId", weLeads.getFollowerId());
                map.put("weUserId", weLeads.getWeUserId());
                map.put("userName", weLeads.getFollowerName());
                return AjaxResult.success(Arrays.asList(map));
            }
            return AjaxResult.success(CollectionUtil.newArrayList());
        }

        LambdaQueryWrapper<WeLeadsFollowRecordContent> wrapper = Wrappers.lambdaQuery(WeLeadsFollowRecordContent.class);
        wrapper.eq(WeLeadsFollowRecordContent::getParentId, content.getParentId());
        List<WeLeadsFollowRecordContent> list = weLeadsFollowRecordContentService.list(wrapper);
        List<Map<String, Object>> collect = list.stream().map(i -> {
            Map<String, Object> map = new HashedMap(3);
            map.put("userId", i.getReplierFromId());
            map.put("weUserId", i.getReplierFromWeUserId());
            map.put("userName", i.getReplierFrom());
            return map;
        }).collect(Collectors.toList());
        return AjaxResult.success(collect);
    }

    /**
     * 回复
     *
     * @param request 回复请求参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/25 10:14
     */
    @ApiOperation(value = "回复")
    @PostMapping("/reply")
    public AjaxResult reply(@RequestBody WeLeadsFollowRecordReplyRequest request) {
        weLeadsFollowRecordService.reply(request);
        return AjaxResult.success();
    }


}
