package com.linkwechat.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.annotation.ShortLinkView;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.ip.IpUtils;
import com.linkwechat.domain.index.vo.WeIndexVo;
import com.linkwechat.domain.operation.vo.WeCustomerAnalysisVo;
import com.linkwechat.domain.operation.vo.WeGroupAnalysisVo;
import com.linkwechat.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 首页相关
 */
@Api(tags = "首页管理")
@Slf4j
@RestController
public class WeIndexController {
    @Autowired
    private IWeCustomerService iWeCustomerService;


    @Autowired
    private IWeOperationCenterService weOperationCenterService;


    @Autowired
    private IWeShortLinkService weShortLinkService;

    @Autowired
    private IWeQrCodeService weQrCodeService;

    @Autowired
    private IWeGroupCodeService iWeGroupCodeService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IWeCustomerLinkService iWeCustomerLinkService;

    /**
     * 系统首页相关基础数据获取
     *
     * @return
     * @throws ParseException
     */
    @GetMapping("/getWeIndex")
    public AjaxResult<WeIndexVo> getWeIndex() throws ParseException {
        WeIndexVo weIndexVo = new WeIndexVo();
        weIndexVo.setCurrentEdition("标准版");
        weIndexVo.setUserNumbers(iWeCustomerService.findAllSysUser().size());

        WeCustomerAnalysisVo customerAnalysis = weOperationCenterService.getCustomerAnalysis();
        if (null != customerAnalysis) {
            weIndexVo.setCustomerTotalNumber(
                    customerAnalysis.getTotalCnt()
            );
        }

        WeGroupAnalysisVo groupAnalysis = weOperationCenterService.getGroupAnalysis();
        if (null != groupAnalysis) {
            weIndexVo.setGroupTotalNumber(
                    groupAnalysis.getTotalCnt()
            );
            weIndexVo.setGroupMemberTotalNumber(
                    groupAnalysis.getMemberTotalCnt()
            );
        }

        weIndexVo.setSynchTime(DateUtil.date());

        return AjaxResult.success(weIndexVo);
    }

