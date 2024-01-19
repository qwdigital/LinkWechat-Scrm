package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.SiteStatsConstants;
import com.linkwechat.common.enums.CategoryMediaType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.ip.IpUtils;
import com.linkwechat.domain.WeFormSurveyAnswer;
import com.linkwechat.domain.WeFormSurveyCatalogue;
import com.linkwechat.domain.WeFormSurveyCount;
import com.linkwechat.domain.form.query.WeAddFormSurveyCatalogueQuery;
import com.linkwechat.domain.form.query.WeFormSurveyCatalogueQuery;
import com.linkwechat.domain.material.entity.WeCategory;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.vo.WeMaterialNewVo;
import com.linkwechat.mapper.WeCategoryMapper;
import com.linkwechat.mapper.WeFormSurveyAnswerMapper;
import com.linkwechat.mapper.WeFormSurveyCatalogueMapper;
import com.linkwechat.service.IWeFormSurveyCatalogueService;
import com.linkwechat.service.IWeFormSurveyCountService;
import com.linkwechat.service.IWeFormSurveyStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 问卷-目录列表(WeFormSurveyCatalogue)
 *
 * @author danmo
 * @since 2022-09-20 18:02:56
 */
@Slf4j
@Service
public class WeFormSurveyCatalogueServiceImpl extends ServiceImpl<WeFormSurveyCatalogueMapper, WeFormSurveyCatalogue> implements IWeFormSurveyCatalogueService {


    @Resource
    private WeFormSurveyCatalogueMapper weFormSurveyCatalogueMapper;
    @Resource
    private WeFormSurveyAnswerMapper weFormSurveyAnswerMapper;
    @Resource
    private WeCategoryMapper weCategoryMapper;

    @Resource
    private RedisTemplate redisTemplate;


    @Resource
    private LinkWeChatConfig linkWeChatConfig;


    @Resource
    private IWeFormSurveyCountService iWeFormSurveyCountService;

    @Override
    public Long add(WeAddFormSurveyCatalogueQuery query) {
        List<WeFormSurveyCatalogue> surveyCatalogues = list(new LambdaQueryWrapper<WeFormSurveyCatalogue>()
                .eq(WeFormSurveyCatalogue::getSurveyName, query.getSurveyName())
                .eq(WeFormSurveyCatalogue::getDelFlag, 0));
        if (CollectionUtil.isNotEmpty(surveyCatalogues)) {
            throw new WeComException("表单名称已重复!");
        }
        WeFormSurveyCatalogue catalogue = new WeFormSurveyCatalogue();
        BeanUtil.copyProperties(query, catalogue);
        save(catalogue);

        //保存站点数据到Redis
        String channelsName = catalogue.getChannelsName();
        if (StringUtils.isNotBlank(channelsName)) {
            String[] split = channelsName.split(",");
            for (String channelName : split) {
                //PV
                String pvKey = StringUtils.format(SiteStatsConstants.PREFIX_KEY_PV, catalogue.getId(), channelName);
                redisTemplate.opsForValue().set(pvKey, 0);
                //IP
                String ipKey = StringUtils.format(SiteStatsConstants.PREFIX_KEY_IP, catalogue.getId(), channelName);
                redisTemplate.opsForSet().add(ipKey, "");
            }
        }
        return catalogue.getId();
    }

    @Override
    public void deleteSurvey(List<Long> ids) {
        update(new LambdaUpdateWrapper<WeFormSurveyCatalogue>()
                .set(WeFormSurveyCatalogue::getDelFlag, 1)
                .in(WeFormSurveyCatalogue::getId, ids));
    }

    @Override
    public void updateSurvey(WeAddFormSurveyCatalogueQuery query) {
        WeFormSurveyCatalogue catalogue = new WeFormSurveyCatalogue();
        BeanUtil.copyProperties(query, catalogue);
        update(catalogue, new LambdaQueryWrapper<WeFormSurveyCatalogue>().eq(WeFormSurveyCatalogue::getId, query.getId()));
    }


