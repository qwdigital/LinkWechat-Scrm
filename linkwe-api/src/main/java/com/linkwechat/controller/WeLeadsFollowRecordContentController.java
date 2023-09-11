package com.linkwechat.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordAttachment;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordContent;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordAttachmentVO;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordContentVO;
import com.linkwechat.service.IWeLeadsFollowRecordAttachmentService;
import com.linkwechat.service.IWeLeadsFollowRecordContentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 跟进记录内容
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/24 15:39
 */
@RestController
@RequestMapping("/leads/follow/record/content")
public class WeLeadsFollowRecordContentController extends BaseController {

    @Resource
    private IWeLeadsFollowRecordContentService weLeadsFollowRecordContentService;
    @Resource
    private IWeLeadsFollowRecordAttachmentService weLeadsFollowRecordAttachmentService;

    /**
     * 子类跟进记录内容
     *
     * @param parentId 父类Id
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/07/24 15:42
     */
    @ApiOperation("子类跟进记录内容")
    @GetMapping("/list")
    public TableDataInfo list(Long parentId) {
        //父类Id为0L时，不处理。
        if (parentId.equals(0L)) {
            return getDataTable(new ArrayList<>(0));
        }

        startPage();
        LambdaQueryWrapper<WeLeadsFollowRecordContent> queryWrapper = Wrappers.lambdaQuery(WeLeadsFollowRecordContent.class);
        queryWrapper.eq(WeLeadsFollowRecordContent::getParentId, parentId);
        List<WeLeadsFollowRecordContent> list = weLeadsFollowRecordContentService.list(queryWrapper);
        TableDataInfo dataTable = getDataTable(list);

        if (CollectionUtil.isNotEmpty(list)) {
            //附件
            LambdaQueryWrapper<WeLeadsFollowRecordAttachment> lambdaQuery = Wrappers.lambdaQuery(WeLeadsFollowRecordAttachment.class);
            lambdaQuery.in(WeLeadsFollowRecordAttachment::getContentId, list.stream().map(WeLeadsFollowRecordContent::getId).collect(Collectors.toList()));
            List<WeLeadsFollowRecordAttachment> attachments = weLeadsFollowRecordAttachmentService.list(lambdaQuery);
            //返回值
            List<WeLeadsFollowRecordContentVO> vos = new ArrayList<>();
            for (WeLeadsFollowRecordContent content : list) {
                WeLeadsFollowRecordContentVO vo = new WeLeadsFollowRecordContentVO();
                vo.setId(content.getId());
                vo.setItemKey(content.getItemKey());
                List<WeLeadsFollowRecordAttachment> attachmentList = attachments.stream().filter(i -> i.getContentId().equals(content.getId())).collect(Collectors.toList());

                List<WeLeadsFollowRecordContentVO.Content> contentList = new ArrayList<>();
                WeLeadsFollowRecordContentVO.Content item = new WeLeadsFollowRecordContentVO.Content();
                item.setContent(content.getItemValue());
                item.setParentId(content.getParentId());
                item.setSubNum(content.getSubNum());
                if (attachmentList.size() > 0) {
                    item.setIsAttachment(1);
                    List<WeLeadsFollowRecordAttachmentVO> attachmentVOS = BeanUtil.copyToList(attachmentList, WeLeadsFollowRecordAttachmentVO.class);
                    item.setAttachments(attachmentVOS);
                } else {
                    item.setIsAttachment(0);
                }
                contentList.add(item);
                vo.setItemValue(contentList);
                vos.add(vo);
            }
            dataTable.setRows(vos);
        }
        return dataTable;
    }

}
