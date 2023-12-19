package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.utils.QREncode;
import com.linkwechat.domain.community.WeKeywordGroupTask;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.mapper.WeKeywordGroupTaskMapper;
import com.linkwechat.service.IWeCommunityKeywordToGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Service
public class WeCommunityKeywordToGroupServiceImpl  extends ServiceImpl<WeKeywordGroupTaskMapper, WeKeywordGroupTask> implements IWeCommunityKeywordToGroupService {


    @Autowired
    private LinkWeChatConfig linkWeChatConfig;


    @Autowired
    private QwFileClient qwFileClient;

    @Override
    public void createOrUpdate(WeKeywordGroupTask groupTask) throws IOException {

        WeKeywordGroupTask weKeywordGroupTask = getById(groupTask.getId());
        if(null != weKeywordGroupTask){
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


//    @Override
//    public boolean isNameOccupied(String taskName) {
//
//        List<WeKeywordGroupTask> groupTasks = this.list(new LambdaQueryWrapper<WeKeywordGroupTask>()
//                .eq(WeKeywordGroupTask::getTaskName, taskName)
//        );
//        if(CollectionUtil.isNotEmpty(groupTasks)){
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public List<WeKeywordGroupTask> getTaskList(WeKeywordGroupTask task) {
//        return this.baseMapper.getTaskList(task);
//    }
//
//    @Override
//    public WeKeywordGroupTask getTaskById(Long taskId) {
//        return this.baseMapper.getTaskById(taskId);
//    }
//
//    @Override
//    public List<WeKeywordGroupTask> filterByNameOrKeyword(String word) {
//        return this.baseMapper.filterByNameOrKeyword(word);
//    }


}
