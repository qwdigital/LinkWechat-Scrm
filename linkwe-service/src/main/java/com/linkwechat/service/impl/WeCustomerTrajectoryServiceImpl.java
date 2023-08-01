package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.enums.TrackState;
import com.linkwechat.common.enums.TrajectorySceneType;
import com.linkwechat.common.enums.TrajectoryType;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.*;
import com.linkwechat.mapper.WeCustomerTrajectoryMapper;
import com.linkwechat.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@SuppressWarnings("all")
public class WeCustomerTrajectoryServiceImpl extends ServiceImpl<WeCustomerTrajectoryMapper, WeCustomerTrajectory> implements IWeCustomerTrajectoryService {


    @Autowired
    @Lazy
    private IWeCustomerService iWeCustomerService;

    @Autowired
    private IWeCustomerTrackRecordService iWeCustomerTrackRecordService;

    @Autowired
    @Lazy
    private IWeGroupService iWeGroupService;

    @Autowired
    private IWeStrackStageService iWeStrackStageService;



    @Override
    public void createInteractionTrajectory(String externalUserid, String weUserId, Integer trajectorySceneType, String otherInfo) {
        String userName = iWeCustomerService.findUserNameByUserId(weUserId);
        WeCustomer weCustomer = iWeCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getAddUserId, weUserId)
                .eq(WeCustomer::getExternalUserid, externalUserid));

        String action="";
        String content="";
        if(trajectorySceneType.equals(TrajectorySceneType.TRAJECTORY_TITLE_DZPYQ.getType())){
            action="点赞了员工朋友圈";
            content=String.format(TrajectorySceneType.TRAJECTORY_TITLE_DZPYQ.getMsgTpl(),weCustomer!=null?weCustomer.getCustomerName():"@客户"
                    ,StringUtils.isNotEmpty(userName)?userName:"@员工");

        }else if(trajectorySceneType.equals(TrajectorySceneType.TRAJECTORY_TITLE_PLPYQ.getType())){
            action="评论了员工朋友圈";
            content=String.format(TrajectorySceneType.TRAJECTORY_TITLE_PLPYQ.getMsgTpl(),weCustomer!=null?weCustomer.getCustomerName():"@客户"
                    ,StringUtils.isNotEmpty(userName)?userName:"@员工");

        }else if(trajectorySceneType.equals(TrajectorySceneType.TRAJECTORY_TITLE_LHB.getName())){
            action="领取了员工红包";
            content=String.format(TrajectorySceneType.TRAJECTORY_TITLE_LHB.getMsgTpl(),weCustomer!=null?weCustomer.getCustomerName():"@客户"
                    ,StringUtils.isNotEmpty(userName)?userName:"@员工",otherInfo);

        }else if(trajectorySceneType.equals(TrajectorySceneType.TRAJECTORY_TITLE_WXKF.getName())){
            action="咨询了客服";

            content=String.format(TrajectorySceneType.TRAJECTORY_TITLE_WXKF.getMsgTpl(),weCustomer!=null?weCustomer.getCustomerName():"@客户",otherInfo
                    ,StringUtils.isNotEmpty(userName)?userName:"@员工");

        }else if(trajectorySceneType.equals(TrajectorySceneType.TRAJECTORY_TITLE_ZLBD.getName())){
            action="填写了员工的表单";
            content=String.format(TrajectorySceneType.TRAJECTORY_TITLE_ZLBD.getMsgTpl(),weCustomer!=null?weCustomer.getCustomerName():"@客户"
                    ,StringUtils.isNotEmpty(userName)?userName:"@员工",otherInfo);
        }

        this.save(
                WeCustomerTrajectory.builder()
                        .trajectorySceneType(trajectorySceneType)
                        .trajectoryType(TrajectoryType.TRAJECTORY_TYPE_HDGZ.getType())
                        .operatorType(1)
                        .externalUseridOrChatid(externalUserid)
                        .weUserId(weUserId)
                        .operatorId(externalUserid)
                        .operatorName(weCustomer==null?"@客户":weCustomer.getCustomerName())
                        .operatoredObjectType(2)
                        .operatoredObjectName(StringUtils.isNotEmpty(userName) ?userName:"@员工")
                        .operatoredObjectId(weUserId)
                        .action(action)
                        .title(TrajectoryType.TRAJECTORY_TYPE_SJDT.getName())
                        .content(content)
                        .build()
        );

    }

    @Override
    public void createEditTrajectory(String externalUserid, String weUserId, Integer trajectorySceneType,String editInfo) {
        String userName = iWeCustomerService.findUserNameByUserId(weUserId);
        WeCustomer weCustomer = iWeCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getAddUserId, weUserId)
                .eq(WeCustomer::getExternalUserid, externalUserid));

          String action="";
          String content="";
          if(trajectorySceneType.equals(TrajectorySceneType.TRAJECTORY_TITLE_GXQYBQ.getType())){ //更新客户企业标签
              action="更新客户企业标签";
              content=String.format(TrajectorySceneType.TRAJECTORY_TITLE_GXQYBQ.getMsgTpl()
                      ,StringUtils.isNotEmpty(userName)?userName:"@员工",weCustomer!=null?weCustomer.getCustomerName():"@客户",editInfo);
          }else if(trajectorySceneType.equals(TrajectorySceneType.TRAJECTORY_TITLE_GXGRBQ.getType())){ //更新客户个人标签
              action="更新客户个人标签";
              content=String.format(TrajectorySceneType.TRAJECTORY_TITLE_GXGRBQ.getMsgTpl()
                      ,StringUtils.isNotEmpty(userName)?userName:"@员工",weCustomer!=null?weCustomer.getCustomerName():"@客户",editInfo);
          }else if(trajectorySceneType.equals(TrajectorySceneType.TRAJECTORY_TITLE_BJBQ.getType())){ //编辑资料
              action="更新客户详细资料";
              content=String.format(TrajectorySceneType.TRAJECTORY_TITLE_BJBQ.getMsgTpl()
                      ,StringUtils.isNotEmpty(userName)?userName:"@员工",weCustomer!=null?weCustomer.getCustomerName():"@客户");
          }else if(trajectorySceneType.equals(TrajectorySceneType.TRAJECTORY_TITLE_QXKHQYBQ.getType())){ //取消企业标签
              action="取消企业标签";
              content=String.format(TrajectorySceneType.TRAJECTORY_TITLE_QXKHQYBQ.getMsgTpl()
                      ,StringUtils.isNotEmpty(userName)?userName:"@员工",weCustomer!=null?weCustomer.getCustomerName():"@客户");

          }else if(trajectorySceneType.equals(TrajectorySceneType.TRAJECTORY_TITLE_QXKHGRBQ.getType())){ //取消个人标签
              action="取消个人标签";
              content=String.format(TrajectorySceneType.TRAJECTORY_TITLE_QXKHGRBQ.getMsgTpl()
                      ,StringUtils.isNotEmpty(userName)?userName:"@员工",weCustomer!=null?weCustomer.getCustomerName():"@客户");

          }
            this.save(
                    WeCustomerTrajectory.builder()
                            .trajectorySceneType(trajectorySceneType)
                            .trajectoryType(TrajectoryType.TRAJECTORY_TYPE_SJDT.getType())
                            .operatorType(2)
                            .externalUseridOrChatid(externalUserid)
                            .weUserId(weUserId)
                            .operatorId(weUserId)
                            .operatorName(StringUtils.isNotEmpty(userName) ?userName:"@员工")
                            .operatoredObjectType(1)
                            .operatoredObjectName(weCustomer==null?"@客户":weCustomer.getCustomerName())
                            .operatoredObjectId(externalUserid)
                            .action(action)
                            .title(TrajectoryType.TRAJECTORY_TYPE_SJDT.getName())
                            .content(
                                 content
                            )
                            .build()
            );

    }

    @Override
    @Transactional
    public void createTrackTrajectory(String externalUserid, String weUserId, Integer trackState,String trackContent) {
        String userName = iWeCustomerService.findUserNameByUserId(weUserId);
        WeCustomer weCustomer = iWeCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getAddUserId, weUserId)
                .eq(WeCustomer::getExternalUserid, externalUserid));


        String trackStateName=null;

        TrackState statetate = TrackState.of(trackState);

        if(null == statetate){
            List<WeStrackStage> weStrackStages = iWeStrackStageService.list(new LambdaQueryWrapper<WeStrackStage>()
                    .eq(WeStrackStage::getStageVal, trackState));
            if(CollectionUtil.isNotEmpty(weStrackStages)){
                trackStateName=weStrackStages.stream().findFirst().get().getStageKey();
            }
        }else{
            trackStateName=TrackState.of(trackState).getName();
        }

       if(this.save(
               WeCustomerTrajectory.builder()
                       .trajectorySceneType(TrajectorySceneType.TRAJECTORY_TITLE_TJGJ.getType())
                       .trajectoryType(TrajectoryType.TRAJECTORY_TYPE_DBDT.getType())
                       .operatorType(2)
                       .externalUseridOrChatid(externalUserid)
                       .weUserId(weUserId)
                       .operatorId(weUserId)
                       .operatorName(StringUtils.isNotEmpty(userName)?userName:"@员工")
                       .operatoredObjectType(1)
                       .operatoredObjectName(weCustomer==null?"@客户":weCustomer.getCustomerName())
                       .operatoredObjectId(externalUserid)
                       .action("跟进客户")
                       .title(TrajectoryType.TRAJECTORY_TYPE_DBDT.getName())
                       .content(
                               String.format(TrajectorySceneType.TRAJECTORY_TITLE_TJGJ.getMsgTpl()
                                       ,StringUtils.isNotEmpty(userName)?userName:"@员工",weCustomer!=null?weCustomer.getCustomerName():"@客户",
                                       trackStateName)
                       )
                       .build()
       )){
           //跟进记录表入库
           iWeCustomerTrackRecordService.save(
                   WeCustomerTrackRecord.builder()
                           .trackTime(new Date())
                           .trackContent(trackContent)
                           .trackTitle("更新跟进动态")
                           .trackState(trackState)
                           .externalUserid(externalUserid)
                           .weUserId(weUserId)
                           .build()

           );
       }







    }


    @Override
    public void createAddOrRemoveTrajectory(String externalUserid, String weUserId, boolean addOrRemove,boolean removeTarget) {
        SysUser sysUser = iWeCustomerService.findSysUserInfoByWeUserId(weUserId);


        WeCustomer weCustomer = iWeCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getAddUserId, weUserId)
                .eq(WeCustomer::getExternalUserid, externalUserid));

        if(sysUser != null && weCustomer != null){

            WeCustomerTrajectory weCustomerTrajectory = WeCustomerTrajectory.builder()
                    .trajectorySceneType(addOrRemove ? TrajectorySceneType.TRAJECTORY_TITLE_TJYG.getType() :
                            removeTarget?
                                    TrajectorySceneType.TRAJECTORY_TITLE_SCYG.getType():
                                    TrajectorySceneType.TRAJECTORY_TITLE_SCKH.getType())
                    .trajectoryType(TrajectoryType.TRAJECTORY_TYPE_XXDT.getType())
                    .operatorType(1)
                    .externalUseridOrChatid(externalUserid)
                    .weUserId(weUserId)
                    .operatorId(externalUserid)
                    .operatorName(weCustomer == null ? "@客户" : weCustomer.getCustomerName())
                    .operatoredObjectType(2)
                    .operatoredObjectName(sysUser == null?"@员工":sysUser.getUserName())
                    .operatoredObjectId(weUserId)
                    .action(addOrRemove ? "添加员工" : removeTarget?"删除员工":"删除客户")
                    .title(TrajectoryType.TRAJECTORY_TYPE_XXDT.getName())
                    .content(
                            String.format(addOrRemove ? TrajectorySceneType.TRAJECTORY_TITLE_TJYG.getMsgTpl() :
                                            removeTarget?
                                            TrajectorySceneType.TRAJECTORY_TITLE_SCYG.getMsgTpl():
                                            TrajectorySceneType.TRAJECTORY_TITLE_SCKH.getMsgTpl()
                                    ,removeTarget?weCustomer == null ? "@客户" : weCustomer.getCustomerName():sysUser == null?"@员工":sysUser.getUserName()
                                    , removeTarget?sysUser == null?"@员工":sysUser.getUserName():weCustomer == null ? "@客户" : weCustomer.getCustomerName())
                    )
                    .build();

            weCustomerTrajectory.setCreateBy(sysUser == null?"@员工":sysUser.getUserName());
            weCustomerTrajectory.setCreateById(sysUser == null?new Long(0):sysUser.getUserId());
            weCustomerTrajectory.setUpdateBy(sysUser == null?"@员工":sysUser.getUserName());
            weCustomerTrajectory.setUpdateById(sysUser == null?new Long(0):sysUser.getUserId());

            this.save(
                    weCustomerTrajectory
            );


        }


    }


    @Override
    public void createJoinOrExitGroupTrajectory(List<WeGroupMember> members,String groupChatName,boolean joinOrQuit) {
        List<WeCustomerTrajectory>  trajectories=new ArrayList<>();
        members.stream().forEach(member->{
            WeGroup weGroup = iWeGroupService.getOne(new LambdaQueryWrapper<WeGroup>()
                    .eq(WeGroup::getChatId, member.getChatId()));
            if(null != weGroup){
                SysUser sysUser = iWeCustomerService.findSysUserInfoByWeUserId(weGroup.getOwner());
                if(null != sysUser){

                    WeCustomerTrajectory trajectory = WeCustomerTrajectory.builder()
                            .trajectorySceneType(joinOrQuit ? TrajectorySceneType.TRAJECTORY_TITLE_JRQL.getType() :
                                    member.getQuitScene() !=null || member.getQuitScene()==0?TrajectorySceneType.TRAJECTORY_TITLE_TCQL.getType():
                                            TrajectorySceneType.TRAJECTORY_TITLE_YCQL.getType())
                            .trajectoryType(TrajectoryType.TRAJECTORY_TYPE_KQDT.getType())
                            .operatorType(1)
                            .externalUseridOrChatid(member.getChatId())
                            .weUserId(member.getUserId())
                            .operatorId(member.getUserId())
                            .operatorName(member.getName())
                            .operatoredObjectType(3)
                            .operatoredObjectName(StringUtils.isNotEmpty(groupChatName)?groupChatName:"@客群")
                            .operatoredObjectId(member.getChatId())
                            .action(joinOrQuit ? "进入群聊" : member.getQuitScene()==0?"退出群聊":"移除群聊")
                            .title(TrajectoryType.TRAJECTORY_TYPE_KQDT.getName())
                            .content(
                                    String.format(joinOrQuit ? TrajectorySceneType.TRAJECTORY_TITLE_JRQL.getMsgTpl() :
                                                    member.getQuitScene()==0?TrajectorySceneType.TRAJECTORY_TITLE_TCQL.getMsgTpl():
                                                            TrajectorySceneType.TRAJECTORY_TITLE_YCQL.getMsgTpl()
                                            , member.getName(), StringUtils.isNotEmpty(groupChatName)?groupChatName:"@客群")
                            )
                            .build();
                    trajectory.setCreateBy(sysUser.getUserName());
                    trajectory.setCreateById(sysUser.getUserId());
                    trajectory.setUpdateBy(sysUser.getUserName());
                    trajectory.setUpdateById(sysUser.getUserId());
                    trajectories.add(
                            trajectory
                    );

                }
            }

        });
        if(CollectionUtil.isNotEmpty(trajectories)){
            this.saveBatch(trajectories);
        }

    }


    @Override
    public void createBuildOrDissGroupTrajectory(List<WeGroup> weGroups, boolean buildOrDiss) {

        List<WeCustomerTrajectory>  trajectories=new ArrayList<>();

        weGroups.stream().forEach(weGroup -> {
            SysUser sysUser
                    = iWeCustomerService.findSysUserInfoByWeUserId(weGroup.getOwner());

            if(null != sysUser){
                WeCustomerTrajectory trajectory= WeCustomerTrajectory.builder()
                        .trajectorySceneType(buildOrDiss ? TrajectorySceneType.TRAJECTORY_TITLE_YGCJQL.getType() :
                                TrajectorySceneType.TRAJECTORY_TITLE_YGJSQL.getType())
                        .externalUseridOrChatid(weGroup.getChatId())
                        .trajectoryType(TrajectoryType.TRAJECTORY_TYPE_KQDT.getType())
                        .operatorType(1)
                        .weUserId(sysUser.getWeUserId())
                        .operatorId(sysUser.getWeUserId())
                        .operatorName(sysUser.getUserName())
                        .operatoredObjectType(3)
                        .operatoredObjectName(weGroup.getGroupName())
                        .operatoredObjectId(weGroup.getChatId())
                        .action(buildOrDiss ? "创建群聊" : "解散群聊")
                        .title(TrajectoryType.TRAJECTORY_TYPE_KQDT.getName())
                        .content(
                                String.format(buildOrDiss ? TrajectorySceneType.TRAJECTORY_TITLE_YGCJQL.getMsgTpl() :
                                                TrajectorySceneType.TRAJECTORY_TITLE_YGJSQL.getMsgTpl()
                                        , sysUser != null ? sysUser.getUserName() : null, weGroup.getGroupName())
                        )
                        .build();
                trajectory.setCreateBy(sysUser.getUserName());
                trajectory.setCreateById(sysUser.getUserId());
                trajectory.setUpdateBy(sysUser.getUserName());
                trajectory.setUpdateById(sysUser.getUserId());
                trajectories.add(
                        trajectory
                );
            }


        });

        if(CollectionUtil.isNotEmpty(trajectories)){
            this.saveBatch(trajectories);
        }


    }

    @Override
    public List<WeCustomerTrajectory> findPersonTrajectory(WeCustomerTrajectory trajectory) {

        LambdaQueryWrapper<WeCustomerTrajectory> wrapper = new LambdaQueryWrapper<>();
        if(trajectory.getTrajectoryType()==null){
            wrapper.in(WeCustomerTrajectory::getTrajectoryType, ListUtil.toList(
                    TrajectoryType.TRAJECTORY_TYPE_XXDT.getType(),TrajectoryType.TRAJECTORY_TYPE_KQDT.getType()
            ));
        }else{
            wrapper.eq(WeCustomerTrajectory::getTrajectoryType,trajectory.getTrajectoryType());
        }
        wrapper.eq(WeCustomerTrajectory::getCreateById, SecurityUtils.getUserId());
        wrapper.groupBy(WeCustomerTrajectory::getContent);
        wrapper.orderByDesc(WeCustomerTrajectory::getCreateTime);

        return this.list(wrapper);
    }


    @Override
    @DataScope(type = "2", value = @DataColumn(alias = "we_customer_trajectory", name = "create_by_id", userid = "user_id"))
    public List<WeCustomerTrajectory> findAllTrajectory(WeCustomerTrajectory trajectory) {

        LambdaQueryWrapper<WeCustomerTrajectory> wrapper = new LambdaQueryWrapper<>();
        if(trajectory.getTrajectoryType()==null){
            wrapper.in(WeCustomerTrajectory::getTrajectoryType, ListUtil.toList(
                    TrajectoryType.TRAJECTORY_TYPE_XXDT.getType(),TrajectoryType.TRAJECTORY_TYPE_KQDT.getType()
            ));
        }else{
            wrapper.eq(WeCustomerTrajectory::getTrajectoryType,trajectory.getTrajectoryType());
        }

        wrapper.groupBy(WeCustomerTrajectory::getContent);
        wrapper.orderByDesc(WeCustomerTrajectory::getCreateTime);

        if(StringUtils.isNotEmpty(trajectory.getParams().get("dataScope").toString())){
            wrapper.apply(""+trajectory.getParams().get("dataScope").toString()+"");
        }

        return this.list(wrapper);
    }


}
