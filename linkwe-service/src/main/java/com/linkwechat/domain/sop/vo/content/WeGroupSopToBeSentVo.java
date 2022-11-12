package com.linkwechat.domain.sop.vo.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 待发送客群相关
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeGroupSopToBeSentVo {

    //群名
    private String groupName;
    //群id
    private  String chatId;
    //群创建时间
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date addTime;

    //发送sop的相关信息
    private List<WeSopToBeSentContentInfoVo> weSopToBeSentContentInfoVo;
}
