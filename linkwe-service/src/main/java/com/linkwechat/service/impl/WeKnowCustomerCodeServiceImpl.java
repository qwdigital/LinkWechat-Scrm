package com.linkwechat.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.QREncode;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.know.WeKnowCustomerAttachments;
import com.linkwechat.domain.know.WeKnowCustomerCode;
import com.linkwechat.domain.know.WeKnowCustomerCodeTag;
import com.linkwechat.domain.material.ao.WePoster;
import com.linkwechat.domain.material.ao.WePosterSubassembly;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.user.vo.WeUserScreenConditVo;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeKnowCustomerCodeMapper;
import com.linkwechat.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

/**
 * @author robin
 * @description 针对表【we_know_customer_code(识客码)】的数据库操作Service实现
 * @createDate 2023-01-09 17:13:50
 */
@Service
public class WeKnowCustomerCodeServiceImpl extends ServiceImpl<WeKnowCustomerCodeMapper, WeKnowCustomerCode>
        implements IWeKnowCustomerCodeService {

    @Autowired
    private IWeKnowCustomerAttachmentsService iWeKnowCustomerAttachmentsService;

    @Autowired
    private IWeKnowCustomerCodeTagService iWeKnowCustomerCodeTagService;


    @Autowired
    private IWeQrCodeService iWeQrCodeService;


    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @Autowired
    private QwFileClient qwFileClient;

    @Autowired
    private IWeMaterialService materialService;


    @Autowired
    private QwSysUserClient qwSysUserClient;

    @Override
    @Transactional
    public void addOrUpdateKnowCustomer(WeKnowCustomerCode weKnowCustomerCode,boolean isUpdate) throws IOException {

        if(weKnowCustomerCode.getId()==null){
            weKnowCustomerCode.setId(SnowFlakeUtil.nextId());

            //(新增)构造识客码
            String contentUrl = MessageFormat.format(linkWeChatConfig.getKnowCustomerUrl(), weKnowCustomerCode.getId().toString());
            FileEntity fileEntity = qwFileClient.upload(QREncode.getQRCodeMultipartFile(contentUrl, null)).getData();
            if (null != fileEntity) {
                weKnowCustomerCode.setKnowCustomerQr(fileEntity.getUrl());
            }
            weKnowCustomerCode.setKnowCustomerUrl(contentUrl);
        }

        //添加员工的二维码
        //新客员工id
        String weUserIds=null;String deptIds=null;String positions=null;

        WeUserScreenConditVo addWeUser = weKnowCustomerCode.getAddWeUser();
        if(null != addWeUser){
            WeUserScreenConditVo.ExecuteUserCondit executeUserCondit = addWeUser.getExecuteUserCondit();
            if(null != executeUserCondit){
                List<String> weUserIdss = executeUserCondit.getWeUserIds();
                if(CollectionUtil.isNotEmpty(weUserIdss)){
                    weUserIds=StringUtils.join(weUserIdss,",");
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

        if(StringUtils.isEmpty(weKnowCustomerCode.getAddWeUserConfig())){

            AjaxResult<List<String>> listAjaxResult
                    = qwSysUserClient.screenConditWeUser(weUserIds,deptIds,positions);
            if(null != listAjaxResult){
                List<String> addWeUserIds = listAjaxResult.getData();

                if(CollectionUtil.isEmpty(addWeUserIds)){
                    throw new WeComException("当前添加的员工不存在或为无效员工");
                }
                WeAddWayVo weAddWayVo = iWeQrCodeService.createQrbyWeUserIds(
                        addWeUserIds,
                        WeConstans.WE_KNOW_CUSTOMER_CODE_PREFIX + weKnowCustomerCode.getId()
                );

                if(weAddWayVo.getErrCode() !=null && WeConstans.WE_SUCCESS_CODE.equals(weAddWayVo.getErrCode())) {

                    weKnowCustomerCode.setAddWeUserState(WeConstans.WE_KNOW_CUSTOMER_CODE_PREFIX + weKnowCustomerCode.getId());
                    if(weAddWayVo != null){
                        weKnowCustomerCode.setAddWeUserUrl(weAddWayVo.getQrCode());
                        weKnowCustomerCode.setAddWeUserConfig(weAddWayVo.getConfigId());
                    }

                }else{
                    throw new WeComException(weAddWayVo.getErrMsg());
                }

            }

        }else{
            AjaxResult<List<String>> listAjaxResult
                    = qwSysUserClient.screenConditWeUser(weUserIds,deptIds,positions);

            if(null != listAjaxResult){
                List<String> addWeUserIds = listAjaxResult.getData();
                if(CollectionUtil.isEmpty(addWeUserIds)){
                    throw new WeComException("当前添加的员工不存在或为无效员工");
                }
                iWeQrCodeService.updateQrbyWeUserIds(addWeUserIds
                        ,weKnowCustomerCode.getAddWeUserConfig());



            }

        }

        //替换海报中的二维码为上面新生成的二维码
        if(weKnowCustomerCode.getPostersId() != null){
            WeMaterial material = materialService.getById(weKnowCustomerCode.getPostersId());
            if (StringUtils.isNotEmpty(material.getPosterSubassembly())) {
                List<WePosterSubassembly> wePosterSubassemblies = JSONArray.parseArray(material.getPosterSubassembly(), WePosterSubassembly.class);
                wePosterSubassemblies.stream().filter(Objects::nonNull)
                        .filter(wePosterSubassembly -> wePosterSubassembly.getType() == 3).forEach(wePosterSubassembly -> {
                            wePosterSubassembly.setImgPath(weKnowCustomerCode.getAddWeUserUrl());
                        });
                WePoster wePoster = BeanUtil.copyProperties(material, WePoster.class);
                wePoster.setTitle(material.getMaterialName());
                wePoster.setSampleImgPath(material.getMaterialUrl());
                wePoster.setBackgroundImgPath(material.getBackgroundImgUrl());
                wePoster.setPosterSubassemblyList(wePosterSubassemblies);
                WeMaterial weMaterial = materialService.generateSimpleImg(wePoster);
                weKnowCustomerCode.setPosterUrl(weMaterial.getMaterialUrl());
            } else {
                throw new WeComException("生成海报错误，无二维码位置");
            }
        }


        //添加标签
        List<WeKnowCustomerCodeTag> weKnowCustomerCodeTags = weKnowCustomerCode.getWeKnowCustomerCodeTags();
        if(CollectionUtil.isNotEmpty(weKnowCustomerCodeTags)){
            weKnowCustomerCodeTags.stream().forEach(k->k.setKnowCustomerCodeId(weKnowCustomerCode.getId()));
            iWeKnowCustomerCodeTagService.remove(new LambdaQueryWrapper<WeKnowCustomerCodeTag>()
                    .eq(WeKnowCustomerCodeTag::getKnowCustomerCodeId,weKnowCustomerCode.getId()));
            iWeKnowCustomerCodeTagService.saveOrUpdateBatch(weKnowCustomerCodeTags);
        }

        //素材附件
        List<WeMessageTemplate> attachments = weKnowCustomerCode.getAttachments();
        if(CollectionUtil.isNotEmpty(attachments)){
            if(!isUpdate){
                iWeKnowCustomerAttachmentsService.saveBatchByKnowCustomerId(weKnowCustomerCode.getId(),attachments);
            }else{
                iWeKnowCustomerAttachmentsService.updateBatchByKnowCustomerId(weKnowCustomerCode.getId(), attachments);
            }
        }





        this.saveOrUpdate(weKnowCustomerCode);
    }

    @Override
    public List<WeKnowCustomerCode> findWeKnowCustomerCodes(String knowCustomerName) {
        return this.baseMapper.findWeKnowCustomerCodes(knowCustomerName);
    }

    @Override
    public WeKnowCustomerCode findWeKnowCustomerCodeDetailById(Long id) {
        WeKnowCustomerCode weKnowCustomerCode = this.getById(id);

        if(weKnowCustomerCode != null){
            //获取添加标签
            weKnowCustomerCode.setWeKnowCustomerCodeTags(
                    iWeKnowCustomerCodeTagService.list(new LambdaQueryWrapper<WeKnowCustomerCodeTag>()
                            .eq(WeKnowCustomerCodeTag::getKnowCustomerCodeId, id))
            );

            //获取附件素材
            weKnowCustomerCode.setWeKnowCustomerAttachments(
                    iWeKnowCustomerAttachmentsService.list(new LambdaQueryWrapper<WeKnowCustomerAttachments>()
                            .eq(WeKnowCustomerAttachments::getKnowCustomerId,id))
            );

        }

        return weKnowCustomerCode;
    }


}
