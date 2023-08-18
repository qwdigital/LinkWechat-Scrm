package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.LeadsCenterConstants;
import com.linkwechat.common.constant.MessageConstants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.enums.leads.leads.LeadsStatusEnum;
import com.linkwechat.common.enums.leads.record.FollowBackModeEnum;
import com.linkwechat.common.enums.leads.record.ImportSourceTypeEnum;
import com.linkwechat.common.enums.leads.template.CanEditEnum;
import com.linkwechat.common.enums.leads.template.DataAttrEnum;
import com.linkwechat.common.enums.leads.template.DatetimeTypeEnum;
import com.linkwechat.common.enums.leads.template.TableEntryAttrEnum;
import com.linkwechat.common.enums.message.MessageTypeEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.*;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
import com.linkwechat.domain.leads.leads.query.*;
import com.linkwechat.domain.leads.leads.vo.Properties;
import com.linkwechat.domain.leads.leads.vo.WeLeadsFollowerNumVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsImportResultVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsVO;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecord;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSea;
import com.linkwechat.domain.leads.sea.query.VisibleRange;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaBaseSettingsVO;
import com.linkwechat.domain.leads.template.entity.WeLeadsTemplateSettings;
import com.linkwechat.domain.leads.template.vo.WeLeadsTemplateSettingsVO;
import com.linkwechat.domain.leads.template.vo.WeLeadsTemplateTableEntryContentVO;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.handler.*;
import com.linkwechat.mapper.*;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 线索 服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:44
 */
@Slf4j
@Service
public class WeLeadsServiceImpl extends ServiceImpl<WeLeadsMapper, WeLeads> implements IWeLeadsService {

