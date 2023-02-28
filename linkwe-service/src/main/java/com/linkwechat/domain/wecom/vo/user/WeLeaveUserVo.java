package com.linkwechat.domain.wecom.vo.user;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * 离职成员对应的信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeLeaveUserVo extends WeResultVo {



    private List<Info> info;
    //是否是最后一条记录 true是 false不是
    private boolean is_last;

   //分页查询游标,已经查完则返回空("")，使用page_id作为查询参数时不返回
    private String next_cursor;


    @Data
    public static class Info{

        //离职成员的userid
        private String handover_userid;

        //外部联系人userid
        private String external_userid;

       //离职时间
        private long dimission_time;




    }



}
