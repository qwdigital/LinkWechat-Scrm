package com.linkwechat.scheduler.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeGroupCodeRange;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatJoinWayQuery;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatUpdateJoinWayQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatGetJoinWayVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.service.IWeGroupCodeRangeService;
import com.linkwechat.service.IWeGroupCodeService;
import com.linkwechat.service.IWeGroupMemberService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * 群活码任务
 *
 * @author danmo
 * @date 2023年06月25日 10:33
 */
@Slf4j
@Component
public class WeGroupCodeTask {

    @Autowired
    private IWeGroupCodeService weGroupCodeService;

    @Autowired
    private IWeGroupMemberService weGroupMemberService;

    @Autowired
    private IWeGroupCodeRangeService weGroupCodeRangeService;

    @Resource
    private QwCustomerClient qwCustomerClient;

    //队列大小按照自己业务去配置
    private final ExecutorService executorService = ThreadUtil.newFixedExecutor(5, "group-code-pool-%d", false);

    //校验群的数量
    private final int checkGroupNum = 4;

    //校验群成员数量
    private final int checkGroupMemberNum = 500;

    /**
     * 校验客群码是否已满5个群
     */
    @XxlJob("weGroupCodeCheck")
    public void process(String param) {

        if (StringUtils.isEmpty(param)) {
            param = XxlJobHelper.getJobParam();
        }
        log.info("weGroupCodeCheck start--------------------------param:{}", param);
        Long codeId = null;
        String configId = null;

        if (StringUtils.isNotEmpty(param)) {
            JSONObject paramObj = JSONObject.parseObject(param);
            codeId = paramObj.getLong("codeId");
            configId = paramObj.getString("configId");
        }
        List<WeGroupCode> groupCodeList = weGroupCodeService.list(new LambdaQueryWrapper<WeGroupCode>()
                .eq(WeGroupCode::getAutoCreateRoom, 1)
                .eq(StringUtils.isNotEmpty(configId), WeGroupCode::getConfigId, configId)
                .eq(Objects.nonNull(codeId), WeGroupCode::getId, codeId)
                .eq(WeGroupCode::getDelFlag, 0));

        if (CollectionUtil.isNotEmpty(groupCodeList)) {
            for (WeGroupCode weGroupCode : groupCodeList) {
                executorService.execute(() -> checkChatGroupNum(weGroupCode));
            }
        }
    }

    /**
     * 检查活码当前群数量
     *
     * @param weGroupCode
     */
    public void checkChatGroupNum(WeGroupCode weGroupCode) {
        if (Objects.isNull(weGroupCode) || StringUtils.isEmpty(weGroupCode.getConfigId())) {
            return;
        }
        log.info("检查活码当前群数量 groupCodeId:{}",weGroupCode.getId());
        WeGroupChatJoinWayQuery query = new WeGroupChatJoinWayQuery();
        query.setConfig_id(weGroupCode.getConfigId());
        WeGroupChatGetJoinWayVo groupChatGetJoinWayVo = qwCustomerClient.getJoinWayForGroupChat(query).getData();

        if (Objects.nonNull(groupChatGetJoinWayVo) && ObjectUtil.equal(0, groupChatGetJoinWayVo.getErrCode())) {

            List<String> chatIdList = groupChatGetJoinWayVo.getJoin_way().getChat_id_list();

            List<String> nowChatIdList = new LinkedList<>();
            List<String> removeChatIdList = new LinkedList<>();

            if (chatIdList.size() >= checkGroupNum) {

                List<Map<String, Object>> memberCountList = weGroupMemberService.listMaps(new QueryWrapper<WeGroupMember>()
                        .select("chat_id, count(1) as total")
                        .lambda()
                        .in(WeGroupMember::getChatId, chatIdList)
                        .eq(WeGroupMember::getDelFlag, 0)
                        .groupBy(WeGroupMember::getChatId));

                if (CollectionUtil.isNotEmpty(memberCountList)) {
                    Map<String, Integer> chatIdAndNumMap = memberCountList.stream().collect(Collectors.toMap(item -> String.valueOf(item.get("chat_id")),
                            item -> Integer.parseInt(String.valueOf(item.get("total"))), (key1, key2) -> key2));

                    chatIdAndNumMap.forEach((chatId, num) -> {
                        if (num >= checkGroupMemberNum) {
                            removeChatIdList.add(chatId);
                        } else {
                            nowChatIdList.add(chatId);
                        }
                    });
                }

                //活码配置
                if (CollectionUtil.isNotEmpty(nowChatIdList) && nowChatIdList.size() != chatIdList.size()) {
                    WeGroupChatUpdateJoinWayQuery updateJoinWayQuery = new WeGroupChatUpdateJoinWayQuery();
                    updateJoinWayQuery.setConfig_id(weGroupCode.getConfigId());
                    updateJoinWayQuery.setScene(2);
                    updateJoinWayQuery.setChat_id_list(nowChatIdList);
                    WeResultVo result = qwCustomerClient.updateJoinWayForGroupChat(updateJoinWayQuery).getData();
                    if (Objects.isNull(result) || ObjectUtil.notEqual(0, result.getErrCode())) {
                        log.error("更新群活码配置失败 query:{},result:{}", JSONObject.toJSONString(updateJoinWayQuery), JSONObject.toJSONString(result));
                    }
                }


                weGroupCodeService.update(new LambdaUpdateWrapper<WeGroupCode>()
                        .set(WeGroupCode::getChatIdList, String.join(",", nowChatIdList))
                        .eq(WeGroupCode::getId, weGroupCode.getId()));

                List<WeGroupCodeRange> rangeList = new LinkedList<>();

                if(CollectionUtil.isNotEmpty(removeChatIdList)){
                    List<WeGroupCodeRange> codeRanges = removeChatIdList.stream().map(chatId -> {
                        WeGroupCodeRange range = new WeGroupCodeRange();
                        range.setCodeId(weGroupCode.getId());
                        range.setChatId(chatId);
                        range.setDelFlag(1);
                        return range;
                    }).collect(Collectors.toList());

                }

                if(CollectionUtil.isNotEmpty(nowChatIdList)){
                    List<WeGroupCodeRange> codeRanges = nowChatIdList.stream().map(chatId -> {
                        WeGroupCodeRange range = new WeGroupCodeRange();
                        range.setCodeId(weGroupCode.getId());
                        range.setChatId(chatId);
                        range.setDelFlag(0);
                        return range;
                    }).collect(Collectors.toList());
                    rangeList.addAll(codeRanges);
                }

                if(CollectionUtil.isNotEmpty(rangeList)){
                    for (WeGroupCodeRange range : rangeList) {
                        weGroupCodeRangeService.saveOrUpdate(range,new LambdaQueryWrapper<WeGroupCodeRange>()
                                .eq(WeGroupCodeRange::getCodeId,weGroupCode.getId())
                                .eq(WeGroupCodeRange::getChatId,range.getChatId()));
                    }
                }
            }
        }
    }

}
