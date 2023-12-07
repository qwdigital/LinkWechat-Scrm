package com.linkwechat.domain.community.vo;

import com.linkwechat.domain.tag.vo.WeTagVo;
import lombok.Data;

import java.util.List;

/**
 * 社区运营 新客自动拉群
 */
@Data
public class WeCommunityWeComeMsgVo {

    private Long id;

    /**
     * 群活码路径
     */
    private String codeUrl;

    /**
     * 欢迎语
     */
    private String welcomeMsg;

    /**
     * 客户标签列表
     */
    private List<WeTagVo> tagList;


    /**
     * 链接标题
     */
    private String linkTitle;

    /**
     * 链接描述
     */
    private String linkDesc;

    /**
     * 链接封面
     */
    private String linkCoverUrl;



}
