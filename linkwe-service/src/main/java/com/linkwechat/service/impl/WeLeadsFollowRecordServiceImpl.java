package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.MessageConstants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.enums.QwAppMsgBusinessTypeEnum;
import com.linkwechat.common.enums.WeMsgTypeEnum;
import com.linkwechat.common.enums.task.WeTasksTitleEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecord;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordAttachment;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordContent;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordCooperateUser;
import com.linkwechat.domain.leads.record.query.WeLeadsFollowRecordAttachmentRequest;
import com.linkwechat.domain.leads.record.query.WeLeadsFollowRecordCooperateUserRequest;
import com.linkwechat.domain.leads.record.query.WeLeadsFollowRecordReplyRequest;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordAttachmentVO;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordContentVO;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordDetailVO;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.domain.task.entity.WeTasks;
import com.linkwechat.domain.task.query.WeTasksRequest;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.*;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 线索跟进记录 服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 9:57
 */
@Slf4j
@Service
public class WeLeadsFollowRecordServiceImpl extends ServiceImpl<WeLeadsFollowRecordMapper, WeLeadsFollowRecord> implements IWeLeadsFollowRecordService {
    @Resource
    private WeTasksMapper weTasksMapper;
    @Resource
    private WeLeadsMapper weLeadsMapper;
    @Resource
    private IWeTasksService weTasksService;
    @Resource
    private QwSysUserClient qwSysUserClient;
    @Resource
    private QwAppSendMsgService qwAppSendMsgService;
    @Resource
    private IWeCorpAccountService weCorpAccountService;
    @Resource
    private WeLeadsFollowRecordMapper weLeadsFollowRecordMapper;
    @Resource
    private WeLeadsFollowRecordContentMapper weLeadsFollowRecordContentMapper;
    @Resource
    private WeLeadsFollowRecordAttachmentMapper weLeadsFollowRecordAttachmentMapper;
    @Resource
    private IWeLeadsFollowRecordAttachmentService weLeadsFollowRecordAttachmentService;
    @Resource
    private IWeLeadsFollowRecordCooperateUserService weLeadsFollowRecordCooperateUserService;

