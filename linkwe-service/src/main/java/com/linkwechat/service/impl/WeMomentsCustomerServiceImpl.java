package com.linkwechat.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.enums.TrackState;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeFlowerCustomerTagRel;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.moments.dto.MomentsParamDto;
import com.linkwechat.domain.moments.dto.MomentsResultDto;
import com.linkwechat.domain.moments.dto.MomentsResultDto.CustomerList;
import com.linkwechat.domain.moments.dto.MomentsSendResultDTO;
import com.linkwechat.domain.moments.entity.WeMomentsCustomer;
import com.linkwechat.domain.moments.entity.WeMomentsUser;
import com.linkwechat.domain.moments.query.WeMomentsTaskAddRequest;
import com.linkwechat.domain.moments.query.WeMomentsTaskEstimateCustomerNumRequest;
import com.linkwechat.domain.moments.vo.MomentsSendResultVO;
import com.linkwechat.domain.moments.vo.MomentsSendResultVO.ExternalUserid;
import com.linkwechat.fegin.QwMomentsClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeMomentsCustomerMapper;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWeFlowerCustomerTagRelService;
import com.linkwechat.service.IWeMomentsCustomerService;
import com.linkwechat.service.IWeMomentsUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 朋友圈可见客户 服务实现类
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/07 10:10
 */
@Service
public class WeMomentsCustomerServiceImpl extends ServiceImpl<WeMomentsCustomerMapper, WeMomentsCustomer> implements IWeMomentsCustomerService {


    @Resource
    private IWeCustomerService weCustomerService;
    @Resource
    private IWeFlowerCustomerTagRelService weFlowerCustomerTagRelService;
    @Resource
    private IWeMomentsUserService weMomentsUserService;
//    @Resource
//    private QwSysUserClient qwSysUserClient;
    @Resource
    private QwMomentsClient qwMomentsClient;


    @Override
    public long estimateCustomerNum(WeMomentsTaskEstimateCustomerNumRequest request) {

           long estimateCustomerNum=0;
          if(new Integer(0).equals(request.getScopeType())){
              WeCustomersQuery weCustomersQuery = new WeCustomersQuery();
              weCustomersQuery.setDelFlag(Constants.COMMON_STATE);
              weCustomersQuery.setIsJoinBlacklist(1);
              weCustomersQuery .setNoContainTrackStates(TrackState.STATE_YLS.getType().toString());
              estimateCustomerNum=weCustomerService.countWeCustomerList(weCustomersQuery);
          }else if(new Integer(1).equals(request.getScopeType())){
              estimateCustomerNum=weCustomerService.countWeCustomerList(request.getWeCustomersQuery());
          }


          return estimateCustomerNum>10000?10000:estimateCustomerNum;

    }

    @Override
    public List<WeCustomer> estimateCustomers(WeMomentsTaskEstimateCustomerNumRequest request) {
        if (request.getScopeType().equals(0)) {
            //全部客户
            LambdaQueryWrapper<WeCustomer> queryWrapper = Wrappers.lambdaQuery(WeCustomer.class);
            queryWrapper.eq(WeCustomer::getDelFlag, Constants.COMMON_STATE);
            queryWrapper.ne(WeCustomer::getTrackState, 5);
            return weCustomerService.list(queryWrapper);
        } else {
            //通过条件筛选
            if (CollectionUtil.isNotEmpty(request.getCustomerTag())) {
                //标签数量不为0时，获取标签对应的客户数量
                List<WeFlowerCustomerTagRel> list = weFlowerCustomerTagRelService.getListByTagIdAndUserId(request.getCustomerTag(), request.getUserIds());
                if (CollectionUtil.isNotEmpty(list)) {
                    List<String> weUserId = list.stream().map(i -> i.getUserId()).distinct().collect(Collectors.toList());
                    List<String> externalUserId = list.stream().map(i -> i.getExternalUserid()).distinct().collect(Collectors.toList());
                    LambdaQueryWrapper<WeCustomer> queryWrapper = Wrappers.lambdaQuery(WeCustomer.class);
                    queryWrapper.eq(WeCustomer::getDelFlag, Constants.COMMON_STATE);
                    queryWrapper.ne(WeCustomer::getTrackState, 5);
                    queryWrapper.in(WeCustomer::getAddUserId, weUserId);
                    queryWrapper.in(WeCustomer::getExternalUserid, externalUserId);
                    return weCustomerService.list(queryWrapper);
                }
                return CollectionUtil.newArrayList();
            }
            //通过条件筛选出的员工数据为0，且没有客户标签时
            if (CollectionUtil.isEmpty(request.getUserIds())) {
                return CollectionUtil.newArrayList();
            }
            //员工数据不为0时，获取员工对应的全部的客户数量
            LambdaQueryWrapper<WeCustomer> queryWrapper = Wrappers.lambdaQuery(WeCustomer.class);
            queryWrapper.eq(WeCustomer::getDelFlag, Constants.COMMON_STATE);
            queryWrapper.ne(WeCustomer::getTrackState, 5);
            queryWrapper.in(WeCustomer::getAddUserId, request.getUserIds());
            return weCustomerService.list(queryWrapper);
        }
    }


    @Override
    public void addMomentsCustomer(Long weMomentsTaskId, WeMomentsTaskAddRequest request) {

    }

