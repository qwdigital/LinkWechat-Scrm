package com.linkwechat.domain.wecom.vo.customer.link;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

import java.util.List;

/**
 * 获取获客链接详情
 */
@Data
public class WeLinkCustomerDetailVo extends WeResultVo {


    private Link link;

    private Range range;


    private boolean skip_verify;



    @Data
    public static class Link{

        //
        private String link_id;
        private String link_name;
        //获客链接实际的url
        private String url;
        private long create_time;


    }

    @Data
    public static class  Range{
        private List<String> user_list;

        private List<Long> department_list;

    }
}
