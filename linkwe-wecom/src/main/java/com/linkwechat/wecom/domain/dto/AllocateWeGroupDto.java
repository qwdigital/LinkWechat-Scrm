package com.linkwechat.wecom.domain.dto;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 离职成员群分配
 * @author: HaoN
 * @create: 2020-10-24 23:37
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AllocateWeGroupDto {

    /**需要转群主的客户群ID列表。*/
    private  String[] chat_id_list;

    /**新群主ID。*/
    private String new_owner;
}
