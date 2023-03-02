package com.linkwechat.wecom.service;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Var;
import com.linkwechat.domain.wecom.query.WeCorpQrQuery;
import com.linkwechat.domain.wecom.query.user.WeAddUserQuery;
import com.linkwechat.domain.wecom.query.user.WeLeaveUserQuery;
import com.linkwechat.domain.wecom.query.user.WeUserListQuery;
import com.linkwechat.domain.wecom.query.user.WeUserQuery;
import com.linkwechat.domain.wecom.vo.WeCorpQrVo;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.user.WeLeaveUserVo;
import com.linkwechat.domain.wecom.vo.user.WeLoginUserVo;
import com.linkwechat.domain.wecom.vo.user.WeUserDetailVo;
import com.linkwechat.domain.wecom.vo.user.WeUserListVo;

/**
 * @author danmo
 * @Description 三方授权业务方法
 * @date 2021/12/16 17:12
 **/
public interface IQwUserService {

    /**
     * 获取自建访问用户身份
     *
     * @param query code和corpid
     * @return
     */
    WeLoginUserVo getLoginUser(WeUserQuery query);

    /**
     * 创建成员
     * @param query
     * @return
     */
    WeResultVo addUser(WeAddUserQuery query);

    /**
     * 更新成员
     * @param query
     * @return
     */
    WeResultVo updateUser(WeAddUserQuery query);

    /**
     * 读取成员信息
     *
     * @param query
     * @return
     */
    WeUserDetailVo getUserInfo(WeUserQuery query);

    /**
     * 删除成员
     * @param query
     * @return
     */
    WeResultVo delUser(WeUserQuery query);

    /**
     * 批量删除成员
     * @param query
     * @return
     */
    WeResultVo batchDelUser(WeUserQuery query);

    /**
     * 获取部门成员
     * @param query
     * @return
     */
    WeUserListVo getSimpleList(WeUserListQuery query);

    /**
     * 获取部门成员详情
     * @param query
     * @return
     */
    WeUserListVo getList(WeUserListQuery query);

    /**
     * 获取访问用户敏感信息
     * @param query
     * @return
     */
    WeUserDetailVo getUserSensitiveInfo(WeUserQuery query);

    /**
     * 获取员工入群码
     * @param query
     * @return
     */
    WeCorpQrVo getJoinQrcode(WeCorpQrQuery query);


    /**
     * 获取待分配的离职成员列表
     * @param query
     * @return
     */
    WeLeaveUserVo getUnassignedList(WeLeaveUserQuery query);
}
