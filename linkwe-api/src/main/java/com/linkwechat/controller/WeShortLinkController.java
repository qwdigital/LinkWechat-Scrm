package com.linkwechat.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.domain.shortlink.query.WeShortLinkAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkStatisticQuery;
import com.linkwechat.domain.shortlink.vo.WeShortLinkAddVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkListVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkStatisticsVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkVo;
import com.linkwechat.service.IWeShortLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author danmo
 * @description 短链管理
 * @date 2022/12/18 18:22
 **/

@RestController
@RequestMapping(value = "/short/link")
@Api(tags = "短链管理")
public class WeShortLinkController extends BaseController {

    @Autowired
    private IWeShortLinkService weShortLinkService;

    @Autowired
    private RedisService redisService;


    @ApiOperation(value = "校验短链环境", httpMethod = "GET")
    @GetMapping("/check/env")
    public AjaxResult<Boolean> checkEnv() {
        Boolean result = weShortLinkService.checkEnv();
        return AjaxResult.success(result);
    }

    @ApiOperation(value = "新增短链", httpMethod = "POST")
    @Log(title = "新增短链", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult<WeShortLinkAddVo> addShortLink(@RequestBody @Validated WeShortLinkAddQuery query) {
        WeShortLinkAddVo result = weShortLinkService.addShortLink(query);
        return AjaxResult.success(result);
    }


    @ApiOperation(value = "修改短链", httpMethod = "PUT")
    @Log(title = "修改短链", businessType = BusinessType.UPDATE)
    @PutMapping("/update/{id}")
    public AjaxResult<WeShortLinkAddVo> updateShortLink(@PathVariable("id") Long id, @RequestBody @Validated WeShortLinkAddQuery query) {
        query.setId(id);
        WeShortLinkAddVo result = weShortLinkService.updateShortLink(query);
        return AjaxResult.success(result);
    }


    @ApiOperation(value = "删除短链", httpMethod = "DELETE")
    @Log(title = "删除短链", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public AjaxResult deleteShortLink(@PathVariable("ids") List<Long> ids) {
        weShortLinkService.deleteShortLink(ids);
        return AjaxResult.success();
    }

    @ApiOperation(value = "通过ID获取短链详情", httpMethod = "GET")
    @Log(title = "短链详情", businessType = BusinessType.SELECT)
    @GetMapping("/get/{id}")
    public AjaxResult<WeShortLinkVo> getShortLinkInfo(@PathVariable("id") Long id) {
        WeShortLinkVo result = weShortLinkService.getShortLinkInfo(id);
        return AjaxResult.success(result);
    }

    @ApiOperation(value = "通过短链接获取短链详情", httpMethod = "GET")
    @Log(title = "短链详情", businessType = BusinessType.SELECT)
    @GetMapping(value = {"/getByShort/{shortUrl}", "/getByShort/{shortUrl}/{promotionId}"})
    public AjaxResult<WeShortLinkVo> getShortLinkInfo(
            @PathVariable("shortUrl") String shortUrl,
            @PathVariable(value = "promotionId", required = false) Long promotionId) {
        long id = Base62NumUtil.decode(shortUrl);
        WeShortLinkVo result = weShortLinkService.getShortLinkInfo(id);
        //短链
        redisService.increment(WeConstans.WE_SHORT_LINK_KEY + WeConstans.OPEN_APPLET + shortUrl);

        //短链推广
        if (ObjectUtil.isNotEmpty(promotionId)) {
            String encode = Base62NumUtil.encode(promotionId);
            redisService.increment(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.OPEN_APPLET + encode);
        }
        return AjaxResult.success(result);
    }


    @ApiOperation(value = "短链列表", httpMethod = "GET")
    @Log(title = "短链列表", businessType = BusinessType.SELECT)
    @GetMapping("/list")
    public TableDataInfo<PageInfo<WeShortLinkListVo>> getShortLinkList(WeShortLinkQuery query) {
        startPage();
        PageInfo<WeShortLinkListVo> shortLinkList = weShortLinkService.getShortLinkList(query);
        return getDataTable(shortLinkList);
    }


    @ApiOperation(value = "短链数据统计（传ID查单个）", httpMethod = "GET")
    @Log(title = "短链数据统计", businessType = BusinessType.SELECT)
    @GetMapping("/data/statistics")
    public AjaxResult<WeShortLinkStatisticsVo> getDataStatistics(WeShortLinkStatisticQuery query) {
        if (Objects.isNull(query)) {
            throw new WeComException("入参不能为空");
        }
        WeShortLinkStatisticsVo result = weShortLinkService.getDataStatistics(query);
        return AjaxResult.success(result);
    }

    @ApiOperation(value = "短链折线统计", httpMethod = "GET")
    @Log(title = "短链折线统计", businessType = BusinessType.SELECT)
    @GetMapping("/line/statistics")
    public AjaxResult<WeShortLinkStatisticsVo> getLineStatistics(WeShortLinkStatisticQuery query) {
        if (Objects.isNull(query)) {
            throw new WeComException("入参不能为空");
        }
        if (Objects.isNull(query.getId())) {
            throw new WeComException("ID不能为空");
        }
        if (Objects.isNull(query.getBeginTime())) {
            throw new WeComException("开始时间不能为空");
        }
        WeShortLinkStatisticsVo result = weShortLinkService.getLineStatistics(query);
        return AjaxResult.success(result);
    }

}
