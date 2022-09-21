package com.linkwechat.web.controller.system;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.annotation.RepeatSubmit;
import com.linkwechat.common.constant.UserConstants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.system.domain.SysConfig;
import com.linkwechat.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 共建代码测试controller
 * 
 * @author Dana
 */
@RestController
@RequestMapping("/elite")
public class EliteController extends BaseController
{
    @Autowired
    private ISysConfigService configService;

}
