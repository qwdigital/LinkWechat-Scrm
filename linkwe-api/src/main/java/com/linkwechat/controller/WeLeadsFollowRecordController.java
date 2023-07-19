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
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
import com.linkwechat.domain.leads.leads.vo.WeLeadsFollowerVO;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecord;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordAttachment;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordContent;
import com.linkwechat.domain.leads.record.query.WeLeadsAddFollowRequest;
import com.linkwechat.domain.leads.record.query.WeLeadsFollowRecordRequest;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordAttachmentVO;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordContentVO;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordContentVO.Content;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordVO;
import com.linkwechat.service.IWeLeadsFollowRecordAttachmentService;
import com.linkwechat.service.IWeLeadsFollowRecordContentService;
import com.linkwechat.service.IWeLeadsFollowRecordService;
import com.linkwechat.service.IWeLeadsFollowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
     * @param rows
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


}
