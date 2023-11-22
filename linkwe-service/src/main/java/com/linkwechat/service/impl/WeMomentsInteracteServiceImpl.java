package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.TrajectorySceneType;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeFlowerCustomerTagRel;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.moments.dto.MomentsInteracteParamDto;
import com.linkwechat.domain.moments.dto.MomentsInteracteResultDto;
import com.linkwechat.domain.moments.entity.WeMomentsInteracte;
import com.linkwechat.domain.moments.entity.WeMomentsTask;
import com.linkwechat.domain.moments.entity.WeMomentsUser;
import com.linkwechat.domain.wecom.query.customer.tag.WeMarkTagQuery;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.fegin.QwMomentsClient;
import com.linkwechat.mapper.*;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWeCustomerTrajectoryService;
import com.linkwechat.service.IWeMomentsInteracteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 朋友圈互动
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/12 17:24
 */
@Service
public class WeMomentsInteracteServiceImpl extends ServiceImpl<WeMomentsInteracteMapper, WeMomentsInteracte> implements IWeMomentsInteracteService {

    @Resource
    private QwMomentsClient qwMomentsClient;
    @Resource
    private WeMomentsUserMapper weMomentsUserMapper;
    @Resource
    private WeMomentsTaskMapper weMomentsTaskMapper;
    @Resource
    private WeFlowerCustomerTagRelMapper weFlowerCustomerTagRelMapper;
    @Resource
    private WeTagMapper weTagMapper;
    @Resource
    private QwCustomerClient qwCustomerClient;
    @Resource
    private IWeCustomerService weCustomerService;
    @Resource
    private IWeCustomerTrajectoryService iWeCustomerTrajectoryService;

    @Override
    public void syncAddWeMomentsInteracte(Long momentsTaskId, String momentsId) {
        //1.获取执行员工
        LambdaQueryWrapper<WeMomentsUser> queryWrapper = Wrappers.lambdaQuery(WeMomentsUser.class);
        queryWrapper.eq(WeMomentsUser::getMomentsTaskId, momentsTaskId);
//        queryWrapper.eq(WeMomentsUser::getMomentsId, momentsId);
        queryWrapper.eq(WeMomentsUser::getDelFlag, Constants.COMMON_STATE);
        List<WeMomentsUser> list = weMomentsUserMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {

            //数据库旧的已存在的朋友圈互动数据
            LambdaQueryWrapper<WeMomentsInteracte> wrapper = Wrappers.lambdaQuery(WeMomentsInteracte.class);
            wrapper.eq(WeMomentsInteracte::getMomentsTaskId, momentsTaskId);
//            wrapper.eq(WeMomentsInteracte::getMomentId, momentsId);
            wrapper.eq(WeMomentsInteracte::getDelFlag, Constants.COMMON_STATE);
            List<WeMomentsInteracte> oldData = this.list(wrapper);

            //2.删除同步之前的互动数据
            removeInteracte(momentsTaskId, momentsId);
            //3.同步互动数据
            for (WeMomentsUser weMomentsUser : list) {
                MomentsInteracteParamDto dto = new MomentsInteracteParamDto();
                dto.setMoment_id(momentsId);
                dto.setUserid(weMomentsUser.getWeUserId());
                AjaxResult<MomentsInteracteResultDto> comments = qwMomentsClient.comments(dto);
                if (comments.getCode() == HttpStatus.SUCCESS) {
                    MomentsInteracteResultDto data = comments.getData();
                    List<MomentsInteracteResultDto.Interacte> comment_list = data.getComment_list();
                    List<WeMomentsInteracte> result = new ArrayList<>();
                    if (CollectionUtil.isNotEmpty(comment_list)) {
                        result.addAll(build(momentsTaskId, momentsId, weMomentsUser.getWeUserId(), 0, comment_list));
                    }
                    List<MomentsInteracteResultDto.Interacte> like_list = data.getLike_list();
                    if (CollectionUtil.isNotEmpty(like_list)) {
                        result.addAll(build(momentsTaskId, momentsId, weMomentsUser.getWeUserId(), 1, like_list));
                    }
                    if (CollectionUtil.isNotEmpty(result)) {
                        //保存
                        this.saveBatch(result);
                        //打标签
//                        this.interactTag(momentsTaskId, result);
                        //添加客户轨迹
                        this.addCustomerTrajectory(oldData, result);
                    }
                }
            }
        }
    }

