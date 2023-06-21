package com.linkwechat.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.moments.dto.MomentsParamDto;
import com.linkwechat.domain.moments.dto.MomentsResultDto;
import com.linkwechat.domain.moments.dto.MomentsResultDto.TaskList;
import com.linkwechat.domain.moments.entity.WeMomentsAttachments;
import com.linkwechat.domain.moments.entity.WeMomentsCustomer;
import com.linkwechat.domain.moments.entity.WeMomentsUser;
import com.linkwechat.domain.moments.query.WeMomentsTaskMobileRequest;
import com.linkwechat.domain.moments.vo.WeMomentsTaskMobileVO;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwMomentsClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeMomentsUserMapper;
import com.linkwechat.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 朋友圈员工 服务实现类
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/07 10:10
 */
@Service
public class WeMomentsUserServiceImpl extends ServiceImpl<WeMomentsUserMapper, WeMomentsUser> implements IWeMomentsUserService {

    @Resource
    private QwSysUserClient qwSysUserClient;
    @Resource
    private QwMomentsClient qwMomentsClient;
    @Resource
    private IWeMomentsAttachmentsService weMomentsAttachmentsService;
    @Resource
    private IWeMaterialService weMaterialService;
    @Resource
    private IWeTagService weTagService;
    @Resource
    private IWeMomentsCustomerService weMomentsCustomerService;
    @Resource
    private IWeFlowerCustomerTagRelService weFlowerCustomerTagRelService;
    @Resource
    private IWeCustomerService weCustomerService;

    @Override
    public void addMomentsUser(Long momentsTaskId, List<SysUser> users) {
        List<WeMomentsUser> weMomentsUsers = new ArrayList<>();
        users.forEach(user -> weMomentsUsers.add(build(momentsTaskId, null, user, 0)));
        this.saveBatch(weMomentsUsers);
    }

    @Override
    public List<SysUser> getMomentsTaskExecuteUser(Integer scopeType, List<Long> deptIds, List<String> postIds, List<String> weUserIds) {
        List<SysUser> sysUsers = new ArrayList<>();
        if (scopeType.equals(0)) {
            //全部客户
            AjaxResult<List<SysUser>> result = qwSysUserClient.listAll();
            if (result.getCode() != HttpStatus.SUCCESS) {
                throw new ServiceException("获取员工数据异常！");
            }
            sysUsers = result.getData();
        } else if (scopeType.equals(1)) {
            //按条件筛选
            if (CollectionUtil.isNotEmpty(deptIds)) {
                AjaxResult<List<SysUser>> result = qwSysUserClient.findAllSysUser(null, null, StrUtil.join(",", deptIds));
                if (result.getCode() != HttpStatus.SUCCESS) {
                    throw new ServiceException("获取员工数据异常！");
                }
                sysUsers.addAll(result.getData());
            }
            if (CollectionUtil.isNotEmpty(postIds)) {
                AjaxResult<List<SysUser>> result = qwSysUserClient.findAllSysUser(null, StrUtil.join(",", postIds), null);
                if (result.getCode() != HttpStatus.SUCCESS) {
                    throw new ServiceException("获取员工数据异常！");
                }
                sysUsers.addAll(result.getData());
            }
            if (CollectionUtil.isNotEmpty(weUserIds)) {
                AjaxResult<List<SysUser>> result = qwSysUserClient.findAllSysUser(StrUtil.join(",", weUserIds), null, null);
                if (result.getCode() != HttpStatus.SUCCESS) {
                    throw new ServiceException("获取员工数据异常！");
                }
                sysUsers.addAll(result.getData());
            }
            sysUsers = sysUsers.stream().distinct().collect(Collectors.toList());
        }
        return sysUsers;
    }

