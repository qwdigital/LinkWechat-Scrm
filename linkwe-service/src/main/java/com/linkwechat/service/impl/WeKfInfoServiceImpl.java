package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeKfInfo;
import com.linkwechat.domain.WeKfScenes;
import com.linkwechat.domain.WeKfServicer;
import com.linkwechat.domain.WeKfWelcome;
import com.linkwechat.domain.kf.WeKfMenu;
import com.linkwechat.domain.kf.WeKfMenuList;
import com.linkwechat.domain.kf.WeKfUser;
import com.linkwechat.domain.kf.WeKfWelcomeInfo;
import com.linkwechat.domain.kf.query.WeAddKfInfoQuery;
import com.linkwechat.domain.kf.query.WeKfListQuery;
import com.linkwechat.domain.kf.vo.WeKfInfoVo;
import com.linkwechat.domain.kf.vo.QwKfListVo;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.kf.WeKfAddQuery;
import com.linkwechat.domain.wecom.query.kf.WeKfQuery;
import com.linkwechat.domain.wecom.vo.kf.*;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.mapper.WeKfInfoMapper;
import com.linkwechat.service.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客服信息表(WeKfInfo)
 *
 * @author danmo
 * @since 2022-04-15 15:53:35
 */
@Service
public class WeKfInfoServiceImpl extends ServiceImpl<WeKfInfoMapper, WeKfInfo> implements IWeKfInfoService {

    @Autowired
    private QwKfClient qwKfClient;

     @Autowired
     private IWeMaterialService weMaterialService;

    @Autowired
    private IWeKfServicerService weKfServicerService;

    @Autowired
    private IWeKfWelcomeService weKfWelcomeService;

