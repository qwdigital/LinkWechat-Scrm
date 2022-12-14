package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeKfServicer;
import com.linkwechat.domain.kf.WeKfUser;
import com.linkwechat.domain.kf.query.WeKfServicerListQuery;
import com.linkwechat.domain.kf.vo.WeKfServicerListVo;
import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.domain.wecom.query.kf.WeKfQuery;
import com.linkwechat.domain.wecom.vo.kf.WeKfUserListVo;
import com.linkwechat.domain.wecom.vo.kf.WeKfUserVo;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.fegin.QwSysDeptClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeKfServicerMapper;
import com.linkwechat.service.IWeKfServicerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客服接待人员表(WeKfServicer)
 *
 * @author danmo
 * @since 2022-04-15 15:53:39
 */
@Slf4j
@Service
public class WeKfServicerServiceImpl extends ServiceImpl<WeKfServicerMapper, WeKfServicer> implements IWeKfServicerService {

    @Resource
    private QwKfClient qwKfClient;

    @Resource
    private QwSysUserClient qwSysUserClient;

    @Resource
    private QwSysDeptClient qwSysDeptClient;

    @Autowired
    private RedisService redisService;

    @Override
    public List<WeKfUser> getServicerByKfId(Long kfId) {
        List<WeKfUser> servicers = this.baseMapper.getServicerByKfId(kfId);
        if(CollectionUtil.isNotEmpty(servicers)){
            Set<String> userIdSet = servicers.stream().map(WeKfUser::getUserId).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
            Set<Integer> deptIdSet = servicers.stream().map(WeKfUser::getDepartmentId).filter(Objects::nonNull).collect(Collectors.toSet());
            Map<String, String> userId2NameMap = new HashMap<>();
            Map<Integer, String> deptId2NameMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(userIdSet)){
                SysUserQuery userQuery = new SysUserQuery();
                userQuery.setOpenUserIds(new ArrayList<>(userIdSet));
                try {
                    List<SysUserVo> sysUserList = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
                    if(CollectionUtil.isNotEmpty(sysUserList)){
                        Map<String, String> userMap = sysUserList.stream().collect(Collectors.toMap(SysUserVo::getOpenUserid, SysUserVo::getUserName, (key1, key2) -> key2));
                        userId2NameMap.putAll(userMap);
                    }
                } catch (Exception e) {
                    log.error("换取用户名称失败：userQuery：{}",JSONObject.toJSONString(userQuery),e);
                }
            }
            if(CollectionUtil.isNotEmpty(deptIdSet)){
                SysDeptQuery sysDeptQuery = new SysDeptQuery();
                sysDeptQuery.setDeptIds(new ArrayList<>(deptIdSet));
                try {
                    List<SysDeptVo> sysDeptList = qwSysDeptClient.getListByDeptIds(sysDeptQuery).getData();
                    if(CollectionUtil.isNotEmpty(sysDeptList)){
                        Map<Integer, String> deptMap = sysDeptList.stream().collect(Collectors.toMap(item -> item.getDeptId().intValue(),item -> item.getDeptName(),(key1, key2) -> key2));
                        deptId2NameMap.putAll(deptMap);
                    }
                } catch (Exception e) {
                    log.error("换取部门名称失败：sysDeptQuery：{}",JSONObject.toJSONString(sysDeptQuery),e);
                }
            }
            for (WeKfUser servicer : servicers) {
                if(userId2NameMap.containsKey(servicer.getUserId())){
                    servicer.setUserName(userId2NameMap.get(servicer.getUserId()));
                }
                if(deptId2NameMap.containsKey(servicer.getDepartmentId())){
                    servicer.setDeptName(deptId2NameMap.get(servicer.getDepartmentId()));
                }
            }
        }
        return servicers;
    }

    @Override
    public void delServicerByKfId(Long kfId) {
        WeKfServicer weKfServicer = new WeKfServicer();
        weKfServicer.setDelFlag(1);
        this.update(weKfServicer, new LambdaQueryWrapper<WeKfServicer>().eq(WeKfServicer::getKfId, kfId)
                .eq(WeKfServicer::getDelFlag, 0));
    }


    @Override
    public void updateServicerStatus(String corpId, String openKfId, String servicerUserId, Integer status) {
        WeKfServicer weKfServicer = new WeKfServicer();
        weKfServicer.setStatus(status);
        update(weKfServicer, new LambdaQueryWrapper<WeKfServicer>()
                .eq(WeKfServicer::getCorpId,corpId)
                .eq(WeKfServicer::getOpenKfId, openKfId)
                .eq(WeKfServicer::getUserId, servicerUserId)
                .eq(WeKfServicer::getDelFlag, 0));
    }

    @Override
    public void updateServicer(Long id, String openKfId, List<String> userIds, List<Integer> departmentIdList) {
        List<WeKfUser> servicers = getServicerByKfId(id);
        //需要添加的接待人员
        List<String> addUserList = userIds.stream().filter(userId -> Optional.ofNullable(servicers).orElseGet(ArrayList::new)
                .stream().map(WeKfUser::getUserId)
                .noneMatch(servicer -> ObjectUtil.equal(servicer, userId)))
                .collect(Collectors.toList());
        //需要添加的接待部门
        List<Integer> addDepartmentList = departmentIdList.stream().filter(userId -> Optional.ofNullable(servicers).orElseGet(ArrayList::new)
                        .stream().map(WeKfUser::getDepartmentId)
                        .noneMatch(servicer -> ObjectUtil.equal(servicer, userId)))
                .collect(Collectors.toList());

        //需要移除的接待人员
        List<String> removeUserList = Optional.ofNullable(servicers).orElseGet(ArrayList::new).stream()
                .filter(servicer -> userIds.stream().noneMatch(userId -> ObjectUtil.equal(servicer.getUserId(), userId)))
                .map(WeKfUser::getUserId).filter(StringUtils::isNotEmpty).collect(Collectors.toList());

        List<Integer> removeDeptList = Optional.ofNullable(servicers).orElseGet(ArrayList::new).stream()
                .filter(servicer -> departmentIdList.stream().noneMatch(departmentId -> ObjectUtil.equal(servicer.getDepartmentId(), departmentId)))
                .map(WeKfUser::getDepartmentId).filter(Objects::nonNull).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(removeUserList) || CollectionUtil.isNotEmpty(removeDeptList)){
            WeKfQuery removeQuery = new WeKfQuery();
            removeQuery.setOpen_kfid(openKfId);
            removeQuery.setUserid_list(removeUserList);
            removeQuery.setDepartment_id_list(removeDeptList);
            try {
                qwKfClient.delServicer(removeQuery);
            } catch (Exception e) {
                log.error("删除接待人员失败 query:{},msg:{}", JSONObject.toJSONString(removeQuery),e.getMessage(),e);
                throw new WeComException("修改接待人员失败");
            }
            WeKfServicer weKfServicer = new WeKfServicer();
            weKfServicer.setDelFlag(1);
            update(weKfServicer,new LambdaQueryWrapper<WeKfServicer>()
                    .eq(WeKfServicer::getKfId,id)
                    .in(CollectionUtil.isNotEmpty(removeUserList),WeKfServicer::getUserId,removeUserList)
                    .in(CollectionUtil.isNotEmpty(removeDeptList),WeKfServicer::getDepartmentId,removeDeptList)
                    .eq(WeKfServicer::getDelFlag,0));
        }

        if(CollectionUtil.isNotEmpty(addUserList) || CollectionUtil.isNotEmpty(addDepartmentList)){
            WeKfQuery addQuery = new WeKfQuery();
            addQuery.setOpen_kfid(openKfId);
            addQuery.setUserid_list(addUserList);
            addQuery.setDepartment_id_list(addDepartmentList);
            try {
                WeKfUserListVo weKfUserListVo = qwKfClient.addServicer(addQuery).getData();
                List<WeKfUserVo> resultList = weKfUserListVo.getResultList();
                List<WeKfUserVo> validUserIds = resultList.stream()
                        .filter(item -> item.getErrCode().equals(0))
                        .collect(Collectors.toList());
                if (CollectionUtil.isEmpty(validUserIds)) {
                    throw new WeComException("修改接待人员失败");
                }
                List<WeKfServicer> servicerList = validUserIds.stream().map(weKfUser -> {
                    WeKfServicer servicer = new WeKfServicer();
                    servicer.setKfId(id);
                    servicer.setOpenKfId(openKfId);
                    servicer.setDepartmentId(weKfUser.getDepartmentId());
                    servicer.setUserId(weKfUser.getUserId());
                    servicer.setCorpId(SecurityUtils.getCorpId());
                    return servicer;
                }).collect(Collectors.toList());
                //客服接待人员信息入库
                saveBatch(servicerList);
            } catch (Exception e) {
                log.error("修改接待人员失败 query:{},msg:{}",JSONObject.toJSONString(addQuery),e.getMessage(),e);
                throw new WeComException("修改接待人员失败");
            }

        }

    }

    @Override
    public List<WeKfServicerListVo> getKfServicerList(WeKfServicerListQuery query) {
        return this.baseMapper.getKfServicerList(query);
    }

    @Override
    public List<WeKfUser> getKfUserIdList(Long kfId) {
        List<WeKfUser> kfUserResultList = new LinkedList<>();
        List<WeKfUser> servicers = this.baseMapper.getServicerByKfId(kfId);
        if(CollectionUtil.isNotEmpty(servicers)) {
            Set<String> userIdSet = servicers.stream().map(WeKfUser::getUserId).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
            Set<Integer> deptIdSet = servicers.stream().map(WeKfUser::getDepartmentId).filter(Objects::nonNull).collect(Collectors.toSet());

            if(CollectionUtil.isNotEmpty(userIdSet) || CollectionUtil.isNotEmpty(deptIdSet)){
                SysUserQuery userQuery = new SysUserQuery();
                userQuery.setOpenUserIds(new ArrayList<>(userIdSet));
                userQuery.setDeptIds(new ArrayList<>(deptIdSet));
                try {
                    List<SysUserVo> sysUserList = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
                    if(CollectionUtil.isNotEmpty(sysUserList)){
                        Map<String, Integer> userMap = sysUserList.stream().collect(Collectors.toMap(SysUserVo::getOpenUserid, SysUserVo::getKfStatus, (key1, key2) -> key2));
                        userMap.forEach((userId, status) ->{
                            WeKfUser weKfUser = new WeKfUser();
                            weKfUser.setUserId(userId);
                            weKfUser.setStatus(status);
                            kfUserResultList.add(weKfUser);
                        });
                    }
                } catch (Exception e) {
                    log.error("换取用户名称失败：userQuery：{}",JSONObject.toJSONString(userQuery),e);
                }
            }
        }
        return kfUserResultList;
    }
}
