package com.linkwechat.domain.groupcode.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeMakeGroupCodeTagQuery {
    //群活码id
    private Long groupCodeId;

    //标签Id列表
    private List<String> tagIds;
}
