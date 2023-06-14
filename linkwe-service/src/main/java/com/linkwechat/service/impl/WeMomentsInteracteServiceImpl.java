package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.moments.dto.MomentsInteracteParamDto;
import com.linkwechat.domain.moments.dto.MomentsInteracteResultDto;
import com.linkwechat.domain.moments.entity.WeMomentsInteracte;
import com.linkwechat.domain.moments.entity.WeMomentsUser;
import com.linkwechat.fegin.QwMomentsClient;
import com.linkwechat.mapper.WeMomentsInteracteMapper;
import com.linkwechat.service.IWeMomentsInteracteService;
import com.linkwechat.service.IWeMomentsUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 朋友圈互动
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/12 17:24
 */
@Service
public class WeMomentsInteracteServiceImpl extends ServiceImpl<WeMomentsInteracteMapper, WeMomentsInteracte> implements IWeMomentsInteracteService {

    @Resource
    private QwMomentsClient qwMomentsClient;
    @Resource
    private IWeMomentsUserService weMomentsUserService;

    @Override
    public void syncAddWeMomentsInteracte(Long momentsTaskId, String momentsId) {
        //1.获取执行员工
        LambdaQueryWrapper<WeMomentsUser> queryWrapper = Wrappers.lambdaQuery(WeMomentsUser.class);
        queryWrapper.eq(WeMomentsUser::getMomentsTaskId, momentsTaskId);
        queryWrapper.eq(WeMomentsUser::getMomentsId, momentsId);
        queryWrapper.eq(WeMomentsUser::getDelFlag, Constants.COMMON_STATE);
        List<WeMomentsUser> list = weMomentsUserService.list(queryWrapper);
        if (BeanUtil.isNotEmpty(list)) {
            //2.删除同步之前的互动数据
            removeInteracte(momentsTaskId, momentsId);
            //3.同步互动数据
            for (WeMomentsUser weMomentsUser : list) {
                MomentsInteracteParamDto dto = new MomentsInteracteParamDto();
                dto.setMoment_id(momentsId);
                dto.setUserid(weMomentsUser.getWeUserId());
                AjaxResult<MomentsInteracteResultDto> comments = qwMomentsClient.comments(dto);
                if (comments.getCode() == HttpStatus.SUCCESS) {
                    MomentsInteracteResultDto data = comments.getData();
                    List<MomentsInteracteResultDto.Interacte> comment_list = data.getComment_list();
                    List<WeMomentsInteracte> result = new ArrayList<>();
                    if (CollectionUtil.isNotEmpty(comment_list)) {
                        result.addAll(build(momentsTaskId, momentsId, 0, comment_list));
                    }
                    List<MomentsInteracteResultDto.Interacte> like_list = data.getLike_list();
                    if (CollectionUtil.isNotEmpty(like_list)) {
                        result.addAll(build(momentsTaskId, momentsId, 1, like_list));
                    }
                    if (CollectionUtil.isNotEmpty(result)) {
                        this.saveBatch(result);
                    }
                }
            }
        }
    }

    /**
     * 构建互动数据
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param momentsId     朋友圈id
     * @param type          互动类型:0:评论；1:点赞
     * @param interactes    互动数据集合
     * @return {@link WeMomentsInteracte}
     * @author WangYX
     * @date 2023/06/12 17:42
     */
    public List<WeMomentsInteracte> build(Long momentsTaskId, String momentsId, Integer type, List<MomentsInteracteResultDto.Interacte> interactes) {
        List<WeMomentsInteracte> result = new ArrayList<>();
        for (MomentsInteracteResultDto.Interacte interacte : interactes) {
            WeMomentsInteracte weMomentsInteracte = new WeMomentsInteracte();
            weMomentsInteracte.setId(IdUtil.getSnowflake().nextId());
            weMomentsInteracte.setMomentsTaskId(momentsTaskId);
            weMomentsInteracte.setMomentId(momentsId);
            weMomentsInteracte.setInteracteType(type);
            String userid = interacte.getUserid();
            String external_userid = interacte.getExternal_userid();
            weMomentsInteracte.setInteracteUserType(StrUtil.isNotBlank(userid) ? 0 : 1);
            weMomentsInteracte.setInteracteUserId(StrUtil.isNotBlank(userid) ? userid : external_userid);
            weMomentsInteracte.setInteracteTime(new Date(interacte.getCreate_time()));
            weMomentsInteracte.setCreateTime(new Date());
            weMomentsInteracte.setDelFlag(Constants.COMMON_STATE);
            result.add(weMomentsInteracte);
        }
        return result;
    }

    /**
     * 删除同步之前的互动数据
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param momentsId     朋友圈id
     * @return
     * @author WangYX
     * @date 2023/06/12 17:50
     */
    public void removeInteracte(Long momentsTaskId, String momentsId) {
        LambdaUpdateWrapper<WeMomentsInteracte> updateWrapper = Wrappers.lambdaUpdate(WeMomentsInteracte.class);
        updateWrapper.eq(WeMomentsInteracte::getMomentsTaskId, momentsTaskId);
        updateWrapper.eq(WeMomentsInteracte::getMomentId, momentsId);
        this.remove(updateWrapper);
    }
}
