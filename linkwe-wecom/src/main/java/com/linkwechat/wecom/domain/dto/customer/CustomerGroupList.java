package com.linkwechat.wecom.domain.dto.customer;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description: 客户群相关
 * @author: HaoN
 * @create: 2020-10-20 21:52
 **/
@Data
public class CustomerGroupList extends WeResultDto {

    //返回结果
    private List<GroupChat> group_chat_list;


    /**
     * 请求参数
     */
    @Data
    public class Params{


        /**群状态过滤: 0 - 所有列表;1 - 离职待继承;2 - 离职继承中;3 - 离职继承完成; */
        private Integer status_filter;

        /**分页，偏移量。默认为0 */
        private Integer offset=new Integer(0);

         /**分页，预期请求的数据量，取值范围 1 ~ 1000 */
        private Integer limit=new Integer(1000);

    }


    @Data
    public class GroupChat{

        /**客户群ID*/
        private String chat_id;

        /**0 - 正常;1 - 跟进人离职;2 - 离职继承中;3 - 离职继承完成*/
        private Integer status;
    }





}
