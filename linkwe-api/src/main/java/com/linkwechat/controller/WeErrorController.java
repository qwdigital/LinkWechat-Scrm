package com.linkwechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeErrorMsg;
import com.linkwechat.domain.live.WeLive;
import com.linkwechat.service.IWeErrorMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 获取错误信息
 */
@RestController
public class WeErrorController extends BaseController {

    @Autowired
    private IWeErrorMsgService iWeErrorMsgService;


    /**
     *  获取错误信息列表
     * @param weErrorMsg
     * @return
     */
    @GetMapping("/findWeError")
    public TableDataInfo findWeError(WeErrorMsg weErrorMsg){
        return getDataTable(
                iWeErrorMsgService.list(
                        new LambdaQueryWrapper<WeErrorMsg>()
                                .like(StringUtils.isNotEmpty(weErrorMsg.getUrl()), WeErrorMsg::getUrl, weErrorMsg.getUrl())
                                .apply(weErrorMsg.getBeginTime() != null && weErrorMsg.getEndTime() != null,
                                        "date_format(create_time,'%Y-%m-%d') BETWEEN '"+
                                                weErrorMsg.getBeginTime()
                                                +"' AND '"+
                                                weErrorMsg.getEndTime()+"'")
                                .orderByDesc(WeErrorMsg::getCreateTime)
                )
        );
    }


    /**
     * 清空所有数据
     * @return
     */
    @GetMapping("/removeWeError")
    public AjaxResult removeWeError(){
        iWeErrorMsgService.remove(new LambdaQueryWrapper<>());

        return AjaxResult.success();
    }





}
