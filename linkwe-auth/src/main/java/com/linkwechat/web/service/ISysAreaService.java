package com.linkwechat.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.vo.SysAreaVo;
import com.linkwechat.web.domain.SysArea;

import java.util.List;

/**
 * 行政区划(SysArea)
 *
 * @author danmo
 * @since 2022-06-27 11:01:07
 */
public interface ISysAreaService extends IService<SysArea> {

    List<SysArea> getList(SysArea sysArea);

    List<SysAreaVo> getChildListById(Integer id);
}
