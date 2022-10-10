package com.linkwechat.domain.side.query;

import lombok.Data;

@Data
public class WeChatItemQuery {
    String categoryId;
    String search;
    String mediaType;
    Long sideId;
    String keyword;
    String userId;
}
