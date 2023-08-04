package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.leads.leads.LeadsStatusEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.vo.WeLeadsVO;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecord;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSea;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSeaRuleRecord;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSeaVisibleRange;
import com.linkwechat.domain.leads.sea.query.VisibleRange;
import com.linkwechat.domain.leads.sea.query.WeLeadsSeaSaveRequest;
import com.linkwechat.domain.leads.sea.query.WeLeadsSeaUpdateRequest;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaDataDetailVO;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaEmployeeRankVO;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaTrendVO;
import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwSysDeptClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.*;
import com.linkwechat.service.IWeLeadsSeaService;
import com.linkwechat.service.IWeLeadsSeaVisibleRangeService;
import com.linkwechat.service.IWeLeadsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 线索公海 服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/10 18:09
 */
@Service
@Slf4j
public class WeLeadsSeaServiceImpl extends ServiceImpl<WeLeadsSeaMapper, WeLeadsSea> implements IWeLeadsSeaService {

    @Resource
    private WeLeadsSeaRuleRecordMapper weLeadsSeaRuleRecordMapper;
    @Resource
    private QwSysDeptClient qwSysDeptClient;
    @Resource
    private QwSysUserClient qwSysUserClient;
    @Resource
    private IWeLeadsSeaVisibleRangeService weLeadsSeaVisibleRangeService;
    @Resource
    private IWeLeadsService weLeadsService;
    @Resource
    private WeLeadsMapper weLeadsMapper;
    @Resource
    private WeLeadsFollowRecordMapper weLeadsFollowRecordMapper;
    @Resource
    private WeLeadsFollowerMapper weLeadsFollowerMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(WeLeadsSeaSaveRequest request) {
        //判断公海名称是否重复
        LambdaQueryWrapper<WeLeadsSea> queryWrapper = Wrappers.lambdaQuery(WeLeadsSea.class);
        queryWrapper.eq(WeLeadsSea::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.eq(WeLeadsSea::getName, request.getName());
        WeLeadsSea one = this.getOne(queryWrapper);
        if (BeanUtil.isNotEmpty(one)) {
            throw new ServiceException("公海名称重复！", HttpStatus.BAD_REQUEST);
        }

        //保存
        WeLeadsSea weLeadsSea = BeanUtil.copyProperties(request, WeLeadsSea.class);
        weLeadsSea.setVersion(0);
        weLeadsSea.setDelFlag(Constants.COMMON_STATE);
        this.save(weLeadsSea);

        //新增公海规则记录
        addLeadsSeaRuleRecord(weLeadsSea);

        //获取公海的可见范围数据
        List<WeLeadsSeaVisibleRange> weLeadsSeaVisibleRanges = getWeLeadsSeaVisibleRanges(request.getDeptRange(), request.getPostRange(), request.getUserRange(), weLeadsSea.getId());
        //保存可见范围
        weLeadsSeaVisibleRangeService.saveBatch(weLeadsSeaVisibleRanges);
        return weLeadsSea.getId();
    }

    @Override
    public void update(WeLeadsSeaUpdateRequest request) {
        //判断公海名称是否重复
        LambdaQueryWrapper<WeLeadsSea> wrapper = Wrappers.lambdaQuery(WeLeadsSea.class);
        wrapper.eq(WeLeadsSea::getName, request.getName());
        wrapper.ne(WeLeadsSea::getId, request.getId());
        WeLeadsSea one = this.getOne(wrapper);
        if (BeanUtil.isNotEmpty(one)) {
            throw new ServiceException("公海名称重复！");
        }

        WeLeadsSea weLeadsSea = BeanUtil.copyProperties(request, WeLeadsSea.class);

        //判断回收规则是否变化
        WeLeadsSea oldData = this.getById(request.getId());
        boolean b1 = recoveryRuleIsChange(oldData, weLeadsSea);
        if (b1) {
            weLeadsSea.setVersion(oldData.getVersion() + 1);
            //新增公海规则记录
            addLeadsSeaRuleRecord(weLeadsSea);
        }

        boolean b = this.updateById(weLeadsSea);
        if (b) {
            //删除旧的可见范围，保留管理员
            LambdaQueryWrapper<WeLeadsSeaVisibleRange> queryWrapper = Wrappers.lambdaQuery(WeLeadsSeaVisibleRange.class);
            queryWrapper.eq(WeLeadsSeaVisibleRange::getSeaId, weLeadsSea.getId());
            weLeadsSeaVisibleRangeService.remove(queryWrapper);
            //获取公海的可见范围数据
            List<WeLeadsSeaVisibleRange> weLeadsSeaVisibleRanges = getWeLeadsSeaVisibleRanges(request.getDeptRange(), request.getPostRange(), request.getUserRange(), weLeadsSea.getId());
            //保存公海可见范围
            weLeadsSeaVisibleRangeService.saveBatch(weLeadsSeaVisibleRanges);
        }
    }


    /**
     * 新增公海规则记录
     *
     * @param data
     * @author WangYX
     * @date 2023/07/10 18:11
     */
    private void addLeadsSeaRuleRecord(WeLeadsSea data) {
        //新增公海规则记录
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        WeLeadsSeaRuleRecord weLeadsSeaRuleRecord = new WeLeadsSeaRuleRecord();
        weLeadsSeaRuleRecord.setId(IdUtil.getSnowflake().nextId());
        weLeadsSeaRuleRecord.setSeaId(data.getId());
        weLeadsSeaRuleRecord.setIsAutoRecovery(data.getIsAutoRecovery());
        weLeadsSeaRuleRecord.setFirst(data.getFirst());
        weLeadsSeaRuleRecord.setVersion(data.getVersion());
        weLeadsSeaRuleRecord.setCreateBy(sysUser.getUserName());
        weLeadsSeaRuleRecord.setCreateById(sysUser.getUserId());
        weLeadsSeaRuleRecord.setCreateTime(new Date());
        weLeadsSeaRuleRecordMapper.insert(weLeadsSeaRuleRecord);
    }


    /**
     * 获取公海可见范围数据
     *
     * @param deptRange 部门范围
     * @param postRange 岗位范围
     * @param userRange 用户范围
     * @param seaId     公海id
     * @return
     */
    private List<WeLeadsSeaVisibleRange> getWeLeadsSeaVisibleRanges(VisibleRange.DeptRange deptRange,
                                                                    VisibleRange.PostRange postRange,
                                                                    VisibleRange.UserRange userRange,
                                                                    Long seaId) {
        List<WeLeadsSeaVisibleRange> weLeadsSeaVisibleRanges = new ArrayList<>();
        //部门可见范围
        if (deptRange != null && deptRange.isSelect()) {
            List<Long> deptIds = deptRange.getDeptIds();
            //部门数据
            SysDeptQuery sysDeptQuery = new SysDeptQuery();
            sysDeptQuery.setDeptIds(deptIds);
            AjaxResult<List<SysDeptVo>> deptList = qwSysDeptClient.getListByDeptIds(sysDeptQuery);
            Map<Long, String> map = new HashMap<>(deptIds.size());
            if (deptList.getCode() == HttpStatus.SUCCESS) {
                List<SysDeptVo> data = deptList.getData();
                if (CollectionUtil.isEmpty(data)) {
                    throw new ServiceException("部门数据不存在！", HttpStatus.BAD_REQUEST);
                }
                map = deptList.getData().stream().collect(Collectors.toMap(SysDeptVo::getDeptId, SysDeptVo::getDeptName));
            }
            //遍历部门ID
            Map<Long, String> finalMap = map;
            deptIds.stream().forEach(i -> {
                WeLeadsSeaVisibleRange weLeadsSeaVisibleRange = new WeLeadsSeaVisibleRange();
                weLeadsSeaVisibleRange.setSeaId(seaId);
                weLeadsSeaVisibleRange.setType(VisibleRange.dept);
                weLeadsSeaVisibleRange.setDataId(i.toString());
                weLeadsSeaVisibleRange.setDataName(finalMap.get(i));
                weLeadsSeaVisibleRange.setIsAdmin(false);
                weLeadsSeaVisibleRange.setDelFlag(Constants.COMMON_STATE);
                weLeadsSeaVisibleRanges.add(weLeadsSeaVisibleRange);
            });
        }

        //岗位可见范围
        if (postRange != null && postRange.isSelect()) {
            List<String> posts = postRange.getPosts();
            //遍历岗位
            posts.stream().forEach(i -> {
                WeLeadsSeaVisibleRange weLeadsSeaVisibleRange = new WeLeadsSeaVisibleRange();
                weLeadsSeaVisibleRange.setSeaId(seaId);
                weLeadsSeaVisibleRange.setType(VisibleRange.post);
                weLeadsSeaVisibleRange.setDataName(i);
                weLeadsSeaVisibleRange.setIsAdmin(false);
                weLeadsSeaVisibleRange.setDelFlag(Constants.COMMON_STATE);
                weLeadsSeaVisibleRanges.add(weLeadsSeaVisibleRange);
            });
        }

        //用户可见范围
        if (userRange != null && userRange.isSelect()) {
            List<String> weUserIds = userRange.getWeUserIds();
            //员工数据
            Map<String, String> map = new HashMap<>(weUserIds.size());
            SysUserQuery sysUserQuery = new SysUserQuery();
            sysUserQuery.setWeUserIds(weUserIds);
            AjaxResult<List<SysUserVo>> result = qwSysUserClient.getUserListByWeUserIds(sysUserQuery);
            if (result.getCode() == HttpStatus.SUCCESS) {
                List<SysUserVo> data = result.getData();
                if (CollectionUtil.isEmpty(data)) {
                    throw new ServiceException("用户数据不存在！", HttpStatus.BAD_REQUEST);
                }
                map = data.stream().collect(Collectors.toMap(SysUserVo::getWeUserId, SysUserVo::getUserName));
            }
            //遍历企微员工ID
            Map<String, String> finalMap = map;
            weUserIds.stream().forEach(i -> {
                WeLeadsSeaVisibleRange weLeadsSeaVisibleRange = new WeLeadsSeaVisibleRange();
                weLeadsSeaVisibleRange.setSeaId(seaId);
                weLeadsSeaVisibleRange.setType(VisibleRange.user);
                weLeadsSeaVisibleRange.setDataId(i);
                weLeadsSeaVisibleRange.setDataName(finalMap.get(i));
                weLeadsSeaVisibleRange.setIsAdmin(false);
                weLeadsSeaVisibleRange.setDelFlag(Constants.COMMON_STATE);
                weLeadsSeaVisibleRanges.add(weLeadsSeaVisibleRange);
            });
        }
        return weLeadsSeaVisibleRanges;
    }

    /**
     * 判断回收规则是否改变
     * true规则变化 false规则未变
     *
     * @param oldData 旧数据
     * @param newData 新数据
     * @return
     */
    private boolean recoveryRuleIsChange(WeLeadsSea oldData, WeLeadsSea newData) {
        //判断回收类型是否变化
        if (!oldData.getIsAutoRecovery().equals(newData.getIsAutoRecovery())) {
            //变化了，返回true
            return true;
        } else {
            //回收类型未变化,并且是自动回收
            if (oldData.getIsAutoRecovery().equals(1)) {
                //判断回收规则是否变化
                if (!oldData.getFirst().equals(newData.getFirst())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean checkSeaById(Long seaId) {
        if (Objects.isNull(seaId)) {
            return false;
        }
        String weUserId = SecurityUtils.getLoginUser().getSysUser().getWeUserId();
        LambdaQueryWrapper<WeLeadsSeaVisibleRange> queryWrapper = Wrappers.lambdaQuery(WeLeadsSeaVisibleRange.class);
        queryWrapper.eq(WeLeadsSeaVisibleRange::getSeaId, seaId);
        queryWrapper.eq(WeLeadsSeaVisibleRange::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.eq(WeLeadsSeaVisibleRange::getDataId, weUserId);
        int count = weLeadsSeaVisibleRangeService.count(queryWrapper);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> getCounts(Long seaId) {
        log.info("公海池id:{}", seaId);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Map<String, Object> result = new HashMap<>(8);

        // 1、有效线索总量
        int leadsNum = weLeadsService.count(Wrappers.lambdaQuery(WeLeads.class).eq(WeLeads::getSeaId, seaId).eq(WeLeads::getDelFlag, Constants.COMMON_STATE));
        result.put("leadsNum", leadsNum);

        // 2、总跟进量
        int allFollowNum = weLeadsService.count(Wrappers.lambdaQuery(WeLeads.class).eq(WeLeads::getSeaId, seaId).eq(WeLeads::getLeadsStatus, LeadsStatusEnum.BE_FOLLOWING_UP.getCode()).eq(WeLeads::getDelFlag, Constants.COMMON_STATE));
        result.put("allFollowNum", allFollowNum);

        // 3、今日领取量
        QueryWrapper<WeLeadsFollowRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sea_id", seaId);
        queryWrapper.eq("record_status", 0);
        queryWrapper.eq("DATE_FORMAT(create_time, '%Y-%m-%d')", DateUtil.today());
        int todayReceiveNum = weLeadsFollowRecordMapper.selectCount(queryWrapper);
        result.put("todayReceiveNum", todayReceiveNum);

        // 4、较昨日领取量  DateUtil.yesterday().toDateStr()
        QueryWrapper<WeLeadsFollowRecord> query = Wrappers.query();
        query.in("sea_id", seaId).eq("DATE_FORMAT(create_time, '%Y-%m-%d')", DateUtil.yesterday().toDateStr()).eq("record_status", 0);
        int yesterdayReceiveNum1 = weLeadsFollowRecordMapper.selectCount(query);
        log.info("今日日期:{},今日领取线索量:{},昨日日期:{},昨日领取线索量:{}", DateUtil.today(), todayReceiveNum, DateUtil.yesterday().toDateStr(), yesterdayReceiveNum1);
        int yesterdayReceiveNum = todayReceiveNum - yesterdayReceiveNum1;
        result.put("yesterdayReceiveNum", yesterdayReceiveNum);

        // 5、今日有效跟进
        QueryWrapper<WeLeadsFollowRecord> query1 = Wrappers.query();
        query1.in("sea_id", seaId).eq("record_status", 3).eq("DATE_FORMAT(create_time, '%Y-%m-%d')", DateUtil.today());
        int todayFollowNum = weLeadsFollowRecordMapper.selectCount(query1);
        result.put("todayFollowNum", todayFollowNum);

        // 6、较昨日跟进
        QueryWrapper<WeLeadsFollowRecord> query2 = Wrappers.query();
        query2.in("sea_id", seaId).eq("record_status", 3).eq("DATE_FORMAT(create_time, '%Y-%m-%d')", DateUtil.yesterday().toDateStr());
        int yesterdayFollowNum1 = weLeadsFollowRecordMapper.selectCount(query2);
        log.info("今日日期:{},今日有效跟进量:{},昨日日期:{},昨日有效跟进量:{}", DateUtil.today(), todayFollowNum, DateUtil.yesterday().toDateStr(), yesterdayFollowNum1);
        int yesterdayFollowNum = todayFollowNum - yesterdayFollowNum1;
        result.put("yesterdayFollowNum", yesterdayFollowNum);
        double todayFollowRatio = 0;
        double yesterdayFollowRatio = 0;
        if (0 != allFollowNum) {
            //今日跟进率: 今日已产生跟进行为的客户数/当前公海池内处于跟进中状态的线索客户数
            todayFollowRatio = NumberUtil.div(todayFollowNum, allFollowNum);
            // 较昨日跟进率
            yesterdayFollowRatio = NumberUtil.div(yesterdayFollowNum1, allFollowNum);
            yesterdayFollowRatio = NumberUtil.sub(todayFollowRatio, yesterdayFollowRatio);
        }
        result.put("todayFollowRatio", todayFollowRatio);
        result.put("yesterdayFollowRatio", yesterdayFollowRatio);
        return result;
    }

    @Override
    public List<WeLeadsSeaTrendVO> seaLeadsTrend(Long seaId, String beginTime, String endTime, String weUserId) {
        if (seaId == null) {
            throw new ServiceException("公海池Id必填", HttpStatus.BAD_REQUEST);
        }
        //日期
        List<DateTime> dateTimes = DateUtil.rangeToList(DateUtil.parseDate(beginTime), DateUtil.parseDate(endTime), DateField.DAY_OF_YEAR);
        //数据
        List<WeLeadsFollowRecord> records = weLeadsFollowRecordMapper.seaLeadsTrend(seaId, beginTime, endTime, weUserId);

        List<WeLeadsSeaTrendVO> result = new ArrayList<>();
        for (DateTime dateTime : dateTimes) {
            WeLeadsSeaTrendVO weLeadsSeaTrendVO = new WeLeadsSeaTrendVO();
            weLeadsSeaTrendVO.setDateTime(dateTime);
            weLeadsSeaTrendVO.setTodayReceiveNum(records.stream().filter(i -> DateUtil.format(i.getCreateTime(), DatePattern.NORM_DATE_PATTERN).equals(dateTime.toDateStr()) && i.getRecordStatus().equals(0)).count());
            weLeadsSeaTrendVO.setTodayFollowNum(records.stream().filter(i -> DateUtil.format(i.getCreateTime(), DatePattern.NORM_DATE_PATTERN).equals(dateTime.toDateStr()) && i.getRecordStatus().equals(1)).count());
            result.add(weLeadsSeaTrendVO);
        }
        return result;
    }

    @Override
    public List<WeLeadsSeaEmployeeRankVO> getCustomerRank(Long seaId, String beginTime, String endTime) {
        if (seaId == null) {
            throw new ServiceException("公海池Id必填", HttpStatus.BAD_REQUEST);
        }
        List<WeLeadsSeaEmployeeRankVO> customerRank = weLeadsFollowRecordMapper.getCustomerRank(Long.valueOf(seaId), beginTime, endTime);
        return customerRank;
    }

    @Override
    public List<WeLeadsSeaDataDetailVO> getSeaDataDetail(Long seaId, String weUserId, List<DateTime> dateTimes) {
        if (seaId == null) {
            throw new ServiceException("公海池Id不能为null", HttpStatus.BAD_REQUEST);
        }
        ArrayList<WeLeadsSeaDataDetailVO> list = new ArrayList<>();
        for (DateTime vo : dateTimes) {
            list.add(getWeLeadsSeaDataDetailVO(seaId, weUserId, vo));
        }
        return list;
    }


    @Override
    public List<WeLeadsSeaDataDetailVO> getSeaDataDetail(Long seaId, String beginTime, String endTime, String weUserId) {
        if (seaId == null) {
            throw new ServiceException("公海池Id不能为null", HttpStatus.BAD_REQUEST);
        }
        ArrayList<WeLeadsSeaDataDetailVO> list = new ArrayList<>();
        List<DateTime> dateTimes = DateUtil.rangeToList(DateUtil.parseDate(beginTime), DateUtil.parseDate(endTime), DateField.DAY_OF_YEAR);
        for (DateTime vo : dateTimes) {
            list.add(getWeLeadsSeaDataDetailVO(seaId, weUserId, vo));
        }
        return list;
    }

    /**
     * 获取公海数据详情
     *
     * @param seaId    公海Id
     * @param weUserId 企微用户Id
     * @param vo       日期
     * @return {@link WeLeadsSeaDataDetailVO} 返回值
     * @author WangYX
     * @date 2023/08/01 18:37
     */
    private WeLeadsSeaDataDetailVO getWeLeadsSeaDataDetailVO(Long seaId, String weUserId, DateTime vo) {
        WeLeadsSeaDataDetailVO dataDetailVO = new WeLeadsSeaDataDetailVO();
        dataDetailVO.setDateTime(vo.toDateStr());
        // 1、总线索量
        QueryWrapper<WeLeads> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sea_id", seaId)
                .eq("del_flag", Constants.COMMON_STATE)
                .eq("DATE_FORMAT(create_time, '%Y-%m-%d')", vo.toDateStr())
                .eq(StrUtil.isNotBlank(weUserId), "we_user_id", weUserId);
        int totalNum = weLeadsMapper.selectCount(queryWrapper);
        dataDetailVO.setTotalNum(totalNum);

        // 2、总跟进量
        QueryWrapper<WeLeads> allFollowQuery = new QueryWrapper<>();
        allFollowQuery.eq("sea_id", seaId)
                .eq("leads_status", LeadsStatusEnum.BE_FOLLOWING_UP.getCode())
                .eq("del_flag", Constants.COMMON_STATE)
                .eq("DATE_FORMAT(create_time, '%Y-%m-%d')", vo.toDateStr())
                .eq(StrUtil.isNotBlank(weUserId), "we_user_id", weUserId);
        int allFollowNum = weLeadsMapper.selectCount(allFollowQuery);
        dataDetailVO.setAllFollowNum(allFollowNum);

        // 3、今日领取量
        List<WeLeadsFollowRecord> list1 = weLeadsFollowRecordMapper.list(seaId, vo.toDateStr(), weUserId, 2);
        dataDetailVO.setTodayReceiveNum(list1.size());

        // 4、今日有效跟进
        List<WeLeadsFollowRecord> list2 = weLeadsFollowRecordMapper.list(seaId, vo.toDateStr(), weUserId, 3);
        dataDetailVO.setTodayFollowNum(list2.size());

        //5、今日跟进率: 今日已产生跟进行为的客户数/当前公海池内处于跟进中状态的线索客户数
        double todayFollowRatio = 0;
        if (0 != allFollowNum) {
            todayFollowRatio = NumberUtil.div(list2.size(), allFollowNum);
        }
        dataDetailVO.setTodayFollowRatio(todayFollowRatio);
        return dataDetailVO;
    }

}