    @Override
    public WeLeadsFollowRecordDetailVO detail(Long id) {
        WeLeadsFollowRecord record = this.getById(id);
        if (BeanUtil.isEmpty(record)) {
            throw new ServiceException("跟进记录不存在！", HttpStatus.BAD_REQUEST);
        }
        WeLeads weLeads = weLeadsMapper.selectById(record.getWeLeadsId());
        if (BeanUtil.isEmpty(weLeads)) {
            throw new ServiceException("跟进线索不存在！", HttpStatus.BAD_REQUEST);
        }

        WeLeadsFollowRecordDetailVO detail = new WeLeadsFollowRecordDetailVO();
        detail.setRecordId(id);
        detail.setLeadsName(weLeads.getName());
        detail.setFollowerName(weLeads.getFollowerName());
        detail.setSex(weLeads.getSex());

        //根基记录内容
        LambdaQueryWrapper<WeLeadsFollowRecordContent> queryWrapper = Wrappers.lambdaQuery(WeLeadsFollowRecordContent.class);
        queryWrapper.eq(WeLeadsFollowRecordContent::getRecordId, id);
        queryWrapper.eq(WeLeadsFollowRecordContent::getParentId, 0L);
        queryWrapper.orderByAsc(WeLeadsFollowRecordContent::getRank);
        List<WeLeadsFollowRecordContent> contents = weLeadsFollowRecordContentMapper.selectList(queryWrapper);

        if (CollectionUtil.isNotEmpty(contents)) {

            List<WeLeadsFollowRecordContentVO> vos = new ArrayList<>();
            LambdaQueryWrapper<WeLeadsFollowRecordAttachment> lambdaQuery = Wrappers.lambdaQuery(WeLeadsFollowRecordAttachment.class);
            lambdaQuery.in(WeLeadsFollowRecordAttachment::getContentId, contents.stream().map(WeLeadsFollowRecordContent::getId).collect(Collectors.toList()));
            List<WeLeadsFollowRecordAttachment> attachments = weLeadsFollowRecordAttachmentMapper.selectList(lambdaQuery);
            for (WeLeadsFollowRecordContent content : contents) {
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
            detail.setContents(vos);
        }
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reply(WeLeadsFollowRecordReplyRequest request) {
        //获取跟进记录父类的数据
        LambdaQueryWrapper<WeLeadsFollowRecordContent> queryWrapper = Wrappers.lambdaQuery(WeLeadsFollowRecordContent.class);
        queryWrapper.eq(WeLeadsFollowRecordContent::getRecordId, request.getRecordId());
        queryWrapper.eq(WeLeadsFollowRecordContent::getParentId, 0L);
        queryWrapper.eq(WeLeadsFollowRecordContent::getItemKey, "记录内容");
        WeLeadsFollowRecordContent content = weLeadsFollowRecordContentMapper.selectOne(queryWrapper);
        //附件
        List<WeLeadsFollowRecordAttachmentRequest> attachmentList = request.getAttachmentList();

        //添加跟进记录内容回复
        AjaxResult<SysUser> info = qwSysUserClient.getInfo(request.getWeUserId());
        if (info.getCode() != HttpStatus.SUCCESS) {
            throw new ServiceException("回复目标不存在！");
        }
        SysUser data = info.getData();
        WeLeadsFollowRecordContent build = WeLeadsFollowRecordContent.builder()
                .id(IdUtil.getSnowflakeNextId())
                .recordId(request.getRecordId())
                .itemValue(request.getReplyContent())
                .rank(content.getSubNum() + 1)
                .visible(0)
                .attachment(CollectionUtil.isNotEmpty(attachmentList) ? 1 : 0)
                .parentId(content.getId())
                .createTime(new Date())
                .replierFrom(SecurityUtils.getLoginUser().getSysUser().getUserName())
                .replierFromId(SecurityUtils.getLoginUser().getSysUser().getUserId())
                .replierFromWeUserId(SecurityUtils.getLoginUser().getSysUser().getWeUserId())
                .replierFromAvatar(SecurityUtils.getLoginUser().getSysUser().getAvatar())
                .replierTo(data.getUserName())
                .replierToId(data.getUserId())
                .replierToWeUserId(data.getWeUserId())
                .replierToAvatar(data.getAvatar())
                .subNum(0)
                .build();
        weLeadsFollowRecordContentMapper.insert(build);

        //添加线索的跟进记录附件
        if (CollectionUtil.isNotEmpty(attachmentList)) {
            List<WeLeadsFollowRecordAttachment> collect = attachmentList.stream().map(i -> WeLeadsFollowRecordAttachment.builder()
                    .id(IdUtil.getSnowflake().nextId())
                    .contentId(build.getId())
                    .type(i.getAttachmentType())
                    .title(i.getAttachmentName())
                    .url(i.getAttachmentAddress())
                    .build()).collect(Collectors.toList());
            weLeadsFollowRecordAttachmentService.saveBatch(collect);
        }

        //添加线索的跟进记录协作成员
        List<WeLeadsFollowRecordCooperateUserRequest> cooperateUsers = request.getCooperateUsers();
        if (CollectionUtil.isNotEmpty(cooperateUsers)) {
            List<WeLeadsFollowRecordCooperateUser> collect = cooperateUsers.stream().map(i -> WeLeadsFollowRecordCooperateUser.builder()
                    .id(IdUtil.getSnowflake().nextId())
                    .contentId(build.getId())
                    .userId(i.getUserId())
                    .weUserId(i.getWeUserId())
                    .userName(i.getUserName())
                    .build()).collect(Collectors.toList());
            weLeadsFollowRecordCooperateUserService.saveBatch(collect);
        }

        //回复完成之后，待办任务状态设置为已完成
        WeTasks weTasks = WeTasks.builder().id(request.getTasksId()).status(1).build();
        weTasksMapper.updateById(weTasks);

        //回复消息中存在协作员工，发送"成员的线索跟进@了你"任务
        if (CollectionUtil.isNotEmpty(cooperateUsers)) {
            WeLeadsFollowRecord record = weLeadsFollowRecordMapper.selectById(request.getRecordId());
            //有成员的线索跟进@了你
            WeTasksRequest req = WeTasksRequest.builder()
                    .userId(SecurityUtils.getLoginUser().getSysUser().getUserId())
                    .weUserId(SecurityUtils.getLoginUser().getSysUser().getWeUserId())
                    .userName(SecurityUtils.getLoginUser().getSysUser().getUserName())
                    .leadsId(record.getWeLeadsId())
                    .recordId(request.getRecordId())
                    .cooperateUsers(cooperateUsers).build();
            weTasksService.userFollowUp2You(req);
        }

        //被回复方发送应用通知消息
        //TODO 链接等待前端给，需要包含contentId参数
        String url = "";
        String desc = StrUtil.format(MessageConstants.LEADS_ASSIST_USER_FOLLOW_UP_REPLY_YOU, DateUtil.today(), SecurityUtils.getLoginUser().getSysUser().getUserName());
        this.sendAppMsg(request.getWeUserId(), WeTasksTitleEnum.LEADS_ASSIST_USER_FOLLOW_UP_REPLY_YOU.getTitle(), desc, url);
    }


    /**
     * 发送应用消息-文本卡片消息
     *
     * @param weUserId 发送员工的企微Id
     * @param title    文本卡片消息的标题
     * @param desc     文本卡片消息的描述
     * @param url      文本卡片消息的链接
     * @author WangYX
     * @date 2023/07/24 14:08
     */
    private void sendAppMsg(String weUserId, String title, String desc, String url) {
        //发送应用通知消
        WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
        QwAppMsgBody body = new QwAppMsgBody();
        body.setCorpId(weCorpAccount.getCorpId());
        body.setCorpUserIds(Arrays.asList(weUserId));
        //类型
        body.setBusinessType(QwAppMsgBusinessTypeEnum.COMMON.getType());
        WeMessageTemplate messageTemplate = new WeMessageTemplate();
        messageTemplate.setMsgType(WeMsgTypeEnum.TASKCARD.getMessageType());
        messageTemplate.setTitle(title);
        messageTemplate.setDescription(desc);
        //TODO 链接，等待前端给, 参数要加上
        messageTemplate.setLinkUrl(url);
        messageTemplate.setBtntxt("详情");
        body.setMessageTemplates(messageTemplate);
        qwAppSendMsgService.appMsgSend(body);
    }
}
