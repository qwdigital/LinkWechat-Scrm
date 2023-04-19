package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeMoments;
import com.linkwechat.domain.moments.dto.MomentsListDetailParamDto;
import com.linkwechat.domain.moments.dto.MomentsListDetailResultDto;

import java.util.List;

public interface IWeMomentsService extends IService<WeMoments> {

    List<WeMoments> findMoments(WeMoments weMoments);

    void addOrUpdateMoments(WeMoments weMoments) throws InterruptedException;

    void synchPersonMoments(Integer filterType);

    void synchEnterpriseMoments(Integer filterType);

    WeMoments findMomentsDetail(String momentId);

    void synchMomentsInteracteHandler(String msg);

    void synchMomentsInteracte(List<String> userIds);

    void synchWeMomentsHandler(String msg);

    void getByMoment(String nextCursor, List<MomentsListDetailResultDto.Moment> list, MomentsListDetailParamDto query);

    void syncMentsDataHandle(List<MomentsListDetailResultDto.Moment> moments);
}
