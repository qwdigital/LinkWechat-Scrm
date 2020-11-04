package com.linkwechat.wecom.service.impl;

import cn.hutool.core.date.DateUtil;
import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.config.ServerConfig;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.file.FileUploadUtils;
import com.linkwechat.wecom.domain.WeMaterial;
import com.linkwechat.wecom.domain.vo.WeMaterialFileVO;
import com.linkwechat.wecom.mapper.WeMaterialMapper;
import com.linkwechat.wecom.service.IWeMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Override
    public WeMaterialFileVO uploadWeMaterialFile(MultipartFile file, String type) {
        if (null == file) {
            throw new WeComException("文件为空！");
        }

        try {
            //上传文件到文件服务器
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            //上传临时素材
            Optional<MediaType> mediaType = MediaType.of(type);
            if (!mediaType.isPresent()) {
                throw new WeComException("媒体类型出错！");
            }
            //构造返回结果
            return WeMaterialFileVO.builder().materialUrl(url).materialName(file.getOriginalFilename()).build();
        } catch (Exception e) {
            throw new WeComException("临时素材上传失败！");
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
    public List<WeMaterial> findWeMaterials(String categoryId, String search) {
        return weMaterialMapper.findWeMaterials(categoryId, search);
    }

}