    @Override
    public void syncUpdateWeMomentsInteract(Long momentsTaskId, String momentsId) {
        //1.获取执行员工
        LambdaQueryWrapper<WeMomentsUser> queryWrapper = Wrappers.lambdaQuery(WeMomentsUser.class);
        queryWrapper.eq(WeMomentsUser::getMomentsTaskId, momentsTaskId);
        queryWrapper.eq(WeMomentsUser::getMomentsId, momentsId);
        queryWrapper.eq(WeMomentsUser::getDelFlag, Constants.COMMON_STATE);
        List<WeMomentsUser> list = weMomentsUserMapper.selectList(queryWrapper);
        if (BeanUtil.isNotEmpty(list)) {

            //数据库旧的已存在的朋友圈互动数据
            LambdaQueryWrapper<WeMomentsInteracte> wrapper = Wrappers.lambdaQuery(WeMomentsInteracte.class);
            wrapper.eq(WeMomentsInteracte::getMomentsTaskId, momentsTaskId);
            wrapper.eq(WeMomentsInteracte::getMomentId, momentsId);
            wrapper.eq(WeMomentsInteracte::getDelFlag, Constants.COMMON_STATE);
            List<WeMomentsInteracte> oldData = this.list(wrapper);

            //2.删除同步之前的互动数据
            removeInteracte(momentsTaskId, momentsId);
            //3.同步互动数据
            for (WeMomentsUser weMomentsUser : list) {
                MomentsInteracteParamDto dto = new MomentsInteracteParamDto();
                dto.setMoment_id(momentsId);
                dto.setUserid(weMomentsUser.getWeUserId());
                AjaxResult<MomentsInteracteResultDto> comments = qwMomentsClient.comments(dto);
                if (comments.getCode() == HttpStatus.SUCCESS) {
                    MomentsInteracteResultDto data = comments.getData();
                    List<MomentsInteracteResultDto.Interacte> comment_list = data.getComment_list();
                    List<WeMomentsInteracte> result = new ArrayList<>();
                    if (CollectionUtil.isNotEmpty(comment_list)) {
                        result.addAll(build(momentsTaskId, momentsId, weMomentsUser.getWeUserId(), 0, comment_list));
                    }
                    List<MomentsInteracteResultDto.Interacte> like_list = data.getLike_list();
                    if (CollectionUtil.isNotEmpty(like_list)) {
                        result.addAll(build(momentsTaskId, momentsId, weMomentsUser.getWeUserId(), 1, like_list));
                    }
                    if (CollectionUtil.isNotEmpty(result)) {
                        //保存
                        this.saveBatch(result);
                        //打标签
                        this.interactTag(momentsTaskId, result);
                        //添加客户轨迹
                        this.addCustomerTrajectory(oldData, result);
                    }
                }
            }
        }
    }

