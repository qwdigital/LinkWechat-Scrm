package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.exception.wecom.WeComException;
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
    public int insertWeDepartment(WeDepartment weDepartment)
    {

         int returnCode=weDepartmentMapper.insertWeDepartment(weDepartment);

         if(returnCode>0){

             WeDepartMentDto.DeartMentDto deartMentDto = weDepartment.transformDeartMentDto(weDepartment);

//             WeResultDto weResultDto=weDepartMentClient.createWeDepartMent(
//                     weDepartment.transformDeartMentDto(weDepartment)
//             );
//
//             //微信端调用不成功
//             if(!WeConstans.WE_SUCCESS_CODE.equals(weResultDto.getErrcode())){
//                 throw new WeComException(weResultDto.getErrmsg());
//             }

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

            WeResultDto weResultDto=weDepartMentClient.updateWeDepartMent(
                    weDepartment.transformDeartMentDto(weDepartment)
            );

            //微信端调用不成功
            if(!WeConstans.WE_SUCCESS_CODE.equals(weResultDto.getErrcode())){
                throw new WeComException(weResultDto.getErrmsg());
            }

        }

        return returnCode;
    }

    /**
     * 批量删除企业微信组织架构相关
     * 
     * @param ids 需要删除的企业微信组织架构相关ID
     * @return 结果
     */
    @Override
    public int deleteWeDepartmentByIds(Long[] ids)
    {
        return weDepartmentMapper.deleteWeDepartmentByIds(ids);
    }

    /**
     * 删除企业微信组织架构相关信息
     * 
     * @param id 企业微信组织架构相关ID
     * @return 结果
     */
    @Override
    public int deleteWeDepartmentById(Long id)
    {
        return weDepartmentMapper.deleteWeDepartmentById(id);
    }
}
