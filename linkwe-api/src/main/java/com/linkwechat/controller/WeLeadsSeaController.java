package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.annotation.RepeatSubmit;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSea;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSeaVisibleRange;
import com.linkwechat.domain.leads.sea.query.VisibleRange;
import com.linkwechat.domain.leads.sea.query.WeLeadsSeaSaveRequest;
import com.linkwechat.domain.leads.sea.query.WeLeadsSeaUpdateRequest;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaListVo;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaVo;
import com.linkwechat.service.IWeLeadsSeaService;
import com.linkwechat.service.IWeLeadsSeaVisibleRangeService;
import com.linkwechat.service.IWeLeadsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 线索公海
 *
 * @author WangYX
 * @since 2023-04-03
 */
@RestController
@RequestMapping("/sea")
public class WeLeadsSeaController extends BaseController {

    @Resource
    private IWeLeadsSeaService weLeadsSeaService;
    @Resource
    private IWeLeadsService weLeadsService;
    @Resource
    private IWeLeadsSeaVisibleRangeService weLeadsSeaVisibleRangeService;

    /**
     * 新增
     *
     * @param request 线索公海新增请求参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/10 18:06
     */
    @RepeatSubmit
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody WeLeadsSeaSaveRequest request) {
        Long id = weLeadsSeaService.add(request);
        return AjaxResult.success(id);
    }

    /**
     * 修改
     *
     * @param request 线索公海修改请求参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/10 18:18
     */
    @PutMapping("/update")
    public AjaxResult update(@Validated @RequestBody WeLeadsSeaUpdateRequest request) {
        weLeadsSeaService.update(request);
        return AjaxResult.success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public AjaxResult remove(@PathVariable("id") Long id) {
        WeLeadsSea weLeadsSea = weLeadsSeaService.getById(id);
        if (Optional.ofNullable(weLeadsSea).isPresent()) {
            LambdaQueryWrapper<WeLeads> queryWrapper = Wrappers.lambdaQuery(WeLeads.class);
            queryWrapper.eq(WeLeads::getSeaId, weLeadsSea.getId());
            queryWrapper.eq(WeLeads::getDelFlag, Constants.COMMON_STATE);
            int count = weLeadsService.count(queryWrapper);
            if (count > 0) {
                throw new ServiceException("公海内存在线索，无法删除！");
            }
            LambdaUpdateWrapper<WeLeadsSea> updateWrapper = Wrappers.lambdaUpdate(WeLeadsSea.class);
            updateWrapper.set(WeLeadsSea::getDelFlag, Constants.DELETE_STATE);
            updateWrapper.eq(WeLeadsSea::getId, id);
            weLeadsSeaService.update(updateWrapper);
        }
        return AjaxResult.success();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/batch/delete/{ids}")
    public AjaxResult batchRemove(@PathVariable("ids") List<Long> ids) {
        LambdaUpdateWrapper<WeLeadsSea> updateWrapper = Wrappers.lambdaUpdate(WeLeadsSea.class);
        updateWrapper.set(WeLeadsSea::getDelFlag, Constants.COMMON_STATE);
        updateWrapper.in(WeLeadsSea::getId, ids);
        weLeadsSeaService.update(updateWrapper);
        return AjaxResult.success();
    }

    /**
     * 管理员视角公海-列表
     *
     * @return
     */
    @GetMapping("/manager/list")
    public AjaxResult managerList() {
        //是否超级管理员
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        boolean admin = sysUser.isAdmin();

        List<WeLeadsSea> seaList = new ArrayList<>();
        if (admin) {
            //是超级管理员
            seaList = getSeaList();
        } else {
            //分级管理员可见范围
            List<WeLeadsSeaVisibleRange> visibleRanges = weLeadsSeaVisibleRangeService.getVisibleRange(sysUser.getUserId(), sysUser.getWeUserId());
            if (visibleRanges != null && visibleRanges.size() > 0) {
                List<Long> seaIds = visibleRanges.stream().map(i -> i.getSeaId()).collect(Collectors.toList());
                LambdaQueryWrapper<WeLeadsSea> queryWrapper = Wrappers.lambdaQuery(WeLeadsSea.class);
                queryWrapper.eq(WeLeadsSea::getDelFlag, Constants.COMMON_STATE);
                queryWrapper.in(WeLeadsSea::getId, seaIds);
                seaList = weLeadsSeaService.list(queryWrapper);
            }

            //自己创建的公海，在分公海列表可见，产品确认过
            LambdaQueryWrapper<WeLeadsSea> queryWrapper = Wrappers.lambdaQuery(WeLeadsSea.class);
            queryWrapper.eq(WeLeadsSea::getCreateById, SecurityUtils.getUserId());
            queryWrapper.eq(WeLeadsSea::getDelFlag, Constants.COMMON_STATE);
            List<WeLeadsSea> list = weLeadsSeaService.list(queryWrapper);
            seaList.addAll(list);
        }
        seaList = seaList.stream().distinct().collect(Collectors.toList());
        //公海的可见范围
        List<WeLeadsSeaListVo> weLeadsSeaListVos = seaList.stream().map(o -> {
            WeLeadsSeaListVo weLeadsSeaListVo = BeanUtil.copyProperties(o, WeLeadsSeaListVo.class);
            LambdaQueryWrapper<WeLeadsSeaVisibleRange> queryWrapper = Wrappers.lambdaQuery(WeLeadsSeaVisibleRange.class);
            queryWrapper.eq(WeLeadsSeaVisibleRange::getSeaId, weLeadsSeaListVo.getId());
            queryWrapper.eq(WeLeadsSeaVisibleRange::getDelFlag, Constants.COMMON_STATE);
            List<WeLeadsSeaVisibleRange> ranges = weLeadsSeaVisibleRangeService.list(queryWrapper);
            List<String> collect = ranges.stream().map(WeLeadsSeaVisibleRange::getDataName).collect(Collectors.toList());
            weLeadsSeaListVo.setVisibleRange(collect);

            return weLeadsSeaListVo;
        }).collect(Collectors.toList());
        return AjaxResult.success(weLeadsSeaListVos);
    }


    /**
     * 获取所有公海的数据
     *
     * @return
     */
    private List<WeLeadsSea> getSeaList() {
        LambdaQueryWrapper<WeLeadsSea> queryWrapper = Wrappers.lambdaQuery(WeLeadsSea.class);
        queryWrapper.eq(WeLeadsSea::getDelFlag, Constants.COMMON_STATE);
        List<WeLeadsSea> list = weLeadsSeaService.list(queryWrapper);
        return list;
    }

    /**
     * 员工视角公海-列表  (员工可见公海)
     *
     * @return
     */
    @GetMapping("/user/list")
    public AjaxResult userList() {
        //员工可见范围
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        List<WeLeadsSeaVisibleRange> visibleRanges = weLeadsSeaVisibleRangeService.getVisibleRange(sysUser.getUserId(), sysUser.getWeUserId());
        List<WeLeadsSeaListVo> weLeadsSeaListVos = new ArrayList<>();
        if (visibleRanges != null && visibleRanges.size() > 0) {
            List<Long> seaIds = visibleRanges.stream().map(o -> o.getSeaId()).collect(Collectors.toList());
            LambdaQueryWrapper<WeLeadsSea> queryWrapper = Wrappers.lambdaQuery(WeLeadsSea.class);
            queryWrapper.eq(WeLeadsSea::getDelFlag, Constants.COMMON_STATE);
            queryWrapper.in(WeLeadsSea::getId, seaIds);
            List<WeLeadsSea> list = weLeadsSeaService.list(queryWrapper);
            weLeadsSeaListVos = BeanUtil.copyToList(list, WeLeadsSeaListVo.class);
        }
        return AjaxResult.success(weLeadsSeaListVos);
    }


    /**
     * 获取公海详情
     *
     * @param id 公海Id
     * @return
     */
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {
        WeLeadsSea weLeadsSea = weLeadsSeaService.getById(id);
        WeLeadsSeaVo weLeadsSeaVo = BeanUtil.copyProperties(weLeadsSea, WeLeadsSeaVo.class);
        Optional.ofNullable(weLeadsSeaVo).ifPresent(i -> {
            //获取公海对应可见范围
            LambdaQueryWrapper<WeLeadsSeaVisibleRange> queryWrapper = Wrappers.lambdaQuery(WeLeadsSeaVisibleRange.class);
            queryWrapper.eq(WeLeadsSeaVisibleRange::getSeaId, i.getId());
            queryWrapper.eq(WeLeadsSeaVisibleRange::getDelFlag, Constants.COMMON_STATE);
            List<WeLeadsSeaVisibleRange> list = weLeadsSeaVisibleRangeService.list(queryWrapper);
            Map<Integer, List<WeLeadsSeaVisibleRange>> collect = list.stream().collect(Collectors.groupingBy(WeLeadsSeaVisibleRange::getType));

            //部门
            List<WeLeadsSeaVisibleRange> deptVisibleRanges = collect.get(VisibleRange.dept);
            Optional.ofNullable(deptVisibleRanges).ifPresent(o -> {
                List<Long> deptIds = o.stream().map(j -> Long.valueOf(j.getDataId())).collect(Collectors.toList());
                VisibleRange.DeptRange deptRange = new VisibleRange.DeptRange();
                deptRange.setSelect(true);
                deptRange.setDeptIds(deptIds);
                i.setDeptRange(deptRange);
            });

            //岗位
            List<WeLeadsSeaVisibleRange> postVisibleRanges = collect.get(VisibleRange.post);
            Optional.ofNullable(postVisibleRanges).ifPresent(o -> {
                List<String> posts = o.stream().map(WeLeadsSeaVisibleRange::getDataName).collect(Collectors.toList());
                VisibleRange.PostRange postRange = new VisibleRange.PostRange();
                postRange.setSelect(true);
                postRange.setPosts(posts);
                i.setPostRange(postRange);
            });

            //用户
            List<WeLeadsSeaVisibleRange> userVisibleRanges = collect.get(VisibleRange.user);
            Optional.ofNullable(userVisibleRanges).ifPresent(o -> {
                List<String> weUserIds = o.stream().map(WeLeadsSeaVisibleRange::getDataId).collect(Collectors.toList());
                VisibleRange.UserRange userRange = new VisibleRange.UserRange();
                userRange.setSelect(true);
                userRange.setWeUserIds(weUserIds);
                i.setUserRange(userRange);
            });
        });
        return AjaxResult.success(weLeadsSeaVo);
    }


}

