package com.linkwechat.domain.wecom.query.user;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description WeBaseQuery入参
 * @date 2021/12/2 18:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeLoginUserDetailQuery extends WeBaseQuery {

    //成员票据
    private String user_ticket;


}
