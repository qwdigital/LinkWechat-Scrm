package com.linkwechat.wecom.domain.dto.customer;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @description: 客户群相关
 * @author: HaoN
 * @create: 2020-10-20 21:52
 **/
@Data
public class CustomerGroupList extends WeResultDto {

    /**
     * 客户群列表
     */
    private List<GroupChat> groupChatList;

    private String next_cursor;


    /**
     * 请求参数
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Params{


        /**群状态过滤: 0 - 所有列表;1 - 离职待继承;2 - 离职继承中;3 - 离职继承完成; */
        private Integer status_filter;

        /**分页，偏移量。默认为0 */
        private Integer offset=new Integer(0);

         /**分页，预期请求的数据量，取值范围 1 ~ 1000 */
        private Integer limit=new Integer(1000);

        private String cursor;

        private String owner_filter;

    }


    @Data
    public class GroupChat{

        /**客户群ID*/
        private String chatId;

        /**0 - 正常;1 - 跟进人离职;2 - 离职继承中;3 - 离职继承完成*/
        private Integer status;
    }





}
