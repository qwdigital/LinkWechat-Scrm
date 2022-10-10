package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.moments.entity.WeMoments;

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
}
