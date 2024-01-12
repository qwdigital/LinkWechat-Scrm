package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.poi.LwExcelUtil;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeKeyWordGroupSub;
import com.linkwechat.domain.WeKeywordGroupViewCount;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import com.linkwechat.domain.community.WeKeywordGroupTask;
import com.linkwechat.domain.community.query.WeCommunityKeyWordGroupTableQuery;
import com.linkwechat.domain.community.query.WeCommunityNewGroupQuery;
import com.linkwechat.domain.community.vo.WeCommunityKeyWordGroupTableVo;
import com.linkwechat.domain.community.vo.WeKeywordGroupViewCountVo;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.service.IWeCommunityKeywordToGroupService;
import com.linkwechat.service.IWeGroupService;
import com.linkwechat.service.IWeKeyWordGroupSubService;
import com.linkwechat.service.IWeKeywordGroupViewCountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 关键词拉群
 */
@RestController
@RequestMapping(value = "/communityKeywordGroup")
public class WeCommunityKeywordGroupController extends BaseController {

    @Autowired
    private IWeCommunityKeywordToGroupService keywordToGroupService;


    @Autowired
    private IWeKeyWordGroupSubService iWeKeyWordGroupSubService;

    @Autowired
    private IWeKeywordGroupViewCountService iWeKeywordGroupViewCountService;


    @Autowired
    private IWeGroupService iWeGroupService;


    /**
     * 申请构建主键
     * @return
     */
    @GetMapping("/applyToBuildPrimaryKey")
    public AjaxResult<String> applyToBuildPrimaryKey(){


        return AjaxResult.success(SnowFlakeUtil.nextId());
    }


    /**
     * 新增或更新关键词群基础信息
     * @param groupTask
     * @return
     */
    @PostMapping("/createOrUpdateBaseInfo")
    public AjaxResult<WeKeywordGroupTask> createOrUpdateBaseInfo(@RequestBody WeKeywordGroupTask groupTask) throws IOException {

        if(groupTask.getId()==null){
            return AjaxResult.error("关键词群主键不可为空");
        }
        keywordToGroupService.createOrUpdate(groupTask);
        return AjaxResult.success(groupTask);
    }


    /**
     * 点击取消时触发(避免还未新建,导致群活码创建过多占位)
     * @param groupTask
     * @return
     */
    @PostMapping("/cancelCreateBaseInfo")
    public AjaxResult cancelCreateBaseInfo(@RequestBody WeKeywordGroupTask groupTask){
        if(groupTask.getId()==null){
            return AjaxResult.error("关键词群主键不可为空");
        }
        WeKeywordGroupTask keywordGroupTask = keywordToGroupService.getById(groupTask.getId());
        if(null == keywordGroupTask){
            iWeKeyWordGroupSubService.batchRemoveWeKeyWordGroupByKeyWordId(groupTask.getId());
        }

        return AjaxResult.success();
    }


    /**
     * 获取关键词群列表
     * @param task
     * @return
     */
    @GetMapping("/findLists")
    public TableDataInfo<List<WeKeywordGroupTask>> findLists(WeKeywordGroupTask task){
        startPage();
        List<WeKeywordGroupTask> groupTasks = keywordToGroupService.findLists(task);

        return getDataTable(groupTasks);
    }


    /**
     * 获取关键词群基础信息
     * @param id
     * @return
     */
    @GetMapping("/getKeyWordGroupBaseInfo/{id}")
    public AjaxResult<WeKeywordGroupTask> getKeyWordGroupBaseInfo(@PathVariable Long id){

        return AjaxResult.success(
                keywordToGroupService.findBaseInfo(id,null,false)
        );
    }

    /**
     * 关键词群列表
     * @return
     */
    @GetMapping("/findWeKeyWordGroupSubs")
    public TableDataInfo<List<WeKeyWordGroupSub>> findWeKeyWordGroupSubs(WeKeyWordGroupSub keyWordGroupSub){
                startPage();
        List<WeKeyWordGroupSub> weKeyWordGroupSubs = iWeKeyWordGroupSubService.list(new LambdaQueryWrapper<WeKeyWordGroupSub>()
                .eq(WeKeyWordGroupSub::getKeywordGroupId,keyWordGroupSub.getKeywordGroupId()));

        return getDataTable(weKeyWordGroupSubs);
    }

    /**
     * 创建关键词群
     * @param weKeyWordGroupSub
     * @return
     */
    @PostMapping("/createKeyWordGroupSub")
    public AjaxResult<WeKeyWordGroupSub> createKeyWordGroupSub(@RequestBody WeKeyWordGroupSub weKeyWordGroupSub){

        if(weKeyWordGroupSub.getKeywordGroupId()==null){
            return AjaxResult.error("关键词群主键不可为空");
        }

        iWeKeyWordGroupSubService.createWeKeyWordGroup(weKeyWordGroupSub);

        return AjaxResult.success(weKeyWordGroupSub);
    }