    @Resource
    private WeLeadsMapper weLeadsMapper;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private IWeTasksService weTasksService;
    @Resource
    private QwSysUserClient qwSysUserClient;
    @Resource
    private WeCustomerMapper weCustomerMapper;
    @Resource
    private WeLeadsSeaMapper weLeadsSeaMapper;
    @Resource
    private WeLeadsFollowerMapper weLeadsFollowerMapper;
    @Resource
    private IWeLeadsFollowerService weLeadsFollowerService;
    @Resource
    private WeLeadsFollowRecordMapper weLeadsFollowRecordMapper;
    @Resource
    private IWeLeadsAutoRecoveryService weLeadsAutoRecoveryService;
    @Resource
    private IWeLeadsImportRecordService weLeadsImportRecordService;
    @Resource
    private IWeMessageNotificationService weMessageNotificationService;
    @Resource
    private WeLeadsTemplateSettingsMapper weLeadsTemplateSettingsMapper;
    @Resource
    private IWeLeadsSeaBaseSettingsService weLeadsSeaBaseSettingsService;
    @Resource
    private IWeLeadsFollowRecordContentService weLeadsFollowRecordContentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiveLeads(Long leadsId) {
        //判断线索是否存在
        WeLeads weLeads = this.getById(leadsId);
        if (BeanUtil.isEmpty(weLeads)) {
            throw new ServiceException("线索不存在！");
        }
        //判断线索是否已分配
        if (weLeads.getLeadsStatus().equals(LeadsStatusEnum.BE_FOLLOWING_UP.getCode())
                || weLeads.getLeadsStatus().equals(LeadsStatusEnum.VISIT.getCode())) {
            throw new ServiceException("线索已分配", HttpStatus.CONFLICT);
        }
        //检查领取数量是否超过基础配置
        checkReceiveNum(weLeads.getSeaId());

        boolean flag = false;
        WeLeadsFollower weLeadsFollower = null;
        String key = LeadsCenterConstants.LEADS + leadsId;
        RLock lock = redissonClient.getLock(key);
        try {
            //尝试拿锁5秒后停止重试，未拿到锁返回false，拿到返回true，具有看门狗机制30s
            boolean b = lock.tryLock(5, TimeUnit.SECONDS);
            if (b) {
                //1.再次判断线索所属状态
                WeLeads leads = this.getById(leadsId);
                if (!(leads.getLeadsStatus().equals(LeadsStatusEnum.WAIT_FOR_DISTRIBUTION.getCode())
                        || leads.getLeadsStatus().equals(LeadsStatusEnum.RETURNED.getCode()))) {
                    throw new ServiceException("线索已分配", HttpStatus.CONFLICT);
                }
                //执行领取操作
                weLeadsFollower = receiveLeads(weLeads);
                flag = true;
            }
        } catch (InterruptedException e) {
            log.error("领取线索时，尝试获取分布式锁失败！");
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        if (flag) {
            //添加跟进记录
            Long recordId = addFollowRecord(weLeads.getId(), weLeads.getSeaId(), weLeadsFollower.getId(), 0);

            //添加跟进记录内容
            weLeadsFollowRecordContentService.memberToReceive(recordId, weLeads.getSeaId());

            //自动回收
            weLeadsAutoRecoveryService.save(weLeads.getId(), SecurityUtils.getLoginUser().getSysUser().getUserId(), weLeads.getSeaId());

            //添加线索长时间待跟进
            weTasksService.addLeadsLongTimeNotFollowUp(weLeads.getId(), weLeads.getName(), SecurityUtils.getLoginUser().getSysUser().getUserId(),
                    SecurityUtils.getLoginUser().getSysUser().getWeUserId(), weLeads.getSeaId());
        }
    }

    /**
     * 主动领取
     *
     * @author WangYX
     * @date 2023/07/12 11:18
     * @version 1.0.0
     */
    private WeLeadsFollower receiveLeads(WeLeads weLeads) {
        //修改线索状态和添加当前跟进人信息
        updateLeadsStatusAndAddCurrentFollower(weLeads.getId(),
                SecurityUtils.getLoginUser().getSysUser().getUserId(),
                SecurityUtils.getLoginUser().getSysUser().getWeUserId(),
                SecurityUtils.getLoginUser().getSysUser().getUserName());
        //添加线索跟进人
        WeLeadsFollower weLeadsFollower = addLeadsFollower(weLeads.getId(), weLeads.getSeaId());
        return weLeadsFollower;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String allocation(WeLeadsAllocationRequest request) {
        //待分配线索
        List<Long> leads = getLeads(request);
        //待分配员工
        List<SysUser> users = getUsers(request);
        //平均分配
        List<WeLeadsFollower> list = this.averageAllocation(leads, users);
        return this.executeAllocation(list);
    }

    @Override
    public void export(WeLeadsExportRequest request) {
        //获取公海
        if (request.getSeaId() == null) {
            throw new ServiceException("公海Id必填");
        }
        WeLeadsSea weLeadsSea = checkExistSea(request.getSeaId());
        //获取线索
        List<WeLeads> weLeads;
        if (request.getAll()) {
            LambdaQueryWrapper<WeLeads> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(WeLeads::getSeaId, request.getSeaId());
            wrapper.eq(WeLeads::getDelFlag, Constants.COMMON_STATE);
            wrapper.like(StrUtil.isNotBlank(request.getName()), WeLeads::getName, StringUtils.queryReplace(request.getName()));
            wrapper.like(StrUtil.isNotBlank(request.getPhone()), WeLeads::getPhone, StringUtils.queryReplace(request.getName()));
            wrapper.eq(request.getLeadsStatus() != null, WeLeads::getLeadsStatus, request.getLeadsStatus());
            wrapper.notIn(CollectionUtil.isNotEmpty(request.getUnLeadsIds()), WeLeads::getId, request.getUnLeadsIds());
            weLeads = weLeadsMapper.selectList(wrapper);
        } else {
            List<Long> leadsIds = request.getLeadsIds();
            weLeads = weLeadsMapper.selectBatchIds(leadsIds);
        }
        //导出表格的头部数据
        LambdaQueryWrapper<WeLeadsTemplateSettings> wrapper = Wrappers.lambdaQuery(WeLeadsTemplateSettings.class);
        wrapper.eq(WeLeadsTemplateSettings::getDelFlag, Constants.COMMON_STATE);
        wrapper.orderByAsc(WeLeadsTemplateSettings::getRank);
        List<WeLeadsTemplateSettings> settingsList = weLeadsTemplateSettingsMapper.selectList(wrapper);

        List<String> headList = new ArrayList<>();
        settingsList.stream().forEach(i -> headList.add(i.getTableEntryName()));

        HttpServletResponse response = ServletUtils.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType(LeadsCenterConstants.XLSX_FILE_HEAD);
            ServletOutputStream outputStream = response.getOutputStream();
            String dateStr = DateUtil.today();
            String fileName = dateStr + " " + weLeadsSea.getName() + " " + "线索" + LeadsCenterConstants.XLSX_FILE_EXTENSION;
            String encode = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            /* 解决axios无法获取响应头headers的Content-Disposition */
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + encode);

            List<List<String>> head = this.head(settingsList);
            List<List<Object>> dataList = this.dataList(settingsList, this.getRows(weLeads, settingsList));
            EasyExcel.write(response.getOutputStream()).head(head).sheet("线索转接").doWrite(dataList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importTemplate() {
        List<WeLeadsTemplateSettingsVO> settings = weLeadsTemplateSettingsMapper.queryWithTableEntryContent();
        //日期格式
        Map<Integer, Integer> dateMap = new HashMap<>();
        //下拉框
        Map<Integer, String[]> dropDownMap = new HashMap<>();
        for (int i = 0; i < settings.size(); i++) {
            WeLeadsTemplateSettingsVO leadsTemplateSettings = settings.get(i);
            //下拉框
            Integer tableEntryAttr = leadsTemplateSettings.getTableEntryAttr();
            if (tableEntryAttr.intValue() == TableEntryAttrEnum.COMBOBOX.getCode()) {
                List<WeLeadsTemplateTableEntryContentVO> tableEntryContent = leadsTemplateSettings.getTableEntryContent();
                if (CollectionUtil.isNotEmpty(tableEntryContent)) {
                    List<String> collects = tableEntryContent.stream().map(WeLeadsTemplateTableEntryContentVO::getContent).collect(Collectors.toList());
                    dropDownMap.put(i, ArrayUtil.toArray(collects, String.class));
                }
            }
            //日期
            Integer dataAttr = leadsTemplateSettings.getDataAttr();
            if (dataAttr.intValue() == DataAttrEnum.DATE.getCode()) {
                dateMap.put(i, leadsTemplateSettings.getDatetimeType());
            }
        }
        List<List<Object>> dataList = new ArrayList<>();
        HttpServletResponse response = ServletUtils.getResponse();
        try {
            String fileName = DateUtils.getTime() + " 线索导入模板.xlsx";
            String encode = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            /*解决axios无法获取响应头headers的Content-Disposition*/
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + encode);
            response.setContentType(LeadsCenterConstants.XLSX_FILE_HEAD);
            response.setCharacterEncoding("utf-8");
            ServletOutputStream outputStream = response.getOutputStream();
            List<String> head = settings.stream().map(i -> {
                if (i.getRequired().equals(1)) {
                    return "*" + i.getTableEntryName();
                }
                return i.getTableEntryName();
            }).collect(Collectors.toList());

            ExcelWriterBuilder builder = EasyExcelFactory.write();
            EasyExcelUtils.defaultExcelStyle(builder);
            builder.relativeHeadRowIndex(1);
            builder.registerWriteHandler(new WeLeadsExportHeadsWriteHandler(head.size() - 1));
            builder.registerWriteHandler(new SpinnerWriteHandler(dropDownMap));
            builder.registerWriteHandler(new WeLeasExportDateHandler(dateMap));
            builder.registerWriteHandler(new DataValidityWriteHandler(settings));
            ExcelWriter excelWriter = builder.excelType(ExcelTypeEnum.XLSX).file(outputStream).autoCloseStream(true).build();
            WriteSheet sheet = new WriteSheet();
            sheet.setSheetNo(0);
            sheet.setSheetName("sheet1");
            List<List<String>> headList = EasyExcelUtils.head(head);
            sheet.setHead(headList);
            excelWriter.write(dataList, sheet);
            excelWriter.finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public WeLeadsImportResultVO batchImportFromExcel(MultipartFile file, Long seaId) throws IOException {
        //解决中文乱码问题
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        //校验是否存在此公海
        checkExistSea(seaId);

        InputStream inputStream = file.getInputStream();
        List<WeLeadsTemplateSettingsVO> settingsList = weLeadsTemplateSettingsMapper.queryWithTableEntryContent();
        Map<String, WeLeadsTemplateSettingsVO> settingsMap = settingsList.stream().collect(Collectors.toMap(WeLeadsTemplateSettingsVO::getTableEntryName, Function.identity()));

        WeLeadsImportDataListener weLeadsImportDataListener = new WeLeadsImportDataListener(seaId, originalFilename, this, settingsMap, weLeadsImportRecordService);
        EasyExcel.read(inputStream, weLeadsImportDataListener).sheet().headRowNumber(2).doRead();
        WeLeadsImportResultVO result = new WeLeadsImportResultVO();
        result.setSucceedCount(weLeadsImportDataListener.successNum);
        result.setFailCount(weLeadsImportDataListener.failNum);
        result.setRepetitionCount(weLeadsImportDataListener.repetitionNum);
        result.buildFeedbackMessage();
        return result;
    }

    /**
     * 获取等待分配的线索
     *
     * @param request 请求参数
     * @return {@link List<Long>}
     * @author WangYX
     * @date 2023/07/12 17:07
     */
    private List<Long> getLeads(WeLeadsAllocationRequest request) {
        List<Long> leadIds;
        if (request.isAll()) {
            if (request.getLeadsStatus() != null) {
                if (LeadsStatusEnum.BE_FOLLOWING_UP.getCode().equals(request.getLeadsStatus()) || LeadsStatusEnum.VISIT.getCode().equals(request.getLeadsStatus())) {
                    throw new ServiceException("线索状态必须待分配或者已退回！", HttpStatus.BAD_REQUEST);
                }
            }
            Long seaId = Optional.ofNullable(request.getSeaId()).orElseThrow(() -> new ServiceException("公海Id必填"));
            LambdaQueryWrapper<WeLeads> queryWrapper = Wrappers.lambdaQuery(WeLeads.class);
            queryWrapper.eq(WeLeads::getSeaId, seaId);
            queryWrapper.eq(WeLeads::getDelFlag, Constants.COMMON_STATE);
            //批量化分配必须是待分配或者已退回状态的数据
            queryWrapper.eq(WeLeads::getLeadsStatus, request.getLeadsStatus());
            queryWrapper.like(StrUtil.isNotBlank(request.getPhone()), WeLeads::getPhone, StringUtils.queryReplace(request.getPhone()));
            queryWrapper.like(StrUtil.isNotBlank(request.getName()), WeLeads::getName, StringUtils.queryReplace(request.getName()));
            List<WeLeads> weLeads = this.list(queryWrapper);
            List<WeLeads> list = Optional.ofNullable(weLeads).orElseThrow(() -> new ServiceException("公海中不存在被选中的线索"));
            leadIds = list.stream().map(WeLeads::getId).collect(Collectors.toList());
        } else {
            leadIds = Optional.ofNullable(request.getLeadsIds()).orElseThrow(() -> new ServiceException("线索Id集合必填"));
        }
        return leadIds;
    }


    /**
     * 获取员工数据
     *
     * @param request 线索分配参数
     * @return {@link List<SysUser>}
     * @author WangYX
     * @date 2023/07/12 17:48
     */
    private List<SysUser> getUsers(WeLeadsAllocationRequest request) {
        //获取员工
        VisibleRange.DeptRange depts = request.getDepts();
        VisibleRange.PostRange posts = request.getPosts();
        VisibleRange.UserRange users = request.getWeUserIds();

        //部门
        String deptIds = null;
        if (depts != null && depts.isSelect()) {
            List<Long> deptIdList = Optional.ofNullable(depts.getDeptIds()).orElseThrow(() -> new ServiceException("部门数据未选"));
            deptIds = StringUtils.join(deptIdList, ",");
        }
        //岗位
        String postIds = null;
        if (posts != null && posts.isSelect()) {
            List<String> postList = Optional.ofNullable(posts.getPosts()).orElseThrow(() -> new ServiceException("岗位数据未选"));
            postIds = StringUtils.join(postList, ",");
        }
        //员工
        String weUserIds = null;
        if (users != null && users.isSelect()) {
            List<String> list = Optional.ofNullable(users.getWeUserIds()).orElseThrow(() -> new ServiceException("用户数据未选择"));
            weUserIds = StringUtils.join(list, ",");
        }
        List<SysUser> sysUsers = getRemoteUser(weUserIds, postIds, deptIds);

        if (sysUsers != null && sysUsers.size() <= 0) {
            throw new ServiceException("请选择有效成员！（不可分配给管理员或当前所选岗位/部门无人员");
        }
        sysUsers = sysUsers.stream().distinct().sorted(Comparator.comparing(SysUser::getUserId)).collect(Collectors.toList());
        return sysUsers;
    }

    /**
     * 获取auth服务的用户
     *
     * @param weUserIds 企微员工Id
     * @param postIds   岗位
     * @param deptIds   部门Id
     * @return {@link List<SysUser>}
     * @author WangYX
     * @date 2023/07/12 17:47
     */
    private List<SysUser> getRemoteUser(String weUserIds, String postIds, String deptIds) {
        List<SysUser> sysUsers = new ArrayList<>();
        AjaxResult<List<SysUser>> allSysUser = qwSysUserClient.findAllSysUser(weUserIds, postIds, deptIds);
        if (allSysUser.getCode() == HttpStatus.SUCCESS) {
            sysUsers = Optional.ofNullable(allSysUser.getData()).orElseThrow(() -> new ServiceException("未选中员工"));
        }
        return sysUsers;
    }

    /**
     * 平均分配
     *
     * @param leads 线索id集合
     * @param users 员工集合
     * @return {@link List<WeLeadsFollower>}
     * @author WangYX
     * @date 2023/07/13 9:35
     */
    private List<WeLeadsFollower> averageAllocation(List<Long> leads, List<SysUser> users) {
        //获取公海
        WeLeads weLeads = weLeadsMapper.selectById(leads.get(0));
        if (BeanUtil.isEmpty(weLeads)) {
            throw new ServiceException("线索Id异常！");
        }
        Long seaId = weLeads.getSeaId();
        //分配
        List<WeLeadsFollower> result = new ArrayList<>();
        int i = 0;
        for (Long leadsId : leads) {
            i = i % users.size();
            SysUser sysUser = users.get(i);
            result.add(WeLeadsFollower.builder()
                    .id(IdUtil.getSnowflake().nextId())
                    .leadsId(leadsId)
                    .followerId(sysUser.getUserId())
                    .followerWeUserId(sysUser.getWeUserId())
                    .followerName(sysUser.getUserName())
                    .deptId(sysUser.getDeptId())
                    .deptName(sysUser.getDeptName())
                    .getType(0)
                    .followerStatus(LeadsStatusEnum.BE_FOLLOWING_UP.getCode())
                    .assignerId(SecurityUtils.getLoginUser().getSysUser().getUserId())
                    .assignerName(SecurityUtils.getLoginUser().getSysUser().getUserName())
                    .followerStartTime(new Date())
                    .isCurrentFollower(1)
                    .latest(1)
                    .seaId(seaId)
                    .build());
            i++;
        }
        return result;
    }

    /**
     * 执行分配
     *
     * @param vos 线索分配数据
     * @author WangYX
     * @date 2023/05/18 14:41
     */
    private String executeAllocation(List<WeLeadsFollower> vos) {
        int success = 0, fail = 0;
        for (WeLeadsFollower vo : vos) {
            WeLeads leads = null;
            boolean flag = false;
            String key = LeadsCenterConstants.LEADS + vo.getLeadsId();
            RLock lock = redissonClient.getLock(key);
            try {
                //尝试拿锁5秒后停止重试，未拿到锁返回false，拿到返回true，具有看门狗机制30s
                boolean b = lock.tryLock(5, TimeUnit.SECONDS);
                if (b) {
                    //再次判断线索所属状态
                    leads = this.getById(vo.getLeadsId());
                    if (leads.getLeadsStatus().equals(LeadsStatusEnum.BE_FOLLOWING_UP.getCode())
                            || leads.getLeadsStatus().equals(LeadsStatusEnum.VISIT.getCode())) {
                        fail++;
                        continue;
                    }
                    executeAllocation(vo);
                    flag = true;
                    success++;
                }
            } catch (InterruptedException e) {
                log.error("领取线索时，尝试获取分布式锁失败！");
                e.printStackTrace();
            } finally {
                //检查锁是否被当前线程持有，是返回ture，否返回false
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
            if (flag) {
                //添加跟进记录
                Long recordId = addFollowRecord(vo.getLeadsId(), vo.getSeaId(), vo.getId(), 0);
                //添加跟进记录内容
                weLeadsFollowRecordContentService.adminAllocation(recordId, SecurityUtils.getLoginUser().getSysUser().getUserName());
                //自动回收
                weLeadsAutoRecoveryService.save(vo.getId(), vo.getFollowerId(), vo.getSeaId());
                //消息通知
                weMessageNotificationService.saveAndSend(MessageTypeEnum.LEADS.getType(), MessageConstants.LEADS_ALLOCATION, leads.getName());
                //添加线索长时间待跟进
                weTasksService.addLeadsLongTimeNotFollowUp(leads.getId(), leads.getName(), vo.getFollowerId(), vo.getFollowerName(), leads.getSeaId());
            }
        }
        return "已成功分配 " + success + " 条线索，有 " + fail + " 条线索状态有更新未能成功分配";
    }

    /**
     * 执行分配
     *
     * @param vo 线索跟进人
     * @author WangYX
     * @date 2023/07/13 11:15
     */
    private void executeAllocation(WeLeadsFollower vo) {
        //修改线索状态和添加当前跟进人信息
        updateLeadsStatusAndAddCurrentFollower(vo.getLeadsId(), vo.getFollowerId(), vo.getFollowerWeUserId(), vo.getFollowerName());

        //添加线索跟进人
        //更新上次最新跟进人为否
        WeLeadsFollower one = weLeadsFollowerService.getOne(Wrappers.lambdaQuery(WeLeadsFollower.class).eq(WeLeadsFollower::getLeadsId, vo.getLeadsId()).eq(WeLeadsFollower::getLatest, 1));
        if (BeanUtil.isNotEmpty(one)) {
            one.setLatest(0);
            weLeadsFollowerService.updateById(one);
        }
        weLeadsFollowerService.save(vo);
    }


    /**
     * 添加线索跟进人
     *
     * @param leadsId 线索Id
     * @return {@link WeLeadsFollower}
     * @author WangYX
     * @date 2023/07/18 10:27
     */
    private WeLeadsFollower addLeadsFollower(Long leadsId, Long seaId) {
        //更新最新跟进人为否
        WeLeadsFollower one = weLeadsFollowerService.getOne(Wrappers.lambdaQuery(WeLeadsFollower.class).eq(WeLeadsFollower::getLeadsId, leadsId).eq(WeLeadsFollower::getLatest, 1));
        if (BeanUtil.isNotEmpty(one)) {
            one.setLatest(0);
            weLeadsFollowerService.updateById(one);
        }

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        //添加跟进
        WeLeadsFollower weLeadsFollower = new WeLeadsFollower();
        weLeadsFollower.setId(IdUtil.getSnowflake().nextId());
        weLeadsFollower.setLeadsId(leadsId);
        weLeadsFollower.setFollowerId(SecurityUtils.getLoginUser().getSysUser().getUserId());
        weLeadsFollower.setFollowerWeUserId(SecurityUtils.getLoginUser().getSysUser().getWeUserId());
        weLeadsFollower.setFollowerName(SecurityUtils.getLoginUser().getSysUser().getUserName());
        weLeadsFollower.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
        weLeadsFollower.setDeptName(SecurityUtils.getLoginUser().getSysUser().getDeptName());
        weLeadsFollower.setGetType(1);
        weLeadsFollower.setFollowerStatus(1);
        weLeadsFollower.setFollowerStartTime(new Date());
        weLeadsFollower.setIsCurrentFollower(1);
        weLeadsFollower.setLatest(1);
        weLeadsFollower.setSeaId(seaId);
        weLeadsFollowerService.save(weLeadsFollower);
        return weLeadsFollower;
    }

    /**
     * 添加跟进记录
     *
     * @param leadsId      线索Id
     * @param seaId        公海Id
     * @param followUserId 线索跟进人记录表Id
     * @param recordStatus 记录状态 0已领取 1跟进中 2已转化 3已退回
     * @author WangYX
     * @date 2023/07/13 11:00
     */
    private Long addFollowRecord(Long leadsId, Long seaId, Long followUserId, Integer recordStatus) {
        WeLeadsFollowRecord record = new WeLeadsFollowRecord();
        record.setId(IdUtil.getSnowflake().nextId());
        record.setWeLeadsId(leadsId);
        record.setSeaId(seaId);
        record.setFollowUserId(followUserId);
        record.setRecordStatus(recordStatus);
        record.setCreateTime(new Date());
        weLeadsFollowRecordMapper.insert(record);
        return record.getId();
    }

    /**
     * 修改线索状态和添加当前跟进人
     *
     * @param leadsId 线索Id
     * @author WangYX
     * @date 2023/07/13 10:50
     */
    private void updateLeadsStatusAndAddCurrentFollower(Long leadsId, Long followerId, String weUserId, String userName) {
        LambdaUpdateWrapper<WeLeads> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(WeLeads::getId, leadsId);
        wrapper.set(WeLeads::getLeadsStatus, LeadsStatusEnum.BE_FOLLOWING_UP.getCode());
        wrapper.set(WeLeads::getFollowerId, followerId);
        wrapper.set(WeLeads::getWeUserId, weUserId);
        wrapper.set(WeLeads::getFollowerName, userName);
        this.update(wrapper);
    }

    /**
     * 检查领取数量是否超过基础配置
     *
     * @param seaId 公海Id
     * @author WangYX
     * @date 2023/07/11 17:51
     */
    private void checkReceiveNum(Long seaId) {
        //公海基础配置
        WeLeadsSeaBaseSettingsVO weLeadsSeaBaseSettingsVO = weLeadsSeaBaseSettingsService.queryBaseSetting();
        if (BeanUtil.isEmpty(weLeadsSeaBaseSettingsVO)) {
            throw new ServiceException("公海基础配置未设置！");
        }

        WeLeadsFollowerNumVO leadsFollowerNum = weLeadsFollowerService.getLeadsFollowerNum(seaId);
        if (leadsFollowerNum.getMaxClaim() >= weLeadsSeaBaseSettingsVO.getMaxClaim()) {
            throw new ServiceException("今日领取数量已超过员工每日领取上限");
        }
        if (leadsFollowerNum.getStockMaxClaim() >= weLeadsSeaBaseSettingsVO.getStockMaxClaim()) {
            throw new ServiceException("累计领取数量超过员工客户存量上限");
        }
    }

    /**
     * 根据公海Id获取公海数据
     *
     * @param seaId 公海Id
     * @return {@link WeLeadsSea}
     * @author WangYX
     * @date 2023/07/11 15:16
     */
    private WeLeadsSea checkExistSea(Long seaId) {
        WeLeadsSea weLeadsSea = weLeadsSeaMapper.selectById(seaId);
        boolean exist = false;
        if (BeanUtil.isNotEmpty(weLeadsSea)) {
            if (weLeadsSea.getDelFlag().equals(Constants.COMMON_STATE)) {
                exist = true;
            }
        }
        if (!exist) {
            throw new ServiceException("不存在id为" + seaId + "的公海", HttpStatus.BAD_REQUEST);
        }
        return weLeadsSea;
    }

    /**
     * excel表格导出的头部
     *
     * @param settings 模板数据
     * @return {@link List<List<String>>}
     * @author WangYX
     * @date 2023/07/13 14:56
     */
    private List<List<String>> head(List<WeLeadsTemplateSettings> settings) {
        List<List<String>> list = ListUtils.newArrayList();
        for (WeLeadsTemplateSettings head : settings) {
            List<String> head0 = ListUtils.newArrayList();
            head0.add(head.getTableEntryName());
            list.add(head0);
        }
        return list;
    }

    /**
     * excel表格导出的数据
     *
     * @param settings 模板数据
     * @param rows     行数据
     * @return {@link List<List<Object>>}
     * @author WangYX
     * @date 2023/07/13 14:57
     */
    private List<List<Object>> dataList(List<WeLeadsTemplateSettings> settings, List<Map<String, Object>> rows) {
        List<List<Object>> list = ListUtils.newArrayList();
        for (Map<String, Object> row : rows) {
            List<Object> data = ListUtils.newArrayList();
            for (WeLeadsTemplateSettings head : settings) {
                data.add(row.get(head.getTableEntryId()));
            }
            list.add(data);
        }
        return list;
    }

    /**
     * 获取返回的行数据
     *
     * @param list         线索数据
     * @param settingsList 模板数据
     * @return {@link List< Map<String,Object>>}
     * @author WangYX
     * @date 2023/07/13 14:52
     */
    private List<Map<String, Object>> getRows(List<WeLeads> list, List<WeLeadsTemplateSettings> settingsList) {
        List<Map<String, Object>> result = new ArrayList<>();
        //可编辑的表头
        List<WeLeadsTemplateSettings> allows = settingsList.stream().filter(s -> s.getCanEdit().equals(CanEditEnum.ALLOW.getCode())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(list)) {
            for (WeLeads item : list) {
                Map map = BeanUtil.copyProperties(item, Map.class);
                map.remove("properties");
                //自定义数据处理
                String properties = item.getProperties();
                Optional.ofNullable(properties).ifPresent(i -> {
                    List<Properties> propertiesList = JSONObject.parseArray(i, Properties.class);
                    for (WeLeadsTemplateSettings allow : allows) {
                        Optional<Properties> first = propertiesList.stream().filter(o -> allow.getId().equals(o.getId())).findFirst();
                        if (first.isPresent()) {
                            //是否日期
                            if (allow.getDataAttr().equals(DataAttrEnum.DATE.getCode())) {
                                String value = first.get().getValue();
                                if (StrUtil.isNotBlank(value)) {
                                    if (allow.getDatetimeType().equals(DatetimeTypeEnum.DATE.getCode())) {
                                        //日期
                                        String format = DateUtil.format(DateUtil.date(Long.valueOf(value)), DatetimeTypeEnum.DATE.getFormat());
                                        map.put(allow.getTableEntryId(), format);
                                    } else {
                                        //日期+时间
                                        String format = DateUtil.format(DateUtil.date(Long.valueOf(value)), DatetimeTypeEnum.DATETIME.getFormat());
                                        map.put(allow.getTableEntryId(), format);
                                    }
                                } else {
                                    map.put(allow.getTableEntryId(), value);
                                }
                            } else {
                                map.put(allow.getTableEntryId(), first.get().getValue());
                            }
                        }
                    }
                });
                result.add(map);
            }
        }
        return result;
    }

    @Override
    public List<WeLeads> myFollowList(String name, Integer status) {
        LambdaQueryWrapper<WeLeads> queryWrapper = Wrappers.lambdaQuery(WeLeads.class);
        queryWrapper.select(WeLeads::getId, WeLeads::getLeadsStatus, WeLeads::getName, WeLeads::getUpdateTime, WeLeads::getLabelsIds, WeLeads::getSex);
        queryWrapper.like(StrUtil.isNotBlank(name), WeLeads::getName, StringUtils.queryReplace(name));
        queryWrapper.eq(WeLeads::getLeadsStatus, status);
        queryWrapper.eq(WeLeads::getFollowerId, SecurityUtils.getLoginUser().getSysUser().getUserId());
        return weLeadsMapper.selectList(queryWrapper);
    }

    @Override
    public List<WeLeadsVO> seaLeadsList(String name, Long seaId) {
        return this.baseMapper.seaLeadsList(name, seaId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userReturn(WeLeadsUserReturnRequest request) {
        //
        WeLeads weLeads = weLeadsMapper.selectById(request.getLeadsId());
        if (BeanUtil.isEmpty(weLeads)) {
            return;
        }
        if (!weLeads.getLeadsStatus().equals(LeadsStatusEnum.BE_FOLLOWING_UP.getCode())) {
            throw new ServiceException("线索不处于跟进中，无法退回");
        }
        if (!weLeads.getFollowerId().equals(SecurityUtils.getLoginUser().getSysUser().getUserId())) {
            throw new ServiceException("您不是线索当前跟进人，无法退回线索");
        }
        //
        Long followerUserId = null;
        String key = LeadsCenterConstants.LEADS + request.getLeadsId();
        RLock lock = redissonClient.getLock(key);
        try {
            //尝试拿锁5秒后停止重试，未拿到锁返回false，拿到返回true，具有看门狗机制30s
            boolean b = lock.tryLock(5, TimeUnit.SECONDS);
            if (b) {
                //1.再次判断线索所属状态
                WeLeads leads = this.getById(request.getLeadsId());
                if (!leads.getLeadsStatus().equals(LeadsStatusEnum.BE_FOLLOWING_UP.getCode())) {
                    throw new ServiceException("线索不处于跟进中，无法退回");
                }
                //执行退回操作
                followerUserId = exeUserReturn(leads, request);
            }
        } catch (InterruptedException e) {
            log.error("退回线索时，尝试获取分布式锁失败！");
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        //添加退回跟进记录
        Long recordId = this.addFollowRecord(request.getLeadsId(), weLeads.getSeaId(), followerUserId, 3);
        //添加退回跟进记录内容
        weLeadsFollowRecordContentService.userReturn(recordId, request.getRemark(), request.getSeaId());
        //取消自动回收
        weLeadsAutoRecoveryService.cancel(weLeads.getId(), SecurityUtils.getLoginUser().getSysUser().getUserId());
    }

    /**
     * 执行退回操作
     *
     * @param weLeads 线索信息
     * @param request 请求参数
     * @return
     * @author WangYX
     * @date 2023/07/18 11:49
     */
    private Long exeUserReturn(WeLeads weLeads, WeLeadsUserReturnRequest request) {
        //修改线索状态
        LambdaUpdateWrapper<WeLeads> updateWrapper = Wrappers.lambdaUpdate(WeLeads.class);
        updateWrapper.eq(WeLeads::getId, request.getLeadsId());
        updateWrapper.set(WeLeads::getSeaId, request.getSeaId());
        updateWrapper.set(WeLeads::getRecoveryTimes, weLeads.getRecoveryTimes() + 1);
        updateWrapper.set(WeLeads::getLeadsStatus, LeadsStatusEnum.RETURNED.getCode());
//        updateWrapper.set(WeLeads::getFollowerId, null);
//        updateWrapper.set(WeLeads::getWeUserId, null);
//        updateWrapper.set(WeLeads::getFollowerName, null);
//        updateWrapper.set(WeLeads::getDeptId, null);
        this.update(updateWrapper);

        //修改跟进人状态
        LambdaQueryWrapper<WeLeadsFollower> queryWrapper = Wrappers.lambdaQuery(WeLeadsFollower.class);
        queryWrapper.eq(WeLeadsFollower::getLeadsId, weLeads.getId());
        queryWrapper.eq(WeLeadsFollower::getFollowerStatus, 1);
        queryWrapper.eq(WeLeadsFollower::getFollowerId, SecurityUtils.getLoginUser().getSysUser().getUserId());
        queryWrapper.eq(WeLeadsFollower::getIsCurrentFollower, 1);
        WeLeadsFollower one = weLeadsFollowerMapper.selectOne(queryWrapper);
        if (BeanUtil.isNotEmpty(one)) {
            WeLeadsFollower weLeadsFollower = new WeLeadsFollower();
            weLeadsFollower.setId(one.getId());
            weLeadsFollower.setFollowerStatus(3);
            weLeadsFollower.setReturnType(FollowBackModeEnum.MEMBERS_RETURN.getCode());
            weLeadsFollower.setReturnReason(request.getRemark());
            weLeadsFollower.setFollowerEndTime(new Date());
            weLeadsFollower.setIsCurrentFollower(0);
            weLeadsFollowerMapper.updateById(weLeadsFollower);
        }
        return one.getId();
    }

    @Override
    public void update(WeLeadsUpdateRequest request) {
        WeLeads weLeads = weLeadsMapper.selectById(request.getLeadsId());
        if (BeanUtil.isEmpty(weLeads)) {
            return;
        }
        LambdaUpdateWrapper<WeLeads> updateWrapper = Wrappers.lambdaUpdate(WeLeads.class);
        updateWrapper.eq(WeLeads::getId, request.getLeadsId());
        updateWrapper.set(WeLeads::getLabelsIds, request.getLabelsIds());
        weLeadsMapper.update(null, updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindCustomer(WeLeadsBindCustomerRequest request) {
        //客户信息
        WeCustomer weCustomer = weCustomerMapper.selectById(request.getCustomerId());
        if (BeanUtil.isEmpty(weCustomer)) {
            throw new ServiceException("客户不存在！");
        }
        //绑定客户
        WeLeads weLeads = weLeadsMapper.selectById(request.getLeadsId());
        if (BeanUtil.isEmpty(weLeads)) {
            throw new ServiceException("线索不存在！");
        }
        //校验手机号码是否一致
        if (!weCustomer.getPhone().equals(weLeads.getPhone())) {
            throw new ServiceException("客户手机号码与线索手机号码不一致！");
        }

        //判断用户是否线索的当前跟进人
        LambdaQueryWrapper<WeLeadsFollower> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(WeLeadsFollower::getLeadsId, request.getLeadsId());
        lambdaQuery.eq(WeLeadsFollower::getFollowerId, SecurityUtils.getLoginUser().getSysUser().getUserId());
        lambdaQuery.eq(WeLeadsFollower::getIsCurrentFollower, 1);
        WeLeadsFollower weLeadsFollower = weLeadsFollowerMapper.selectOne(lambdaQuery);
        if (BeanUtil.isEmpty(weLeadsFollower)) {
            throw new ServiceException("不是当前跟进人，无法绑定用户！");
        }

        //更新线索信息
        LambdaUpdateWrapper<WeLeads> updateWrapper = Wrappers.lambdaUpdate(WeLeads.class);
        updateWrapper.eq(WeLeads::getId, request.getLeadsId());
        updateWrapper.set(WeLeads::getLeadsStatus, LeadsStatusEnum.VISIT.getCode());
        updateWrapper.set(WeLeads::getCustomerId, request.getCustomerId());
        updateWrapper.set(WeLeads::getExternalUserid, request.getExternalUserid());
        updateWrapper.set(WeLeads::getBindCustomerTime, new Date());
        weLeadsMapper.update(null, updateWrapper);

        //修改跟进人信息
        weLeadsFollower.setFollowerStatus(LeadsStatusEnum.VISIT.getCode());
        weLeadsFollower.setFollowerEndTime(new Date());
        weLeadsFollowerMapper.updateById(weLeadsFollower);

        //添加跟进记录
        Long recordId = this.addFollowRecord(request.getLeadsId(), weLeads.getSeaId(), SecurityUtils.getLoginUser().getSysUser().getUserId(), 2);
        //添加跟进记录内容
        weLeadsFollowRecordContentService.convertedCustomer(recordId, weCustomer.getCustomerName(), weCustomer.getCustomerType().toString(), weCustomer.getAvatar());
        //取消自动回收
        weLeadsAutoRecoveryService.cancel(request.getLeadsId(), SecurityUtils.getLoginUser().getSysUser().getUserId());
        //取消线索长时间待跟任务
        weTasksService.cancelLeadsLongTimeNotFollowUp(weLeads.getId(), SecurityUtils.getLoginUser().getSysUser().getUserId());
    }

    @Override
    public Long manualAdd(WeLeadsAddRequest request) {
        WeLeads weLeads = new WeLeads();
        weLeads.setId(IdUtil.getSnowflakeNextId());
        weLeads.setName(request.getName());
        weLeads.setPhone(request.getPhone());
        weLeads.setLeadsStatus(LeadsStatusEnum.WAIT_FOR_DISTRIBUTION.getCode());
        if (CollectionUtil.isNotEmpty(request.getPropertiesList())) {
            String jsonString = JSONObject.toJSONString(request.getPropertiesList());
            weLeads.setProperties(jsonString);
        }
        weLeads.setSex(request.getSex());
        weLeads.setSeaId(request.getSeaId() == null ? 1 : request.getSeaId());
        weLeads.setDelFlag(Constants.COMMON_STATE);
        weLeads.setSource(ImportSourceTypeEnum.WECHAT_CUSTOMER_SERVICE.getCode());
        weLeads.setRecoveryTimes(0);
        weLeadsMapper.insert(weLeads);
        return weLeads.getId();
    }

    @Override
    public List<WeLeadsVO> selectList(WeLeadsBaseRequest request) {
        request.setName(StringUtils.queryReplace(request.getName()));
        request.setPhone(StringUtils.queryReplace(request.getPhone()));
        return this.weLeadsMapper.leadsList(request);
    }
}