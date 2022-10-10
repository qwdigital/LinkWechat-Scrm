package com.linkwechat.domain.community.vo;

import com.linkwechat.domain.taggroup.vo.WePresTagGroupTaskVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeEmplTaskVo {
    private List todo;
    private List done;
}
