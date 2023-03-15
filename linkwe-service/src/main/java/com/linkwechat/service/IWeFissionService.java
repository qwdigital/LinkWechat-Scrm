package com.linkwechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.fission.WeFission;
import java.util.List;

/**
* @author robin
* @description 针对表【we_fission(裂变（任务宝,群裂变）)】的数据库操作Service
* @createDate 2023-03-14 14:07:21
*/
public interface IWeFissionService extends IService<WeFission> {

    List<WeFission> findWeFissions(WeFission weFission);
}
