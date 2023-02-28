package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.material.entity.WeContentViewRecord;
import com.linkwechat.domain.material.query.ContentDetailQuery;
import com.linkwechat.domain.material.query.WeContentViewRecordQuery;
import com.linkwechat.domain.material.vo.ContentAxisVo;
import com.linkwechat.domain.material.vo.ContentDataDetailVo;
import com.linkwechat.domain.material.vo.WeContentCountVo;

import java.util.Date;
import java.util.List;

public interface IWeContentViewRecordService extends IService<WeContentViewRecord> {

    //查看总次数
    List<WeContentViewRecord> getViewTotal(Long id);

    List<WeContentViewRecord> getViewTotal(List<Long> ids);

    List<WeContentViewRecord> getViewTotal(List<Long> ids, Integer resourceType, Long talkId);

    List<WeContentViewRecord> getViewTotal(Long id, Integer resourceType);

    /**
     * 查看明细
     *
     * @param contentDetailQuery
     * @return
     */
    List<ContentDataDetailVo> getViewDetail(ContentDetailQuery contentDetailQuery);

    //今日查看总次数
    int getViewTotalToday(List<WeContentViewRecord> weContentViewRecordList);

    List<ContentAxisVo> getContentAxis(Date beginTime, Date endTime, List<WeContentViewRecord> weContentSendRecordList, List<ContentAxisVo> contentAxisVoList);

    //今日查看总人数
    int getViewByToday(List<WeContentViewRecord> weContentViewRecordList);

    //查看历史总人数
    int getViewByTotal(List<WeContentViewRecord> weContentViewRecordList);


    /**
     * 查看次数统计
     *
     * @param contentDetailQuery
     * @param weContentCountVo
     */
    void setWeContentCountVoForViewRecord(ContentDetailQuery contentDetailQuery, WeContentCountVo weContentCountVo);


    /**
     * 新增查看记录
     *
     * @param weContentViewRecordQuery
     * @return
     * @author WangYX
     * @date 2022/10/24 15:26
     */
    void addView(WeContentViewRecordQuery weContentViewRecordQuery);


}
