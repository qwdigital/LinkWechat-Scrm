package com.linkwechat.wecom.domain.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.domain.WeUser;
import lombok.Data;

import java.util.ArrayList;
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
            userlist.stream().forEach(k->{
                WeUser weUser=new WeUser();
                BeanUtils.copyPropertiesASM(k,weUser);
                weUser.setIsActivate(k.getStatus());
                weUser.setAvatarMediaid(k.getAvatar());
                weUsers.add(weUser);
            });
        }
        return weUsers;
    }

}