    /**
     * 更新关键词群
     * @param weKeyWordGroupSub
     * @return
     */
    @PostMapping("/updateKeyWordGroupSub")
    public AjaxResult<WeKeyWordGroupSub> updateKeyWordGroupSub(@RequestBody WeKeyWordGroupSub weKeyWordGroupSub){


        iWeKeyWordGroupSubService.updateWeKeyWordGroup(weKeyWordGroupSub);

        return AjaxResult.success(weKeyWordGroupSub);
    }


    /**
     * 批量更新关键词群(主要针对顺序的调整)
     * @param weKeywordGroupTask
     * @return
     */
    @PostMapping("/batchUpdateKeyWordGroupSub")
    public AjaxResult batchUpdateKeyWordGroupSub(@RequestBody WeKeywordGroupTask weKeywordGroupTask){

        List<WeKeyWordGroupSub> keyWordGroupSubs = weKeywordGroupTask.getKeyWordGroupSubs();

        if(CollectionUtil.isNotEmpty(keyWordGroupSubs)){
            iWeKeyWordGroupSubService.updateBatchById(keyWordGroupSubs);
        }
        return AjaxResult.success();

    }


    /**
     * 删除关键词群
     * @param ids
     * @return
     */
    @DeleteMapping("/batchRemoveKeyWordGroup/{ids}")
    public AjaxResult batchRemoveKeyWordGroup(@PathVariable Long[] ids){
        ListUtil.toList(ids).forEach(id->{
            iWeKeyWordGroupSubService.batchRemoveWeKeyWordGroupByKeyWordId(id);
        });
        return AjaxResult.success();
    }

    /**
     * 删除客户群活码
     */
    @DeleteMapping("/batchRemoveKeyWordGroupSub/{ids}")
    public AjaxResult batchRemoveKeyWordGroupSub(@PathVariable Long[] ids) {

        iWeKeyWordGroupSubService.batchRemoveWeKeyWordGroupByIds(Arrays.asList(ids));
        return AjaxResult.success();
    }


    /**
     * 获取详情头部统计
     * @param keywordGroupId
     * @return
     */
    @GetMapping("/countTab/{keywordGroupId}")
    public AjaxResult<WeKeywordGroupViewCountVo> countTab(@PathVariable Long keywordGroupId){
        return AjaxResult.success(
                iWeKeywordGroupViewCountService.countTab(keywordGroupId)
        );
    }


    /**
     * 折线图统计
     * @param groupViewCount
     * @return
     */
    @GetMapping("/countTrend")
    public AjaxResult<List<WeKeywordGroupViewCountVo>> countTrend(WeKeywordGroupViewCount groupViewCount){

        return AjaxResult.success(
                iWeKeywordGroupViewCountService.countTrend(groupViewCount)
        );
    }


    /**
     * 数据明细
     * @param query
     * @return
     */
    @GetMapping("/findKeyWordGroupTable")
   public TableDataInfo<WeCommunityKeyWordGroupTableVo> findKeyWordGroupTable(WeCommunityKeyWordGroupTableQuery query){
        TableDataInfo tableDataInfo=new TableDataInfo();
        PageInfo<WeCommunityKeyWordGroupTableVo> keyWordGroupTable =
                iWeKeywordGroupViewCountService.findKeyWordGroupTable(query, TableSupport.buildPageRequest());
        tableDataInfo.setTotal(keyWordGroupTable.getTotal());
        List<WeCommunityKeyWordGroupTableVo> groupTableList = keyWordGroupTable.getList();
        if(CollectionUtil.isNotEmpty(groupTableList)){
            iWeKeywordGroupViewCountService.setJoinGroupNumber(groupTableList,query.getStates());
        }
        tableDataInfo.setRows(
                groupTableList
        );
        return tableDataInfo;
    }


    /**
     * 关键词群数据明细导出
     *
     * @param query 请求参数
     * @author WangYX
     * @date 2023/08/23 14:00
     */
    @GetMapping("/exprotKeyWordGroupTable")
    public void exprotKeyWordGroupTable(WeCommunityKeyWordGroupTableQuery query) {
        List<WeCommunityKeyWordGroupTableVo> tableVos = iWeKeywordGroupViewCountService.exprotKeyWordGroupTable(query);
        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WeCommunityKeyWordGroupTableVo.class,tableVos,"关键词群_" + System.currentTimeMillis()
        );
    }


    /**
     * 获取当前客户对应的群
     * @param query
     * @return
     */
    @GetMapping("/findWeKeyWordGroupChatTable")
    public TableDataInfo<WeGroup> findWeKeyWordGroupChatTable(WeCommunityKeyWordGroupTableQuery query){
        List<WeGroup> weGroups =new ArrayList<>();
        List<WeKeyWordGroupSub> weKeyWordGroupSubs = iWeKeyWordGroupSubService.list(new LambdaQueryWrapper<WeKeyWordGroupSub>()
                .eq(WeKeyWordGroupSub::getKeywordGroupId, query.getKeywordGroupId()));

        if(CollectionUtil.isNotEmpty(weKeyWordGroupSubs)){
            startPage();
            weGroups=iWeGroupService
                    .findGroupByUserId(query.getExternalUserid()
                            ,  weKeyWordGroupSubs.stream().map(WeKeyWordGroupSub::getGroupCodeState).collect(Collectors.joining(",")));
        }

        return getDataTable(weGroups);
    }







}
