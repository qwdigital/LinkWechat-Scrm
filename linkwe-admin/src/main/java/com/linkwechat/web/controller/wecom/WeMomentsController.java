package com.linkwechat.web.controller.wecom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeMessagePush;
import com.linkwechat.wecom.domain.WeMoments;
import com.linkwechat.wecom.service.IWeMomentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 朋友圈相关
 */
@RestController
@RequestMapping("/wecom/moments")
public class WeMomentsController extends BaseController {


    @Autowired
    IWeMomentsService iWeMomentsService;


    /**
     * 获取朋友圈列表
     * @param weMoments
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo list(WeMoments weMoments) {
        startPage();
        return getDataTable(
                iWeMomentsService.list(
                        new LambdaQueryWrapper<WeMoments>()
                                .eq(StringUtils.isNotEmpty(weMoments.getCreateBy()), WeMoments::getCreateBy
                                        , weMoments.getCreateBy())
                                .apply(weMoments.getBeginTime() != null, "to_date(create_time::text,'YYYY-MM-DD') >= to_date('" + weMoments.getBeginTime() + "','YYYY-MM-DD')")
                                .apply(weMoments.getEndTime() != null, "to_date(create_time::text,'YYYY-MM-DD') <= to_date('" + weMoments.getEndTime() + "','YYYY-MM-DD')")
                )
        );
    }


    /**
     * 新增或者编辑朋友圈
     * @return
     */
    @PostMapping("/addOrUpdate")
    public AjaxResult addOrUpdate(@RequestBody WeMoments weMoments){

        iWeMomentsService.addOrUpdateMoments(weMoments);


        return AjaxResult.success();
    }





}
