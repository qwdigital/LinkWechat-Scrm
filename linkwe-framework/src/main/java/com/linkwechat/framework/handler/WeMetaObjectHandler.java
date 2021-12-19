package com.linkwechat.framework.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author danmo
 * @description 填充器
 * @date 2021/6/19 21:42
 **/
@Slf4j
@Component
public class WeMetaObjectHandler implements MetaObjectHandler {
    private static final String CREATE_TIME = "createTime";
    private static final String CREATE_BY = "createBy";
    private static final String UPDATE_TIME = "updateTime";
    private static final String UPDATE_BY = "updateBy";

    @Override
    public void insertFill(MetaObject metaObject) {
        //创建时间
        this.setFieldValByName(CREATE_TIME, new Date(), metaObject);
        this.setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        //创建人
        String userName = getUserName();
        this.setFieldValByName(CREATE_BY, userName, metaObject);
        this.setFieldValByName(UPDATE_BY, userName, metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //更新时间
        this.setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        //更新人
        this.setFieldValByName(UPDATE_BY, getUserName(), metaObject);
    }

    private String getUserName() {

        String userName;
        try {
            userName = SecurityUtils.getUsername();
        } catch (CustomException e){
            userName = "admin";
        }
        return userName;
    }
}
