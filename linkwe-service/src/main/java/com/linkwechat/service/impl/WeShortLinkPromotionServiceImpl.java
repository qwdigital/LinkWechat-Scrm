package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.domain.*;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
import com.linkwechat.domain.shortlink.vo.*;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeMaterialMapper;
import com.linkwechat.mapper.WeShortLinkPromotionMapper;
import com.linkwechat.service.*;
import com.linkwechat.service.impl.strategic.shortlink.PromotionType;
import com.linkwechat.service.impl.strategic.shortlink.ShortLinkPromotionStrategyFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 短链推广 服务实现类
 * </p>
 *
 * @author WangYX
 * @since 2023-03-07
 */
@Service
public class WeShortLinkPromotionServiceImpl extends ServiceImpl<WeShortLinkPromotionMapper, WeShortLinkPromotion> implements IWeShortLinkPromotionService {

    @Resource
    private WeShortLinkPromotionMapper weShortLinkPromotionMapper;
    @Resource
    private WeMaterialMapper weMaterialMapper;
    @Resource
    private LinkWeChatConfig linkWeChatConfig;
    @Resource
    private QwFileClient qwFileClient;
    @Resource
    private IWeShortLinkPromotionAttachmentService weShortLinkPromotionAttachmentService;
    @Resource
    private IWeShortLinkService weShortLinkService;
    @Resource
    private IWeShortLinkPromotionTemplateClientService weShortLinkPromotionTemplateClientService;
    @Resource
    private IWeShortLinkPromotionTemplateGroupService weShortLinkPromotionTemplateGroupService;
    @Resource
    private IWeShortLinkPromotionTemplateMomentsService weShortLinkPromotionTemplateMomentsService;
    @Resource
    private IWeShortLinkPromotionTemplateAppMsgService weShortLinkPromotionTemplateAppMsgService;
    @Resource
    private QwSysUserClient qwSysUserClient;
    @Resource
    private IWeTagService weTagService;


