package com.linkwechat.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.poi.LwExcelUtil;
import com.linkwechat.domain.live.WeLive;
import com.linkwechat.domain.live.WeLiveWatchUser;
import com.linkwechat.domain.live.vo.WeLinveUserVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.IWeLiveService;
import com.linkwechat.service.IWeLiveTipService;
import com.linkwechat.service.IWeLiveWatchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.Arrays;


/**
 * 直播相关
 */
@RestController
@RequestMapping("/live")
public class WeLiveController extends BaseController {

    @Autowired
    IWeLiveService iWeLiveService;

    @Autowired
    IWeLiveTipService iWeLiveTipService;

    @Autowired
    IWeLiveWatchUserService iWeLiveWatchUserService;

    @Autowired
    QwSysUserClient qwSysUserClient;


    /**
     * 新增或更新直播
     * @param weLive
     * @return
     */
    @PostMapping("/addOrUpdate")
    public AjaxResult  addOrUpdate(@RequestBody WeLive weLive) throws ParseException {
        iWeLiveService.addOrUpdate(weLive);
        return AjaxResult.success();
    }


    /**
     * 获取直播列表
     * @param weLive
     * @return
     */
    @GetMapping("/findWelives")
    public TableDataInfo findWelives(WeLive weLive){

        startPage();
        return getDataTable(iWeLiveService.findLives(weLive));
    }


    /**
     * 获取直播详情
     * @return
     */
    @GetMapping("/findLiveDetail/{weLiveId}")
    public AjaxResult findLiveDetail(@PathVariable String weLiveId){

        return AjaxResult.success(
                iWeLiveService.findLiveDetail(weLiveId)
        );

    }


    /**
     * 通过id列表删除
     *
     * @param ids id列表
     * @return 结果
     */
    @DeleteMapping(path = "/{ids}")
    public AjaxResult batchDeleteLiwes(@PathVariable("ids") Long[] ids) {
        iWeLiveService.removeLives(Arrays.asList(ids));
        return AjaxResult.success();
    }


    /**
     * 取消直播
     * @param live
     * @return
     */
    @PostMapping("/cancelLive")
    public AjaxResult cancelLive(@RequestBody WeLive live){
        iWeLiveService.cancleLive(live);
        return AjaxResult.success();
    }


    /**
     * 同步直播
     * @return
     */
    @GetMapping("/synchLive")
    public AjaxResult synchLive(){

        iWeLiveService.synchLive();

        return AjaxResult.success();
    }


    /**
     * 导出直播
     * @return
     */
    @GetMapping("/exportWeLives")
    public void exportWeLives(){



        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WeLive.class, iWeLiveService.findLives(new WeLive()),"直播"
        );

    }


    /**
     *  分享统计-tab（客户或客群维度） （客户或客群维度 1:客户 2:客群）
     * @param liveId
     * @return
     */
    @GetMapping("/findWeLiveTaskCustomerDetailTab")
    public AjaxResult findWeLiveTaskCustomerDetailTab(String liveId){
        WeLive weLive = iWeLiveService.getById(liveId);
        if(null != weLive){
            return AjaxResult.success(
                    iWeLiveTipService.findWeLiveTaskExecuteUserDetailTab(liveId,weLive.getTargetType())
            );
        }

        return AjaxResult.success();
    }
    /**
     *  分享统计（员工维度）-tab
     * @param liveId
     * @return
     */
    @GetMapping("findWeLiveTaskUserDetailTab")
    public AjaxResult findWeLiveTaskUserDetailTab(String liveId){

        WeLive weLive = iWeLiveService.getById(liveId);
        if(null != weLive){
            return AjaxResult.success(
                    iWeLiveTipService.findWeLiveTaskUserDetailTab(liveId,weLive.getTargetType())
            );
        }

        return AjaxResult.success();
    }


    /**
     *  分享统计-员工详情(客户或客群)
     * @param userName 员工名称
     * @param liveId 直播id
     * @param sendTargetType 目标类型  发送目标1:客户;2:客群
     * @return
     */
    @GetMapping("/findWeLiveTaskUserDetail")
    public TableDataInfo findWeLiveTaskUserDetail(String userName, String liveId,@RequestParam(defaultValue = "1") Integer sendTargetType){
          startPage();

          return getDataTable(
                  iWeLiveTipService.findWeLiveTaskUserDetail(userName, liveId, sendTargetType)
          );

    }


    /**
     * 分享统计-客户详情(客户或客群)
     * @param sendTargetType 目标类型  发送目标1:客户;2:客群
     * @param userName 员工名称
     * @param liveId 直播id
     * @param sendState 直播状态(0:预约中;1:直播中;2:已结束;3:已过期;4:已取消)
     * @return
     */
    @GetMapping("/findWeLiveTaskCustomerDetail")
    public TableDataInfo findWeLiveTaskCustomerDetail(Integer sendTargetType,String userName,String liveId,Integer sendState){
        startPage();

        return getDataTable(
                iWeLiveTipService.findWeLiveTaskCustomerDetail(sendTargetType,userName,liveId,sendState)
        );
    }


    /**
     * 获取观众列表
     * @param weLiveWatchUser
     * @return
     */
    @GetMapping("/weLiveWatchUser")
    public TableDataInfo  findVisitorList(WeLiveWatchUser weLiveWatchUser){
        startPage();
        return getDataTable(
                iWeLiveWatchUserService.list(
                        new LambdaQueryWrapper<WeLiveWatchUser>()
                                .eq(weLiveWatchUser.getLiveId() !=null,WeLiveWatchUser::getLiveId,weLiveWatchUser.getLiveId())
                                .eq(weLiveWatchUser.getWatchUserType()!=null,WeLiveWatchUser::getWatchUserType,weLiveWatchUser.getWatchUserType())

                )
        );
    }

    /**
     * 获取直播凭证
     * @param livingId
     * @param openId
     * @return
     */
    @GetMapping("/getLivingCode")
    public AjaxResult  getLivingCode(String livingId,String openId){
        WeLinveUserVo weLinveUserVo=new WeLinveUserVo();

        WeLive weLive = iWeLiveService.getById(livingId);

        if(null != weLive){
            weLinveUserVo.setLiveCode(iWeLiveService.getLivingCode(weLive.getLivingId(), openId));
        }


        return AjaxResult.success(
                weLinveUserVo
        );
    }

    /**
     * 导出观看成员
     * @param watchUserType
     * @return
     */
    @GetMapping("/exprotWeLiveWatchUser")
    public AjaxResult exprotWeLiveWatchUser(Integer watchUserType,String liveId){

        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WeLiveWatchUser.class,
                iWeLiveWatchUserService.list(
                        new LambdaQueryWrapper<WeLiveWatchUser>()
                                .eq(StringUtils.isNotEmpty(liveId),WeLiveWatchUser::getLiveId,liveId)
                                .eq(watchUserType != null, WeLiveWatchUser::getWatchUserType, watchUserType)

                )
                ,"观看成员"
        );

        return AjaxResult.success();
    }


    /**
     * 同步发送结果
     * @param id
     * @return
     */
    @GetMapping("/synchExecuteResult/{id}")
    public AjaxResult synchExecuteResult(@PathVariable String id){
        iWeLiveService.synchExecuteResult(id);

        return AjaxResult.success();

    }
















}
