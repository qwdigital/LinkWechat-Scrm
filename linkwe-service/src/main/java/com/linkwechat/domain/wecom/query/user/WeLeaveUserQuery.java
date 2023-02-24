package com.linkwechat.domain.wecom.query.user;


import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeLeaveUserQuery extends WeBaseQuery {

    private String cursor;
    private Integer page_size;


}