    @Override
    public void interactTag(Long weMomentsTaskId, List<WeMomentsInteracte> list) {
        WeMomentsTask weMomentsTask = weMomentsTaskMapper.selectById(weMomentsTaskId);
        //朋友圈不存在不执行
        if (BeanUtil.isEmpty(weMomentsTask)) {
            return;
        }
        //标签不存在不执行
        if (StrUtil.isBlank(weMomentsTask.getCommentTagIds()) && StrUtil.isBlank(weMomentsTask.getLikeTagIds())) {
            return;
        }
        List<String> commentTags = null;
        if (StrUtil.isNotBlank(weMomentsTask.getCommentTagIds())) {
            commentTags = JSONObject.parseArray(weMomentsTask.getCommentTagIds(), String.class);
        }

        List<String> likeTags = null;
        if (StrUtil.isNotBlank(weMomentsTask.getLikeTagIds())) {
            likeTags = JSONObject.parseArray(weMomentsTask.getLikeTagIds(), String.class);
        }
        for (WeMomentsInteracte weMomentsInteracte : list) {
            //互动类型:0:评论；1:点赞
            if (weMomentsInteracte.getInteracteType().equals(0)) {
                if (CollectionUtil.isNotEmpty(commentTags)) {
                    List<String> alreadyTags = getAlreadyTags(weMomentsInteracte.getWeUserId(), weMomentsInteracte.getInteracteUserId());
                    List<String> addTags = commentTags.stream().filter(i -> !alreadyTags.contains(i)).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(addTags)) {
                        makeTags(addTags, weMomentsInteracte.getWeUserId(), weMomentsInteracte.getInteracteUserId());
                    }
                }
            }
            if (weMomentsInteracte.getInteracteType().equals(1)) {
                if (CollectionUtil.isNotEmpty(likeTags)) {
                    List<String> alreadyTags = getAlreadyTags(weMomentsInteracte.getWeUserId(), weMomentsInteracte.getInteracteUserId());
                    List<String> addTags = likeTags.stream().filter(i -> !alreadyTags.contains(i)).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(addTags)) {
                        makeTags(addTags, weMomentsInteracte.getWeUserId(), weMomentsInteracte.getInteracteUserId());
                    }
                }
            }
        }
    }

    /**
     * 获取客户已有标签
     *
     * @param weUserId       企微员工Id
     * @param externalUserid 客户Id
     * @return
     * @author WangYX
     * @date 2023/06/26 17:32
     */
    private List<String> getAlreadyTags(String weUserId, String externalUserid) {
        //获取客户已有标签
        LambdaQueryWrapper<WeFlowerCustomerTagRel> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WeFlowerCustomerTagRel::getUserId, weUserId);
        wrapper.eq(WeFlowerCustomerTagRel::getExternalUserid, externalUserid);
        wrapper.eq(WeFlowerCustomerTagRel::getDelFlag, Constants.COMMON_STATE);
        List<WeFlowerCustomerTagRel> tagRelList = weFlowerCustomerTagRelMapper.selectList(wrapper);
        //标签
        return tagRelList.stream().map(WeFlowerCustomerTagRel::getTagId).collect(Collectors.toList());
    }

    /**
     * 打标签
     *
     * @param addTags        标签集合
     * @param weUserId       企微员工id
     * @param externalUserid 客户id
     * @return
     * @author WangYX
     * @date 2023/06/26 18:32
     */
    private void makeTags(List<String> addTags, String weUserId, String externalUserid) {
        //1.获取标签信息
        LambdaQueryWrapper<WeTag> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WeTag::getDelFlag, Constants.COMMON_STATE);
        wrapper.in(WeTag::getTagId, addTags);
        List<WeTag> list = weTagMapper.selectList(wrapper);

        //2.添加客户标签关联
        List<WeFlowerCustomerTagRel> tagRelList = new ArrayList<>();
        list.forEach(i -> {
            WeFlowerCustomerTagRel weFlowerCustomerTagRel = WeFlowerCustomerTagRel.builder()
                    .id(SnowFlakeUtil.nextId())
                    .externalUserid(externalUserid)
                    .userId(weUserId)
                    .tagId(i.getTagId())
                    .isCompanyTag(i.getTagType().equals(1))
                    .delFlag(Constants.COMMON_STATE)
                    .build();
            weFlowerCustomerTagRel.setUpdateTime(new Date());
            weFlowerCustomerTagRel.setUpdateBy(weUserId);
            tagRelList.add(weFlowerCustomerTagRel);
        });
        weFlowerCustomerTagRelMapper.batchAddOrUpdate(tagRelList);

        //3.更新客户表冗余标签
        WeCustomer weCustomer = weCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getAddUserId, weUserId).eq(WeCustomer::getExternalUserid, externalUserid));
        if (BeanUtil.isNotEmpty(weCustomer)) {
            String tagIds = weCustomer.getTagIds();
            if (StrUtil.isBlank(tagIds)) {
                tagIds = CollectionUtil.join(addTags, ",");
            } else {
                tagIds = tagIds + "," + CollectionUtil.join(addTags, ",");
            }
            LambdaUpdateWrapper<WeCustomer> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(WeCustomer::getId, weCustomer.getId());
            updateWrapper.set(WeCustomer::getTagIds, tagIds);
            weCustomerService.update(updateWrapper);
        }

        //4.如果是企业标签，同步关联标签到企微
        List<String> enterpriseTags = list.stream().filter(i -> i.getTagType().equals(1)).map(WeTag::getTagId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(enterpriseTags)) {
            WeMarkTagQuery query = WeMarkTagQuery.builder()
                    .external_userid(externalUserid)
                    .userid(weUserId)
                    .add_tag(enterpriseTags)
                    .build();

            //同步标签到企微
            AjaxResult result = qwCustomerClient.makeCustomerLabel(query);
        }
    }

    /**
     * 构建互动数据
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param momentsId     朋友圈id
     * @param weUserId      企业成员发表UserId
     * @param type          互动类型:0:评论；1:点赞
     * @param interactes    互动数据集合
     * @return {@link WeMomentsInteracte}
     * @author WangYX
     * @date 2023/06/12 17:42
     */
    public List<WeMomentsInteracte> build(Long momentsTaskId, String momentsId, String weUserId, Integer type, List<MomentsInteracteResultDto.Interacte> interactes) {
        List<WeMomentsInteracte> result = new ArrayList<>();
        for (MomentsInteracteResultDto.Interacte interacte : interactes) {
            WeMomentsInteracte weMomentsInteracte = new WeMomentsInteracte();
            weMomentsInteracte.setId(IdUtil.getSnowflake().nextId());
            weMomentsInteracte.setMomentsTaskId(momentsTaskId);
            weMomentsInteracte.setMomentId(momentsId);
            weMomentsInteracte.setWeUserId(weUserId);
            weMomentsInteracte.setInteracteType(type);
            String userid = interacte.getUserid();
            String external_userid = interacte.getExternal_userid();
            weMomentsInteracte.setInteracteUserType(StrUtil.isNotBlank(userid) ? 0 : 1);
            weMomentsInteracte.setInteracteUserId(StrUtil.isNotBlank(userid) ? userid : external_userid);
            weMomentsInteracte.setInteracteTime(new Date(interacte.getCreate_time()));
            weMomentsInteracte.setCreateTime(new Date());
            weMomentsInteracte.setDelFlag(Constants.COMMON_STATE);
            result.add(weMomentsInteracte);
        }
        return result;
    }

    /**
     * 删除同步之前的互动数据
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param momentsId     朋友圈id
     * @return
     * @author WangYX
     * @date 2023/06/12 17:50
     */
    public void removeInteracte(Long momentsTaskId, String momentsId) {
        LambdaUpdateWrapper<WeMomentsInteracte> updateWrapper = Wrappers.lambdaUpdate(WeMomentsInteracte.class);
        updateWrapper.eq(WeMomentsInteracte::getMomentsTaskId, momentsTaskId);
        updateWrapper.eq(WeMomentsInteracte::getMomentId, momentsId);
        this.remove(updateWrapper);
    }


    /**
     * 添加互动数据
     *
     * @param oldData 数据库存在的旧互动数据
     * @param newData 从企微拉下来的所有互动数据
     * @return
     * @author WangYX
     * @date 2023/07/21 11:07
     */
    public void addCustomerTrajectory(List<WeMomentsInteracte> oldData, List<WeMomentsInteracte> newData) {
        if (CollectionUtil.isEmpty(oldData) && CollectionUtil.isNotEmpty(newData)) {
            //添加客户轨迹
            newData.forEach(i -> addCustomerTrajectory(i));
        } else if (CollectionUtil.isNotEmpty(oldData) && CollectionUtil.isNotEmpty(newData)) {
            List<WeMomentsInteracte> repeatingData = new ArrayList<>();
            for (WeMomentsInteracte oldDatum : oldData) {
                for (WeMomentsInteracte newDatum : newData) {
                    if (oldDatum.getWeUserId().equals(newDatum.getWeUserId())
                            && oldDatum.getInteracteUserId().equals(newDatum.getInteracteUserId())
                            && oldDatum.getInteracteType().equals(newDatum.getInteracteType())
                            && oldDatum.getInteracteUserType().equals(newDatum.getInteracteUserType())) {
                        repeatingData.add(newDatum);
                    }
                }
            }
            //移除重复的数据
            newData.removeAll(repeatingData);
            //添加客户轨迹
            newData.forEach(i -> addCustomerTrajectory(i));
        }
    }

    /**
     * 添加朋友圈互动护具
     *
     * @param weMomentsInteracte 互动数据
     * @return
     * @author WangYX
     * @date 2023/07/21 11:08
     */
    private void addCustomerTrajectory(WeMomentsInteracte weMomentsInteracte) {
        //互动人员为客户
        if (new Integer(1).equals(weMomentsInteracte.getInteracteUserType())) {
            iWeCustomerTrajectoryService.createInteractionTrajectory(
                    weMomentsInteracte.getInteracteUserId(),
                    weMomentsInteracte.getWeUserId(),
                    new Integer(1).equals(weMomentsInteracte.getInteracteType()) ? TrajectorySceneType.TRAJECTORY_TITLE_DZPYQ.getType() : TrajectorySceneType.TRAJECTORY_TITLE_PLPYQ.getType(),
                    null);
        }
    }
}
