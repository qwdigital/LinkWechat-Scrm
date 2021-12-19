package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 同步记录表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_synch_record")
public class WeSynchRecord {
    @TableId
    private Long id;


    private Integer synchType;


    private Date synchTime;


}
