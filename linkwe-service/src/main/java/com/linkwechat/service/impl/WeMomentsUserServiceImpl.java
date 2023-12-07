package com.linkwechat.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.moments.dto.MomentsListDetailResultDto;
import com.linkwechat.domain.moments.dto.MomentsParamDto;
import com.linkwechat.domain.moments.dto.MomentsResultDto;
import com.linkwechat.domain.moments.dto.MomentsResultDto.TaskList;
import com.linkwechat.domain.moments.entity.WeMomentsAttachments;
import com.linkwechat.domain.moments.entity.WeMomentsEstimateCustomer;
import com.linkwechat.domain.moments.entity.WeMomentsTask;
import com.linkwechat.domain.moments.entity.WeMomentsUser;
import com.linkwechat.domain.moments.query.WeMomentsTaskEstimateCustomerNumRequest;
import com.linkwechat.domain.moments.query.WeMomentsTaskMobileRequest;
import com.linkwechat.domain.moments.vo.WeMomentsTaskMobileVO;
import com.linkwechat.domain.moments.vo.WeMomentsTaskVO;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwMomentsClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeMomentsEstimateCustomerMapper;
import com.linkwechat.mapper.WeMomentsTaskMapper;
import com.linkwechat.mapper.WeMomentsUserMapper;
import com.linkwechat.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private WeMomentsEstimateCustomerMapper weMomentsEstimateCustomerMapper;
//    @Resource
//    private WeMomentsTaskMapper weMomentsTaskMapper;

    @Autowired
    @Lazy
    private IWeMomentsTaskService iWeMomentsTaskService;

    @Resource
    private IWeMomentsCustomerService weMomentsCustomerService;

    @Override
    public void addMomentsUser(Long momentsTaskId, List<SysUser> users) {
        List<WeMomentsUser> weMomentsUsers = new ArrayList<>();
        users.forEach(user -> weMomentsUsers.add(build(momentsTaskId, null, user, 0)));
        if(CollectionUtil.isNotEmpty(weMomentsUsers)){
            List<List<WeMomentsUser>> partitions = Lists.partition(weMomentsUsers, 1000);
            for(List<WeMomentsUser> partition:partitions){
                this.baseMapper.insertBatchSomeColumn(partition);
            }

        }

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
    public void syncAddMomentsUser(Long momentsTaskId, MomentsListDetailResultDto.Moment moment, SysUser sysUser) {
        //朋友圈创建来源。0：企业 1：个人
        if (moment.getCreate_type().equals(0)) {
            this.syncAddMomentsUser(momentsTaskId, moment.getMoment_id());
        } else if (moment.getCreate_type().equals(1)) {
            WeMomentsUser build = build(momentsTaskId, moment.getMoment_id(), sysUser, 1);
            this.save(build);
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
                updateWrapper.in(WeMomentsUser::getWeUserId, collect);
                updateWrapper.set(WeMomentsUser::getExecuteStatus, 1);
                this.update(updateWrapper);
            }
        }
    }

    @Override
    public List<WeMomentsTaskMobileVO> mobileList(WeMomentsTaskMobileRequest request) {

        List<WeMomentsTaskMobileVO> vos = this.baseMapper.mobileList(request);

        //客户标签
        List<String> tagIds = new ArrayList<>();

        for (WeMomentsTaskMobileVO vo : vos) {
            //客户数
            LambdaQueryWrapper<WeMomentsEstimateCustomer> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(WeMomentsEstimateCustomer::getMomentsTaskId, vo.getWeMomentsTaskId());
            int count = weMomentsEstimateCustomerMapper.selectCount(queryWrapper);
            vo.setCustomerNum(count);

            //附件
            LambdaQueryWrapper<WeMomentsAttachments> attachmentsWrapper = Wrappers.lambdaQuery();
            attachmentsWrapper.eq(WeMomentsAttachments::getMomentsTaskId, vo.getWeMomentsTaskId());
            List<WeMomentsAttachments> list = weMomentsAttachmentsService.list(attachmentsWrapper);
            if (CollectionUtil.isNotEmpty(list)) {
                List<Long> materialIds = list.stream().map(WeMomentsAttachments::getMaterialId).collect(Collectors.toList());
                LambdaQueryWrapper<WeMaterial> wrapper = Wrappers.lambdaQuery();
                wrapper.select(WeMaterial::getId,
                        WeMaterial::getMaterialName,
                        WeMaterial::getMaterialUrl,
                        WeMaterial::getMediaType,
                        WeMaterial::getDigest,
                        WeMaterial::getCoverUrl,
                        WeMaterial::getWidth,
                        WeMaterial::getHeight,
                        WeMaterial::getPixelSize,
                        WeMaterial::getMemorySize
                );
                wrapper.in(WeMaterial::getId, materialIds);
                List<WeMaterial> weMaterials = weMaterialService.list(wrapper);
                vo.setMaterialList(weMaterials);
            }

            //客户标签
            if (StrUtil.isNotBlank(vo.getCustomerTag())) {
                List<String> tagIdLists = JSONArray.parseArray(vo.getCustomerTag(), String.class);
                vo.setCustomerTags(tagIdLists);
                tagIds.addAll(tagIdLists);
            }
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
            throw new ServiceException("未登录！", HttpStatus.UNAUTHORIZED);
        }
        //数据不存在，直接返回
//        WeMomentsTask weMomentsTask = iWeMomentsTaskService.getById(weMomentsTaskId);
        WeMomentsTaskVO weMomentsTaskVO = iWeMomentsTaskService.get(weMomentsTaskId);

        if (BeanUtil.isEmpty(weMomentsTaskVO)) {
            return null;
        }

        //详情
        String weUserId = loginUser.getSysUser().getWeUserId();
        WeMomentsTaskMobileVO vo = this.baseMapper.mobileGet(weUserId, weMomentsTaskId);
        if (BeanUtil.isEmpty(vo)) {
            throw new ServiceException("暂无权限操作！", HttpStatus.FORBIDDEN);
        }
        //附件
        LambdaQueryWrapper<WeMomentsAttachments> wrapper = Wrappers.lambdaQuery(WeMomentsAttachments.class);
        wrapper.eq(WeMomentsAttachments::getMomentsTaskId, weMomentsTaskId);
        List<WeMomentsAttachments> attachmentsList = weMomentsAttachmentsService.list(wrapper);
        if (CollectionUtil.isNotEmpty(attachmentsList)) {
            List<Long> materialIds = new ArrayList<>();
            attachmentsList.stream().forEach(i -> materialIds.add(i.getMaterialId()));
            List<WeMaterial> weMaterials = weMaterialService.listByIds(materialIds);
            //防止重复的素材被过滤掉
            if (BeanUtil.isNotEmpty(weMaterials)) {
                Map<Long, WeMaterial> collect = weMaterials.stream().collect(Collectors.toMap(WeMaterial::getId, Function.identity()));
                List<WeMaterial> materialList = new ArrayList<>();
                materialIds.forEach(i -> {
                    WeMaterial weMaterial = collect.get(i);
                    if (BeanUtil.isNotEmpty(weMaterial)) {
                        materialList.add(weMaterial);
                    }
                });
                vo.setMaterialList(materialList);
            }
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

        WeCustomersQuery weCustomersQuery = weMomentsTaskVO.getWeCustomersQuery();
        if(weCustomersQuery !=null){
            weCustomersQuery.setNoRepeat(true);
            vo.setCustomerNum(
                    weMomentsCustomerService.estimateCustomerNum(
                            WeMomentsTaskEstimateCustomerNumRequest.builder().scopeType( weMomentsTaskVO.getScopeType())
                                    .weCustomersQuery(weCustomersQuery).build()
                    )
            );

        }
        return vo;
    }

    @Override
    public int count(WeMomentsTaskMobileRequest request) {
        return this.baseMapper.count(request);
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
