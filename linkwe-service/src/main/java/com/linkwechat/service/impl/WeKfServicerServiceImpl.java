package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeKfServicer;
import com.linkwechat.domain.kf.WeKfUser;
import com.linkwechat.domain.kf.query.WeKfServicerListQuery;
import com.linkwechat.domain.kf.vo.WeKfServicerListVo;
import com.linkwechat.domain.wecom.query.kf.WeKfQuery;
import com.linkwechat.domain.wecom.vo.kf.WeKfUserListVo;
import com.linkwechat.domain.wecom.vo.kf.WeKfUserVo;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.mapper.WeKfServicerMapper;
import com.linkwechat.service.IWeKfServicerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 客服接待人员表(WeKfServicer)
 *
 * @author danmo
 * @since 2022-04-15 15:53:39
 */
@Service
public class WeKfServicerServiceImpl extends ServiceImpl<WeKfServicerMapper, WeKfServicer> implements IWeKfServicerService {

    @Autowired
    private QwKfClient qwKfClient;

    @Autowired
    private RedisService redisService;

    @Override
    public List<WeKfUser> getServicerByKfId(Long kfId) {
        return this.baseMapper.getServicerByKfId(kfId);
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
        redisService.deleteObject(StringUtils.format(WeConstans.KF_ACCOUNT_SET_UP_KEY, corpId, openKfId));
        WeKfServicer weKfServicer = new WeKfServicer();
        weKfServicer.setStatus(status);
        update(weKfServicer, new LambdaQueryWrapper<WeKfServicer>()
                .eq(WeKfServicer::getCorpId,corpId)
                .eq(WeKfServicer::getOpenKfId, openKfId)
                .eq(WeKfServicer::getUserId, servicerUserId)
                .eq(WeKfServicer::getDelFlag, 0));
        redisService.deleteObject(StringUtils.format(WeConstans.KF_ACCOUNT_SET_UP_KEY, corpId, openKfId));
    }

    @Override
    public void updateServicer(Long id, String openKfId, List<String> userIds) {
        List<WeKfUser> servicers = getServicerByKfId(id);
        //需要移除的接待人员
        List<String> removeList = Optional.ofNullable(servicers).orElseGet(ArrayList::new).stream()
                .map(WeKfUser::getUserId).filter(servicer -> userIds.stream()
                        .noneMatch(userId -> ObjectUtil.equal(servicer, userId)))
                .collect(Collectors.toList());
        //需要添加的接待人员
        List<String> addList = userIds.stream().filter(userId -> Optional.ofNullable(servicers).orElseGet(ArrayList::new)
                .stream().map(WeKfUser::getUserId)
                .noneMatch(servicer -> ObjectUtil.equal(servicer, userId)))
                .collect(Collectors.toList());

        if(CollectionUtil.isNotEmpty(removeList)){
            List<List<String>> removePartition = Lists.partition(removeList, 100);
            removePartition.forEach(removeServicers ->{
                WeKfQuery removeQuery = new WeKfQuery();
                removeQuery.setOpen_kfid(openKfId);
                removeQuery.setUserid_list(removeList);
                try {
                    qwKfClient.delServicer(removeQuery);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new WeComException(1001, "修改接待人员失败");
                }
            });
            WeKfServicer weKfServicer = new WeKfServicer();
            weKfServicer.setDelFlag(1);
            update(weKfServicer,new LambdaQueryWrapper<WeKfServicer>()
                    .eq(WeKfServicer::getKfId,id)
                    .in(WeKfServicer::getUserId,removeList)
                    .eq(WeKfServicer::getDelFlag,0));
        }

        if(CollectionUtil.isNotEmpty(addList)){
            List<WeKfServicer> addServicerList = new ArrayList<>();
            List<List<String>> addPartition = Lists.partition(addList, 100);
            addPartition.forEach(addServicers ->{
                WeKfQuery addQuery = new WeKfQuery();
                addQuery.setOpen_kfid(openKfId);
                addQuery.setUserid_list(addList);
                try {
                    WeKfUserListVo weKfUserListVo = qwKfClient.addServicer(addQuery).getData();
                    List<WeKfUserVo> resultList = weKfUserListVo.getResultList();
                    List<String> validUserIds = resultList.stream()
                            .filter(item -> item.getErrCode().equals(0))
                            .map(WeKfUserVo::getUserId).collect(Collectors.toList());
                    if (CollectionUtil.isEmpty(validUserIds)) {
                        throw new WeComException(1001, "修改接待人员失败");
                    }
                    List<WeKfServicer> servicerList = validUserIds.stream().map(userId -> {
                        WeKfServicer servicer = new WeKfServicer();
                        servicer.setKfId(id);
                        servicer.setOpenKfId(openKfId);
                        servicer.setUserId(userId);
                        return servicer;
                    }).collect(Collectors.toList());
                    addServicerList.addAll(servicerList);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new WeComException(1001, "修改接待人员失败");
                }
            });
            //客服接待人员信息入库
            saveBatch(addServicerList);
        }

    }

    @Override
    public List<WeKfServicerListVo> getKfServicerList(WeKfServicerListQuery query) {
        return this.baseMapper.getKfServicerList(query);
    }
}
