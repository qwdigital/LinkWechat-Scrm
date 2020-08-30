package com.linkwechat.wecom.domain;

import java.util.List;

/**
 * @description: 企业部门
 * @author: HaoN
 * @create: 2020-08-27 15:54
 **/
public class WeDepartMent extends WeResult {



    private List<DeartMent> deartMents;


    class DeartMent{

        private String name;

        private String name_en;

        private Long parentid;

        private Long order;

    }

}
