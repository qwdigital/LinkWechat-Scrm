package com.linkwechat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.protobuf.ServiceException;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.enums.ProductOrderStateEnum;
import com.linkwechat.common.enums.ProductRefundOrderStateEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WeProduct;
import com.linkwechat.domain.WeProductOrder;
import com.linkwechat.domain.product.order.query.WePlaceAnOrderQuery;
import com.linkwechat.domain.product.order.query.WeProductOrderQuery;
import com.linkwechat.domain.product.order.vo.WeProductOrderVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeCorpAccountMapper;
import com.linkwechat.mapper.WeProductMapper;
import com.linkwechat.mapper.WeProductOrderMapper;
import com.linkwechat.service.IWeProductOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * 商品订单表
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/21 18:20
 */
@Service
public class WeProductOrderServiceImpl extends ServiceImpl<WeProductOrderMapper, WeProductOrder> implements IWeProductOrderService {

    @Resource
    private WeProductOrderMapper weProductOrderMapper;
    @Resource
    private WeProductMapper weProductMapper;
    @Resource
    private WeCorpAccountMapper weCorpAccountMapper;
    @Resource
    private QwSysUserClient qwSysUserClient;

    @Override
    public List<WeProductOrderVo> list(WeProductOrderQuery query) {
        List<WeProductOrderVo> list = weProductOrderMapper.list(query);
        if (list != null && list.size() > 0) {
            for (WeProductOrderVo weProductOrderVo : list) {
                //订单状态
                weProductOrderVo.setOrderStateStr(ProductOrderStateEnum.of(weProductOrderVo.getOrderState()).getMsg());
                //退款订单状态
                weProductOrderVo.setRefundStateStr(ProductRefundOrderStateEnum.of(weProductOrderVo.getRefundState()).getMsg());
                //订单金额
                BigDecimal totalFee = new BigDecimal(weProductOrderVo.getTotalFee()).divide(BigDecimal.valueOf(100L)).setScale(2, BigDecimal.ROUND_HALF_UP);
                weProductOrderVo.setTotalFee(totalFee.toString());
                //退款订单金额
                BigDecimal refundFee = new BigDecimal(weProductOrderVo.getRefundFee()).divide(BigDecimal.valueOf(100L)).setScale(2, BigDecimal.ROUND_HALF_UP);
                weProductOrderVo.setRefundFee(refundFee.toString());
                //客户类型
                weProductOrderVo.setExternalTypeStr(weProductOrderVo.getExternalType() == 1 ? "微信" : "企业微信");
            }
        }
        return list;
    }

    @Override
    public String placeAnOrder(WePlaceAnOrderQuery query) throws ServiceException {
        //商品信息
        WeProduct weProduct = weProductMapper.selectById(query.getProductId());
        if (ObjectUtil.isEmpty(weProduct)) {
            throw new ServiceException("产品不存在!");
        }

        //商户信息
        LambdaQueryWrapper<WeCorpAccount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeCorpAccount::getDelFlag, 0);
        WeCorpAccount weCorpAccount = weCorpAccountMapper.selectOne(queryWrapper);
        if (ObjectUtil.isEmpty(weCorpAccount)) {
            throw new ServiceException("企业未配置!");
        }

        //添加订单
        insertOrder(query, weProduct, weCorpAccount);

        //调用支付

        return null;
    }

    /**
     * 预支付 JSAPI 支付
     *
     * @return
     */
    public String prepayment(WeCorpAccount weCorpAccount) throws ServiceException, KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        String wxAppId = weCorpAccount.getWxAppId();
        String certP12Url = weCorpAccount.getCertP12Url();
        if (StringUtils.isBlank(wxAppId)) {
            throw new ServiceException("微信公众号信息未配置！");
        }
        if (StringUtils.isBlank(certP12Url)) {
            throw new ServiceException("微信支付API证书未配置！");
        }

        String merChantNumber = weCorpAccount.getMerChantNumber();
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        //读取p12文件
        InputStream inputStream = FileUtils.getInputStreamByUrl(certP12Url);

        keyStore.load(inputStream,merChantNumber.toCharArray());

        Enumeration<String> aliases = keyStore.aliases();

        return "";
    }


    /**
     * 添加订单
     *
     * @param query         订单信息
     * @param weProduct     商品信息
     * @param weCorpAccount 企业信息
     * @return
     * @author WangYX
     * @date 2022/11/24 9:42
     */
    private void insertOrder(WePlaceAnOrderQuery query, WeProduct weProduct, WeCorpAccount weCorpAccount) throws ServiceException {

        WeProductOrder weProductOrder = new WeProductOrder();
        weProductOrder.setOrderNo(getOrderNo(query.getOpenId()));
        weProductOrder.setOrderState(ProductOrderStateEnum.NON_PAYMENT.getCode());
        weProductOrder.setProductId(query.getProductId());
        weProductOrder.setProductNum(query.getProductNum());

        //验证订单总金额
        String price = weProduct.getPrice();
        BigDecimal bigDecimal = new BigDecimal(price).divide(BigDecimal.valueOf(100L)).setScale(2, BigDecimal.ROUND_HALF_UP);
        bigDecimal = bigDecimal.multiply(BigDecimal.valueOf(query.getProductNum()));
        if (!query.getTotalFee().equals(bigDecimal.toString())) {
            throw new ServiceException("订单总额不正确!");
        }
        BigDecimal totalFee = new BigDecimal(query.getTotalFee()).multiply(BigDecimal.valueOf(100L));
        weProductOrder.setTotalFee(totalFee.toString());

        //订单地址信息
        weProductOrder.setContact(query.getContact());
        weProductOrder.setPhone(query.getPhone());
        weProductOrder.setProvinceId(query.getProvinceId());
        weProductOrder.setCityId(query.getCityId());
        weProductOrder.setAreaId(query.getAreaId());
        weProductOrder.setArea(query.getArea());
        weProductOrder.setAddress(query.getAddress());

        //客户信息
        weProductOrder.setExternalUserid(query.getOpenId());
        weProductOrder.setExternalName(query.getName());
        if (StringUtils.isNotBlank(query.getAvatar())) {
            weProductOrder.setExternalAvatar(query.getAvatar());
        }
        weProductOrder.setExternalType(query.getType() == null ? 1 : query.getType());

        //商品发送者信息
        weProductOrder.setWeUserId(query.getWeUserId());
        AjaxResult<SysUser> info = qwSysUserClient.getInfo(query.getWeUserId());
        if (info.getCode() == 200) {
            SysUser data = info.getData();
            if (ObjectUtil.isNotEmpty(data)) {
                weProductOrder.setWeUserName(data.getUserName());
            }
        }

        //商户信息
        weProductOrder.setMchName(weCorpAccount.getMerChantName());
        weProductOrder.setMchNo(weCorpAccount.getMerChantNumber());

        weProductOrder.setCreateTime(new Date());
        weProductOrder.setUpdateTime(new Date());
        //保存订单
        int insert = weProductOrderMapper.insert(weProductOrder);
    }

    /**
     * 生成订单编号
     *
     * @return
     */
    private String getOrderNo(String openId) {
        String date = DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss");
        return "PO" + date + openId.hashCode();
    }

}
