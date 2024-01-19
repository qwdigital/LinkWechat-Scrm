package com.linkwechat.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.utils.QREncode;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeKeyWordGroupSub;
import com.linkwechat.domain.WeKeywordGroupViewCount;
import com.linkwechat.domain.community.WeKeywordGroupTask;
import com.linkwechat.domain.community.vo.WeKeywordGroupViewCountVo;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.mapper.WeKeywordGroupTaskMapper;
import com.linkwechat.service.IWeCommunityKeywordToGroupService;
import com.linkwechat.service.IWeKeyWordGroupSubService;
import com.linkwechat.service.IWeKeywordGroupViewCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class WeCommunityKeywordToGroupServiceImpl  extends ServiceImpl<WeKeywordGroupTaskMapper, WeKeywordGroupTask> implements IWeCommunityKeywordToGroupService {



    @Autowired
    private IWeKeywordGroupViewCountService iWeKeywordGroupViewCountService;

    @Autowired
    private IWeKeyWordGroupSubService iWeKeyWordGroupSubService;

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;


    @Autowired
    private QwFileClient qwFileClient;

    @Override
    public List<WeKeywordGroupTask> findLists(WeKeywordGroupTask task) {

        List<WeKeywordGroupTask> groupTasks = this.list(new LambdaQueryWrapper<WeKeywordGroupTask>()
                .like(StringUtils.isNotEmpty(task.getTitle()), WeKeywordGroupTask::getTitle, task.getTitle())
                .orderByDesc(WeKeywordGroupTask::getUpdateTime));


        if(CollectionUtil.isNotEmpty(groupTasks)){
            groupTasks.stream().forEach(k->{

                WeKeywordGroupViewCountVo groupViewCountVo = iWeKeywordGroupViewCountService.countTab(k.getId());

                   if(null != groupViewCountVo){
                       k.setTotalJoinGroupNmber(groupViewCountVo.getTotalJoinGroupNmber());
                       k.setTotalViewNumber(groupViewCountVo.getTotalViewNumber());
                   }
                //设置关键词数量
                k.setKeyWordGroupNumber(
                        iWeKeyWordGroupSubService.count(new LambdaQueryWrapper<WeKeyWordGroupSub>()
                                .eq(WeKeyWordGroupSub::getKeywordGroupId,k.getId()))
                );


            });
        }


        return groupTasks;
    }

    @Override
    public WeKeywordGroupTask findBaseInfo(Long id,String unionId, Boolean isCount) {
        if(isCount){
            List<WeKeywordGroupViewCount> groupViewCounts = iWeKeywordGroupViewCountService.list(new LambdaQueryWrapper<WeKeywordGroupViewCount>()
                    .eq(WeKeywordGroupViewCount::getKeywordGroupId, id)
                    .eq(WeKeywordGroupViewCount::getUnionId,unionId)
                    .apply("date_format (create_time,'%Y-%m-%d') = date_format ({0},'%Y-%m-%d')", new Date())
            );
            if(CollectionUtil.isNotEmpty(groupViewCounts)){
                WeKeywordGroupViewCount weKeywordGroupViewCount = groupViewCounts.stream().findFirst().get();
                weKeywordGroupViewCount.setViewNum(weKeywordGroupViewCount.getViewNum()+1);
                iWeKeywordGroupViewCountService.updateById(weKeywordGroupViewCount);
            }else{
                iWeKeywordGroupViewCountService.save(WeKeywordGroupViewCount.builder()
                                .keywordGroupId(id)
                                .delFlag(Constants.COMMON_STATE)
                                .viewNum(1)
                                .unionId(unionId)
                        .build());
            }
        }

        return this.getById(id);
    }

    @Override
    public void createOrUpdate(WeKeywordGroupTask groupTask) throws IOException {

        WeKeywordGroupTask weKeywordGroupTask = getById(groupTask.getId());
        if(null != weKeywordGroupTask){
            groupTask.setKeywordGroupUrl(weKeywordGroupTask.getKeywordGroupUrl());
            groupTask.setKeywordGroupQrUrl(weKeywordGroupTask.getKeywordGroupQrUrl());
        }
          if(StringUtils.isEmpty(groupTask.getKeywordGroupUrl())||StringUtils.isEmpty(groupTask.getKeywordGroupQrUrl())){
              //构造关键词群专属二维码
              String contentUrl = MessageFormat.format(linkWeChatConfig.getKeyWordGroupUrl(), groupTask.getId().toString());
              FileEntity fileEntity = qwFileClient.upload(QREncode.getQRCodeMultipartFile(contentUrl, null)).getData();
              if (null != fileEntity) {
                  groupTask.setKeywordGroupUrl(contentUrl);
                  groupTask.setKeywordGroupQrUrl(fileEntity.getUrl());
              }
          }
        saveOrUpdate(groupTask);
    }



}
