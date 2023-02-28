package com.linkwechat.domain.live.query;

import com.linkwechat.domain.wx.WxBaseQuery;
import lombok.Data;

@Data
public class WeLiveQuery extends WxBaseQuery {

    String livingId;
    String openid;
}
