package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeProduct;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.product.query.WeAddProductQuery;
import com.linkwechat.domain.product.query.WeProductQuery;
import com.linkwechat.domain.product.vo.WeProductListVo;
import com.linkwechat.domain.product.vo.WeProductVo;
import com.linkwechat.domain.wecom.query.product.QwAddProductQuery;
import com.linkwechat.domain.wecom.query.product.QwProductListQuery;
import com.linkwechat.domain.wecom.query.product.QwProductQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwAddProductVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductListVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductVo;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.fegin.QwProductAlbumClient;
import com.linkwechat.mapper.WeProductMapper;
import com.linkwechat.service.IWeMaterialService;
import com.linkwechat.service.IWeProductService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品信息表(WeProduct)
 *
 * @author danmo
 * @since 2022-09-30 11:36:06
 */
@Service
public class WeProductServiceImpl extends ServiceImpl<WeProductMapper, WeProduct> implements IWeProductService {

    @Resource
    private QwProductAlbumClient qwProductAlbumClient;

    @Autowired
    private IWeMaterialService iWeMaterialService;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void addProduct(WeAddProductQuery query) {
        query.setProductSn(generateProductSn());

        //获取附件媒体ID
        if (CollectionUtil.isNotEmpty(query.getAttachments())) {
            for (WeMessageTemplate attachment : query.getAttachments()) {
                String mediaId = iWeMaterialService.uploadAttachmentMaterial(attachment.getPicUrl(),
                        MediaType.IMAGE.getMediaType(), 2, SnowFlakeUtil.nextId().toString()).getMediaId();
                if (StringUtils.isNotEmpty(mediaId)) {
                    attachment.setMediaId(mediaId);
                } else {
                    throw new WeComException(12003, "获取附件素材ID失败");
                }
            }
        }
        QwAddProductQuery productQuery = query.convert2Qw();
        QwAddProductVo productResult = qwProductAlbumClient.addProduct(productQuery).getData();
        if (Objects.isNull(productResult)) {
            throw new WeComException(12001, "新增商品失败");
        }
        if (Objects.equals(productResult.getErrCode(), WeConstans.WE_SUCCESS_CODE)) {
            throw new WeComException(12002, "新增商品失败");
        }

        WeProduct product = new WeProduct();
        product.setProductId(productResult.getProductId());
        product.setPrice(query.getPrice());
        product.setDescribe(query.getDescribe());
        product.setProductSn(query.getProductSn());
        product.setAttachments(JSONObject.toJSONString(query.getAttachments()));
        save(product);
    }

    @Override
    public void updateProduct(Long id, WeAddProductQuery query) {
        WeProduct product = getById(id);
        //获取附件媒体ID
        if (CollectionUtil.isNotEmpty(query.getAttachments())) {
            for (WeMessageTemplate attachment : query.getAttachments()) {
                String mediaId = iWeMaterialService.uploadAttachmentMaterial(attachment.getPicUrl(),
                        MediaType.IMAGE.getMediaType(), 2, SnowFlakeUtil.nextId().toString()).getMediaId();
                if (StringUtils.isNotEmpty(mediaId)) {
                    attachment.setMediaId(mediaId);
                } else {
                    throw new WeComException(12013, "获取附件素材ID失败");
                }
            }
        }
        QwAddProductQuery productQuery = query.convert2Qw();
        productQuery.setProduct_id(product.getProductId());
        WeResultVo productResult = qwProductAlbumClient.updateProductAlbum(productQuery).getData();
        if (Objects.isNull(productResult)) {
            throw new WeComException(12011, "修改商品失败");
        }
        if (!Objects.equals(productResult.getErrCode(), WeConstans.WE_SUCCESS_CODE)) {
            throw new WeComException(12012, "修改商品失败");
        }
        product.setPrice(query.getPrice());
        product.setDescribe(query.getDescribe());
        product.setAttachments(JSONObject.toJSONString(query.getAttachments()));
        updateById(product);
    }