    @Autowired
    private IWeKfScenesService weKfScenesService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAccount(WeAddKfInfoQuery query) {
        checkTime(query);
        //换取头像素材id
        WeMediaVo weMediaVo = weMaterialService.uploadTemporaryMaterial(query.getAvatar(),
                MessageType.IMAGE.getMessageType(),
                FileUtil.getName(query.getAvatar()));

        //创建客服
        WeKfAddQuery weKfAddQuery = new WeKfAddQuery();
        weKfAddQuery.setMedia_id(weMediaVo.getMediaId());
        weKfAddQuery.setName(query.getName());
        WeAddKfVo weAddKfVo = qwKfClient.addAccount(weKfAddQuery).getData();
        if (weAddKfVo == null || StringUtils.isEmpty(weAddKfVo.getOpenKfId())) {
            throw new WeComException(weAddKfVo.getErrCode(),WeErrorCodeEnum.parseEnum(weAddKfVo.getErrCode()).getErrorMsg());
        }
        WeKfInfo weKfInfo = new WeKfInfo();
        BeanUtil.copyProperties(query, weKfInfo);
        weKfInfo.setOpenKfId(weAddKfVo.getOpenKfId());
        weKfInfo.setCorpId(SecurityUtils.getCorpId());
        //客服信息入库
        if (save(weKfInfo)) {
            //添加接待人员
            WeKfQuery weKfQuery = new WeKfQuery();
            List<String> userIds = query.getUserIdList().stream().map(WeKfUser::getUserId).collect(Collectors.toList());
            List<List<String>> partition = Lists.partition(userIds, 100);
            List<WeKfServicer> addServicerList = new ArrayList<>();
            partition.forEach(userIdList -> {
                weKfQuery.setOpen_kfid(weAddKfVo.getOpenKfId());
                weKfQuery.setUserid_list(userIdList);
                WeKfUserListVo weKfUserListVo = qwKfClient.addServicer(weKfQuery).getData();
                List<WeKfUserVo> resultList = weKfUserListVo.getResultList();
                List<String> validUserIds = resultList.stream()
                        .filter(item -> item.getErrCode().equals(0))
                        .map(WeKfUserVo::getUserId).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(validUserIds)) {
                    throw new WeComException(1001, "添加接待人员失败");
                }
                List<WeKfServicer> servicerList = validUserIds.stream().map(userId -> {
                    WeKfServicer weKfServicer = new WeKfServicer();
                    weKfServicer.setKfId(weKfInfo.getId());
                    weKfServicer.setOpenKfId(weAddKfVo.getOpenKfId());
                    weKfServicer.setUserId(userId);
                    weKfServicer.setCorpId(SecurityUtils.getCorpId());
                    return weKfServicer;
                }).collect(Collectors.toList());
                addServicerList.addAll(servicerList);
            });
            //客服接待人员信息入库
            weKfServicerService.saveBatch(addServicerList);

            //欢迎语入库
            List<WeKfWelcomeInfo> welcomeList = query.getWelcome();
            List<WeKfWelcome> weKfWelcomes = welcomeList.stream().map(welcomeInfo -> {
                WeKfWelcome weKfWelcome = new WeKfWelcome();
                weKfWelcome.setCorpId(SecurityUtils.getCorpId());
                weKfWelcome.setKfId(weKfInfo.getId());
                weKfWelcome.setWorkCycle(welcomeInfo.getWorkCycle());
                weKfWelcome.setBeginTime(welcomeInfo.getBeginTime());
                weKfWelcome.setEndTime(welcomeInfo.getEndTime());
                weKfWelcome.setType(welcomeInfo.getType());
                if (ObjectUtil.equal(1, welcomeInfo.getType())) {
                    weKfWelcome.setContent(welcomeInfo.getContent());
                }
                if (ObjectUtil.equal(2, welcomeInfo.getType())) {
                    List<WeKfMenu> menuList = welcomeInfo.getMenuList();
                    menuList.forEach(menu -> {
                        if (ObjectUtil.equal("click", menu.getType()) || ObjectUtil.equal("manual", menu.getType())) {
                            Snowflake snowflake = IdUtil.getSnowflake(RandomUtil.randomLong(6), RandomUtil.randomInt(6));
                            menu.setClickId(snowflake.nextIdStr());
                        }
                    });
                    WeKfMenuList kfMenuList = new WeKfMenuList();
                    kfMenuList.setHeadContent(welcomeInfo.getContent());
                    kfMenuList.setList(menuList);
                    weKfWelcome.setContent(JSONObject.toJSONString(kfMenuList));
                }
                return weKfWelcome;
            }).collect(Collectors.toList());
            weKfWelcomeService.saveBatch(weKfWelcomes);
        }
    }

    private void checkTime(WeAddKfInfoQuery query) {
        DateTime endOffSet = null;
        if (ObjectUtil.equal(1, query.getEndTimeType())) {
            endOffSet = DateUtil.offset(new Date(), DateField.MINUTE, query.getEndNoticeTime());
        } else {
            endOffSet = DateUtil.offset(new Date(), DateField.HOUR_OF_DAY, query.getEndNoticeTime());
        }

        DateTime timeOutOffSet = null;
        if (ObjectUtil.equal(1, query.getTimeOutType())) {
            timeOutOffSet = DateUtil.offset(new Date(), DateField.MINUTE, query.getTimeOut());
        } else {
            timeOutOffSet = DateUtil.offset(new Date(), DateField.HOUR_OF_DAY, query.getTimeOut());
        }
        if (timeOutOffSet.isAfterOrEquals(endOffSet)) {
            throw new WeComException(10010, "会话超时时间不能小于结束时间");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editAccountInfo(WeAddKfInfoQuery query) {
        if (query.getId() == null) {
            throw new WeComException(1004, "客服id不能为空");
        }
        WeKfInfo weKfInfo = getById(query.getId());
        if (weKfInfo == null) {
            throw new WeComException(1005, "未找到有效客服账号");
        }
        redisService.deleteObject(StringUtils.format(WeConstans.KF_ACCOUNT_SET_UP_KEY,SecurityUtils.getCorpId(), weKfInfo.getOpenKfId()));
        //修改客服
        if (ObjectUtil.notEqual(query.getName(), weKfInfo.getName())
                || ObjectUtil.notEqual(query.getAvatar(), weKfInfo.getAvatar())) {
            WeKfQuery weKfQuery = new WeKfQuery();
            if (ObjectUtil.notEqual(query.getName(), weKfInfo.getName())) {
                weKfQuery.setName(query.getName());
                weKfInfo.setName(query.getName());
            }
            if (ObjectUtil.notEqual(query.getAvatar(), weKfInfo.getAvatar())) {
                weKfInfo.setAvatar(query.getAvatar());
                //换取头像素材id
                WeMediaVo weMediaVo = weMaterialService.uploadTemporaryMaterial(query.getAvatar(),
                        MessageType.IMAGE.getMessageType(),
                        FileUtil.getName(query.getAvatar()));
                weKfQuery.setMedia_id(weMediaVo.getMediaId());
            }
            weKfQuery.setOpen_kfid(weKfInfo.getOpenKfId());
            qwKfClient.updateAccount(weKfQuery);
        }
        try {
            //客服信息修改
            weKfInfo.setAllocationWay(query.getAllocationWay());
            weKfInfo.setIsPriority(query.getIsPriority());
            weKfInfo.setReceptionType(query.getReceptionType());
            weKfInfo.setReceiveLimit(query.getReceiveLimit());
            weKfInfo.setQueueNotice(query.getQueueNotice());
            weKfInfo.setQueueNoticeContent(query.getQueueNoticeContent());
            weKfInfo.setTimeOutNotice(query.getTimeOutNotice());
            weKfInfo.setTimeOutContent(query.getTimeOutContent());
            weKfInfo.setTimeOut(query.getTimeOut());
            weKfInfo.setTimeOutType(query.getTimeOutType());
            weKfInfo.setEndNotice(query.getEndNotice());
            weKfInfo.setEndContent(query.getEndContent());
            weKfInfo.setEndNoticeTime(query.getEndNoticeTime());
            weKfInfo.setEndTimeType(query.getEndTimeType());
            updateById(weKfInfo);

            //欢迎语修改
            weKfWelcomeService.delWelcomByKfId(weKfInfo.getId());
            List<WeKfWelcomeInfo> welcomeList = query.getWelcome();
            List<WeKfWelcome> weKfWelcomes = welcomeList.stream().map(welcomeInfo -> {
                WeKfWelcome weKfWelcome = new WeKfWelcome();
                weKfWelcome.setKfId(weKfInfo.getId());
                weKfWelcome.setWorkCycle(welcomeInfo.getWorkCycle());
                weKfWelcome.setBeginTime(welcomeInfo.getBeginTime());
                weKfWelcome.setEndTime(welcomeInfo.getEndTime());
                weKfWelcome.setType(welcomeInfo.getType());
                if (ObjectUtil.equal(1, welcomeInfo.getType())) {
                    weKfWelcome.setContent(welcomeInfo.getContent());
                }
                if (ObjectUtil.equal(2, welcomeInfo.getType())) {
                    List<WeKfMenu> menuList = welcomeInfo.getMenuList();
                    menuList.forEach(menu -> {
                        if (ObjectUtil.equal("click", menu.getType()) || ObjectUtil.equal("manual", menu.getType())) {
                            Snowflake snowflake = IdUtil.getSnowflake(RandomUtil.randomLong(6), RandomUtil.randomInt(6));
                            menu.setClickId(snowflake.nextIdStr());
                        }
                    });
                    WeKfMenuList kfMenuList = new WeKfMenuList();
                    kfMenuList.setHeadContent(welcomeInfo.getContent());
                    kfMenuList.setList(menuList);
                    weKfWelcome.setContent(JSONObject.toJSONString(kfMenuList));
                }
                return weKfWelcome;
            }).collect(Collectors.toList());
            weKfWelcomeService.saveBatch(weKfWelcomes);

            //添加接待人员
            List<String> userIds = query.getUserIdList().stream().map(WeKfUser::getUserId).collect(Collectors.toList());
            weKfServicerService.updateServicer(weKfInfo.getId(), weKfInfo.getOpenKfId(), userIds);
            redisService.deleteObject(StringUtils.format(WeConstans.KF_ACCOUNT_SET_UP_KEY,weKfInfo.getCorpId(), weKfInfo.getOpenKfId()));
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            throw new WeComException(1006, "修改客服账号失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editAccountWelcome(WeAddKfInfoQuery query) {
        if (query.getId() == null) {
            throw new WeComException(1004, "客服id不能为空");
        }
        WeKfInfo weKfInfo = getById(query.getId());
        if (weKfInfo == null) {
            throw new WeComException(1005, "未找到有效客服账号");
        }
        //欢迎语修改
        weKfWelcomeService.delWelcomByKfId(weKfInfo.getId());
        List<WeKfWelcomeInfo> welcomeList = query.getWelcome();
        List<WeKfWelcome> weKfWelcomes = welcomeList.stream().map(welcomeInfo -> {
            WeKfWelcome weKfWelcome = new WeKfWelcome();
            weKfWelcome.setCorpId(SecurityUtils.getCorpId());
            weKfWelcome.setKfId(weKfInfo.getId());
            weKfWelcome.setWorkCycle(welcomeInfo.getWorkCycle());
            weKfWelcome.setBeginTime(welcomeInfo.getBeginTime());
            weKfWelcome.setEndTime(welcomeInfo.getEndTime());
            weKfWelcome.setType(welcomeInfo.getType());
            if (ObjectUtil.equal(1, welcomeInfo.getType())) {
                weKfWelcome.setContent(welcomeInfo.getContent());
            }
            if (ObjectUtil.equal(2, welcomeInfo.getType())) {
                List<WeKfMenu> menuList = welcomeInfo.getMenuList();
                menuList.forEach(menu -> {
                    if (ObjectUtil.equal("click", menu.getType())) {
                        Snowflake snowflake = IdUtil.getSnowflake(RandomUtil.randomLong(6), RandomUtil.randomInt(6));
                        menu.setClickId(snowflake.nextIdStr());
                    }
                });
                WeKfMenuList kfMenuList = new WeKfMenuList();
                kfMenuList.setHeadContent(welcomeInfo.getContent());
                kfMenuList.setList(menuList);
                weKfWelcome.setContent(JSONObject.toJSONString(kfMenuList));
            }
            return weKfWelcome;
        }).collect(Collectors.toList());
        weKfWelcomeService.saveBatch(weKfWelcomes);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editAccountReception(WeAddKfInfoQuery query) {
        checkTime(query);
        if (query.getId() == null) {
            throw new WeComException(1004, "客服id不能为空");
        }
        WeKfInfo weKfInfo = getById(query.getId());
        if (weKfInfo == null) {
            throw new WeComException(1005, "未找到有效客服账号");
        }
        weKfInfo.setCorpId(SecurityUtils.getCorpId());
        weKfInfo.setAllocationWay(query.getAllocationWay());
        weKfInfo.setIsPriority(query.getIsPriority());
        weKfInfo.setReceptionType(query.getReceptionType());
        weKfInfo.setReceiveLimit(query.getReceiveLimit());
        weKfInfo.setQueueNotice(query.getQueueNotice());
        weKfInfo.setQueueNoticeContent(query.getQueueNoticeContent());
        weKfInfo.setTimeOutNotice(query.getTimeOutNotice());
        weKfInfo.setTimeOutContent(query.getTimeOutContent());
        weKfInfo.setTimeOut(query.getTimeOut());
        weKfInfo.setTimeOutType(query.getTimeOutType());
        weKfInfo.setEndNotice(query.getEndNotice());
        weKfInfo.setEndContent(query.getEndContent());
        weKfInfo.setEndNoticeTime(query.getEndNoticeTime());
        weKfInfo.setEndTimeType(query.getEndTimeType());
        updateById(weKfInfo);
        //添加接待人员
        List<String> userIds = query.getUserIdList().stream().map(WeKfUser::getUserId).collect(Collectors.toList());
        weKfServicerService.updateServicer(weKfInfo.getId(), weKfInfo.getOpenKfId(), userIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delKfInfo(Long id) {
        WeKfInfo weKfInfo = getById(id);
        if (weKfInfo == null) {
            throw new WeComException(1002, "客服数据不存在");
        }
        List<WeKfScenes> scenesList = weKfScenesService.getScenesByKfId(id);
        if (CollectionUtil.isNotEmpty(scenesList)) {
            throw new WeComException(1003, "请删除当前客服下所有关联场景后，再删除当前客服。");
        }

        //删除企微客服
        WeKfQuery weKfQuery = new WeKfQuery();
        weKfQuery.setOpen_kfid(weKfInfo.getOpenKfId());
        try {
            qwKfClient.delAccount(weKfQuery);
        } catch (Exception e) {
            throw new WeComException(1009, "删除企微客服账号失败");
        }

        weKfInfo.setDelFlag(1);
        this.updateById(weKfInfo);

        weKfServicerService.delServicerByKfId(id);
        //删除欢迎语信息
        weKfWelcomeService.delWelcomByKfId(id);

        //删除缓存信息
        redisService.deleteObject(StringUtils.format(WeConstans.KF_SERVICER_POLLING_KEY,weKfInfo.getCorpId(), weKfInfo.getOpenKfId()));
        redisService.deleteObject(StringUtils.format(WeConstans.KF_ACCOUNT_SET_UP_KEY, weKfInfo.getCorpId(), weKfInfo.getOpenKfId()));
    }

    @Override
    public WeKfInfoVo getKfInfo(Long id) {
        WeKfInfoVo weKfInfoVo = new WeKfInfoVo();
        WeKfInfo weKfInfo = getById(id);
        BeanUtil.copyProperties(weKfInfo, weKfInfoVo);
        //查询接待人员
        List<WeKfUser> weKfUsers = weKfServicerService.getServicerByKfId(id);
        weKfInfoVo.setUserIdList(weKfUsers);
        //查询欢迎语设置
        List<WeKfWelcomeInfo> kfWelcomeInfoList = weKfWelcomeService.getWelcomeByKfId(id);
        weKfInfoVo.setWelcome(kfWelcomeInfoList);
        return weKfInfoVo;
    }

    @Override
    public List<QwKfListVo> getKfList(WeKfListQuery query) {
        return this.baseMapper.getKfList(query);
    }

    @Override
    public WeKfInfoVo getKfInfoByOpenKfId(String corpId, String openKfId) {
        String cacheKey = StringUtils.format(WeConstans.KF_ACCOUNT_SET_UP_KEY,corpId, openKfId);
        WeKfInfoVo weKfInfoVo = redisService.getCacheObject(cacheKey);
        if (weKfInfoVo == null) {
            WeKfInfo weKfInfo = getOne(new LambdaQueryWrapper<WeKfInfo>()
                    .eq(WeKfInfo::getCorpId,corpId)
                    .eq(WeKfInfo::getOpenKfId, openKfId)
                    .eq(WeKfInfo::getDelFlag, 0).last("limit 1"));
            if (weKfInfo == null) {
                return null;
            }
            weKfInfoVo = new WeKfInfoVo();
            BeanUtil.copyProperties(weKfInfo, weKfInfoVo);
            //查询接待人员
            List<WeKfUser> weKfUsers = weKfServicerService.getServicerByKfId(weKfInfo.getId());
            weKfInfoVo.setUserIdList(weKfUsers);
            //查询欢迎语设置
            List<WeKfWelcomeInfo> kfWelcomeInfoList = weKfWelcomeService.getWelcomeByKfId(weKfInfo.getId());
            weKfInfoVo.setWelcome(kfWelcomeInfoList);
            if (weKfInfoVo != null) {
                redisService.setCacheObject(cacheKey, weKfInfoVo);
            }
        }
        return weKfInfoVo;
    }

    @Override
    public void asyncKfList() {
        //发送同步消息
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(),rabbitMQSettingConfig.getWeKfAccountRk(),JSONObject.toJSONString(SecurityUtils.getLoginUser()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void synchKfAccountHandler(String msg) {
        LoginUser loginUser = JSONObject.parseObject(msg, LoginUser.class);
        SecurityContextHolder.setCorpId(loginUser.getCorpId());
        SecurityContextHolder.setUserName(loginUser.getUserName());
        SecurityContextHolder.setUserType(loginUser.getUserType());
        WeBaseQuery query = new WeBaseQuery();
        WeKfListVo accountListVo = qwKfClient.getAccountList(query).getData();
        List<WeKfDetailVo> accountList = accountListVo.getAccountList();
        if (CollectionUtil.isEmpty(accountList)) {
            return;
        }
        List<String> openKfIds = accountList.stream().map(WeKfDetailVo::getOpenKfId).collect(Collectors.toList());
        List<WeKfInfo> weKfInfos = list(new LambdaQueryWrapper<WeKfInfo>()
                .in(WeKfInfo::getOpenKfId, openKfIds)
                .eq(WeKfInfo::getDelFlag, 0));
        List<WeKfDetailVo> addAccountList = accountList.stream()
                .filter(account -> weKfInfos.stream()
                        .noneMatch(weKfInfo -> ObjectUtil.equal(weKfInfo.getOpenKfId(), account.getOpenKfId())))
                .collect(Collectors.toList());
        List<WeKfInfo> weKfInfoList = addAccountList.stream().map(account -> {
            WeKfInfo weKfInfo = new WeKfInfo();
            weKfInfo.setCorpId(SecurityUtils.getCorpId());
            weKfInfo.setName(account.getName());
            weKfInfo.setAvatar(account.getAvatar());
            weKfInfo.setOpenKfId(account.getOpenKfId());
            return weKfInfo;
        }).collect(Collectors.toList());
        //保存客服数据
        saveBatch(weKfInfoList);

        List<WeKfServicer> weKfServicers = new LinkedList<>();
        weKfInfoList.forEach(weKfInfo -> {
            WeKfQuery weKfQuery = new WeKfQuery();
            weKfQuery.setOpen_kfid(weKfInfo.getOpenKfId());
            WeKfUserListVo weKfUserListVo = qwKfClient.getServicerList(weKfQuery).getData();
            List<WeKfUserVo> servicerList = weKfUserListVo.getServicerList();
            List<WeKfServicer> weKfServicerList = servicerList.stream().map(servicer -> {
                WeKfServicer weKfServicer = new WeKfServicer();
                weKfServicer.setKfId(weKfInfo.getId());
                weKfServicer.setCorpId(SecurityUtils.getCorpId());
                weKfServicer.setOpenKfId(weKfInfo.getOpenKfId());
                weKfServicer.setUserId(servicer.getUserId());
                weKfServicer.setStatus(servicer.getStatus());
                return weKfServicer;
            }).collect(Collectors.toList());
            weKfServicers.addAll(weKfServicerList);
        });
        weKfServicerService.saveBatch(weKfServicers);
    }
}
