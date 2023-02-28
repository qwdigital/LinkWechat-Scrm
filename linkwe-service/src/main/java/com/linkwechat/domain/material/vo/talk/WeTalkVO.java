package com.linkwechat.domain.material.vo.talk;

import com.linkwechat.domain.material.vo.WeMaterialVo;
import lombok.Data;

import java.util.List;

/**
 * 话术中心
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/27 16:42
 */
@Data
public class WeTalkVO {

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 分组Id
     */
    private Long categoryId;

    /**
     * 话术标题
     */
    private String talkTitle;

    /**
     * 话术类型（0企业话术1客服话术）
     */
    private Integer talkType;

    /**
     * 素材信息
     */
    private List<WeMaterialVo> materials;

}
