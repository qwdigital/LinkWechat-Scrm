package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.QREncode;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.img.NetFileUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.WeKfScenes;
import com.linkwechat.domain.kf.query.WeAddKfScenesQuery;
import com.linkwechat.domain.kf.query.WeEditKfScenesQuery;
import com.linkwechat.domain.kf.query.WeKfScenesQuery;
import com.linkwechat.domain.kf.vo.WeKfInfoVo;
import com.linkwechat.domain.kf.vo.WeKfScenesListVo;
import com.linkwechat.domain.wecom.query.kf.WeKfQuery;
import com.linkwechat.domain.wecom.vo.kf.WeKfDetailVo;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.mapper.WeKfScenesMapper;
import com.linkwechat.service.IWeKfInfoService;
import com.linkwechat.service.IWeKfScenesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.List;

/**
 * 客服场景信息表(WeKfScenes)
 *
 * @author danmo
 * @since 2022-04-15 15:53:38
 */
@Slf4j
@Service
public class WeKfScenesServiceImpl extends ServiceImpl<WeKfScenesMapper, WeKfScenes> implements IWeKfScenesService {

    @Autowired
    private QwKfClient qwKfClient;

    @Autowired
    private QwFileClient qwFileClient;

    @Override
    public List<WeKfScenes> getScenesByKfId(Long kfId) {
        return list(new LambdaQueryWrapper<WeKfScenes>()
                .eq(WeKfScenes::getKfId, kfId)
                .eq(WeKfScenes::getDelFlag, 0));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addKfScenes(WeAddKfScenesQuery query) {
        //创建客服链接
        Snowflake snowflake = IdUtil.getSnowflake(RandomUtil.randomLong(6), RandomUtil.randomInt(6));
        String scenes = snowflake.nextIdStr();
        WeKfQuery weKfQuery = new WeKfQuery();
        weKfQuery.setOpen_kfid(query.getOpenKfId());
        weKfQuery.setScene(scenes);
        WeKfDetailVo weKfDetailVo = null;
        try {
            weKfDetailVo = qwKfClient.addContactWay(weKfQuery).getData();
        } catch (Exception e) {
            e.printStackTrace();
            throw new WeComException(2001,"新增企微场景失败");
        }
        WeKfInfoVo weKfInfoVo = SpringUtils.getBean(IWeKfInfoService.class).getKfInfoByOpenKfId(SecurityUtils.getCorpId(),query.getOpenKfId());
        if(weKfInfoVo == null){
            throw new WeComException(2001,"新增企微场景失败");
        }
        WeKfScenes weKfScenes = new WeKfScenes();
        String avatar = weKfInfoVo.getAvatar();
        String content = weKfDetailVo.getUrl();
        try{
            FastByteArrayOutputStream os = new FastByteArrayOutputStream();
            QrConfig config = new QrConfig(200,200);
            // 高纠错级别
            config.setErrorCorrection(ErrorCorrectionLevel.H);
            config.setImg(ImageIO.read(new URL(avatar)));
            QrCodeUtil.generate(content, config,"png",os);
            NetFileUtils.StreamMultipartFile streamMultipartFile = new NetFileUtils.StreamMultipartFile(IdUtil.simpleUUID() + ".png", os.toByteArray());
            FileEntity fileInfo = qwFileClient.upload(streamMultipartFile).getData();
            weKfScenes.setQrCode(fileInfo.getUrl());
        } catch (Exception e) {
            log.error("生成场景二维码异常, openKfId={}", query.getOpenKfId(),e);
            throw new WeComException("生成二维码异常");
        }

        weKfScenes.setName(query.getName());
        weKfScenes.setScenes(scenes);
        weKfScenes.setOpenKfId(query.getOpenKfId());
        weKfScenes.setType(query.getType());
        weKfScenes.setUrl(weKfDetailVo.getUrl());
        weKfScenes.setKfId(query.getKfId());
        if (!save(weKfScenes)) {
            throw new WeComException(2001, "新增场景失败");
        }
    }

    @Override
    public void delKfScenes(List<Long> ids) {
        WeKfScenes weKfScenes = new WeKfScenes();
        weKfScenes.setDelFlag(1);
        update(weKfScenes,new LambdaQueryWrapper<WeKfScenes>()
                .in(WeKfScenes::getId,ids)
                .eq(WeKfScenes::getDelFlag,0));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editKfScenes(WeEditKfScenesQuery query) {
        WeKfScenes kfScenes = getById(query.getId());
        if(kfScenes == null){
            throw new WeComException(2002,"修改失败,无效ID");
        }
        if(ObjectUtil.equal(kfScenes.getKfId(),query.getKfId())
                && ObjectUtil.equal(kfScenes.getOpenKfId(),query.getOpenKfId())){
            kfScenes.setName(query.getName());
            kfScenes.setType(query.getType());
            updateById(kfScenes);
        }else {
            kfScenes.setDelFlag(1);
            updateById(kfScenes);
            WeAddKfScenesQuery weAddKfScenesQuery = new WeAddKfScenesQuery();
            BeanUtil.copyProperties(query,weAddKfScenesQuery);
            addKfScenes(weAddKfScenesQuery);
        }
    }

    @Override
    public List<WeKfScenesListVo> getScenesList(WeKfScenesQuery query) {
        return this.baseMapper.getScenesList(query);
    }

}
