package com.linkwechat.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.LinkWeApiApplication;
import com.linkwechat.domain.wx.coupon.WxSendCouponQuery;
import com.linkwechat.service.IWeCouponService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@Slf4j
@SpringBootTest(classes = LinkWeApiApplication.class)
class WeCouponControllerTest {

}