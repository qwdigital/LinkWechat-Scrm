package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.WeExceptionTip;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.wecom.client.WeDepartMentClient;
import com.linkwechat.wecom.domain.WeDepartment;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.WeDepartMentDto;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.mapper.WeDepartmentMapper;
import com.linkwechat.wecom.service.IWeDepartmentService;
import com.linkwechat.wecom.service.IWeUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 企业微信组织架构相关Service业务层处理
 *
 * @author ruoyi
 * @date 2020-09-01
 */
@Service
public class WeDepartmentServiceImpl extends ServiceImpl<WeDepartmentMapper, WeDepartment> implements IWeDepartmentService {


    @Autowired
    private WeDepartMentClient weDepartMentClient;


    /**
     * 查询企业微信组织架构相关列表
     *
     * @return 企业微信组织架构相关
     */
    @Override
    public List<WeDepartment> getList() {
        //校验数据中中是否存在根节点,如果不存在,从微信端获取,同时入库
        //暂时弃用
        /*WeDepartment weDepartment = this.getOne(new LambdaQueryWrapper<WeDepartment>()
                .eq(WeDepartment::getDeptId, WeConstans.WE_ROOT_DEPARMENT_ID));
        if (null == weDepartment) {
            WeDepartMentDto weDepartMentDto = weDepartMentClient.weDepartMents(WeConstans.WE_ROOT_DEPARMENT_ID);
            if (CollectionUtils.isNotEmpty(weDepartMentDto.getDepartment())) {
                this.save(WeDepartment.transformWeDepartment(weDepartMentDto.getDepartment().stream()
                        .filter(item -> item.getId().equals(WeConstans.WE_ROOT_DEPARMENT_ID))
                        .findFirst().get())
                );
            }
        }*/
        return this.list();
    }

    /**
     * 新增企业微信组织架构相关
     *
     * @param weDepartment 企业微信组织架构相关
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {Exception.class,WeComException.class},isolation = Isolation.READ_COMMITTED)
    public void insert(WeDepartment weDepartment) {
        WeResultDto resultDto = weDepartMentClient.createWeDepartMent(
                weDepartment.transformDeartMentDto(weDepartment)
        );
        if (resultDto.getId() != null) {
            weDepartment.setId(resultDto.getId());
            this.insert2Data(weDepartment);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,WeComException.class},isolation = Isolation.READ_COMMITTED)
    public void insert2Data(WeDepartment weDepartment) {
        if(!super.update(weDepartment,new LambdaQueryWrapper<WeDepartment>()
        .eq(WeDepartment::getId,weDepartment.getId()))){
             super.save(weDepartment);
        }
    }

    /**
     * 修改企业微信组织架构相关
     *
     * @param weDepartment 企业微信组织架构相关
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {Exception.class,WeComException.class},isolation = Isolation.READ_COMMITTED)
    public void update(WeDepartment weDepartment) {
        WeResultDto resultDto = weDepartMentClient.updateWeDepartMent(
                weDepartment.transformDeartMentDto(weDepartment)
        );
        if (resultDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)) {
            this.updateById(weDepartment);
        }
    }


    /**
     * 同步部门
     */
    @Override
    @Transactional(rollbackFor = {Exception.class,WeComException.class},isolation = Isolation.READ_COMMITTED)
    public List<WeDepartment> synchWeDepartment() {
        List<WeDepartment> weDepartments = weDepartMentClient.weAllDepartMents().findWeDepartments();
        if (CollectionUtil.isNotEmpty(weDepartments)) {
            List<List<WeDepartment>> lists = Lists.partition(weDepartments, 500);
            for(List<WeDepartment> list : lists){
                this.baseMapper.insertBatch(list);
            }
        }
        return weDepartments;
    }


    /**
     * 根据部门id删除部门
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = {Exception.class,WeComException.class},isolation = Isolation.READ_COMMITTED)
    public void deleteByIds(Long[] ids) {
        List<WeDepartment> weDepartmentList = this.list(new LambdaQueryWrapper<WeDepartment>()
                .in(WeDepartment::getId, CollectionUtil.toList(ids)));
        if(CollectionUtil.isNotEmpty(weDepartmentList)){
            //查询当前部门下所有的子部门,如果存在,则不可以删除
            List<WeDepartment> weDepartments = this.list(new LambdaQueryWrapper<WeDepartment>()
                    .in(WeDepartment::getParentId, weDepartmentList.stream().map(WeDepartment::getId).collect(Collectors.toList())));
            if (CollectionUtil.isNotEmpty(weDepartments)) {
                //抛出异常，请删除此部门下的成员或子部门后，再删除此部门
                throw new CustomException(WeExceptionTip.WE_EXCEPTION_TIP_60006.getTipMsg(), WeExceptionTip.WE_EXCEPTION_TIP_60006.getCode());
            }

            weDepartmentList.forEach(weDepartment -> {
                List<String> deptIds = CollectionUtil.newArrayList(String.valueOf(weDepartment.getId()));
                List<WeUser> weUsers = SpringUtils.getBean(IWeUserService.class).getList(WeUser.builder()
                        .department(String.valueOf(deptIds))
                        //这里多企业的话需要加上企业id
                        .build());
                if (CollectionUtil.isNotEmpty(weUsers)) {
                    //该部门存在成员无法删除
                    throw new CustomException(WeExceptionTip.WE_EXCEPTION_TIP_60005.getTipMsg(), WeExceptionTip.WE_EXCEPTION_TIP_60005.getCode());
                }
            });
            //删除数据库中数据
            if (this.removeByIds(ListUtil.toList(ids))) {
                weDepartmentList.forEach(weDepartment -> {
                    //移除微信端
                    weDepartMentClient.deleteWeDepartMent(String.valueOf(weDepartment.getId()));
                });

            }
        }
    }

    @Override
    public WeDepartment getByDeptId(Long deptId) {
        return this.getOne(new LambdaQueryWrapper<WeDepartment>().eq(WeDepartment::getId, deptId));
    }


}
