package com.linkwechat.wecom.domain.dto.message;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class QueryCustomerMessageStatusResultDto extends WeResultDto {

    /**
     * 返回查询信息列表
     */
    private List<DetailMessageStatusResultDto> detail_list;

    private String check_status;


}