    @Override
    public void syncAddMomentsUser(Long momentsTaskId, String momentsId) {
        MomentsResultDto momentTask = qwMomentsClient.get_moment_task(MomentsParamDto.builder().moment_id(momentsId).build()).getData();
        if (BeanUtil.isNotEmpty(momentTask)) {
            List<TaskList> task_list = momentTask.getTask_list();
            if (CollectionUtil.isNotEmpty(task_list)) {
                //根据企微员工Id获取员工的详细信息
                List<String> weUserIds = task_list.stream().map(TaskList::getUserid).collect(Collectors.toList());
                AjaxResult<List<SysUserVo>> result = qwSysUserClient.getUserListByWeUserIds(SysUserQuery.builder().weUserIds(weUserIds).build());
                Map<String, SysUserVo> userIdMap = new HashMap<>();
                if (result.getCode() == HttpStatus.SUCCESS) {
                    if (CollectionUtil.isNotEmpty(result.getData())) {
                        userIdMap = result.getData().stream().collect(Collectors.toMap(SysUserVo::getWeUserId, Function.identity(), (key1, key2) -> key2));
                    }
                }
                //添加朋友圈执行员工
                List<WeMomentsUser> weMomentsUsers = new ArrayList<>();
                for (TaskList taskList : task_list) {
                    if (BeanUtil.isNotEmpty(userIdMap.get(taskList.getUserid()))) {
                        SysUserVo sysUserVo = userIdMap.get(taskList.getUserid());
                        weMomentsUsers.add(build(momentsTaskId, momentsId, BeanUtil.copyProperties(sysUserVo, SysUser.class), taskList.getPublish_status()));
                    }
                }
                this.saveBatch(weMomentsUsers);
            }
        }
    }

