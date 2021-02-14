package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.config.ServerConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUploadUtils;
import com.linkwechat.wecom.client.WeMediaClient;
import com.linkwechat.wecom.domain.WeMaterial;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.vo.WeMaterialFileVO;
import com.linkwechat.wecom.mapper.WeMaterialMapper;
import com.linkwechat.wecom.service.IWeMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 素材service
 *
 * @author KEWEN
 * @date 2020-10-08
 */
@Slf4j
@Service
public class WeMaterialServiceImpl implements IWeMaterialService {

    @Autowired
    private WeMaterialMapper weMaterialMapper;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private WeMediaClient weMediaClient;

    @Override
    public WeMaterialFileVO uploadWeMaterialFile(MultipartFile file, String type) {
        if (null == file) {
            throw new WeComException("文件为空！");
        }

        try {
            //上传文件到文件服务器
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            String fileName = "";
            if (com.linkwechat.common.enums.MediaType .FILE.getType().equals(type)) {
                fileName = FileUploadUtils.uploadFile(file);
            } else {
                // 上传并返回新文件名称
                fileName = FileUploadUtils.upload(filePath, file);
            }
            String url =  fileName;
            //上传临时素材
            Optional<com.linkwechat.common.enums.MediaType > mediaType = com.linkwechat.common.enums.MediaType .of(type);
            if (!mediaType.isPresent()) {
                throw new WeComException("媒体类型出错！");
            }


            //构造返回结果
            return WeMaterialFileVO.builder().materialUrl(url).materialName(file.getOriginalFilename()).build();
        } catch (Exception e) {
            throw new WeComException(e.getMessage());
        }

    }

    @Override
    public int insertWeMaterial(WeMaterial weMaterial) {
        weMaterial.setCreateTime(DateUtil.date());
        weMaterial.setId(SnowFlakeUtil.nextId());
        return weMaterialMapper.insertWeMaterial(weMaterial);
    }

    @Override
    public int deleteWeMaterialById(Long id) {
        return weMaterialMapper.deleteWeMaterialById(id);
    }

    @Override
    public int deleteWeMaterialByIds(Long[] ids) {
        return weMaterialMapper.deleteWeMaterialByIds(ids);
    }

    @Override
    public int updateWeMaterial(WeMaterial weMaterial) {
        weMaterial.setCreateTime(DateUtil.date());
        return weMaterialMapper.updateWeMaterial(weMaterial);
    }

    @Override
    public WeMaterial findWeMaterialById(Long id) {
        return weMaterialMapper.findWeMaterialById(id);
    }

    @Override
    public List<WeMaterial> findWeMaterials(String categoryId, String search, String mediaType) {
        return weMaterialMapper.findWeMaterials(categoryId, search, mediaType);
    }


    @Override
    public void resetCategory(String categoryId, String materials) {
        List<String> materialList = Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(materials, WeConstans.COMMA));
        if (CollectionUtil.isNotEmpty(materialList)) {
            materialList.forEach(s -> {
                weMaterialMapper.resetCategory(categoryId, s);
            });
        }
    }

    @Override
    public WeMediaDto uploadTemporaryMaterial(String url, String type) {
        try {
            File file = new File(url);
            return weMediaClient.upload(file, type);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传临时文件失败......url:{},type:{},ex:{},st:{}",url,type,e.getMessage(),e.getStackTrace());
        }
        return null;
    }

    @Override
    public WeMediaDto uploadTemporaryMaterial(String url, String type, String name) {
        HttpURLConnection conn = null;
        try{
            URL materialUrl = new URL(url);
            conn = (HttpURLConnection) materialUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            InputStream inputStream = conn.getInputStream();
            return weMediaClient.upload(inputStream, name, type);
        }catch (Exception e){
            e.printStackTrace();
            log.error("上传临时文件失败......url:{},type:{},name:{},ex:{},st:{}",url,type,name,e.getMessage(),e.getStackTrace());
        }finally {
            if (conn !=null){
                conn = null;
            }
        }
        return null;
    }

}
