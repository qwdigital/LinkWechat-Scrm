package com.linkwechat.service.impl.strategic.shortlink;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.domain.WeShortLinkPromotion;
import com.linkwechat.domain.WeShortLinkPromotionTemplateClient;
import com.linkwechat.domain.WeShortLinkPromotionTemplateGroup;
import com.linkwechat.domain.WeShortLinkUserPromotionTask;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.wecom.vo.customer.msg.WeAddCustomerMsgVo;
import com.linkwechat.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 短链推广群发逻辑
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/08 17:58
 */
@Service("shortLinkPromotionGroupMsgService")
public class ShortLinkPromotionGroupMsgServiceImpl extends AbstractGroupMsgSendTaskService {

    @Resource
    private IWeShortLinkPromotionService weShortLinkPromotionService;
    @Resource
    private IWeShortLinkPromotionTemplateClientService weShortLinkPromotionTemplateClientService;
    @Resource
    private IWeShortLinkPromotionTemplateGroupService weShortLinkPromotionTemplateGroupService;
    @Resource
    private IWeShortLinkUserPromotionTaskService weShortLinkUserPromotionTaskService;


    @Override
    public void sendGroupMsg(WeAddGroupMessageQuery query) {
        //1.判断任务是否处于待推广的状态
        WeShortLinkPromotion weShortLinkPromotion = weShortLinkPromotionService.getById(query.getId());

        //重试机制，4次
        if (BeanUtil.isEmpty(weShortLinkPromotion)) {
            for (int i = 0; i < 4; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                weShortLinkPromotion = weShortLinkPromotionService.getById(query.getId());
                if (BeanUtil.isNotEmpty(weShortLinkPromotion)) {
                    break;
                }
            }
        }

        //判断任务的推广状态和删除状态
        //删除标识 0 正常 1 删除
        //任务状态: 0待推广 1推广中 2已结束
        if (!(weShortLinkPromotion.getDelFlag().equals(0) && weShortLinkPromotion.getTaskStatus().equals(0))) {
            return;
        }

        LoginUser loginUser = query.getLoginUser();
        SecurityContextHolder.setUserName(loginUser.getUserName());
        SecurityContextHolder.setCorpId(loginUser.getCorpId());

        //查看短链推广模板是否已经删除
        Long businessId = query.getBusinessId();
        if (weShortLinkPromotion.getType().equals(0)) {
            WeShortLinkPromotionTemplateClient client = weShortLinkPromotionTemplateClientService.getById(businessId);
            if (client == null || client.getDelFlag().equals(1)) {
                //删除直接跳出，不继续执行。
                return;
            }
        }

        //发送消息
        WeShortLinkPromotion finalWeShortLinkPromotion = weShortLinkPromotion;
        query.getSenderList().forEach(senderInfo -> {
            WeAddCustomerMsgVo weAddCustomerMsgVo = sendSpecGroupMsgTemplate(query, senderInfo);
            Optional.ofNullable(weAddCustomerMsgVo).ifPresent(i -> {
                if (i.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)) {
                    //更新推广状态
                    LambdaUpdateWrapper<WeShortLinkPromotion> promotionUpdateWrapper = Wrappers.lambdaUpdate();
                    promotionUpdateWrapper.set(WeShortLinkPromotion::getTaskStatus, 1);
                    promotionUpdateWrapper.eq(WeShortLinkPromotion::getId, query.getId());
                    weShortLinkPromotionService.update(promotionUpdateWrapper);

                    //客户
                    if (finalWeShortLinkPromotion.getType().equals(0)) {
                        //获取客户推广模板
                        LambdaQueryWrapper<WeShortLinkPromotionTemplateClient> queryWrapper = Wrappers.lambdaQuery();
                        queryWrapper.eq(WeShortLinkPromotionTemplateClient::getPromotionId, query.getId());
                        queryWrapper.eq(WeShortLinkPromotionTemplateClient::getDelFlag, 0);
                        WeShortLinkPromotionTemplateClient one = weShortLinkPromotionTemplateClientService.getOne(queryWrapper);

                        //更新员工推广短链任务
                        LambdaUpdateWrapper<WeShortLinkUserPromotionTask> updateWrapper = Wrappers.lambdaUpdate();
                        updateWrapper.eq(WeShortLinkUserPromotionTask::getTemplateType, 0);
                        updateWrapper.eq(WeShortLinkUserPromotionTask::getTemplateId, one.getId());
                        updateWrapper.eq(WeShortLinkUserPromotionTask::getUserId, senderInfo.getUserId());
                        updateWrapper.set(WeShortLinkUserPromotionTask::getMsgId, i.getMsgId());
                        updateWrapper.set(WeShortLinkUserPromotionTask::getSendStatus, 2);
                        weShortLinkUserPromotionTaskService.update(updateWrapper);
                    } else if (finalWeShortLinkPromotion.getType().equals(1)) {
                        //获取客群推广模板
                        LambdaQueryWrapper<WeShortLinkPromotionTemplateGroup> queryWrapper = Wrappers.lambdaQuery();
                        queryWrapper.eq(WeShortLinkPromotionTemplateGroup::getPromotionId, query.getId());
                        queryWrapper.eq(WeShortLinkPromotionTemplateGroup::getDelFlag, 0);
                        WeShortLinkPromotionTemplateGroup one = weShortLinkPromotionTemplateGroupService.getOne(queryWrapper);

                        //更新员工推广短链任务
                        LambdaUpdateWrapper<WeShortLinkUserPromotionTask> updateWrapper = Wrappers.lambdaUpdate();
                        updateWrapper.eq(WeShortLinkUserPromotionTask::getTemplateType, 1);
                        updateWrapper.eq(WeShortLinkUserPromotionTask::getTemplateId, one.getId());
                        updateWrapper.eq(WeShortLinkUserPromotionTask::getUserId, senderInfo.getUserId());
                        updateWrapper.set(WeShortLinkUserPromotionTask::getMsgId, i.getMsgId());
                        updateWrapper.set(WeShortLinkUserPromotionTask::getSendStatus, 2);
                        weShortLinkUserPromotionTaskService.update(updateWrapper);
                    }
                }
            });
        });
    }
}
