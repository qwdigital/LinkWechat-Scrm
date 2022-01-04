package com.linkwechat.web.controller.wecom;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.CommunityTaskType;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.ValidateUtils;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.wecom.domain.WeCustomerSeas;
import com.linkwechat.wecom.domain.vo.CustomerSeasRecordVo;
import com.linkwechat.wecom.service.IWeCustomerMessagePushService;
import com.linkwechat.wecom.service.IWeCustomerSeasService;
import com.linkwechat.wecom.service.IWeMessagePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 客户公海相关
 */
@RestController
@RequestMapping("/wecom/seas")
public class WeCustomerSeasController extends BaseController {

    @Autowired
    private IWeCustomerSeasService iWeCustomerSeasService;






    /**
     * 客户公海模版下载
     * @return
     */
    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {

        ExcelUtil<WeCustomerSeas> util = new ExcelUtil<WeCustomerSeas>(WeCustomerSeas.class);
        return util.importTemplateExcel( DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD)+"_客户公海");
    }



    /**
     * 导入公海数据
     * @param file
     * @param weCustomerSea
     * @return
     */
    @PostMapping("/importData")
    public AjaxResult  importData(MultipartFile file, WeCustomerSeas weCustomerSea) throws Exception {
        ExcelUtil<WeCustomerSeas> util = new ExcelUtil<WeCustomerSeas>(WeCustomerSeas.class);
        List<WeCustomerSeas> weCustomerSeas = util.importExcel(file.getInputStream());
        String tip=new String("成功导入{0}条，去重复{1}条");
        if(CollectionUtil.isNotEmpty(weCustomerSeas)){

            if(StringUtils.isEmpty(weCustomerSea.getAddUserName())&&StringUtils.isEmpty(weCustomerSea.getAddUserId())){
                return AjaxResult.error("分配人不可为空！");
            }
            //过滤不符合规范的手机号
            List<WeCustomerSeas> noRuleWeCustomerSeas
                    = weCustomerSeas.stream().filter(s -> ValidateUtils.isMobile(s.getPhone())).collect(Collectors.toList());

            if(CollectionUtil.isEmpty(noRuleWeCustomerSeas)){
                return AjaxResult.error("请传入合法手机号！");
            }

            //过滤字段为空的数据
            List<WeCustomerSeas> deduplicationSeas = noRuleWeCustomerSeas.stream().filter(s -> StringUtils.isNotEmpty(s.getCustomerName())
                    || StringUtils.isNotEmpty(s.getPhone())).collect(Collectors.toList());
            if(CollectionUtil.isEmpty(deduplicationSeas)){
                return AjaxResult.error("导入用户数据不能为空！");
            }
            //根据手机号去重(去除excel中重复的号码)
            List<WeCustomerSeas> deduplicationSeasNoRepeat=deduplicationSeas.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(WeCustomerSeas :: getPhone))), ArrayList::new));


            //根据手机号去除数据库与excel中重复号码
            List<WeCustomerSeas> dbExist = iWeCustomerSeasService.list(new LambdaQueryWrapper<WeCustomerSeas>()
                    .in(WeCustomerSeas::getPhone, deduplicationSeasNoRepeat.stream()
                            .map(WeCustomerSeas::getPhone).collect(Collectors.toList())));
            if(CollectionUtil.isNotEmpty(dbExist)){
                List<WeCustomerSeas> noRepetWeCustomerSeas
                        = deduplicationSeasNoRepeat.stream().filter(item ->
                        !dbExist.stream().map(e->e.getPhone()).collect(Collectors.toList())
                                .contains(item.getPhone())).collect(Collectors.toList());
                deduplicationSeasNoRepeat.clear();
                deduplicationSeasNoRepeat.addAll(
                        noRepetWeCustomerSeas
                );
            }


              if(CollectionUtil.isNotEmpty(deduplicationSeasNoRepeat)){

                  Long tabaleExcelId= SnowFlakeUtil.nextId();

                  List<String> userIds = ListUtil.toList(weCustomerSea.getAddUserId().split(","));
                  List<String> userNames = ListUtil.toList(weCustomerSea.getAddUserName().split(","));

                  IntStream.range(0,deduplicationSeasNoRepeat.size()).forEach(i->{
                      WeCustomerSeas k = deduplicationSeasNoRepeat.get(i);
                      if(Objects.nonNull(k)){
                          k.setTagIds(weCustomerSea.getTagIds());
                          k.setTagNames(weCustomerSea.getTagNames());
                          k.setAddUserId( userIds.get(Math.floorMod(i, userIds.size())));
                          k.setAddUserName(userNames.get(Math.floorMod(i, userNames.size())));
                          k.setTableExcelName(file.getOriginalFilename());
                          k.setTableExcelId(tabaleExcelId);
                      }
                  });
                  if(iWeCustomerSeasService.saveBatch(deduplicationSeasNoRepeat)){
                      //发送提醒
                      deduplicationSeasNoRepeat.stream().collect(Collectors.groupingBy(WeCustomerSeas::getAddUserId)).forEach((k,v)->{

                          iWeCustomerSeasService.remidUser(ListUtil.toList(k),v.size());


                      });

                  }

                  tip = MessageFormat.format(tip, new Object[]{new Integer(deduplicationSeasNoRepeat.size()).toString(),
                          new Integer( weCustomerSeas.size()-deduplicationSeasNoRepeat.size()).toString()});

              }else{
                  tip = MessageFormat.format(tip, new Object[] { "0",weCustomerSeas.size() });
              }


           ;
        }else{
            return AjaxResult.error("导入用户数据不能为空！");
        }

        return AjaxResult.success(tip);
    }


    /**
     * 获取公海客户列表
     * @param weCustomerSea
     * @return
     */
    @GetMapping("list")
    public TableDataInfo list(WeCustomerSeas weCustomerSea){

        startPage();
        List<WeCustomerSeas> weCustomerSeas = iWeCustomerSeasService.list(
                new LambdaQueryWrapper<WeCustomerSeas>()
                        .like(StringUtils.isNotEmpty(weCustomerSea.getPhone()),WeCustomerSeas::getPhone,weCustomerSea.getPhone())
                        .like(StringUtils.isNotEmpty(weCustomerSea.getCustomerName()),WeCustomerSeas::getCustomerName,weCustomerSea.getCustomerName())
                        .like(StringUtils.isNotEmpty(weCustomerSea.getAddUserName()),WeCustomerSeas::getAddUserName,weCustomerSea.getAddUserName())
                        .eq(weCustomerSea.getAddState() !=null ,WeCustomerSeas::getAddState,weCustomerSea.getAddState())
                        .orderByDesc(WeCustomerSeas::getCreateTime)
        );

        return getDataTable(weCustomerSeas);
    }


    /**
     * 删除公海客户
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        iWeCustomerSeasService.removeByIds(
                ListUtil.toList(ids)
        );
        return AjaxResult.success();
    }


    /**
     * 提醒员工
     * @param ids
     * @return
     */
    @PostMapping("/remidUser/{ids}")
    public AjaxResult remidUser(@PathVariable Long[] ids){

        List<WeCustomerSeas> weCustomerSeas
                = iWeCustomerSeasService.listByIds(ListUtil.toList(ids));

        if(CollectionUtil.isNotEmpty(weCustomerSeas)){
            weCustomerSeas.stream().collect(Collectors.groupingBy(WeCustomerSeas::getAddUserId)).forEach((k,v)->{
                iWeCustomerSeasService.remidUser(ListUtil.toList(k),v.size());



            });

        }

        return AjaxResult.success();
    }


    /**
     * 获取指定员工所拥有的状态(移动端)
     * @param userId
     * @param addState
     * @return
     */
    @GetMapping("/findEmployeeCustomer")
    public TableDataInfo findEmployeeCustomer(String userId,Integer addState){
          startPage();
         List<WeCustomerSeas> weCustomerSeas = iWeCustomerSeasService.list(new LambdaQueryWrapper<WeCustomerSeas>()
                .eq(StringUtils.isNotEmpty(userId),WeCustomerSeas::getAddUserId, userId)
                .eq(addState != null,WeCustomerSeas::getAddState, addState));

        return getDataTable(weCustomerSeas);
    }


    /**
     * 详情头部统计
     * @return
     */
    @GetMapping("/countCustomerSeas")
    public AjaxResult countCustomerSeas(){

        return AjaxResult.success(
                iWeCustomerSeasService.countCustomerSeas()
        );
    }


    /**
     * 导入记录或员工添加统计
     * @param groupByType 1:获取导入记录数据 2:员工添加统计
     * @return
     */
    @GetMapping("/findSeasRecord")
    public TableDataInfo findSeasRecord(@RequestParam(defaultValue = "1") Integer groupByType){
        startPage();
        List<CustomerSeasRecordVo> seasRecord = iWeCustomerSeasService.findSeasRecord(groupByType);

        return getDataTable(seasRecord);
    }


    /**
     * 设置公海客户状态
     * @return
     */
    @PostMapping("/setState")
    public AjaxResult setState(@RequestBody WeCustomerSeas weCustomerSea){
        if(StringUtils.isEmpty(weCustomerSea.getPhone())){
            return AjaxResult.error("手机号不可为空");
        }
        iWeCustomerSeasService.update(WeCustomerSeas.builder().addState(new Integer(1)).build()
                ,new LambdaQueryWrapper<WeCustomerSeas>().eq(WeCustomerSeas::getPhone,weCustomerSea.getPhone()));

        return AjaxResult.success();
    }



}
