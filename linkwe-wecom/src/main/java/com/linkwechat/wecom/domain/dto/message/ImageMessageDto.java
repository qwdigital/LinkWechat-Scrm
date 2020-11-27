package com.linkwechat.wecom.domain.dto.message;

import lombok.Data;

@Data
public class ImageMessageDto {

    /**
     * 图片的media_id，可以通过 <a href="https://work.weixin.qq.com/api/doc/90000/90135/90253">素材管理接口</a>获得
     */
    private String media_id;

    /**
     * 图片的链接，仅可使用<a href="https://work.weixin.qq.com/api/doc/90000/90135/90256">上传图片接口</a>得到的链接
     */
    private String pic_url;

}
