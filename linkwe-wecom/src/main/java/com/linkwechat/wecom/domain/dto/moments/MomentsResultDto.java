package com.linkwechat.wecom.domain.dto.moments;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

import java.util.List;

@Data
public class MomentsResultDto extends WeResultDto {

     private String jobid;


     private List<TaskList> task_list;



     private List<CustomerList> customer_list;



     @Data
     public static class  TaskList{

          //发表成员用户userid
          private String userid;
          //成员发表状态。0:未发表 1：已发表
          private Integer publish_status;

     }


     @Data
     public static class  CustomerList{

          private String userid;
          private String external_userid;


     }


}
