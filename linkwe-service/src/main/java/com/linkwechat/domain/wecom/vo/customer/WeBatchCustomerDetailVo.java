package com.linkwechat.domain.wecom.vo.customer;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 获取客户详情返回对象
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeBatchCustomerDetailVo extends WeResultVo {

    /**
     * 客户详情
     */
    private List<WeCustomerDetailVo> externalContactList;

    /**
     * 分页游标
     */
    private String next_cursor;
}
