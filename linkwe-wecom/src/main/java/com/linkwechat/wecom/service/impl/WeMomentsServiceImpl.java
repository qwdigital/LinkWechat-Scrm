package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.annotation.SynchRecord;
import com.linkwechat.wecom.client.WeMomentsClient;
import com.linkwechat.wecom.constants.SynchRecordConstants;
import com.linkwechat.wecom.domain.WeMoments;
import com.linkwechat.wecom.domain.WeMomentsInteracte;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.domain.dto.moments.*;
import com.linkwechat.wecom.mapper.WeMomentsMapper;
import com.linkwechat.wecom.service.IWeMaterialService;
import com.linkwechat.wecom.service.IWeMomentsService;
import com.linkwechat.wecom.service.IWeUserService;
import com.linkwechat.wecom.service.WeMomentsInteracteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WeMomentsServiceImpl extends ServiceImpl<WeMomentsMapper, WeMoments> implements IWeMomentsService {


    @Autowired
    WeMomentsClient weMomentsClient;


    @Autowired
    IWeUserService iWeUserService;


    @Autowired
    IWeMaterialService iWeMaterialService;


    @Autowired
    WeMomentsInteracteService weMomentsInteracteService;





    /**
     * 朋友圈列表
     * @param weMoments
     * @return
     */
    @Override
    public List<WeMoments> findMoments(WeMoments weMoments) {
        return this.baseMapper.findMoments(weMoments);
    }

    /**
     * 发送更新朋友圈
     * @param weMoments
     */
    @Override
    public void addOrUpdateMoments(WeMoments weMoments) {

        if(weMoments.getType().equals(new Integer(0))){//企业动态
            MomentsParamDto momentsParamDto=new MomentsParamDto();

            if(StringUtils.isNotEmpty(weMoments.getContent())){
                momentsParamDto.setText(
                        MomentsParamDto.Text.builder()
                                .content(weMoments.getContent())
                                .build()
                );
            }

            //设置附件
            List<WeMoments.OtherContent> otherContent = weMoments.getOtherContent();
            if(CollectionUtil.isNotEmpty(otherContent)){

                List<WeMoments.OtherContent> otherContents = otherContent.stream().filter(s -> StringUtils.isNotEmpty(s.getAnnexType()) && StringUtils.isNotEmpty(s.getAnnexUrl()))
                        .collect(Collectors.toList());


                if(CollectionUtil.isNotEmpty(otherContents)){

                    List<MomentsParamDto.BaseAttachments> attachments=new ArrayList<>();

                    //图片
                    if(weMoments.getContentType().equals(MediaType.IMAGE.getMediaType())){
                        otherContents.stream().forEach(image->{
                            String media_id = iWeMaterialService.uploadAttachmentMaterial(image.getAnnexUrl(),
                                    MediaType.IMAGE.getMediaType(),
                                    1
                                    , SnowFlakeUtil.nextId().toString()).getMedia_id();


                            if(StringUtils.isNotEmpty(media_id)){
                                attachments.add(
                                        MomentsParamDto.ImageAttachments.builder()
                                                .msgtype(MediaType.IMAGE.getMediaType())
                                                .image(
                                                        MomentsParamDto.Image.builder()
                                                                .media_id(
                                                                        media_id
                                                                )
                                                                .build()
                                                ).build()
                                );
                            }



                        });
                    }

                    //视频
                    if(weMoments.getContentType().equals(MediaType.VIDEO.getMediaType())){
                        otherContents.stream().forEach(video->{

                            String media_id = iWeMaterialService.uploadAttachmentMaterial(video.getAnnexUrl(),
                                    MediaType.VIDEO.getMediaType(),
                                    1
                                    , SnowFlakeUtil.nextId().toString()).getMedia_id();


                            if(StringUtils.isNotEmpty(media_id)){
                                attachments.add(
                                        MomentsParamDto.VideoAttachments.builder()
                                                .msgtype(MediaType.VIDEO.getMediaType())
                                                .video(
                                                        MomentsParamDto.Video.builder()
                                                                .media_id(
                                                                        media_id
                                                                )
                                                                .build()
                                                ).build()
                                );
                            }

                        });
                    }


                    //图文
                    if(weMoments.getContentType().equals(MediaType.LINK.getMediaType())){
                        otherContents.stream().forEach(link->{
                            attachments.add(
                                    MomentsParamDto.LinkAttachments.builder()
                                            .msgtype(MediaType.LINK.getMediaType())
                                            .link(
                                                    MomentsParamDto.Link.builder()
                                                            .url(link.getAnnexUrl())
                                                            .media_id(
                                                                    iWeMaterialService.uploadTemporaryMaterial(link.getAnnexUrl(),
                                                                            MediaType.IMAGE.getMediaType()
                                                                            ,SnowFlakeUtil.nextId().toString()).getMedia_id()
                                                            )
                                                            .build()
                                            ).build()
                            );
                        });



                    }

                    momentsParamDto.setAttachments(
                            attachments
                    );

                }



            }


                //设置可见范围
            if(weMoments.getScopeType().equals(new Integer(0))){ //部分


                   MomentsParamDto.VisibleRange visibleRange
                           = MomentsParamDto.VisibleRange.builder().build();

                   if(StringUtils.isNotEmpty(weMoments.getCustomerTag())){ //客户标签
                       visibleRange.setExternal_contact_list(
                               MomentsParamDto.ExternalContactList.builder()
                                       .tag_list(weMoments.getCustomerTag().split(","))
                                       .build()
                       );
                   }

                   if(StringUtils.isNotEmpty(weMoments.getNoAddUser())){//指定发送人
                        visibleRange.setSender_list(
                                MomentsParamDto.SenderList.builder()
                                        .user_list(weMoments.getNoAddUser().split(","))
                                        .build()
                        );
                   }

                momentsParamDto.setVisible_range(
                        visibleRange
                );

               }
            MomentsResultDto weResultDto = weMomentsClient.addMomentTask(
                       momentsParamDto
               );


               //入库
               if(weResultDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
                   weMoments.setJobId(weResultDto.getJobid());
                   weMoments.setMomentId(weResultDto.getJobid());
                   this.saveOrUpdate(weMoments);
//                   //根据任务id,获取朋友圈主键
//                   MomentsCreateResultDto momentTaskResult
//                           = weMomentsClient.getMomentTaskResult(weResultDto.getJobid());
//                   if(momentTaskResult.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
//
//                       if(StringUtils.isNotEmpty(momentTaskResult.getResult().getMoment_id())){
//                           weMoments.setMomentId(
//                                   momentTaskResult.getResult().getMoment_id()
//                           );
//                           this.saveOrUpdate(weMoments);
//                       }
//
//                   }
               }





        }


    }

    /**
     * 同步个人朋友圈
     * @param filterType
     */
    @Override
    @Async
    @Transactional
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_CUSTOMER_PERSON_MOMENTS)
    public void synchPersonMoments(Integer filterType) {
         this.synchMoments(filterType);
    }


    /**
     * 同步企业朋友圈
     * @param filterType
     */
    @Override
    @Async
    @Transactional
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_CUSTOMER_ENTERPRISE_MOMENTS)
    public void synchEnterpriseMoments(Integer filterType) {
        this.synchMoments(filterType);
    }


    /**
     * 朋友圈详情
     * @param momentId
     * @return
     */
    @Override
    public WeMoments findMomentsDetail(String momentId) {
        return this.baseMapper.findMomentsDetail(momentId);
    }


    /**
     * 同步朋友圈
     */

    private void synchMoments(Integer filterType) {

        List<MomentsListDetailResultDto.Moment> moments=new ArrayList<>();
        this.getByMoment(null,moments,filterType);

        if(CollectionUtil.isNotEmpty(moments)){

            List<WeMoments> weMoments=new ArrayList<>();

            List<WeMomentsInteracte> interactes=new ArrayList<>();

            moments.stream().forEach(moment -> {

                if(moment.getCreate_type().equals(new Integer(1))){//个人,获取互动数据
                    MomentsInteracteResultDto momentComments = weMomentsClient.get_moment_comments(MomentsInteracteParamDto.builder()
                            .moment_id(moment.getMoment_id())
                            .userid(moment.getCreator())
                            .build());
                    if(momentComments.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
                        List<MomentsInteracteResultDto.Interacte> comment_list
                                = momentComments.getComment_list();

                        if(CollectionUtil.isNotEmpty(comment_list)){//评论
                            comment_list.stream().forEach(k->{
                                interactes.add(
                                        WeMomentsInteracte.builder()
                                                .interacteUserType(StringUtils.isNotEmpty(k.getUserid())?new Integer(0):new Integer(1))
                                                .interacteType(new Integer(0))
                                                .interacteUserId(StringUtils.isNotEmpty(k.getUserid())?k.getUserid():k.getExternal_userid())
                                                .interacteTime(new Date(k.getCreate_time()* 1000L))
                                                .momentId(moment.getMoment_id())
                                                .build()
                                );
                            });

                        }

                        List<MomentsInteracteResultDto.Interacte> like_list
                                = momentComments.getLike_list();

                        if(CollectionUtil.isNotEmpty(like_list)){ //点赞
                            like_list.stream().forEach(k->{
                                interactes.add(
                                        WeMomentsInteracte.builder()
                                                .interacteUserType(StringUtils.isNotEmpty(k.getUserid())?new Integer(0):new Integer(1))
                                                .interacteType(new Integer(1))
                                                .interacteUserId(StringUtils.isNotEmpty(k.getUserid())?k.getUserid():k.getExternal_userid())
                                                .interacteTime(new Date(k.getCreate_time()* 1000L))
                                                .momentId(moment.getMoment_id())
                                                .build()
                                );
                            });
                        }
                    }
                }








                WeMoments weMoment=WeMoments.builder()
                        .type(moment.getCreate_type())
                        .scopeType(moment.getVisible_type())
                        .addUser(moment.getCreator())
                        .momentId(moment.getMoment_id())
                        .creator(moment.getCreator())
                        .build();

                //设置发表范围
                if(moment.getCreate_type().equals(new Integer(0))){
                    getSendResult(weMoment);
                }






                //文本
                Optional.ofNullable(moment.getText()).ifPresent(k->{
                    weMoment.setContent(k.getContent());
                    weMoment.setContentType(MediaType.TEXT.getMediaType());
                });

                List<WeMoments.OtherContent> otherContents=new ArrayList<>();

                //图片
                Optional.ofNullable(moment.getImage()).ifPresent(k->{

                   k.stream().forEach(image->{

                       if(StringUtils.isNotEmpty(weMoment.getContent())){
                           weMoment.setContent(iWeMaterialService.mediaGet(image.getMedia_id(), MediaType.IMAGE.getType(),"jpg"));
                       }

                       otherContents.add(
                               WeMoments.OtherContent.builder()
                                       .annexType(MediaType.IMAGE.getMediaType())
                                       .annexUrl(iWeMaterialService.mediaGet(image.getMedia_id(), MediaType.IMAGE.getType(),"jpg"))
                                       .build()
                       );
                   });
                   weMoment.setContentType(MediaType.IMAGE.getMediaType());
               });

               //视频
               Optional.ofNullable(moment.getVideo()).ifPresent(k->{
                   if(StringUtils.isNotEmpty(weMoment.getContent())){
                       weMoment.setContent(iWeMaterialService.mediaGet(k.getThumb_media_id(), MediaType.IMAGE.getType(),"jpg"));
                   }

                   otherContents.add(
                           WeMoments.OtherContent.builder()
                                   .annexType(MediaType.VIDEO.getMediaType())
                                   .annexUrl(iWeMaterialService.mediaGet(k.getMedia_id(), MediaType.VIDEO.getType(),"mp4"))
                                   .other(iWeMaterialService.mediaGet(k.getThumb_media_id(), MediaType.IMAGE.getType(),"jpg"))
                                   .build()
                   );
                   weMoment.setContentType(MediaType.VIDEO.getMediaType());
               });


               //链接
               Optional.ofNullable(moment.getLink()).ifPresent(k->{
                   if(StringUtils.isNotEmpty(weMoment.getContent())){
                       weMoment.setContent(k.getTitle());
                   }

                   otherContents.add(
                           WeMoments.OtherContent.builder()
                                   .annexType(MediaType.LINK.getMediaType())
                                   .annexUrl(k.getUrl())
                                   .other(k.getTitle())
                                   .build()
                   );
                   weMoment.setContentType(MediaType.LINK.getMediaType());
               });

               if(CollectionUtil.isNotEmpty(otherContents)){
                   weMoment.setOtherContent(otherContents);
               }

                weMoments.add(weMoment);

//               //经纬度
//               Optional.ofNullable(moment.getLocation()).ifPresent(k->{
//
//               });


            });


            this.saveOrUpdateBatch(weMoments);
            if(CollectionUtil.isNotEmpty(interactes)){
                weMomentsInteracteService.batchAddOrUpdate(interactes);
            }


        }

    }


    /**
     * 设置员工发送结果
     * @param weMoments
     */
    private void getSendResult(WeMoments weMoments){

        MomentsResultDto moment_task = weMomentsClient.get_moment_task(MomentsParamDto.builder()
                .moment_id(weMoments.getMomentId())
                .build());

        if(moment_task.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
            List<MomentsResultDto.TaskList> task_list = moment_task.getTask_list();
            if(CollectionUtil.isNotEmpty(task_list)){
                task_list.stream().collect(Collectors.groupingBy(MomentsResultDto.TaskList::getPublish_status)).forEach((k,v)->{

                        if(k.equals(new Integer(0))){//未发表
                          weMoments.setNoAddUser(v.stream().map(MomentsResultDto.TaskList::getUserid).collect(Collectors.joining(",")));
                        }else if(k.equals(new Integer(1))){//已发表
                            weMoments.setAddUser(
                                    v.stream().map(MomentsResultDto.TaskList::getUserid).collect(Collectors.joining(","))
                            );
                        }

                });
            }
        }



    }



    /**
     * 批量获取企业朋友圈
     *
     * @param nextCursor
     * @param list
     */
    private void getByMoment(String nextCursor,List<MomentsListDetailResultDto.Moment> list,Integer filterType) {


        Long startTime = strToDate(-29, 0);

        Long endTime = strToDate(0, 1);
        MomentsListDetailResultDto moment_list = weMomentsClient.get_moment_list(MomentsListDetailParamDto.builder()
                .start_time(startTime)
                .end_time(endTime)
                        .filter_type(filterType)
                .build());
        if (WeConstans.WE_SUCCESS_CODE.equals(moment_list.getErrcode())
                || WeConstans.NOT_EXIST_CONTACT.equals(moment_list.getErrcode())
                && CollectionUtil.isNotEmpty(moment_list.getMoment_list())) {
            list.addAll(moment_list.getMoment_list());
            if (StringUtils.isNotEmpty(moment_list.getNext_cursor())) {
                getByMoment(moment_list.getNext_cursor(), list,filterType);
            }
        }
    }


    private Long strToDate(int days, Integer type) {
        Long time = null;
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.DATE, days);
        String tarday = new SimpleDateFormat("yyyy-MM-dd").format(cale.getTime());
        if (type.equals(0)) {
            tarday += " 00:00:00";
        } else {
            tarday += " 23:59:59";
        }
        // String转Date
        try {
            date = format2.parse(tarday);
            System.out.println(date.getTime());
            time = date.getTime() / 1000;
            System.out.println(time.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }



}