    @ShortLinkView(prefix = "t:")
    @ApiOperation(value = "短链换取长链", httpMethod = "GET")
    @GetMapping(value = {"/t/{shortUrl}", "/t/{shortUrl}/{promotionKey}"})
    public void getShort2LongUrl(HttpServletRequest request,
                                 HttpServletResponse resp,
                                 @PathVariable("shortUrl") String shortUrl,
                                 @PathVariable(value = "promotionKey", required = false) String promotionKey) throws IOException {

        log.info("短链换取长链 shortUrl:{},promotionKey:{}", shortUrl, promotionKey);
        if (StringUtils.isEmpty(shortUrl)) {
            return;
        }
        String ipAddr = IpUtils.getIpAddr(request);
        String key = "we:t:shortUrl:" + ipAddr + ":" + shortUrl;

        JSONObject short2LongUrl = new JSONObject();
        //判断键是否存在
        Boolean hasKey = redisService.hasKey(key);
        if (hasKey) {
            short2LongUrl = (JSONObject) redisService.getCacheObject(key);
        } else {
            //尝试加锁
            Boolean lock = redisService.tryLock(key, "lock", 2L);
            if (lock) {
                short2LongUrl = weShortLinkService.getShort2LongUrl(shortUrl, promotionKey);
                log.info("短链换取长链的数据：{}", short2LongUrl.toJSONString());
                if (StringUtils.isNotEmpty(short2LongUrl.getString("errorMsg"))) {
                    redisService.setCacheObject(key, short2LongUrl, 5, TimeUnit.MINUTES);
                } else {
                    redisService.setCacheObject(key, short2LongUrl, 1, TimeUnit.DAYS);
                }
                //释放锁
                redisService.unLock(key, "lock");
            } else {
                throw new WeComException("操作过于频繁，请稍后再试");
            }
        }
        String result = ResourceUtil.readUtf8Str("templates/jump.html");
        resp.setHeader("Content-Type", "text/html; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = resp.getWriter();
        writer.write(StringUtils.format(result, short2LongUrl.toJSONString()).toCharArray());
        writer.close();
    }


    @ShortLinkView(prefix = "pqr:")
    @ApiOperation(value = "活码短链换取长链", httpMethod = "GET")
    @GetMapping(value = "/pqr/{shortUrl}")
    public void getQrShort2LongUrl(HttpServletRequest request,
                                 HttpServletResponse resp,
                                 @PathVariable("shortUrl") String shortUrl) throws IOException {
        log.info("活码短链换取长链 shortUrl:{}", shortUrl);

        if (StringUtils.isEmpty(shortUrl) || Objects.equals("undefined",shortUrl)) {
            return;
        }
        String ipAddr = IpUtils.getIpAddr(request);

        String key = "we:qr:shortUrl:" + ipAddr + ":" + shortUrl;

        JSONObject short2LongUrl = new JSONObject();
        //判断键是否存在
        Boolean hasKey = redisService.hasKey(key);
        if (hasKey) {
            short2LongUrl = (JSONObject) redisService.getCacheObject(key);
        } else {
            //尝试加锁
            Boolean lock = redisService.tryLock(key, "lock", 2L);
            if (lock) {
                short2LongUrl = weQrCodeService.getShort2LongUrl(shortUrl);
                log.info("活码短链换取长链的数据：{}", short2LongUrl.toJSONString());
                if (StringUtils.isNotEmpty(short2LongUrl.getString("errorMsg"))) {
                    redisService.setCacheObject(key, short2LongUrl, 5, TimeUnit.MINUTES);
                } else {
                    redisService.setCacheObject(key, short2LongUrl, 1, TimeUnit.DAYS);
                }
                //释放锁
                redisService.unLock(key, "lock");
            } else {
                throw new WeComException("操作过于频繁，请稍后再试");
            }
        }
        String result = ResourceUtil.readUtf8Str("templates/jump.html");
        resp.setHeader("Content-Type", "text/html; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = resp.getWriter();
        writer.write(StringUtils.format(result, short2LongUrl.toJSONString()).toCharArray());
        writer.close();
    }

    @ShortLinkView(prefix = "gqr:")
    @ApiOperation(value = "群活码短链换取长链", httpMethod = "GET")
    @GetMapping(value = "/gqr/{shortUrl}")
    public void getGroupQrShort2LongUrl(HttpServletRequest request,
                                   HttpServletResponse resp,
                                   @PathVariable("shortUrl") String shortUrl) throws IOException {
        log.info("群活码短链换取长链 shortUrl:{}", shortUrl);

        if (StringUtils.isEmpty(shortUrl) || Objects.equals("undefined",shortUrl)) {
            return;
        }
        String ipAddr = IpUtils.getIpAddr(request);

        String key = "we:gqr:shortUrl:" + ipAddr + ":" + shortUrl;

        JSONObject short2LongUrl = new JSONObject();
        //判断键是否存在
        Boolean hasKey = redisService.hasKey(key);
        if (hasKey) {
            short2LongUrl = (JSONObject) redisService.getCacheObject(key);
        } else {
            //尝试加锁
            Boolean lock = redisService.tryLock(key, "lock", 2L);
            if (lock) {
                short2LongUrl = iWeGroupCodeService.getShort2LongUrl(shortUrl);
                log.info("群活码短链换取长链的数据：{}", short2LongUrl.toJSONString());
                if (StringUtils.isNotEmpty(short2LongUrl.getString("errorMsg"))) {
                    redisService.setCacheObject(key, short2LongUrl, 5, TimeUnit.MINUTES);
                } else {
                    redisService.setCacheObject(key, short2LongUrl, 1, TimeUnit.DAYS);
                }
                //释放锁
                redisService.unLock(key, "lock");
            } else {
                throw new WeComException("操作过于频繁，请稍后再试");
            }
        }
        String result = ResourceUtil.readUtf8Str("templates/jump.html");
        resp.setHeader("Content-Type", "text/html; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = resp.getWriter();
        writer.write(StringUtils.format(result, short2LongUrl.toJSONString()).toCharArray());
        writer.close();
    }


    @ShortLinkView(prefix = "l:")
    @ApiOperation(value = "获客助手短链换取长链", httpMethod = "GET")
    @GetMapping(value = "/l/{shortUrl}")
    public void getLinkShort2LongUrl(HttpServletRequest request,
                                     HttpServletResponse resp,
                                     @PathVariable("shortUrl") String shortUrl) throws IOException {
        log.info("获客助手短链换取长链 shortUrl:{}", shortUrl);

        if (StringUtils.isEmpty(shortUrl) || Objects.equals("undefined",shortUrl)) {
            return;
        }
        String ipAddr = IpUtils.getIpAddr(request);

        String key = "we:link:linkUrl:" + ipAddr + ":" + shortUrl;

        JSONObject short2LongUrl = new JSONObject();
        //判断键是否存在
        Boolean hasKey = redisService.hasKey(key);
        if (hasKey) {
            short2LongUrl = (JSONObject) redisService.getCacheObject(key);
        } else {
            //尝试加锁
            Boolean lock = redisService.tryLock(key, "lock", 2L);
            if (lock) {
                short2LongUrl = iWeCustomerLinkService.getShort2LongUrl(shortUrl);
                log.info("活码短链换取长链的数据：{}", short2LongUrl.toJSONString());
                if (StringUtils.isNotEmpty(short2LongUrl.getString("errorMsg"))) {
                    redisService.setCacheObject(key, short2LongUrl, 5, TimeUnit.MINUTES);
                } else {
                    redisService.setCacheObject(key, short2LongUrl, 1, TimeUnit.DAYS);
                }
                //释放锁
                redisService.unLock(key, "lock");
            } else {
                throw new WeComException("操作过于频繁，请稍后再试");
            }
        }

        resp.sendRedirect(short2LongUrl.getString("linkUrl"));
    }
}
