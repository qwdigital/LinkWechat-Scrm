package com.linkwechat.domain.index.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 首页相关基础数据
 */
@Data
public class WeIndexVo {


    //当前版本
    private String  currentEdition;
    //使用人数
    private Integer userNumbers;

    //到期天数
    private int dueDay;


    //客户总数
    private Integer customerTotalNumber;

    //客户总数
    private int groupTotalNumber;

    //客群成员总数
    private int groupMemberTotalNumber;

    //刷新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date synchTime;
  }
