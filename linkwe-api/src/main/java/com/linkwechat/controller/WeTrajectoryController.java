package com.linkwechat.controller;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.TrajectoryType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomerTrajectory;
import com.linkwechat.service.IWeCustomerTrajectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/trajectory")
public class WeTrajectoryController extends BaseController {

    @Autowired
    IWeCustomerTrajectoryService weCustomerTrajectoryService;

    /**
     * 获取企业动态
     * @param trajectoryType
     * @param dataScope 个人数据:false 全部数据(相对于角色定义的数据权限):true
     * @return
     */
    @GetMapping("/findCompanyDynamics")
    public TableDataInfo<WeCustomerTrajectory> findCompanyDynamics(Integer trajectoryType,@RequestParam(defaultValue = "true") boolean dataScope){
        startPage();
        return getDataTable(
                dataScope?weCustomerTrajectoryService.findAllTrajectory(
                        WeCustomerTrajectory.builder().trajectoryType(trajectoryType).build()
                ):weCustomerTrajectoryService.findPersonTrajectory(
                        WeCustomerTrajectory.builder().trajectoryType(trajectoryType).build()
                )
        );
    }


    /**
     * 获取客户轨迹
     * @return
     */
    @GetMapping("/findTrajectory")
    public TableDataInfo<WeCustomerTrajectory> findTrajectory(String externalUserid,String weUserId,Integer trajectoryType){
        startPage();
        return getDataTable(
                weCustomerTrajectoryService.list(
                        new LambdaQueryWrapper<WeCustomerTrajectory>()
                                .eq(StringUtils.isNotEmpty(externalUserid),WeCustomerTrajectory::getExternalUseridOrChatid,externalUserid)
                                .eq(StringUtils.isNotEmpty(weUserId),WeCustomerTrajectory::getWeUserId,weUserId)
                                .eq(trajectoryType != null,WeCustomerTrajectory::getTrajectoryType,trajectoryType)
                                .orderByDesc(WeCustomerTrajectory::getCreateTime)
                )
        );
    }


}
