package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.WeExceptionTip;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.bean.BeanUtils;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 企业微信组织架构相关Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-09-01
 */
@Service
public class WeDepartmentServiceImpl extends ServiceImpl<WeDepartmentMapper,WeDepartment> implements IWeDepartmentService
{



    @Autowired
    private WeDepartMentClient weDepartMentClient;


    @Autowired
    private IWeUserService weUserService;







    /**
     * 查询企业微信组织架构相关列表
     * 
     * @return 企业微信组织架构相关
     */
    @Override
    public List<WeDepartment> selectWeDepartmentList()
    {

        //校验数据中中是否存在根节点,如果不存在,从微信端获取,同时入库
        WeDepartment weDepartment=this.baseMapper.selectWeDepartmentById(WeConstans.WE_ROOT_DEPARMENT_ID);
        if(null == weDepartment){
            WeDepartMentDto weDepartMentDto=weDepartMentClient.weDepartMents(WeConstans.WE_ROOT_DEPARMENT_ID);
            if(WeConstans.WE_SUCCESS_CODE.equals(weDepartMentDto.getErrcode())
            && CollectionUtils.isNotEmpty(weDepartMentDto.getDepartment())){
                this.baseMapper.insertWeDepartment(
                        WeDepartment.transformWeDepartment(
                                weDepartMentDto.getDepartment().stream().filter(item->item.getId().equals(WeConstans.WE_ROOT_DEPARMENT_ID)).findFirst().get()
                        )
                );

            }
        }

        return this.baseMapper.selectWeDepartmentList();
    }

    /**
     * 新增企业微信组织架构相关
     * 
     * @param weDepartment 企业微信组织架构相关
     * @return 结果
     */
    @Override
    public void insertWeDepartment(WeDepartment weDepartment)
    {

        WeResultDto weDepartMent = weDepartMentClient.createWeDepartMent(
                weDepartment.transformDeartMentDto(weDepartment)
        );

        if(weDepartMent.getErrcode().equals(WeConstans.WE_SUCCESS_CODE) && weDepartMent.getId() != null){

            weDepartment.setId(weDepartMent.getId());

            this.save(weDepartment);
        }

    }

    @Override
    public int insertWeDepartmentNoToWeCom(WeDepartment weDepartment) {
        WeDepartment department = this.baseMapper.selectWeDepartmentById(weDepartment.getId());
        if (department !=null){
            return 0;
        }
        return this.baseMapper.insertWeDepartment(weDepartment);
    }

    /**
     * 修改企业微信组织架构相关
     * 
     * @param weDepartment 企业微信组织架构相关
     * @return 结果
     */
    @Override
    public void updateWeDepartment(WeDepartment weDepartment)
    {

        WeResultDto weDepartMent =  weDepartMentClient.updateWeDepartMent(
                weDepartment.transformDeartMentDto(weDepartment)
        );


        if(weDepartMent.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
               this.updateById(weDepartment);
        }

    }



    /**
     * 同步部门
     */
    @Override
    @Transactional
    public List<WeDepartment> synchWeDepartment() {
        List<WeDepartment> weDepartments = weDepartMentClient.weAllDepartMents().findWeDepartments();
        if(CollectionUtil.isNotEmpty(weDepartments)){
            this.saveOrUpdateBatch(weDepartments);
        }

        return weDepartments;
    }


    /**
     * 根据部门id删除部门
     * @param ids
     */
    @Override
    public void deleteWeDepartmentByIds(String[] ids) {

        //查询当前部门下所有的子部门,如果存在,则不可以删除
        List<WeDepartment> weDepartments = this.list(new LambdaQueryWrapper<WeDepartment>()
                .in(WeDepartment::getParentId, ids));
        if(CollectionUtil.isNotEmpty(weDepartments)) {
            //抛出异常，请删除此部门下的成员或子部门后，再删除此部门
            throw new CustomException(WeExceptionTip.WE_EXCEPTION_TIP_60006.getTipMsg(),WeExceptionTip.WE_EXCEPTION_TIP_60006.getCode());
        }

        List<WeUser> weUsers = weUserService.selectWeUserList(WeUser.builder()
                .department(ids)
                .build());
        if(CollectionUtil.isNotEmpty(weUsers)){
            //该部门存在成员无法删除
            throw new CustomException(WeExceptionTip.WE_EXCEPTION_TIP_60005.getTipMsg(),WeExceptionTip.WE_EXCEPTION_TIP_60005.getCode());
        }

        //删除数据库中数据
        if(this.removeByIds(ListUtil.toList(ids))){

            ListUtil.toList(ids).stream().forEach(k->{
                //移除微信端
                weDepartMentClient.deleteWeDepartMent(k);

            });

        }

    }


}