    @Override
    public List<WeShortLinkPromotionVo> selectList(WeShortLinkPromotionQuery query) {
        return weShortLinkPromotionMapper.selectList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(WeShortLinkPromotionAddQuery query) throws IOException {
        WeShortLinkPromotion weShortLinkPromotion = new WeShortLinkPromotion();
        weShortLinkPromotion.setTaskName(query.getTaskName());
        weShortLinkPromotion.setShortLinkId(query.getShortLinkId());
        weShortLinkPromotion.setStyle(query.getStyle());
        weShortLinkPromotion.setType(query.getType());
        weShortLinkPromotion.setDelFlag(0);
        Optional.ofNullable(query.getMaterialId()).ifPresent(i -> weShortLinkPromotion.setMaterialId(i));

        //推广的二维码或者海报转成附件
        WeMessageTemplate weMessageTemplate = getPromotionUrl(query.getShortLinkId(), query.getStyle(), query.getMaterialId());
        weShortLinkPromotion.setUrl(weMessageTemplate.getPicUrl());
        //添加推广附件
        List<WeMessageTemplate> weMessageTemplates = Optional.ofNullable(query.getAttachmentsList()).orElse(new ArrayList<>());
        weMessageTemplates.add(weMessageTemplate);
        query.setAttachmentsList(weMessageTemplates);
        //保存和发送
        PromotionType promotionType = ShortLinkPromotionStrategyFactory.getPromotionType(query.getType());
        Long id = promotionType.saveAndSend(query, weShortLinkPromotion);
        return id;
    }

    @Override
    public Long ts(WeShortLinkPromotionAddQuery query) throws IOException {
        WeShortLinkPromotion weShortLinkPromotion = BeanUtil.copyProperties(query, WeShortLinkPromotion.class);
        Optional.ofNullable(query.getMaterialId()).ifPresent(i -> weShortLinkPromotion.setMaterialId(i));
        WeMessageTemplate weMessageTemplate = getPromotionUrl(query.getShortLinkId(), query.getStyle(), query.getMaterialId());
        weShortLinkPromotion.setUrl(weMessageTemplate.getPicUrl());
        weShortLinkPromotion.setDelFlag(0);
        weShortLinkPromotion.setTaskStatus(0);
        weShortLinkPromotionMapper.insert(weShortLinkPromotion);
        return weShortLinkPromotion.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(WeShortLinkPromotionUpdateQuery query) {
        Long id = query.getId();
        WeShortLinkPromotion weShortLinkPromotion = weShortLinkPromotionMapper.selectById(id);
        Optional.ofNullable(weShortLinkPromotion).ifPresent(i -> {
            //任务状态: 0带推广 1推广中 2已结束 3暂存中
            if (i.getTaskStatus().equals(1) || i.getTaskStatus().equals(2)) {
                throw new ServiceException("当前状态无法修改！");
            }
            //短链推广
            WeShortLinkPromotion weShortLinkPromotionTemp = BeanUtil.copyProperties(query, WeShortLinkPromotion.class);
            try {
                //推广的二维码或者海报转成附件
                WeMessageTemplate weMessageTemplate = getPromotionUrl(query.getShortLinkId(), query.getStyle(), query.getMaterialId());
                weShortLinkPromotionTemp.setUrl(weMessageTemplate.getPicUrl());
                //添加推广附件
                List<WeMessageTemplate> attachmentsList = query.getAttachmentsList();
                attachmentsList.add(weMessageTemplate);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServiceException("生成海报异常！");
            }

            //更新和发送
            PromotionType promotionType = ShortLinkPromotionStrategyFactory.getPromotionType(query.getType());
            promotionType.updateAndSend(query, weShortLinkPromotionTemp);
        });
    }

    @Override
    public WeShortLinkPromotionGetVo get(Long id) {
        LambdaQueryWrapper<WeShortLinkPromotion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeShortLinkPromotion::getId, id);
        queryWrapper.eq(WeShortLinkPromotion::getDelFlag, 0);
        WeShortLinkPromotion weShortLinkPromotion = getOne(queryWrapper);
        WeShortLinkPromotionGetVo result = Optional.ofNullable(weShortLinkPromotion).map(i -> {
            WeShortLinkPromotionGetVo vo = BeanUtil.copyProperties(weShortLinkPromotion, WeShortLinkPromotionGetVo.class);
            WeShortLink weShortLink = weShortLinkService.getById(weShortLinkPromotion.getShortLinkId());
            //短链详情
            WeShortLinkListVo weShortLinkListVo = BeanUtil.copyProperties(weShortLink, WeShortLinkListVo.class);
            vo.setShortLink(weShortLinkListVo);
            //任务状态: 0待推广 1推广中 2已结束 3暂存中
            if (i.getTaskStatus() != 3) {
                //推广信息
                Integer type = i.getType();
                switch (type) {
                    case 0:
                        getWeShortLinkPromotionTemplateClientVo(i.getId(), vo);
                        break;
                    case 1:
                        getShortLinkPromotionTemplateGroupVo(i.getId(), vo);
                        break;
                    case 2:
                        getWeShortLinkPromotionTemplateMomentsVo(i.getId(), vo);
                        break;
                    case 3:
                        getWeShortLinkPromotionTemplateAppMsgVo(i.getId(), vo);
                        break;
                    default:
                        break;
                }
            }
            return vo;
        }).orElse(null);
        return result;
    }

    /**
     * 获取推广图片
     *
     * @param shortLinkId 短链Id
     * @param style       推广样式 0二维码 1海报
     * @param materialId  素材Id
     * @return {@link WeMessageTemplate}
     * @author WangYX
     * @date 2023/03/09 14:50
     */
    private WeMessageTemplate getPromotionUrl(Long shortLinkId, Integer style, Long materialId) throws IOException {
        //获取短链
        String encode = Base62NumUtil.encode(shortLinkId);
        //TODO 这里要加上任务Id
        String shortLinkUrl = linkWeChatConfig.getShortLinkDomainName() + encode;

        WeMessageTemplate weMessageTemplate = new WeMessageTemplate();
        weMessageTemplate.setMsgType("image");

        if (style.equals(0)) {
            //创建二维码，默认宽高160
            BufferedImage image = QrCodeUtil.generate(shortLinkUrl, 160, 160);
            //上传腾讯云
            String url = upload(image, "jpg");
            weMessageTemplate.setPicUrl(url);
            return weMessageTemplate;
        } else {
            //替换海报中的占位码
            Optional.ofNullable(materialId).orElseThrow(() -> new ServiceException("海报素材必填！"));
            WeMaterial weMaterial = weMaterialMapper.selectById(materialId);

            JSONObject jsonObject = Optional.ofNullable(weMaterial)
                    //判断背景图片是否存在
                    .filter(i -> StrUtil.isNotBlank(i.getBackgroundImgUrl()))
                    //判断海报组件是否存在
                    .map(WeMaterial::getPosterSubassembly)
                    .map(i -> JSONObject.parseArray(i))
                    //判断海报是否有占位码组件
                    .filter(i -> i.getJSONObject(0).getInteger("type").equals(3))
                    .map(i -> i.getJSONObject(0))
                    .orElseThrow(() -> new ServiceException("请确认海报是否正确！"));

            //占位码位置
            Integer left = jsonObject.getInteger("left");
            Integer top = jsonObject.getInteger("top");
            //海报宽高
            Integer width = jsonObject.getInteger("width");
            Integer height = jsonObject.getInteger("height");

            //生成二维码
            BufferedImage qrImage = QrCodeUtil.generate(shortLinkUrl, width, height);

            //背景图片
            String backgroundImgUrl = weMaterial.getBackgroundImgUrl();
            //图片后缀
            String suffix = backgroundImgUrl.substring(backgroundImgUrl.lastIndexOf(".") + 1);
            //取代占位码
            URL backgroundImg = new URL(backgroundImgUrl);
            BufferedImage read = ImageIO.read(backgroundImg);
            Graphics2D graphics = read.createGraphics();
            graphics.drawImage(qrImage, left, top, width, height, null);
            String url = upload(read, suffix);
            weMessageTemplate.setPicUrl(url);
            return weMessageTemplate;
        }
    }

    /**
     * 上传图片
     *
     * @param image  图片
     * @param suffix 图片后缀
     * @return {@link String} 上传后的图片地址
     * @throws IOException
     */
    private String upload(BufferedImage image, String suffix) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, suffix, os);
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        String name = UUID.randomUUID().toString() + "." + suffix;
        MultipartFile file = new MockMultipartFile(name, name, null, input);
        AjaxResult<FileEntity> upload = qwFileClient.upload(file);
        if (upload.getCode() == 200) {
            String url = upload.getData().getUrl();
            return url;
        } else {
            throw new ServiceException("文件上传失败");
        }

    }

    /**
     * 客户
     *
     * @param promotionId 短链推广Id
     * @param vo
     * @return
     */
    private void getWeShortLinkPromotionTemplateClientVo(Long promotionId, WeShortLinkPromotionGetVo vo) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateClient> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeShortLinkPromotionTemplateClient::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionTemplateClient::getDelFlag, 0);
        WeShortLinkPromotionTemplateClient one = weShortLinkPromotionTemplateClientService.getOne(queryWrapper);
        Optional.ofNullable(one).ifPresent(i -> {
            WeShortLinkPromotionTemplateClientVo clientVo = BeanUtil.copyProperties(one, WeShortLinkPromotionTemplateClientVo.class);
            //群发客户分类：0全部客户 1部分客户
            if (one.getType() == 1) {
                SysUserQuery sysUserQuery = new SysUserQuery();
                List<String> list = Arrays.asList(one.getUserIds().split(","));
                sysUserQuery.setWeUserIds(list);
                AjaxResult<List<SysUserVo>> weUsersResult = qwSysUserClient.getUserListByWeUserIds(sysUserQuery);
                if (weUsersResult.getCode() == 200) {
                    List<SysUserVo> data = weUsersResult.getData();
                    Map<String, String> map = new HashMap<>(list.size());
                    data.stream().forEach(o -> map.put(o.getWeUserId(), o.getUserName()));
                    clientVo.setUsers(map);
                }
            }
            //标签
            if (StrUtil.isNotBlank(one.getLabelIds())) {
                LambdaQueryWrapper<WeTag> tagQueryWrapper = Wrappers.lambdaQuery();
                tagQueryWrapper.eq(WeTag::getDelFlag, 0);
                tagQueryWrapper.in(WeTag::getTagId, one.getLabelIds().split(","));
                List<WeTag> list = weTagService.list(tagQueryWrapper);
                Map<String, String> map = new HashMap<>(list.size());
                list.stream().forEach(o -> map.put(o.getTagId(), o.getName()));
                clientVo.setLabels(map);
            }
            //附件
            LambdaQueryWrapper<WeShortLinkPromotionAttachment> attachmentQueryWrapper = Wrappers.lambdaQuery();
            attachmentQueryWrapper.eq(WeShortLinkPromotionAttachment::getTemplateType, 0);
            attachmentQueryWrapper.eq(WeShortLinkPromotionAttachment::getTemplateId, one.getId());
            attachmentQueryWrapper.eq(WeShortLinkPromotionAttachment::getDelFlag, 0);
            List<WeShortLinkPromotionAttachment> list = weShortLinkPromotionAttachmentService.list(attachmentQueryWrapper);
            if (list != null && list.size() > 0) {
                List<WeMessageTemplate> collect = list.stream().map(o -> BeanUtil.copyProperties(o, WeMessageTemplate.class)).collect(Collectors.toList());
                vo.setAttachments(collect);
            }
            vo.setClient(clientVo);
        });
    }

    /**
     * 客群
     *
     * @param promotionId 短链推广Id
     * @param vo
     * @return
     */
    private void getShortLinkPromotionTemplateGroupVo(Long promotionId, WeShortLinkPromotionGetVo vo) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateGroup> groupQueryWrapper = Wrappers.lambdaQuery();
        groupQueryWrapper.eq(WeShortLinkPromotionTemplateGroup::getPromotionId, promotionId);
        groupQueryWrapper.eq(WeShortLinkPromotionTemplateGroup::getDelFlag, 0);
        WeShortLinkPromotionTemplateGroup group = weShortLinkPromotionTemplateGroupService.getOne(groupQueryWrapper);
        Optional.ofNullable(group).ifPresent(i -> {
            WeShortLinkPromotionTemplateGroupVo groupVo = BeanUtil.copyProperties(group, WeShortLinkPromotionTemplateGroupVo.class);
            //客群分类 0全部群 1部分群
            if (groupVo.getType() == 1) {
                SysUserQuery sysUserQuery = new SysUserQuery();
                List<String> userIds = Arrays.asList(groupVo.getUserIds().split(","));
                sysUserQuery.setWeUserIds(userIds);
                AjaxResult<List<SysUserVo>> weUsersResult = qwSysUserClient.getUserListByWeUserIds(sysUserQuery);
                if (weUsersResult.getCode() == 200) {
                    List<SysUserVo> data = weUsersResult.getData();
                    Map<String, String> map = new HashMap<>(userIds.size());
                    data.stream().forEach(o -> map.put(o.getWeUserId(), o.getUserName()));
                    groupVo.setUsers(map);
                }
            }
            //附件
            LambdaQueryWrapper<WeShortLinkPromotionAttachment> attachmentQueryWrapper1 = Wrappers.lambdaQuery();
            attachmentQueryWrapper1.eq(WeShortLinkPromotionAttachment::getTemplateType, 1);
            attachmentQueryWrapper1.eq(WeShortLinkPromotionAttachment::getTemplateId, group.getId());
            attachmentQueryWrapper1.eq(WeShortLinkPromotionAttachment::getDelFlag, 0);
            List<WeShortLinkPromotionAttachment> list1 = weShortLinkPromotionAttachmentService.list(attachmentQueryWrapper1);
            if (list1 != null && list1.size() > 0) {
                List<WeMessageTemplate> collect = list1.stream().map(o -> BeanUtil.copyProperties(o, WeMessageTemplate.class)).collect(Collectors.toList());
                vo.setAttachments(collect);
            }
            vo.setGroup(groupVo);
        });
    }

    /**
     * 朋友圈
     *
     * @param promotionId 短链推广Id
     * @return
     */
    private void getWeShortLinkPromotionTemplateMomentsVo(Long promotionId, WeShortLinkPromotionGetVo result) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateMoments> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeShortLinkPromotionTemplateMoments::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionTemplateMoments::getDelFlag, 0);
        WeShortLinkPromotionTemplateMoments one = weShortLinkPromotionTemplateMomentsService.getOne(queryWrapper);
        Optional.ofNullable(one).ifPresent(i -> {
            WeShortLinkPromotionTemplateMomentsVo vo = BeanUtil.copyProperties(one, WeShortLinkPromotionTemplateMomentsVo.class);
            //群发朋友圈分类 0全部客户 1部分客户
            if (vo.getScopeType().equals(1)) {
                SysUserQuery sysUserQuery = new SysUserQuery();
                List<String> list = Arrays.asList(one.getUserIds().split(","));
                sysUserQuery.setWeUserIds(list);
                AjaxResult<List<SysUserVo>> weUsersResult = qwSysUserClient.getUserListByWeUserIds(sysUserQuery);
                if (weUsersResult.getCode() == 200) {
                    List<SysUserVo> data = weUsersResult.getData();
                    Map<String, String> map = new HashMap<>(list.size());
                    data.stream().forEach(o -> map.put(o.getWeUserId(), o.getUserName()));
                    vo.setUsers(map);
                }
            }
            //标签
            if (StrUtil.isNotBlank(one.getLabelIds())) {
                LambdaQueryWrapper<WeTag> tagQueryWrapper = Wrappers.lambdaQuery();
                tagQueryWrapper.eq(WeTag::getDelFlag, 0);
                tagQueryWrapper.in(WeTag::getTagId, one.getLabelIds().split(","));
                List<WeTag> list = weTagService.list(tagQueryWrapper);
                Map<String, String> map = new HashMap<>(list.size());
                list.stream().forEach(o -> map.put(o.getTagId(), o.getName()));
                vo.setLabels(map);
            }
            result.setMoments(vo);
        });
    }

    /**
     * 应用消息
     *
     * @param promotionId 短链推广Id
     * @return
     */
    private void getWeShortLinkPromotionTemplateAppMsgVo(Long promotionId, WeShortLinkPromotionGetVo result) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateAppMsg> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeShortLinkPromotionTemplateAppMsg::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionTemplateAppMsg::getDelFlag, 0);
        WeShortLinkPromotionTemplateAppMsg one = weShortLinkPromotionTemplateAppMsgService.getOne(queryWrapper);
        Optional.ofNullable(one).ifPresent(i -> {
            WeShortLinkPromotionTemplateAppMsgVo vo = BeanUtil.copyProperties(one, WeShortLinkPromotionTemplateAppMsgVo.class);
            //应用消息发送类型 0成员 1部门或岗位
            if (vo.getSendScope().equals(0)) {
                String userIds = one.getUserIds();
                WeSopExecuteUserConditVo.ExecuteUserCondit user = new WeSopExecuteUserConditVo.ExecuteUserCondit();
                user.setChange(true);
                user.setWeUserIds(Arrays.asList(userIds.split(",")));
                vo.setExecuteUserCondit(user);
            } else if (vo.getSendScope().equals(1)) {
                String deptIds = one.getDeptIds();
                String postIds = one.getPostIds();
                WeSopExecuteUserConditVo.ExecuteDeptCondit dept = new WeSopExecuteUserConditVo.ExecuteDeptCondit();
                dept.setChange(true);
                if (StrUtil.isNotBlank(deptIds)) {
                    dept.setDeptIds(Arrays.asList(deptIds.split(",")));
                }
                if (StrUtil.isNotBlank(postIds)) {
                    dept.setPosts(Arrays.asList(postIds.split(",")));
                }
                vo.setExecuteDeptCondit(dept);
            }
            result.setAppMsg(vo);
        });
    }

}
