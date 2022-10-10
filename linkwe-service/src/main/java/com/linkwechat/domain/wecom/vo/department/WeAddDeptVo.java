package com.linkwechat.domain.wecom.vo.department;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 创建部门
 * @date 2021/12/7 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeAddDeptVo extends WeResultVo {
    /**
     * 创建的部门id
     */
    private Integer id;
}
