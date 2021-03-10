package com.linkwechat.web.controller.wecom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.wecom.domain.WeCustomerPortrait;
import com.linkwechat.wecom.domain.WeCustomerTrajectory;
import com.linkwechat.wecom.domain.WeFlowerCustomerTagRel;
import com.linkwechat.wecom.domain.WeTagGroup;
import com.linkwechat.wecom.domain.vo.WeMakeCustomerTag;
import com.linkwechat.wecom.service.*;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 客户画像相关controller
 * @author: HaoN
 * @create: 2021-03-03 15:10
 **/
@RestController
@RequestMapping("/wecom/portrait")
public class WeCustomerPortraitController extends BaseController {


    @Autowired
    private IWeCustomerService iWeCustomerService;


    @Autowired
    private IWeTagGroupService weTagGroupService;


    @Autowired
    private IWeUserService iWeUserService;


    @Autowired
    private IWeGroupService iWeGroupService;



    @Autowired
    private IWeCustomerTrajectoryService iWeCustomerTrajectoryService;


    /**
     * 根据客户id和当前企业员工id获取客户详细信息
     * @param externalUserid
     * @param userid
     * @return
     */
    @GetMapping(value = "/findWeCustomerInfo")
    public AjaxResult findWeCustomerInfo(String externalUserid, String userid){

        return AjaxResult.success(
                iWeCustomerService.findCustomerByOperUseridAndCustomerId(externalUserid,userid)
        );
    }


    /**
     * 客户画像资料更新
     * @param weCustomerPortrait
     * @return
     */
    @PostMapping(value = "/updateWeCustomerInfo")
    public AjaxResult updateWeCustomerInfo(@RequestBody WeCustomerPortrait weCustomerPortrait){




        iWeCustomerService.updateWeCustomerPortrait(weCustomerPortrait);


        return AjaxResult.success();
    }



    /**
     * 获取当前系统所有可用标签
     * @return
     */
    @GetMapping(value = "/findAllTags")
    public AjaxResult findAllTags(){

        return AjaxResult.success(
                weTagGroupService.selectWeTagGroupList(
                        WeTagGroup.builder()
                                .build()
                )
        );

    }


    /**
     * 更新客户画像标签
     * @param weMakeCustomerTag
     * @return
     */
    @PostMapping(value = "/updateWeCustomerPorTraitTag")
    public AjaxResult updateWeCustomerPorTraitTag(@RequestBody WeMakeCustomerTag weMakeCustomerTag){



        iWeCustomerService.makeLabel(weMakeCustomerTag);

        return AjaxResult.success();
    }


    /**
     * 查看客户添加的员工
     * @param externalUserid
     * @return
     */
    @GetMapping(value = "/findAddaddEmployes/{externalUserid}")
    public AjaxResult findaddEmployes(@PathVariable String externalUserid){


        return AjaxResult.success(
                iWeUserService.findWeUserByCutomerId(externalUserid)
        );
    }


    /**
     * 获取用户添加的群
     * @param externalUserid
     * @param userId
     * @return
     */
    @GetMapping(value = "/findAddGroupNum")
    public AjaxResult findAddGroupNum(String externalUserid,String userId){

        return AjaxResult.success(
                iWeGroupService.findWeGroupByCustomer(userId,externalUserid)
        );
    }


    /**
     * 获取轨迹信息
     * @param trajectoryType
     * @return
     */
    @GetMapping(value = "/findTrajectory")
    public TableDataInfo findTrajectory(Integer trajectoryType){
        startPage();
        return getDataTable(
                iWeCustomerTrajectoryService.list(new LambdaQueryWrapper<WeCustomerTrajectory>()
                .eq(WeCustomerTrajectory::getTrajectoryType,trajectoryType)
                .ne(WeCustomerTrajectory::getStatus,Constants.DELETE_CODE))
        );
    }


    /**
     * 添加或编辑轨迹
     * @param trajectory
     * @return
     */
    @PostMapping(value = "/addOrEditWaitHandle")
    public AjaxResult addOrEditWaitHandle(@RequestBody WeCustomerTrajectory trajectory){


        iWeCustomerTrajectoryService.saveOrUpdate(trajectory);

        return AjaxResult.success();
    }


    /**
     * 删除轨迹
     * @param trajectoryId
     * @return
     */
    @DeleteMapping(value = "/removeTrajectory/{trajectoryId}")
    public AjaxResult removeTrajectory(@PathVariable String trajectoryId){
        iWeCustomerTrajectoryService.updateById(WeCustomerTrajectory.builder()
                .id(trajectoryId)
                .status(Constants.DELETE_CODE)
                .build());
        return AjaxResult.success();
    }


    /**
     * 完成待办
     * @param trajectoryId
     * @return
     */
    @DeleteMapping(value = "/handleWait/{trajectoryId}")
    public AjaxResult handleWait(@PathVariable String trajectoryId){
        iWeCustomerTrajectoryService.updateById(WeCustomerTrajectory.builder()
                .id(trajectoryId)
                .status(Constants.HANDLE_SUCCESS)
                .build());
        return AjaxResult.success();
    }













}
