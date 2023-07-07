package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeMsgTlp;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.msgtlp.dto.WeMsgTlpDto;
import com.linkwechat.domain.msgtlp.entity.WeTlpMaterial;
import com.linkwechat.domain.msgtlp.query.WeMsgTlpAddQuery;
import com.linkwechat.domain.msgtlp.query.WeMsgTlpQuery;
import com.linkwechat.domain.msgtlp.vo.WeMsgTlpVo;
import com.linkwechat.domain.wecom.query.groupmsg.WeGroupMsgQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.goupmsg.WeGroupMsgTplVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.mapper.WeMaterialMapper;
import com.linkwechat.mapper.WeMsgTlpMapper;
import com.linkwechat.mapper.WeTlpMaterialMapper;
import com.linkwechat.service.IWeMsgTlpAttachmentsService;
import com.linkwechat.service.IWeMsgTlpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 欢迎语模板表(WeMsgTlp)
 *
 * @author danmo
 * @since 2022-03-28 10:21:24
 */
@Service
@Slf4j
public class WeMsgTlpServiceImpl extends ServiceImpl<WeMsgTlpMapper, WeMsgTlp> implements IWeMsgTlpService {

    @Resource
    private IWeMsgTlpAttachmentsService weMsgTlpAttachmentsService;

//    @Resource
//    private WeMsgTlpMapper weMsgTlpMapper;

    @Resource
    private WeMaterialMapper weMaterialMapper;

    @Resource
    private WeTlpMaterialMapper weTlpMaterialMapper;


    @Resource
    private QwCustomerClient qwCustomerClient;

    @Resource
    private LinkWeChatConfig linkWeChatConfig;

    @Resource
    private  WeMaterialServiceImpl iWeMaterialService;


    @Transactional(rollbackFor = {WeComException.class, Exception.class})
    @Override
    public void addOrUpdate(WeMsgTlpDto weMsgTlpDto) {
        if (ObjectUtil.isNotEmpty(weMsgTlpDto.getId())) {
            weTlpMaterialMapper.delete(new LambdaQueryWrapper<WeTlpMaterial>().eq(WeTlpMaterial::getTlpId, weMsgTlpDto.getId()));
        }
        WeMsgTlp weMsgTlp = new WeMsgTlp();
        BeanUtil.copyProperties(weMsgTlpDto, weMsgTlp);
        saveOrUpdate(weMsgTlp);
        if (CollectionUtil.isNotEmpty(weMsgTlpDto.getUserIdList())) {
            weMsgTlp.setUserIds(String.join(",", weMsgTlpDto.getUserIds()));
        }
        if (CollectionUtil.isNotEmpty(weMsgTlpDto.getUserNameList())) {
            weMsgTlp.setUserNames(String.join(",", weMsgTlpDto.getUserNames()));
        }
        if (ObjectUtil.isNotEmpty(weMsgTlpDto.getId())) {
            weMsgTlpAttachmentsService.addMsgTlpAttachments(weMsgTlp.getId(), weMsgTlpDto.getAttachmentList());
        } else {
            weMsgTlpAttachmentsService.updateMsgTlpAttachments(weMsgTlp.getId(), weMsgTlpDto.getAttachmentList());
        }
        Long id = weMsgTlp.getId();
        log.info("模板修改删除id:{}", id);
        List<WeTlpMaterial> weTlpMaterialList = weMsgTlpDto.getWeTlpMaterialList();
        weTlpMaterialList.forEach(weTlpMaterial -> {
            weTlpMaterial.setTlpId(id);
            weTlpMaterialMapper.insert(weTlpMaterial);
        });


        if(new Integer(3).equals(weMsgTlpDto.getTplType())){ //群欢迎语同步企业微信
                 this.synchGroupWelcomMsg(weMsgTlpDto,weMsgTlp.getId());
        }
    }

