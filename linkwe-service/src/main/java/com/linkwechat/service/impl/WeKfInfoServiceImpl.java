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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.enums.WeKfMsgTypeEnum;
import com.linkwechat.common.enums.WeKfOriginEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.*;
import com.linkwechat.domain.kf.WeKfMenu;
import com.linkwechat.domain.kf.WeKfMenuList;
import com.linkwechat.domain.kf.WeKfUser;
import com.linkwechat.domain.kf.WeKfWelcomeInfo;
import com.linkwechat.domain.kf.query.WeAddKfInfoQuery;
import com.linkwechat.domain.kf.query.WeAddKfServicerQuery;
import com.linkwechat.domain.kf.query.WeAddKfWelcomeQuery;
import com.linkwechat.domain.kf.query.WeKfListQuery;
import com.linkwechat.domain.kf.vo.QwKfListVo;
import com.linkwechat.domain.kf.vo.WeKfInfoVo;
import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.kf.WeKfAddQuery;
import com.linkwechat.domain.wecom.query.kf.WeKfMsgQuery;
import com.linkwechat.domain.wecom.query.kf.WeKfQuery;
import com.linkwechat.domain.wecom.vo.kf.*;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.fegin.QwSysDeptClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeKfInfoMapper;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客服信息表(WeKfInfo)
 *
 * @author danmo
 * @since 2022-04-15 15:53:35
 */
@Slf4j
@Service
public class WeKfInfoServiceImpl extends ServiceImpl<WeKfInfoMapper, WeKfInfo> implements IWeKfInfoService {

    @Resource
    private QwKfClient qwKfClient;

    @Resource
    private QwSysUserClient qwSysUserClient;

    @Resource
    private QwSysDeptClient qwSysDeptClient;

     @Autowired
     private IWeMaterialService weMaterialService;

    @Autowired
    private IWeKfServicerService weKfServicerService;

    @Autowired
    private IWeKfWelcomeService weKfWelcomeService;

    @Autowired
    private IWeKfScenesService weKfScenesService;

    @Autowired
    private IWeKfMsgService weKfMsgService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${wecom.kf.end.msgmenu.content:}")
    private String kfEndMsgMenuContent;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addAccount(WeAddKfInfoQuery query) {
        //换取头像素材id
        WeMediaVo weMediaVo = weMaterialService.uploadTemporaryMaterial(query.getAvatar(),
                MessageType.IMAGE.getMessageType(),
                FileUtil.getName(query.getAvatar()));
        //创建客服
        WeKfAddQuery weKfAddQuery = new WeKfAddQuery();
        weKfAddQuery.setMedia_id(weMediaVo.getMediaId());
        weKfAddQuery.setName(query.getName());
        WeAddKfVo weAddKfVo = qwKfClient.addAccount(weKfAddQuery).getData();
        if (weAddKfVo == null){
            throw new WeComException("添加客服账号失败");
        }
        if (StringUtils.isEmpty(weAddKfVo.getOpenKfId())) {
            throw new WeComException(weAddKfVo.getErrCode(),WeErrorCodeEnum.parseEnum(weAddKfVo.getErrCode()).getErrorMsg());
        }
        WeKfInfo weKfInfo = new WeKfInfo();
        BeanUtil.copyProperties(query, weKfInfo);
        weKfInfo.setOpenKfId(weAddKfVo.getOpenKfId());
        weKfInfo.setCorpId(SecurityUtils.getCorpId());
        //客服信息入库
        save(weKfInfo);
        return weKfInfo.getId();
    }

