package com.linkwechat.common.handler;

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
    private static final String CREATE_BY_ID = "createById";
    private static final String UPDATE_TIME = "updateTime";
    private static final String UPDATE_BY = "updateBy";
    private static final String UPDATE_BY_ID = "updateById";

    @Override
    public void insertFill(MetaObject metaObject) {
        //创建时间
        this.setFieldValByName(CREATE_TIME, new Date(), metaObject);
        this.setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        //创建人
        String userName = getUserName();
        Long userId = getUserId();

        if(StringUtils.isEmpty((String) this.getFieldValByName(CREATE_BY, metaObject))){
            this.setFieldValByName(CREATE_BY, userName, metaObject);
        }

        if(this.getFieldValByName(CREATE_BY_ID,metaObject)==null){
            this.setFieldValByName(CREATE_BY_ID, userId, metaObject);
        }


        if(StringUtils.isEmpty((String) this.getFieldValByName(UPDATE_BY, metaObject))){
            this.setFieldValByName(UPDATE_BY, userName, metaObject);
        }

        if(this.getFieldValByName(UPDATE_BY_ID,metaObject)==null){
            this.setFieldValByName(UPDATE_BY_ID, userId, metaObject);
        }


    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //更新时间
        this.setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        //更新人
        this.setFieldValByName(UPDATE_BY, getUserName(), metaObject);
        //更新人ID
        this.setFieldValByName(UPDATE_BY_ID, getUserId(), metaObject);
    }

    private String getUserName() {

        String userName;
        try {
            userName = SecurityUtils.getUserName();
        } catch (CustomException e){
            userName = "admin";
        }
        return userName;
    }

    private Long getUserId() {
        Long userId;
        try {
            userId = SecurityUtils.getUserId();
        } catch (CustomException e){
            userId = 0L;
        }
        return userId;
    }
}