    @Override
    public void synchGroupWelcomMsg(WeMsgTlpDto weMsgTlpDto,Long tlpId) {

        WeMsgTlp weMsgTlp = this.getById(tlpId);
        WeGroupMsgQuery weGroupMsgQuery=new WeGroupMsgQuery(
                linkWeChatConfig.getH5Domain(),weMsgTlpDto.getTemplateInfo(),
                iWeMaterialService.msgTplToMediaIdByCategoryMediaType(weMsgTlpDto.getAttachmentList())
        );
//        weGroupMsgQuery.setAttachmentsList(linkWeChatConfig.getH5Domain(), weMsgTlpDto.getAttachmentList());



        if(null != weMsgTlp && StringUtils.isNotEmpty(weMsgTlp.getTemplateId())){
            weGroupMsgQuery.setTemplate_id(weMsgTlp.getTemplateId());
            WeResultVo weResultVo = qwCustomerClient.updateWeGroupMsg(weGroupMsgQuery).getData();

            if(null != weResultVo){
                if(!new Integer(WeConstans.WE_SUCCESS_CODE).equals(weResultVo.getErrCode())){
                    throw new WeComException(weResultVo.getErrMsg());
                }
            }


        }else{//新增
            WeGroupMsgTplVo weGroupMsgTplVo = qwCustomerClient.addWeGroupMsg(
                    weGroupMsgQuery
            ).getData();

            if(null != weGroupMsgTplVo){
                if(!new Integer(WeConstans.WE_SUCCESS_CODE).equals(weGroupMsgTplVo.getErrCode())){
                    throw new WeComException(weGroupMsgTplVo.getErrMsg());
                }

                if(StringUtils.isNotEmpty(weGroupMsgTplVo
                        .getTemplate_id())){
                    weMsgTlp.setTemplateId(weGroupMsgTplVo.getTemplate_id());
                    this.updateById(weMsgTlp);
                }
            }

        }



    }

    @Transactional(rollbackFor = {WeComException.class, Exception.class})
    @Override
    public void addMsgTlp(WeMsgTlp weMsgTlp) {
        throw new WeComException("接口废弃");
    }

    @Transactional(rollbackFor = {WeComException.class, Exception.class})
    @Override
    public void updateMsgTlp(WeMsgTlpAddQuery query) {
        WeMsgTlp weMsgTlp = new WeMsgTlp();
        weMsgTlp.setId(query.getId());
        weMsgTlp.setTplType(query.getTplType());
        if (CollectionUtil.isNotEmpty(query.getUserIds())) {
            weMsgTlp.setUserIds(String.join(",", query.getUserIds()));
        }
        if (CollectionUtil.isNotEmpty(query.getUserNames())) {
            weMsgTlp.setUserNames(String.join(",", query.getUserNames()));
        }
        if (updateById(weMsgTlp)) {
            weMsgTlpAttachmentsService.updateMsgTlpAttachments(weMsgTlp.getId(), query.getAttachments());
        }
    }

    @Transactional(rollbackFor = {WeComException.class, Exception.class})
    @Override
    public void delMsgTlp(List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new WeComException("模板ID不能为空");
        }

        List<WeMsgTlp> weMsgTlps = list(new LambdaQueryWrapper<WeMsgTlp>()
                .in(WeMsgTlp::getId, ids));

        if(CollectionUtil.isNotEmpty(weMsgTlps)){

            removeByIds(ids);


            log.info("模板删除ids:{}", ids.toString());
            weTlpMaterialMapper.delete(new LambdaQueryWrapper<WeTlpMaterial>().in(WeTlpMaterial::getTlpId,
                    ids));


            weMsgTlps.stream().forEach(k->{

                if(k.getTplType().equals(3)){
                    qwCustomerClient.delWeGroupMsg(WeGroupMsgQuery.builder()
                            .template_id(k.getTemplateId())
                            .build());
                }


            });


        }



//        update(Wrappers.lambdaUpdate(WeMsgTlp.class).set(WeMsgTlp::getDelFlag, 1).in(WeMsgTlp::getId, ids));
//        log.info("模板删除ids:{}", ids.toString());
//        weTlpMaterialMapper.delete(new LambdaQueryWrapper<WeTlpMaterial>().in(WeTlpMaterial::getTlpId, ids));
    }

    @Override
    public WeMsgTlpVo getInfo(Long id) {
        return null;
    }

    @Override
    public List<WeMsgTlpVo> getList(WeMsgTlpQuery query) {
        List<String> userIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(query.getUserId())) {
            String[] split = query.getUserId().split(",");
            userIds = Arrays.asList(split);
        }
        List<WeMsgTlpVo> list = this.baseMapper.getList(query, userIds);
        return list;
    }

    @Override
    public List<WeMaterial> preview(Long tlpId) {
        return weMaterialMapper.getWeMaterialListByTlpId(tlpId);
    }

    @Override
    @Transactional
    public void updateCategory(WeMsgTlpQuery query) {
        List<Long> ids = query.getIds();
        Long categoryIdNew = query.getCategoryIdNew();
        update(Wrappers.lambdaUpdate(WeMsgTlp.class).set(WeMsgTlp::getCategoryId, categoryIdNew).in(WeMsgTlp::getId, ids));
    }

}
