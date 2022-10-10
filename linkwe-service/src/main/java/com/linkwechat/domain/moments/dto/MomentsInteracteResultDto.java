package com.linkwechat.domain.moments.dto;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

import java.util.List;

@Data
public class MomentsInteracteResultDto extends WeResultVo {

    //评论人数
    private List<Interacte> comment_list;

    //点赞人数
    private List<Interacte> like_list;

    @Data
    public static class Interacte {
        private String external_userid;
        private String userid;
        private Long create_time;
    }
}
