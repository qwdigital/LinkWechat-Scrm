package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.wecom.domain.WeQrAttachments;

/**
 * 活码附件表(WeQrAttachments)Mapper接口
 *
 * @author makejava
 * @since 2021-11-07 01:29:12
 */
@Repository()
@Mapper
public interface WeQrAttachmentsMapper extends BaseMapper<WeQrAttachments> {


}