    @Override
    public void addAccountWelcome(WeAddKfWelcomeQuery query) {
        WeKfInfo weKfInfo = new WeKfInfo();
        weKfInfo.setId(query.getId());
        weKfInfo.setReceptionType(query.getReceptionType());
        updateById(weKfInfo);
        List<WeKfWelcomeInfo> welcomeList = query.getWelcome();
        List<WeKfWelcome> weKfWelcomes = welcomeList.stream().map(welcomeInfo -> {
            WeKfWelcome weKfWelcome = new WeKfWelcome();
            weKfWelcome.setCorpId(SecurityUtils.getCorpId());
            weKfWelcome.setKfId(query.getId());
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

    @Override
    public void addAccountServicer(WeAddKfServicerQuery query) {
        checkParams(query);
        WeKfInfo weKfInfo = getById(query.getId());
        weKfInfo.setCorpId(SecurityUtils.getCorpId());
        weKfInfo.setAllocationWay(query.getAllocationWay());
        weKfInfo.setIsPriority(query.getIsPriority());
        weKfInfo.setReceiveLimit(query.getReceiveLimit());
        weKfInfo.setQueueNotice(query.getQueueNotice());
        weKfInfo.setQueueNoticeContent(query.getQueueNoticeContent());
        weKfInfo.setTimeOutNotice(query.getTimeOutNotice());
        weKfInfo.setTimeOutContent(query.getTimeOutContent());
        weKfInfo.setTimeOut(query.getTimeOut());
        weKfInfo.setTimeOutType(query.getTimeOutType());

        weKfInfo.setKfTimeOutNotice(query.getKfTimeOutNotice());
        weKfInfo.setKfTimeOut(query.getKfTimeOut());
        weKfInfo.setKfTimeOutType(query.getKfTimeOutType());

        weKfInfo.setEndNotice(query.getEndNotice());
        weKfInfo.setEndContentType(query.getEndContentType());
        weKfInfo.setEndContent(query.getEndContent());
        weKfInfo.setEndNoticeTime(query.getEndNoticeTime());
        weKfInfo.setEndTimeType(query.getEndTimeType());
        updateById(weKfInfo);
        WeKfQuery weKfQuery = new WeKfQuery();
        List<List<String>> partition = Lists.partition(query.getUserIdList(), 100);
        weKfQuery.setOpen_kfid(weKfInfo.getOpenKfId());
        weKfQuery.setUserid_list(query.getUserIdList());
        weKfQuery.setDepartment_id_list(query.getDepartmentIdList());
        WeKfUserListVo weKfUserListVo = qwKfClient.addServicer(weKfQuery).getData();
        List<WeKfUserVo> resultList = weKfUserListVo.getResultList();
        List<WeKfUserVo> validUserIds = resultList.stream()
                .filter(item -> item.getErrCode().equals(0)).collect(Collectors.toList());
        List<WeKfServicer> servicerList = validUserIds.stream().map(kfUserVo -> {
            WeKfServicer weKfServicer = new WeKfServicer();
            weKfServicer.setKfId(weKfInfo.getId());
            weKfServicer.setOpenKfId(weKfInfo.getOpenKfId());
            weKfServicer.setUserId(kfUserVo.getUserId());
            weKfServicer.setDepartmentId(kfUserVo.getDepartmentId());
            weKfServicer.setCorpId(SecurityUtils.getCorpId());
            return weKfServicer;
        }).collect(Collectors.toList());
        //客服接待人员信息入库
        weKfServicerService.saveBatch(servicerList);
    }

    private void checkParams(WeAddKfServicerQuery query) {
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
        DateTime kfTimeOutOffSet = null;
        if (ObjectUtil.equal(1, query.getKfTimeOutType())) {
            kfTimeOutOffSet = DateUtil.offset(new Date(), DateField.MINUTE, query.getKfTimeOut());
        } else {
            kfTimeOutOffSet = DateUtil.offset(new Date(), DateField.HOUR_OF_DAY, query.getKfTimeOut());
        }
        if (timeOutOffSet.isAfterOrEquals(endOffSet)) {
            throw new WeComException("客户超时时间不能小于结束时间");
        }

        if (kfTimeOutOffSet.isAfterOrEquals(endOffSet)) {
            throw new WeComException("客服超时时间不能小于结束时间");
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
            updateById(weKfInfo);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editAccountWelcome(WeAddKfWelcomeQuery query) {
        WeKfInfo kfInfo = getById(query.getId());
        weKfWelcomeService.delWelcomByKfId(query.getId());
        addAccountWelcome(query);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editAccountServicer(WeAddKfServicerQuery query) {
        if (query.getId() == null) {
            throw new WeComException(1004, "客服id不能为空");
        }
        checkParams(query);
        WeKfInfo weKfInfo = getById(query.getId());
        if (weKfInfo == null) {
            throw new WeComException(1005, "未找到有效客服账号");
        }
        weKfInfo.setCorpId(SecurityUtils.getCorpId());
        weKfInfo.setAllocationWay(query.getAllocationWay());
        weKfInfo.setIsPriority(query.getIsPriority());
        weKfInfo.setReceiveLimit(query.getReceiveLimit());
        weKfInfo.setQueueNotice(query.getQueueNotice());
        weKfInfo.setQueueNoticeContent(query.getQueueNoticeContent());
        weKfInfo.setTimeOutNotice(query.getTimeOutNotice());
        weKfInfo.setTimeOutContent(query.getTimeOutContent());
        weKfInfo.setTimeOut(query.getTimeOut());
        weKfInfo.setTimeOutType(query.getTimeOutType());

        weKfInfo.setKfTimeOutNotice(query.getKfTimeOutNotice());
        weKfInfo.setKfTimeOut(query.getKfTimeOut());
        weKfInfo.setKfTimeOutType(query.getKfTimeOutType());


        weKfInfo.setEndNotice(query.getEndNotice());
        weKfInfo.setEndContent(query.getEndContent());
        weKfInfo.setEndContentType(query.getEndContentType());
        weKfInfo.setEndNoticeTime(query.getEndNoticeTime());
        weKfInfo.setEndTimeType(query.getEndTimeType());
        updateById(weKfInfo);
        //添加接待人员
        weKfServicerService.updateServicer(weKfInfo.getId(), weKfInfo.getOpenKfId(), query.getUserIdList(),query.getDepartmentIdList());
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
        List<QwKfListVo> kfList = this.baseMapper.getKfList(query);
        if(CollectionUtil.isNotEmpty(kfList)){
            Set<String> userIdSet = kfList.stream().map(QwKfListVo::getUserIdList).flatMap(Collection::stream).map(WeKfUser::getUserId).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
            Set<Long> deptIdSet = kfList.stream().map(QwKfListVo::getUserIdList).flatMap(Collection::stream).map(WeKfUser::getDepartmentId).filter(Objects::nonNull).collect(Collectors.toSet());
            Map<String, String> userId2NameMap = new HashMap<>();
            Map<Long, String> deptId2NameMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(userIdSet)){
                SysUserQuery userQuery = new SysUserQuery();
                userQuery.setWeUserIds(new ArrayList<>(userIdSet));
                try {
                    List<SysUserVo> sysUserList = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
                    if(CollectionUtil.isNotEmpty(sysUserList)){
                        Map<String, String> userMap = sysUserList.stream().collect(Collectors.toMap(SysUserVo::getWeUserId, SysUserVo::getUserName, (key1, key2) -> key2));
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
                        Map<Long, String> deptMap = sysDeptList.stream().collect(Collectors.toMap(SysDeptVo::getDeptId, SysDeptVo::getDeptName,(key1, key2) -> key2));
                        deptId2NameMap.putAll(deptMap);
                    }
                } catch (Exception e) {
                    log.error("换取部门名称失败：sysDeptQuery：{}",JSONObject.toJSONString(sysDeptQuery),e);
                }

            }
            for (QwKfListVo qwKfListVo : kfList) {
                List<WeKfUser> kfUserList = qwKfListVo.getUserIdList();
                for (WeKfUser weKfUser : kfUserList) {
                    if(userId2NameMap.containsKey(weKfUser.getUserId())){
                        weKfUser.setUserName(userId2NameMap.get(weKfUser.getUserId()));
                    }
                    if(deptId2NameMap.containsKey(weKfUser.getDepartmentId())){
                        weKfUser.setDeptName(deptId2NameMap.get(weKfUser.getDepartmentId()));
                    }
                }
            }
        }
        return kfList;
    }

    @Override
    public PageInfo<QwKfListVo> getKfPageList(WeKfListQuery query) {
        List<Long> kfIds= this.baseMapper.getKfIdList(query);
        WeKfListQuery weKfListQuery = new WeKfListQuery();
        weKfListQuery.setIds(kfIds);

        List<QwKfListVo> kfList = getKfList(weKfListQuery);

        PageInfo<Long> pageIdInfo = new PageInfo<>(kfIds);
        PageInfo<QwKfListVo> pageInfo = new PageInfo<>(kfList);
        pageInfo.setTotal(pageIdInfo.getTotal());
        pageInfo.setPageNum(pageIdInfo.getPageNum());
        pageInfo.setPageSize(pageIdInfo.getPageSize());
        return pageInfo;
    }

    @Override
    public WeKfInfoVo getKfInfoByOpenKfId(String corpId, String openKfId) {
        WeKfInfoVo weKfInfoVo = new WeKfInfoVo();
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
        return weKfInfoVo;
    }

    @Override
    public WeKfInfo getKfDetailByOpenKfId(String corpId, String openKfId) {
        return getOne(new LambdaQueryWrapper<WeKfInfo>()
                .eq(WeKfInfo::getCorpId,corpId)
                .eq(WeKfInfo::getOpenKfId, openKfId)
                .eq(WeKfInfo::getDelFlag, 0).last("limit 1"));
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
                weKfServicer.setDepartmentId(servicer.getDepartmentId());
                weKfServicer.setStatus(servicer.getStatus());
                return weKfServicer;
            }).collect(Collectors.toList());
            weKfServicers.addAll(weKfServicerList);
        });
        weKfServicerService.saveBatch(weKfServicers);
    }

    @Override
    public void sendEndMsg(String code, WeKfInfo weKfInfo, WeKfPool weKfPoolInfo) {
        Date date = new Date();
        //发送结束语
        WeKfMsgQuery weKfMsgQuery = new WeKfMsgQuery();
        JSONObject msgBody = new JSONObject();
        weKfMsgQuery.setCode(code);
        try {
            if(ObjectUtil.equal(1, weKfInfo.getEndContentType())){
                weKfMsgQuery.setMsgtype(WeKfMsgTypeEnum.MSGMENU.getType());
                JSONObject endMsgMenuContentObj = JSONObject.parseObject(kfEndMsgMenuContent);
                JSONArray list = endMsgMenuContentObj.getJSONArray("list");
                list.stream().map(item -> (JSONObject) item).forEach(item ->{
                    if("click".equals(item.getString("type"))){
                        JSONObject click = item.getJSONObject("click");
                        String clickId = "end_" + click.getString("id") +"_" +weKfPoolInfo.getId();
                        click.put("id",clickId);
                    }
                    if("view".equals(item.getString("type"))){
                        JSONObject view = item.getJSONObject("view");
                        view.put("url",StringUtils.format(view.getString("url"), weKfPoolInfo.getId()));
                    }
                });
                endMsgMenuContentObj.put("list",list);
                msgBody = endMsgMenuContentObj;
            }else if(ObjectUtil.equal(2, weKfInfo.getEndContentType())){
                weKfMsgQuery.setMsgtype(WeKfMsgTypeEnum.TEXT.getType());
                msgBody.put("content", weKfInfo.getEndContent());
            }
            weKfMsgQuery.setContext(msgBody);
            WeKfMsgVo weKfMsgVo = qwKfClient.sendMsgOnEvent(weKfMsgQuery).getData();
            if(Objects.nonNull(weKfMsgVo) && StringUtils.isNotEmpty(weKfMsgVo.getMsgId())){
                WeKfMsg weKfMsg = new WeKfMsg();
                weKfMsg.setMsgId(weKfMsgVo.getMsgId());
                weKfMsg.setMsgType(weKfMsgQuery.getMsgtype());
                weKfMsg.setOpenKfId(weKfPoolInfo.getOpenKfId());
                weKfMsg.setExternalUserid(weKfPoolInfo.getExternalUserId());
                weKfMsg.setContent(weKfMsgQuery.getContext().toJSONString());
                weKfMsg.setOrigin(WeKfOriginEnum.SERVICER_END.getType());
                weKfMsg.setCorpId(SecurityUtils.getCorpId());
                weKfMsg.setSendTime(date);
                weKfMsgService.save(weKfMsg);
            }
        } catch (Exception e) {
            log.error("发送结束语失败：code:{},openKfId:{},eid:{} ",code,weKfPoolInfo.getOpenKfId(),weKfPoolInfo.getExternalUserId(),e);
        }
    }
}
