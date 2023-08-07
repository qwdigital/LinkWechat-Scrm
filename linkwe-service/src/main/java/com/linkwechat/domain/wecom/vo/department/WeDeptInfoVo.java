package com.linkwechat.domain.wecom.vo.department;

import com.linkwechat.domain.wecom.entity.department.WeDeptEntity;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 部门详情
 * @date 2021/12/7 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeDeptInfoVo extends WeResultVo {
    /**
     * 部门信息
     */
    private WeDeptEntity department;
}