    @Override
    public void syncUpdateMomentsUser(Long momentsTaskId, String momentsId) {
        List<TaskList> taskLists = iterateGetMomentTask(momentsId, null);
        if (BeanUtil.isNotEmpty(taskLists)) {
            //获取已发送的企微员工Id集合
            List<String> collect = taskLists.stream().filter(i -> i.getPublish_status().equals(1)).map(TaskList::getUserid).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collect)) {
                LambdaUpdateWrapper<WeMomentsUser> updateWrapper = Wrappers.lambdaUpdate(WeMomentsUser.class);
                updateWrapper.eq(WeMomentsUser::getMomentsTaskId, momentsTaskId);
                updateWrapper.eq(WeMomentsUser::getMomentsId, momentsId);
                updateWrapper.in(WeMomentsUser::getWeUserId, collect);
                updateWrapper.set(WeMomentsUser::getExecuteStatus, 1);
                this.update(updateWrapper);
            }
        }
    }

    @Override
    public List<WeMomentsTaskMobileVO> mobileList(WeMomentsTaskMobileRequest request) {
        List<WeMomentsTaskMobileVO> vos = this.baseMapper.mobileList(request);
        //客户数
        for (WeMomentsTaskMobileVO vo : vos) {
            if (vo.getStatus().equals(1)) {
                List<String> tagIds = JSONArray.parseArray(vo.getCustomerTag(), String.class);
                if (CollectionUtil.isNotEmpty(tagIds)) {
                    List<String> weUserIds = weFlowerCustomerTagRelService.getCountByTagIdAndUserId(tagIds, ListUtil.toList(request.getWeUserId()));
                    vo.setCustomerNum(weUserIds.size());
                } else {
                    LambdaQueryWrapper<WeCustomer> queryWrapper = Wrappers.lambdaQuery(WeCustomer.class);
                    queryWrapper.eq(WeCustomer::getAddUserId, request.getWeUserId());
                    queryWrapper.eq(WeCustomer::getDelFlag, Constants.COMMON_STATE);
                    queryWrapper.ne(WeCustomer::getTrackState, 5);
                    int count = weCustomerService.count(queryWrapper);
                    vo.setCustomerNum(count);
                }
            } else {
                LambdaQueryWrapper<WeMomentsCustomer> queryWrapper = Wrappers.lambdaQuery(WeMomentsCustomer.class);
                queryWrapper.eq(WeMomentsCustomer::getMomentsTaskId, vo.getWeMomentsTaskId());
                queryWrapper.eq(WeMomentsCustomer::getWeUserId, request.getWeUserId());
                queryWrapper.eq(WeMomentsCustomer::getDelFlag, Constants.COMMON_STATE);
                int count = weMomentsCustomerService.count(queryWrapper);
                vo.setCustomerNum(count);
            }
        }

        //客户标签
        List<String> tagIds = new ArrayList<>();
        for (WeMomentsTaskMobileVO i : vos) {
            if (StrUtil.isBlank(i.getCustomerTag())) {
                continue;
            }
            List<String> list = JSONArray.parseArray(i.getCustomerTag(), String.class);
            i.setCustomerTags(list);
            tagIds.addAll(list);
        }
        if (CollectionUtil.isNotEmpty(tagIds)) {
            tagIds = tagIds.stream().distinct().collect(Collectors.toList());
            LambdaQueryWrapper<WeTag> queryWrapper = Wrappers.lambdaQuery(WeTag.class);
            queryWrapper.select(WeTag::getTagId, WeTag::getName);
            queryWrapper.in(WeTag::getTagId, tagIds);
            queryWrapper.eq(WeTag::getDelFlag, Constants.COMMON_STATE);
            List<WeTag> list = weTagService.list(queryWrapper);
            if (CollectionUtil.isNotEmpty(list)) {
                Map<String, String> map = list.stream().collect(Collectors.toMap(WeTag::getTagId, WeTag::getName));
                for (WeMomentsTaskMobileVO i : vos) {
                    if (StrUtil.isBlank(i.getCustomerTag())) {
                        continue;
                    }
                    List<String> customerTagIds = i.getCustomerTags();
                    List<String> customerTags = new ArrayList<>();
                    for (String customerTagId : customerTagIds) {
                        String tagName = map.get(customerTagId);
                        if (StrUtil.isNotBlank(tagName)) {
                            customerTags.add(tagName);
                        }
                    }
                    i.setCustomerTags(customerTags);
                }
            }
        }
        return vos;
    }

    @Override
    public WeMomentsTaskMobileVO mobileGet(Long weMomentsTaskId) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (BeanUtil.isEmpty(loginUser)) {
            return null;
        }
        //详情
        String weUserId = loginUser.getSysUser().getWeUserId();
        WeMomentsTaskMobileVO vo = this.baseMapper.mobileGet(weUserId, weMomentsTaskId);

        //附件
        LambdaQueryWrapper<WeMomentsAttachments> wrapper = Wrappers.lambdaQuery(WeMomentsAttachments.class);
        wrapper.eq(WeMomentsAttachments::getMomentsTaskId, weMomentsTaskId);
        List<WeMomentsAttachments> attachmentsList = weMomentsAttachmentsService.list(wrapper);
        if (CollectionUtil.isNotEmpty(attachmentsList)) {
            List<Long> materialIds = attachmentsList.stream().map(WeMomentsAttachments::getMaterialId).collect(Collectors.toList());
            List<WeMaterial> weMaterials = weMaterialService.listByIds(materialIds);
            vo.setMaterialList(weMaterials);
        }
        //客户标签
        if (StrUtil.isNotBlank(vo.getCustomerTag())) {
            List<String> tagIds = JSONArray.parseArray(vo.getCustomerTag(), String.class);
            LambdaQueryWrapper<WeTag> queryWrapper = Wrappers.lambdaQuery(WeTag.class);
            queryWrapper.select(WeTag::getName);
            queryWrapper.in(WeTag::getTagId, tagIds);
            queryWrapper.eq(WeTag::getDelFlag, Constants.COMMON_STATE);
            List<WeTag> list = weTagService.list(queryWrapper);
            if (CollectionUtil.isNotEmpty(list)) {
                List<String> tags = list.stream().map(WeTag::getName).collect(Collectors.toList());
                vo.setCustomerTags(tags);
            }
        }

        //客户数
        //任务状态：1未开始，2进行中，3已结束
        if (vo.getStatus().equals(1)) {
            List<String> tagIds = JSONArray.parseArray(vo.getCustomerTag(), String.class);
            if (CollectionUtil.isNotEmpty(tagIds)) {
                List<String> weUserIds = weFlowerCustomerTagRelService.getCountByTagIdAndUserId(tagIds, ListUtil.toList(weUserId));
                vo.setCustomerNum(weUserIds.size());
            } else {
                LambdaQueryWrapper<WeCustomer> queryWrapper = Wrappers.lambdaQuery(WeCustomer.class);
                queryWrapper.eq(WeCustomer::getAddUserId, weUserId);
                queryWrapper.eq(WeCustomer::getDelFlag, Constants.COMMON_STATE);
                queryWrapper.ne(WeCustomer::getTrackState, 5);
                int count = weCustomerService.count(queryWrapper);
                vo.setCustomerNum(count);
            }
        } else {
            LambdaQueryWrapper<WeMomentsCustomer> queryWrapper = Wrappers.lambdaQuery(WeMomentsCustomer.class);
            queryWrapper.eq(WeMomentsCustomer::getMomentsTaskId, weMomentsTaskId);
            queryWrapper.eq(WeMomentsCustomer::getWeUserId, weUserId);
            queryWrapper.eq(WeMomentsCustomer::getDelFlag, Constants.COMMON_STATE);
            int count = weMomentsCustomerService.count(queryWrapper);
            vo.setCustomerNum(count);
        }
        return vo;
    }

    /**
     * 迭代获取员工的执行情况
     *
     * @param momentsId 朋友圈Id
     * @param cursor    游标
     * @return {@link List<TaskList>}
     * @author WangYX
     * @date 2023/06/12 17:11
     */
    public List<TaskList> iterateGetMomentTask(String momentsId, String cursor) {
        MomentsResultDto momentTask = qwMomentsClient.get_moment_task(MomentsParamDto.builder().moment_id(momentsId).cursor(cursor).build()).getData();
        String nextCursor = momentTask.getNextCursor();
        if (StrUtil.isNotEmpty(nextCursor)) {
            momentTask.getTask_list().addAll(iterateGetMomentTask(momentsId, nextCursor));
        }
        return momentTask.getTask_list();
    }

    /**
     * 构建朋友圈执行员工
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param momentsId     朋友圈Id
     * @param user          执行用户
     * @param status        执行状态:0未执行，1已执行
     * @return {@link WeMomentsUser}
     * @author WangYX
     * @date 2023/06/08 17:02
     */
    private WeMomentsUser build(Long momentsTaskId, String momentsId, SysUser user, Integer status) {
        WeMomentsUser weMomentsUser = new WeMomentsUser();
        weMomentsUser.setId(IdUtil.getSnowflake().nextId());
        weMomentsUser.setMomentsTaskId(momentsTaskId);
        weMomentsUser.setMomentsId(momentsId);
        weMomentsUser.setUserId(user.getUserId());
        weMomentsUser.setWeUserId(user.getWeUserId());
        weMomentsUser.setUserName(user.getUserName());
        weMomentsUser.setDeptId(user.getDeptId());
        weMomentsUser.setDeptName(user.getDeptName());
        weMomentsUser.setExecuteStatus(status);
        weMomentsUser.setExecuteCount(0);
        weMomentsUser.setDelFlag(Constants.COMMON_STATE);
        if (BeanUtil.isNotEmpty(SecurityUtils.getLoginUser())) {
            weMomentsUser.setCreateById(SecurityUtils.getLoginUser().getSysUser().getUserId());
            weMomentsUser.setCreateBy(SecurityUtils.getLoginUser().getSysUser().getUserName());
        }
        weMomentsUser.setCreateTime(new Date());
        return weMomentsUser;
    }
}