    @Override
    public WeFormSurveyCatalogue getInfo(Long id,String ipAddress,String dataSource,boolean isCount) {
        WeFormSurveyCatalogue weFormSurveyCatalogue = weFormSurveyCatalogueMapper.getWeFormSurveyCatalogueById(id);
        if(isCount){
            iWeFormSurveyCountService.weFormCount(id,dataSource,ipAddress);
        }

        return weFormSurveyCatalogue;
    }

    @Override
    public List<WeFormSurveyCatalogue> getList(WeFormSurveyCatalogueQuery query) {
        LambdaQueryWrapper<WeFormSurveyCatalogue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(query.getId()), WeFormSurveyCatalogue::getId, query.getId());
        queryWrapper.eq(StringUtils.isNotBlank(query.getSurveyName()), WeFormSurveyCatalogue::getSurveyName, query.getSurveyName());
        queryWrapper.eq(Objects.nonNull(query.getSurveyState()), WeFormSurveyCatalogue::getSurveyState, query.getSurveyState());
        queryWrapper.eq(Objects.nonNull(query.getGroupId()), WeFormSurveyCatalogue::getGroupId, query.getGroupId());
        queryWrapper.apply(Objects.nonNull(query.getCreateTime()), "date_format(create_time,'%Y-%m-%d') = " + "'" + DateUtil.formatDate(query.getCreateTime()) + "'");
        queryWrapper.between(Objects.nonNull(query.getStartDate()) && Objects.nonNull(query.getEndDate()), WeFormSurveyCatalogue::getCreateTime, DateUtil.formatDate(query.getStartDate()) + " 00:00:00", DateUtil.formatDate(query.getEndDate()) + " 23:59:59");
        queryWrapper.eq(WeFormSurveyCatalogue::getDelFlag, 0);
        queryWrapper.orderByDesc(WeFormSurveyCatalogue::getUpdateTime);
        List<WeFormSurveyCatalogue> catalogueList = list(queryWrapper);
        if (CollectionUtil.isNotEmpty(catalogueList)) {
            //分组
            List<WeCategory> weCategories = weCategoryMapper.categoryList("15");
            WeCategory weCategory = new WeCategory();
            weCategory.setId(1L);
            weCategory.setName("默认分组");
            weCategory.setFlag(1);
            weCategory.setParentId(0L);
            weCategories.add(weCategory);
            Map<Long, WeCategory> collect = weCategories.stream().collect(Collectors.toMap(WeCategory::getId, Function.identity()));

            for (WeFormSurveyCatalogue weFormSurveyCatalogue : catalogueList) {
                //分组名
                WeCategory category = collect.get(weFormSurveyCatalogue.getGroupId());
                if (ObjectUtil.isNotNull(category) && StrUtil.isNotBlank(category.getName())) {
                    weFormSurveyCatalogue.setGroupName(category.getName());
                }
                //总访问量和有效收集量
//                String channelsName = weFormSurveyCatalogue.getChannelsName();
//                Integer pv = 0;
//                String[] split = channelsName.split(",");
//                for (String channelName : split) {
//                    //PV
//                    String pvKey = StringUtils.format(SiteStatsConstants.PREFIX_KEY_PV, weFormSurveyCatalogue.getId(), channelName);
//                    Object o = redisTemplate.opsForValue().get(pvKey);
//                    if (o != null) {
//                        pv += (Integer) o;
//                    }
//                }


                //总访问数
                weFormSurveyCatalogue.setTotalVisits(
                        iWeFormSurveyCountService.sumTotalVisits(weFormSurveyCatalogue.getId())
                );
                //有效的收集量
                QueryWrapper<WeFormSurveyAnswer> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(WeFormSurveyAnswer::getBelongId, weFormSurveyCatalogue.getId());
                wrapper.lambda().eq(WeFormSurveyAnswer::getAnEffective, 0);
                wrapper.lambda().eq(WeFormSurveyAnswer::getDelFlag, Constants.NORMAL_CODE);
                List<WeFormSurveyAnswer> list = weFormSurveyAnswerMapper.selectList(wrapper);
                if (list != null) {
                    weFormSurveyCatalogue.setCollectionVolume(list.size());
                } else {
                    weFormSurveyCatalogue.setCollectionVolume(0);
                }
            }
        }
        return catalogueList;
    }

    @Override
    public List<WeMaterialNewVo> findFormToWeMaterialNewVo(WeFormSurveyCatalogueQuery query){
        List<WeMaterialNewVo>  weMaterialNewVos=new ArrayList<>();

        List<WeFormSurveyCatalogue> catalogues = this.getList(query);


        if(CollectionUtil.isNotEmpty(catalogues)){
            catalogues.stream().forEach(weFormSurveyCatalogue -> {

                if(StringUtils.isNotEmpty(weFormSurveyCatalogue.getChannelsName())){
                    ListUtil.toList(weFormSurveyCatalogue.getChannelsName().split(",")).stream().forEach(k->{
                        WeMaterialNewVo weMaterialNewVo =new  WeMaterialNewVo();
                        weMaterialNewVo.setMaterialName(weFormSurveyCatalogue.getSurveyName() + " 渠道:"+k);
                        weMaterialNewVo.setMaterialUrl(
                                StringUtils.substringBeforeLast(linkWeChatConfig.getH5Domain(), "/") + weFormSurveyCatalogue.getHtmlPath()
                                        + "?id=true&formId=" + weFormSurveyCatalogue.getId() + "&dataSource=" + k
                        );
                        weMaterialNewVo.setMediaType(String.valueOf(CategoryMediaType.ZLBDURL.getType()));
                        weMaterialNewVos.add(
                                weMaterialNewVo
                        );
                    });


                }else{
                    WeMaterialNewVo weMaterialNewVo =new  WeMaterialNewVo();
                    weMaterialNewVo.setMaterialName(weFormSurveyCatalogue.getSurveyName());
                    weMaterialNewVo.setMaterialUrl(
                            StringUtils.substringBeforeLast(linkWeChatConfig.getH5Domain(), "/") + weFormSurveyCatalogue.getHtmlPath()
                                    + "?id=true&formId=" + weFormSurveyCatalogue.getId()
                    );
                    weMaterialNewVos.add(
                            weMaterialNewVo
                    );
                }
            });
        }


        return weMaterialNewVos;

    }
    @Override
    public void updateStatus(WeFormSurveyCatalogueQuery query) {
        if (Objects.isNull(query.getSurveyState())) {
            throw new WeComException("状态不能为空");
        }
        if (Objects.isNull(query.getId())) {
            throw new WeComException("ID不能为空");
        }
        update(new LambdaUpdateWrapper<WeFormSurveyCatalogue>()
                .set(WeFormSurveyCatalogue::getSurveyState, query.getSurveyState())
                .eq(WeFormSurveyCatalogue::getId, query.getId()));
    }

    @Override
    public void deleteFormSurveyGroup(Long groupId) {
        update(new LambdaUpdateWrapper<WeFormSurveyCatalogue>()
                .set(WeFormSurveyCatalogue::getGroupId, 1L)
                .eq(WeFormSurveyCatalogue::getGroupId, groupId)
                .eq(WeFormSurveyCatalogue::getDelFlag, 0));
    }

    @Override
    public WeFormSurveyCatalogue getWeFormSurveyCatalogueById(Long id) {
        WeFormSurveyCatalogue weFormSurveyCatalogue = weFormSurveyCatalogueMapper.getWeFormSurveyCatalogueById(id);
        return weFormSurveyCatalogue;
    }


    @Override
    public List<WeFormSurveyCatalogue> getListIgnoreTenantId() {
        return weFormSurveyCatalogueMapper.getListIgnoreTenantId();
    }

    @Override
    public void updateByIdIgnoreTenantId(WeFormSurveyCatalogue weFormSurveyCatalogue) {
        weFormSurveyCatalogueMapper.updateByIdIgnoreTenantId(weFormSurveyCatalogue);
    }



}
