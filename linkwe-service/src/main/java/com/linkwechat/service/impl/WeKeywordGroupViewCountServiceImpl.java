package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.WeKeyWordGroupSub;
import com.linkwechat.domain.WeKeywordGroupViewCount;
import com.linkwechat.domain.community.query.WeCommunityKeyWordGroupTableQuery;
import com.linkwechat.domain.community.vo.WeCommunityKeyWordGroupTableVo;
import com.linkwechat.domain.community.vo.WeKeywordGroupViewCountVo;
import com.linkwechat.service.IWeGroupMemberService;
import com.linkwechat.service.IWeKeyWordGroupSubService;
import com.linkwechat.service.IWeKeywordGroupViewCountService;
import com.linkwechat.mapper.WeKeywordGroupViewCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author robin
* @description 针对表【we_keyword_group_view_count(关键词群访问统计)】的数据库操作Service实现
* @createDate 2023-12-19 12:33:29
*/
@Service
public class WeKeywordGroupViewCountServiceImpl extends ServiceImpl<WeKeywordGroupViewCountMapper, WeKeywordGroupViewCount>
    implements IWeKeywordGroupViewCountService {

    @Autowired
    private IWeGroupMemberService iWeGroupMemberService;

    @Autowired
    private IWeKeyWordGroupSubService iWeKeyWordGroupSubService;

    @Override
    public WeKeywordGroupViewCountVo countTab(Long keywordGroupId) {
        WeKeywordGroupViewCountVo groupViewCountVo=new WeKeywordGroupViewCountVo();

        //获取访问相关数据
        List<WeKeywordGroupViewCount> totalViewCounts = this.list(new LambdaQueryWrapper<WeKeywordGroupViewCount>()
                .eq(WeKeywordGroupViewCount::getKeywordGroupId, keywordGroupId)
        );

        if(CollectionUtil.isNotEmpty(totalViewCounts)){
            groupViewCountVo.setTotalViewNumber(totalViewCounts.size());
            List<WeKeywordGroupViewCount> tdViewCounts = totalViewCounts.stream()
                    .filter(item -> {
                        LocalDateTime localDateTime = item.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        return localDateTime.toLocalDate().equals(LocalDate.now());
                    })
                    .collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(tdViewCounts)){
                groupViewCountVo.setTdViewNumber(tdViewCounts.size());
            }
        }

        //获取进群相关数据
        List<WeKeyWordGroupSub> weKeyWordGroupSubs = iWeKeyWordGroupSubService.list(new LambdaQueryWrapper<WeKeyWordGroupSub>()
                .eq(WeKeyWordGroupSub::getKeywordGroupId, keywordGroupId));
        if(CollectionUtil.isNotEmpty(weKeyWordGroupSubs)){
            groupViewCountVo.setTotalJoinGroupNmber(
                    iWeGroupMemberService.count(new QueryWrapper<WeGroupMember>()
                            .isNull("quit_scene")
                            .in("state", weKeyWordGroupSubs.stream()
                                    .map(WeKeyWordGroupSub::getGroupCodeState)
                                    .collect(Collectors.toSet()))
                            .select("distinct user_id")
                    )
            );


            groupViewCountVo.setTdJoinGroupNmber(
                    iWeGroupMemberService.count(new QueryWrapper<WeGroupMember>()
                            .isNull("quit_scene")
                            .in("state", weKeyWordGroupSubs.stream()
                                    .map(WeKeyWordGroupSub::getGroupCodeState)
                                    .collect(Collectors.toSet()))
                            .apply("date_format (join_time,'%Y-%m-%d') = date_format ({0},'%Y-%m-%d')", new Date())
                            .select("distinct user_id")
                    )
            );

        }

        return groupViewCountVo;
    }



    @Override
    public List<WeKeywordGroupViewCountVo> countTrend(WeKeywordGroupViewCount groupViewCount) {
        List<WeKeywordGroupViewCountVo> groupViewCountVos=new ArrayList<>();

        List<WeKeyWordGroupSub> weKeyWordGroupSubs = iWeKeyWordGroupSubService.list(new LambdaQueryWrapper<WeKeyWordGroupSub>()
                .eq(WeKeyWordGroupSub::getKeywordGroupId, groupViewCount.getKeywordGroupId()));
        if(CollectionUtil.isNotEmpty(weKeyWordGroupSubs)){
            groupViewCountVos=this.baseMapper.countTrend(weKeyWordGroupSubs.stream().map(WeKeyWordGroupSub::getGroupCodeState).collect(Collectors.toList()),
                    groupViewCount.getKeywordGroupId(),groupViewCount.getBeginTime(),groupViewCount.getEndTime());
        }


        return groupViewCountVos;
    }

    @Override
    public PageInfo<WeCommunityKeyWordGroupTableVo> findKeyWordGroupTable(WeCommunityKeyWordGroupTableQuery query, PageDomain pageDomain) {
        PageInfo<WeCommunityKeyWordGroupTableVo> pageInfo = new PageInfo<>();
        List<WeKeyWordGroupSub> weKeyWordGroupSubs = iWeKeyWordGroupSubService.list(new LambdaQueryWrapper<WeKeyWordGroupSub>()
                .eq(WeKeyWordGroupSub::getKeywordGroupId, query.getKeywordGroupId()));
        if(CollectionUtil.isNotEmpty(weKeyWordGroupSubs)){
            query.setStates(
                    weKeyWordGroupSubs.stream().map(WeKeyWordGroupSub::getGroupCodeState).collect(Collectors.toList())
            );
            PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize());
            List<WeCommunityKeyWordGroupTableVo> keyWordGroupTable = this.baseMapper.findKeyWordGroupTable(query);
            pageInfo = new PageInfo<>(keyWordGroupTable);
        }

        return pageInfo;
    }

    @Override
    public List<WeCommunityKeyWordGroupTableVo> exprotKeyWordGroupTable(WeCommunityKeyWordGroupTableQuery query) {
        List<WeCommunityKeyWordGroupTableVo> tableVos=new ArrayList<>();
        List<WeKeyWordGroupSub> weKeyWordGroupSubs = iWeKeyWordGroupSubService.list(new LambdaQueryWrapper<WeKeyWordGroupSub>()
                .eq(WeKeyWordGroupSub::getKeywordGroupId, query.getKeywordGroupId()));
        if(CollectionUtil.isNotEmpty(weKeyWordGroupSubs)){
            query.setStates(
                    weKeyWordGroupSubs.stream().map(WeKeyWordGroupSub::getGroupCodeState).collect(Collectors.toList())
            );
            List<WeCommunityKeyWordGroupTableVo> keyWordGroupTable = this.baseMapper.findKeyWordGroupTable(query);
            setJoinGroupNumber(keyWordGroupTable,query.getStates());
            if(CollectionUtil.isNotEmpty(keyWordGroupTable)){
                tableVos=keyWordGroupTable;
            }
        }
        return tableVos;
    }


    //设置入群数
    @Override
    public void setJoinGroupNumber(List<WeCommunityKeyWordGroupTableVo> keyWordGroupTable,List<String> states){
        if(CollectionUtil.isNotEmpty(keyWordGroupTable)){
            keyWordGroupTable.stream().forEach(k->{
                k.setJoinGroupNumber(
                        iWeGroupMemberService.count(new LambdaQueryWrapper<WeGroupMember>()
                                .eq(WeGroupMember::getUserId,k.getExternalUserid())
                                .in(WeGroupMember::getState,states)
                                .isNull(WeGroupMember::getQuitScene))
                );
            });
        }
    }


}