    @Override
    public void syncAddMomentsCustomer(Long weMomentsTaskId, String momentsId) {
        //查询跟进员工
        LambdaQueryWrapper<WeMomentsUser> queryWrapper = Wrappers.lambdaQuery(WeMomentsUser.class);
        queryWrapper.eq(WeMomentsUser::getMomentsTaskId, weMomentsTaskId);
        queryWrapper.eq(WeMomentsUser::getMomentsId, momentsId);
        queryWrapper.eq(WeMomentsUser::getDelFlag, Constants.COMMON_STATE);
        List<WeMomentsUser> list = weMomentsUserService.list(queryWrapper);

        for (WeMomentsUser weMomentsUser : list) {
            //获取员工的跟进客户
            List<CustomerList> customerList = iterateGetCustomerList(null, momentsId, weMomentsUser.getWeUserId());

            List<WeMomentsCustomer> customers = new ArrayList<>();
            for (CustomerList item : customerList) {
                //构建数据
                WeMomentsCustomer weMomentsCustomer = new WeMomentsCustomer();
                weMomentsCustomer.setId(IdUtil.getSnowflake().nextId());
                weMomentsCustomer.setMomentsTaskId(weMomentsTaskId);
                weMomentsCustomer.setMomentsId(momentsId);
                weMomentsCustomer.setWeUserId(weMomentsUser.getWeUserId());
                weMomentsCustomer.setUserId(weMomentsUser.getUserId());
                weMomentsCustomer.setUserName(weMomentsUser.getUserName());
                weMomentsCustomer.setExternalUserid(item.getExternal_userid());
                weMomentsCustomer.setDeliveryStatus(1);
                weMomentsCustomer.setCreateTime(new Date());
                weMomentsCustomer.setDelFlag(Constants.COMMON_STATE);
                customers.add(weMomentsCustomer);
            }
            //批量保存数据
            if (CollectionUtil.isNotEmpty(customers)) {
                this.saveBatch(customers);
            }
        }
    }

    /**
     * 迭代获取可见客户数据
     *
     * @param nextCursor 游标
     * @param momentsId  朋友圈Id
     * @param weUserId   执行员工Id
     * @return {@link List< CustomerList>}
     * @author WangYX
     * @date 2023/06/12 15:40
     */
    private List<CustomerList> iterateGetCustomerList(String nextCursor, String momentsId, String weUserId) {
        MomentsParamDto build = MomentsParamDto.builder().moment_id(momentsId).userid(weUserId).cursor(nextCursor).build();
        AjaxResult<MomentsResultDto> result = qwMomentsClient.get_moment_customer_list(build);
        if (result.getCode() == HttpStatus.SUCCESS) {
            MomentsResultDto data = result.getData();
            if (data.getErrCode().equals(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode())) {
                List<CustomerList> customer_list = data.getCustomer_list();
                nextCursor = data.getNextCursor();
                if (StrUtil.isNotEmpty(nextCursor)) {
                    customer_list.addAll(iterateGetCustomerList(nextCursor, momentsId, weUserId));
                }
                return customer_list;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void syncMomentsCustomerSendSuccess(Long weMomentsTaskId, String momentsId) {
        //查询跟进员工
         List<WeMomentsUser> weMomentsUsers = weMomentsUserService.list(new LambdaQueryWrapper<WeMomentsUser>()
                .eq(WeMomentsUser::getMomentsTaskId, weMomentsTaskId)
                .eq(WeMomentsUser::getDelFlag, Constants.COMMON_STATE)
                .eq(WeMomentsUser::getExecuteStatus,1));
            if(CollectionUtil.isNotEmpty(weMomentsUsers)){
                for (WeMomentsUser weMomentsUser : weMomentsUsers) {
                    List<ExternalUserid> externalUserIds = iterateGetCustomerSendResult(momentsId, weMomentsUser.getWeUserId(), null);

                    if(CollectionUtil.isNotEmpty(externalUserIds)){
                        this.update(WeMomentsCustomer.builder()
                                .deliveryStatus(0)
                                .build(), new LambdaQueryWrapper<WeMomentsCustomer>()
                                .eq(WeMomentsCustomer::getMomentsTaskId, weMomentsTaskId)
                                .in(WeMomentsCustomer::getExternalUserid,externalUserIds.stream().map(ExternalUserid::getExternal_userid).collect(Collectors.toList()))
                                .eq(WeMomentsCustomer::getWeUserId,weMomentsUser.getWeUserId()));
                    }

                }

            }

    }

    @Override
    public void saveBatch(List<WeMomentsCustomer> weMomentsCustomers) {
        this.baseMapper.insertBatchSomeColumn(weMomentsCustomers);
    }

    /**
     * 迭代获取发送成功的客户数据
     *
     * @param momentsId  朋友圈id
     * @param weUserId   企微员工id
     * @param nextCursor 游标
     * @return {@link List< ExternalUserid>}
     * @author WangYX
     * @date 2023/06/12 16:42
     */
    private List<ExternalUserid> iterateGetCustomerSendResult(String momentsId, String weUserId, String nextCursor) {
        MomentsSendResultDTO build = MomentsSendResultDTO.builder().moment_id(momentsId).userid(weUserId).cursor(nextCursor).build();
        AjaxResult<MomentsSendResultVO> result = qwMomentsClient.get_moment_send_result(build);
        if (result.getCode() == HttpStatus.SUCCESS) {
            MomentsSendResultVO data = result.getData();
            if (data.getErrCode().equals(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode())) {
                List<ExternalUserid> customer_list = data.getCustomer_list();
                nextCursor = data.getNextCursor();
                if (StrUtil.isNotEmpty(nextCursor)) {
                    customer_list.addAll(iterateGetCustomerSendResult(momentsId, weUserId, nextCursor));
                }
                return customer_list;
            }
        }
        return new ArrayList<>();
    }

}
