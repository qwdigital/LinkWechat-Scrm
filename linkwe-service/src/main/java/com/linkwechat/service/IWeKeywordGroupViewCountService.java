package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.domain.WeKeywordGroupViewCount;
import com.linkwechat.domain.community.query.WeCommunityKeyWordGroupTableQuery;
import com.linkwechat.domain.community.vo.WeCommunityKeyWordGroupTableVo;
import com.linkwechat.domain.community.vo.WeKeywordGroupViewCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author robin
* @description 针对表【we_keyword_group_view_count(关键词群访问统计)】的数据库操作Service
* @createDate 2023-12-19 12:33:29
*/
public interface IWeKeywordGroupViewCountService extends IService<WeKeywordGroupViewCount> {

    /**
     * 头部tab统计
     * @param keywordGroupId
     * @return
     */
    WeKeywordGroupViewCountVo countTab(Long keywordGroupId);


    /**
     * 折现统计图
     * @param groupViewCount
     * @return
     */
    List<WeKeywordGroupViewCountVo> countTrend(WeKeywordGroupViewCount groupViewCount);



    /**
     * 数据明细
     * @param query
     * @return
     */
    PageInfo<WeCommunityKeyWordGroupTableVo> findKeyWordGroupTable(WeCommunityKeyWordGroupTableQuery query, PageDomain pageDomain);


    /**
     * 获取导出数据
     * @param query
     * @return
     */
    List<WeCommunityKeyWordGroupTableVo> exprotKeyWordGroupTable(WeCommunityKeyWordGroupTableQuery query);


    /**
     * 设置客户所在客群数
     * @param keyWordGroupTable
     * @param states
     */
    void setJoinGroupNumber(List<WeCommunityKeyWordGroupTableVo> keyWordGroupTable,List<String> states);
}
