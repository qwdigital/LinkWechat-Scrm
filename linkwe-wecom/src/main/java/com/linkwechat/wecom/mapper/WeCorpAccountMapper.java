package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import org.apache.ibatis.annotations.Param;

/**
 * 企业id相关配置Mapper接口
 * 
 * @author ruoyi
 * @date 2020-08-24
 */
public interface WeCorpAccountMapper extends BaseMapper<WeCorpAccount>
{


    /**
     * 启用有效的企业微信账号
     * @param corpId
     * @return
     */
     int startVailWeCorpAccount(@Param("corpId") String corpId);


}
