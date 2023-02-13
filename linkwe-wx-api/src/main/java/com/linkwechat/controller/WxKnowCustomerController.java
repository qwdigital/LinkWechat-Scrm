package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.know.WeKnowCustomerCode;
import com.linkwechat.domain.know.WeKnowCustomerCodeCount;
import com.linkwechat.domain.user.vo.WeUserScreenConditVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 识客码
 */
@Slf4j
@RestController
@RequestMapping("/kc/")
public class WxKnowCustomerController {

    @Autowired
    private IWeKnowCustomerCodeService iWeKnowCustomerCodeService;

    @Autowired
    private IWeCustomerService iWeCustomerService;

    @Autowired
    private IWeKnowCustomerCodeCountService iWeKnowCustomerCodeCountService;


    @Autowired
    private QwSysUserClient qwSysUserClient;








    /**
     * 处理识客码,识别新老客户
     * @param knowCustomerId 识客码id
     * @param unionid 微信unionid
     * @return
     */
    @GetMapping("/handleKnowCustomer")
    public AjaxResult handleKnowCustomer(Long knowCustomerId,String unionid){


        WeKnowCustomerCode weKnowCustomerCode = iWeKnowCustomerCodeService.getById(knowCustomerId);

        if(weKnowCustomerCode != null){
            List<WeCustomer> weCustomers = iWeCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                    .eq(WeCustomer::getUnionid, unionid));
            WeKnowCustomerCodeCount weKnowCustomerCodeCount = WeKnowCustomerCodeCount.builder()
                    .knowCustomerId(knowCustomerId)
                    .unionid(unionid)
                    .build();

            if(CollectionUtil.isNotEmpty(weCustomers)){//老客新客
                weKnowCustomerCodeCount.setNewOrOld(1);
                if(weKnowCustomerCode.getIsAddAllUser().equals(new Integer(1))){//已添加任意成员
                    weKnowCustomerCode.setNewOrOldWeCustomer(false);
                }else{//已添加指定成员
                    String weUserIds=null;String deptIds=null;String positions=null;

                    WeUserScreenConditVo addWeUser = weKnowCustomerCode.getAddWeUser();
                    if(null != addWeUser){
                        WeUserScreenConditVo.ExecuteUserCondit executeUserCondit = addWeUser.getExecuteUserCondit();
                        if(null != executeUserCondit){
                            List<String> weUserIdss = executeUserCondit.getWeUserIds();
                            if(CollectionUtil.isNotEmpty(weUserIdss)){
                                weUserIds= StringUtils.join(weUserIdss,",");
                            }
                        }

                        WeUserScreenConditVo.ExecuteDeptCondit executeDeptCondit = addWeUser.getExecuteDeptCondit();
                        if(null != executeDeptCondit){
                            List<String> deptIdss = executeDeptCondit.getDeptIds();
                            if(CollectionUtil.isNotEmpty(deptIdss)){
                                deptIds=StringUtils.join(deptIdss,",");
                            }
                            List<String> posts = executeDeptCondit.getPosts();
                            if(CollectionUtil.isNotEmpty(posts)){
                                positions=StringUtils.join(posts,",");
                            }
                        }
                    }

                    AjaxResult<List<String>> listAjaxResult
                            = qwSysUserClient.screenConditWeUser(weUserIds,deptIds,positions);
                    if(null != listAjaxResult) {
                        List<String> appointWeUserIds = listAjaxResult.getData();
                        if(CollectionUtil.isNotEmpty( weCustomers.stream().filter(weCustomer
                                -> appointWeUserIds.contains(weCustomer.getAddUserId())).collect(Collectors.toList()))){
                            weKnowCustomerCode.setNewOrOldWeCustomer(false);
                        }
                    }
                }
            }

            //统计入库
            iWeKnowCustomerCodeCountService.save(
                    weKnowCustomerCodeCount
            );
        }
        return AjaxResult.success(weKnowCustomerCode);
    }




}
