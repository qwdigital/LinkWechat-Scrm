package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.client.WeDepartMentClient;
import com.linkwechat.wecom.domain.WeDepartment;
import com.linkwechat.wecom.domain.dto.WeDepartMentDto;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.mapper.WeDepartmentMapper;
import com.linkwechat.wecom.service.IWeDepartmentService;
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
public class WeDepartmentServiceImpl implements IWeDepartmentService 
{
    @Autowired
    private WeDepartmentMapper weDepartmentMapper;


    @Autowired
    private WeDepartMentClient weDepartMentClient;



    /**
     * 查询企业微信组织架构相关
     * 
     * @param id 企业微信组织架构相关ID
     * @return 企业微信组织架构相关
     */
    @Override
    public WeDepartment selectWeDepartmentById(Long id)
    {
        return weDepartmentMapper.selectWeDepartmentById(id);
    }

    /**
     * 查询企业微信组织架构相关列表
     * 
     * @return 企业微信组织架构相关
     */
    @Override
    public List<WeDepartment> selectWeDepartmentList()
    {

        //校验数据中中是否存在根节点,如果不存在,从微信端获取,同时入库
        WeDepartment weDepartment=weDepartmentMapper.selectWeDepartmentById(WeConstans.WE_ROOT_DEPARMENT_ID);
        if(null == weDepartment){
            WeDepartMentDto weDepartMentDto=weDepartMentClient.weDepartMents(WeConstans.WE_ROOT_DEPARMENT_ID);
            if(WeConstans.WE_SUCCESS_CODE.equals(weDepartMentDto.getErrcode())
            && CollectionUtils.isNotEmpty(weDepartMentDto.getDepartment())){
                weDepartmentMapper.insertWeDepartment(
                        WeDepartment.transformWeDepartment(
                                weDepartMentDto.getDepartment().stream().filter(item->item.getId().equals(WeConstans.WE_ROOT_DEPARMENT_ID)).findFirst().get()
                        )
                );

            }
        }

        return weDepartmentMapper.selectWeDepartmentList();
    }

    /**
     * 新增企业微信组织架构相关
     * 
     * @param weDepartment 企业微信组织架构相关
     * @return 结果
     */
    @Override
    @Transactional
    public int insertWeDepartment(WeDepartment weDepartment)
    {
         weDepartment.setId(SnowFlakeUtil.nextId());
         int returnCode=weDepartmentMapper.insertWeDepartment(weDepartment);

         if(returnCode>0){


              weDepartMentClient.createWeDepartMent(
                     weDepartment.transformDeartMentDto(weDepartment)
             );


         }

        return  returnCode;
    }

    /**
     * 修改企业微信组织架构相关
     * 
     * @param weDepartment 企业微信组织架构相关
     * @return 结果
     */
    @Override
    public int updateWeDepartment(WeDepartment weDepartment)
    {

        int returnCode=weDepartmentMapper.updateWeDepartment(weDepartment);
        if(returnCode>0){

            weDepartMentClient.updateWeDepartMent(
                    weDepartment.transformDeartMentDto(weDepartment)
            );


        }

        return returnCode;
    }


    /**
     * 批量保存
     * @param weDepartments
     * @return
     */
    @Override
    public int batchInsertWeDepartment(List<WeDepartment> weDepartments) {
        return weDepartmentMapper.batchInsertWeDepartment(weDepartments);
    }


    /**
     *  删除部门表所有数据
     * @return
     */
    @Override
    public int deleteAllWeDepartment() {
        return weDepartmentMapper.deleteAllWeDepartment();
    }



    /**
     * 同步部门
     */
    @Override
    @Transactional
    public List<WeDepartment> synchWeDepartment() {
        List<WeDepartment> weDepartments = weDepartMentClient.weAllDepartMents().findWeDepartments();
        if(CollectionUtil.isNotEmpty(weDepartments)){
            this.deleteAllWeDepartment();
            this.batchInsertWeDepartment(weDepartments);
        }

        return weDepartments;
    }


}