    @Override
    public void delProduct(List<Long> ids) {
        List<WeProduct> weProducts = listByIds(ids);
        for (WeProduct weProduct : weProducts) {
            QwProductQuery query = new QwProductQuery();
            query.setProduct_id(weProduct.getProductId());
            WeResultVo weResult = qwProductAlbumClient.delProductAlbum(query).getData();
            if (Objects.isNull(weResult)) {
                throw new WeComException(12021, "删除商品失败");
            }
            if (!Objects.equals(weResult.getErrCode(), WeConstans.WE_SUCCESS_CODE)) {
                throw new WeComException(12022, "删除商品失败");
            }
            weProduct.setDelFlag(1);
            updateById(weProduct);
        }
    }

    @Override
    public WeProductVo getProduct(Long id) {
        WeProduct product = getById(id);
        if(Objects.isNull(product)){
            throw new WeComException("未找到相关商品");
        }
        WeProductVo productVo = new WeProductVo();
        productVo.setId(product.getId());
        productVo.setProductSn(product.getProductSn());
        productVo.setPrice(product.getPrice());
        productVo.setDescribe(product.getDescribe());
        productVo.setPicture(product.getPicture());
        productVo.setAttachments(JSONArray.parseArray(product.getAttachments(),WeMessageTemplate.class));
        return productVo;
    }

    @Override
    public List<WeProductListVo> productList(WeProductQuery query) {
        return null;
    }


    @Override
    public void syncProductList() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(),rabbitMQSettingConfig.getWeProductRk(),JSONObject.toJSONString(loginUser));
    }

    @Override
    public void syncProductListHandle(String msg) {
        LoginUser loginUser = JSONObject.parseObject(msg,LoginUser.class);
        List<WeProduct> weProducts = new LinkedList<>();
        QwProductListQuery query = new QwProductListQuery();
        QwProductListVo qwProductList = getQwProductList(query);
        if(Objects.nonNull(qwProductList) && CollectionUtil.isNotEmpty(qwProductList.getProductList())){
            for (QwProductVo.QwProduct qwProduct : qwProductList.getProductList()) {
                WeProduct product = new WeProduct();
                product.setCreateBy(loginUser.getUserName());
                product.setCreateById(loginUser.getUserId());
                product.setProductId(qwProduct.getProductId());
                product.setProductSn(qwProduct.getProductSn());
                product.setDescribe(qwProduct.getDescription());
                product.setPrice(String.valueOf(qwProduct.getPrice()));
                List<JSONObject> attachments = qwProduct.getAttachments();
                if(CollectionUtil.isNotEmpty(attachments)){
                    List<WeMessageTemplate> weMessageTemplates = attachments.stream().map(attachment -> {
                        WeMessageTemplate template = new WeMessageTemplate();
                        WeMediaVo mediaVo = iWeMaterialService.getMediaToResponse(attachment.getString("media_id"));
                        template.setMsgType(MediaType.IMAGE.getType());
                        template.setPicUrl(mediaVo.getUrl());
                        return template;
                    }).collect(Collectors.toList());
                    product.setAttachments(JSONObject.toJSONString(weMessageTemplates));
                }
                weProducts.add(product);
            }
        }
        saveBatch(weProducts,100);
    }

    private QwProductListVo getQwProductList(QwProductListQuery query) {
        QwProductListVo productList = qwProductAlbumClient.getProductAlbumList(query).getData();
        if (productList != null && productList.getNextCursor() != null){
            query.setCursor(productList.getNextCursor());
            QwProductListVo productChildList = getQwProductList(query);
            productList.getProductList().addAll(productChildList.getProductList());
        }
        return productList;
    }


    /**
     * 生成预约编号
     *
     * @return
     */
    private synchronized String generateProductSn() {
        String prefix = "QP";
        String time = DateUtil.format(new Date(), "yyyyMMddHH");
        String randomCode = RandomUtil.randomNumbers(4);
        return String.format("%s%s%s", prefix, time, randomCode);
    }
}
