package com.linkwechat.wecom.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.wecom.domain.WeUser;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 通讯录用户列表
 * @author: HaoN
 * @create: 2020-10-17 00:19
 **/
@Data
public class WeUserListDto extends WeResultDto{

    private List<WeUserDto> userlist;


    public List<WeUser>  getWeUsers(){
        List<WeUser> weUsers=new ArrayList<>();
        if(CollectionUtil.isNotEmpty(userlist)){
            userlist.forEach(user -> {
                WeUser weUser=new WeUser();
                BeanUtil.copyProperties(user,weUser);
                weUser.setUserId(user.getUserid());
                weUser.setOpenUserId(user.getOpen_userid());
                weUser.setIsActivate(user.getStatus());
                weUser.setHeadImageUrl(user.getAvatar());
                weUser.setJoinTime(new Date());
                if (CollectionUtil.isNotEmpty(user.getDepartment())){
                    weUser.setDepartment(String.join(",", user.getDepartment()));
                }
                if (CollectionUtil.isNotEmpty(user.getIs_leader_in_dept())){
                    weUser.setIsLeaderInDept(String.join(",", user.getIs_leader_in_dept()));
                }
                weUsers.add(weUser);
            });
        }
        return weUsers;
    }

}
