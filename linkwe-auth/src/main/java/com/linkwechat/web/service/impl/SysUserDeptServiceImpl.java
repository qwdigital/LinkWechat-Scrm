package com.linkwechat.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.entity.SysUserDept;
import com.linkwechat.web.mapper.SysUserDeptMapper;
import com.linkwechat.web.service.ISysUserDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author leejoker
 * @date 2022/3/17 2:01
 */
@Service
public class SysUserDeptServiceImpl extends ServiceImpl<SysUserDeptMapper, SysUserDept> implements ISysUserDeptService {

    @Autowired
    @Lazy
    private SysUserServiceImpl sysUserService;


    @Override
    @Transactional
    public void buildSysUserDept(List<SysUserDept> sysUserDeptList) {

        if(CollectionUtil.isNotEmpty(sysUserDeptList)){

            List<SysUser> sysUsers = sysUserService.list();
            if(CollectionUtil.isNotEmpty(sysUsers)){
                Map<String, Long> sysUserMap
                        = sysUsers.stream().collect(Collectors.toMap(SysUser::getWeUserId, SysUser::getUserId));
                sysUserDeptList.stream().forEach(k->{
                    k.setUserId(sysUserMap.get(k.getWeUserId()));
                });
            }

            this.remove(new LambdaQueryWrapper<SysUserDept>()
                    .in(SysUserDept::getWeUserId,sysUserDeptList.stream()
                            .map(SysUserDept::getWeUserId).collect(Collectors.toList())));

            saveBatch(sysUserDeptList);

        }


    }
}
