package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeMomentsClient;
import com.linkwechat.wecom.domain.WeMoments;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.domain.dto.moments.MomentsParamDto;
import com.linkwechat.wecom.mapper.WeMomentsMapper;
import com.linkwechat.wecom.service.IWeMomentsService;
import com.linkwechat.wecom.service.IWeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeMomentsServiceImpl extends ServiceImpl<WeMomentsMapper, WeMoments> implements IWeMomentsService {


    @Autowired
    WeMomentsClient weMomentsClient;


    @Autowired
    IWeUserService iWeUserService;


    /**
     * 发送更新朋友圈
     * @param weMoments
     */
    @Override
    public void addOrUpdateMoments(WeMoments weMoments) {


        String addUser = weMoments.getAddUser();
        if(StringUtils.isNotEmpty(addUser)){

            List<WeUser> weUsers = iWeUserService.listByIds(
                    ListUtil.toList(addUser.split(","))
            );
            if(CollectionUtil.isNotEmpty(weUsers)){
                weMoments.setAddUserName(
                        Joiner.on(",").join( weUsers.stream().map(WeUser::getName).collect(Collectors.toList()))
                );
            }

            List<WeUser> noWeUser = iWeUserService.list(new LambdaQueryWrapper<WeUser>()
                    .notIn(WeUser::getUserId, ListUtil.toList(addUser.split(",")))
            );

            if(CollectionUtil.isNotEmpty(noWeUser)){

                weMoments.setNoAddUser(
                        Joiner.on(",").join( noWeUser.stream().map(WeUser::getUserId).collect(Collectors.toList()))
                );


                weMoments.setNoAddUserName(
                        Joiner.on(",").join( noWeUser.stream().map(WeUser::getName).collect(Collectors.toList()))
                );

            }



        }



        if(this.saveOrUpdate(weMoments)){

           //企业动态,同步企业微信端
           if(weMoments.getType()
                   .equals(new Integer(1))){
               //附件封装
               List<MomentsParamDto.BaseAttachments> attachments=new ArrayList<>();
               WeMoments.OtherContent otherContent = weMoments.getOtherContent();
               if(null != otherContent){
                    //图片
                   if(otherContent.getAnnexType().equals(new Integer(1))){

                      if(StringUtils.isNotEmpty(otherContent.getAnnexMediaid())){
                          Arrays.stream(otherContent.getAnnexMediaid().split(","))
                                  .forEach(k->{
                                      attachments.add(
                                              MomentsParamDto.ImageAttachments.builder().image(
                                                      MomentsParamDto.Image.builder()
                                                              .mediaId(null)
                                                              .build()
                                              ).build()
                                      );

                          });
                      }
                   }

                   //视频
                   if(otherContent.getAnnexType().equals(new Integer(2))){
                       if(StringUtils.isNotEmpty(otherContent.getAnnexMediaid())){
                           attachments.add(
                                   MomentsParamDto.VideoAttachments.builder().video(
                                           MomentsParamDto.Video.builder()
                                                   .mediaId(otherContent.getAnnexMediaid())
                                                   .build()
                                   ).build()
                           );
                       }

                   }

                   //链接
                   if(otherContent.getAnnexType().equals(new Integer(3))){
                      if(StringUtils.isNotEmpty(otherContent.getAnnexUrl())
                      &&StringUtils.isNotEmpty(otherContent.getAnnexMediaid())){
                          attachments.add(
                                  MomentsParamDto.LinkAttachments.builder().link(
                                          MomentsParamDto.Link.builder()
                                                  .url(otherContent.getAnnexUrl())
                                                  .mediaId(otherContent.getAnnexMediaid())
                                                  .build()
                                  ).build()
                          );
                      }
                   }
               }


               MomentsParamDto.VisibleRange visibleRange
                       = MomentsParamDto.VisibleRange.builder().build();

               if(weMoments.getScopeType().equals(new Integer(2))){ //部分
                   if(StringUtils.isNotEmpty(weMoments.getCustomerTag())){ //客户标签
                       visibleRange.setExternal_contact_list(
                               MomentsParamDto.ExternalContactList.builder()
                                       .tag_list(weMoments.getCustomerTag().split(","))
                                       .build()
                       );
                   }

                   if(StringUtils.isNotEmpty(weMoments.getAddUser())){//指定发送人
                        visibleRange.setSender_list(
                                MomentsParamDto.SenderList.builder()
                                        .user_list(weMoments.getAddUser().split(","))
                                        .build()
                        );
                   }


               }
               WeResultDto weResultDto = weMomentsClient.addMomentTask(
                       MomentsParamDto.builder()
                               .text(MomentsParamDto.Text.builder()
                                       .content(weMoments.getTextContent())
                                       .build())
                               .visible_range(visibleRange)
                               .attachments(attachments)
                               .build()
               );
               //更新jobid
               if(weResultDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
                   weMoments.setJobId(weMoments.getJobId());
                   this.updateById(weMoments);
               }

           }
       }


    }


}
