package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.domain.substitute.customer.order.entity.WeSubstituteCustomerOrderCatalogue;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderCatalogueAddRequest;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderCatalogueMoveRequest;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderCatalogueUpdateRequest;
import com.linkwechat.domain.substitute.customer.order.vo.WeSubstituteCustomerOrderCatalogueVO;
import com.linkwechat.service.IWeSubstituteCustomerOrderCatalogueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 代客下单字段分类 前端控制器
 * </p>
 *
 * @author WangYX
 * @since 2023-08-02
 */
@Api(tags = "代客下单字段分类")
@RestController
@RequestMapping("/substitute/customer/order/catalogue")
public class WeSubstituteCustomerOrderCatalogueController extends BaseController {

    @Resource
    private IWeSubstituteCustomerOrderCatalogueService weSubstituteCustomerOrderCatalogueService;

    /**
     * 列表
     *
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/08/02 17:03
     */
    @ApiOperation("列表")
    @GetMapping("")
    public AjaxResult list() {
        LambdaQueryWrapper<WeSubstituteCustomerOrderCatalogue> queryWrapper = Wrappers.lambdaQuery(WeSubstituteCustomerOrderCatalogue.class);
        queryWrapper.eq(WeSubstituteCustomerOrderCatalogue::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.orderByAsc(WeSubstituteCustomerOrderCatalogue::getSort);
        List<WeSubstituteCustomerOrderCatalogue> list = weSubstituteCustomerOrderCatalogueService.list(queryWrapper);
        return AjaxResult.success(BeanUtil.copyToList(list, WeSubstituteCustomerOrderCatalogueVO.class));
    }

    /**
     * 新增
     *
     * @param request 新增请求参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/08/02 17:19
     */
    @ApiOperation("新增")
    @PostMapping("")
    public AjaxResult add(@Validated @RequestBody WeSubstituteCustomerOrderCatalogueAddRequest request) {
        //检查分类名称是否重复
        LambdaQueryWrapper<WeSubstituteCustomerOrderCatalogue> queryWrapper = Wrappers.lambdaQuery(WeSubstituteCustomerOrderCatalogue.class);
        queryWrapper.eq(WeSubstituteCustomerOrderCatalogue::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.eq(WeSubstituteCustomerOrderCatalogue::getName, request.getName());
        WeSubstituteCustomerOrderCatalogue one = weSubstituteCustomerOrderCatalogueService.getOne(queryWrapper);
        if (BeanUtil.isNotEmpty(one)) {
            throw new ServiceException("分类名称不能重复！");
        }
        //获取数量
        queryWrapper.clear();
        queryWrapper.eq(WeSubstituteCustomerOrderCatalogue::getDelFlag, Constants.COMMON_STATE);
        int count = weSubstituteCustomerOrderCatalogueService.count(queryWrapper);

        //新增
        WeSubstituteCustomerOrderCatalogue catalogue = new WeSubstituteCustomerOrderCatalogue();
        catalogue.setId(IdUtil.getSnowflakeNextId());
        catalogue.setName(request.getName());
        catalogue.setSort(count);
        catalogue.setFixed(0);
        catalogue.setDelFlag(Constants.COMMON_STATE);
        weSubstituteCustomerOrderCatalogueService.save(catalogue);
        return AjaxResult.success(catalogue.getId());
    }

    /**
     * 修改
     *
     * @param request 修改请求参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/08/02 17:19
     */
    @ApiOperation("修改")
    @PutMapping("")
    public AjaxResult update(@Validated @RequestBody WeSubstituteCustomerOrderCatalogueUpdateRequest request) {
        WeSubstituteCustomerOrderCatalogue weSubstituteCustomerOrderCatalogue = weSubstituteCustomerOrderCatalogueService.getById(request.getId());
        if (BeanUtil.isEmpty(weSubstituteCustomerOrderCatalogue)) {
            return AjaxResult.success();
        }
        //检查分类名称是否重复
        LambdaQueryWrapper<WeSubstituteCustomerOrderCatalogue> queryWrapper = Wrappers.lambdaQuery(WeSubstituteCustomerOrderCatalogue.class);
        queryWrapper.eq(WeSubstituteCustomerOrderCatalogue::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.eq(WeSubstituteCustomerOrderCatalogue::getName, request.getName());
        queryWrapper.ne(WeSubstituteCustomerOrderCatalogue::getId, request.getId());
        WeSubstituteCustomerOrderCatalogue one = weSubstituteCustomerOrderCatalogueService.getOne(queryWrapper);
        if (BeanUtil.isNotEmpty(one)) {
            throw new ServiceException("分类名称不能重复！");
        }

        //修改
        LambdaUpdateWrapper<WeSubstituteCustomerOrderCatalogue> updateWrapper = Wrappers.lambdaUpdate(WeSubstituteCustomerOrderCatalogue.class);
        updateWrapper.eq(WeSubstituteCustomerOrderCatalogue::getId, request.getId());
        updateWrapper.set(StrUtil.isNotBlank(request.getName()), WeSubstituteCustomerOrderCatalogue::getName, request.getName());
        weSubstituteCustomerOrderCatalogueService.update(updateWrapper);
        return AjaxResult.success();
    }

    /**
     * 删除
     *
     * @param id 主键Id
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/08/02 17:20
     */
    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        weSubstituteCustomerOrderCatalogueService.delete(id);
        return AjaxResult.success();
    }

    /**
     * 移动
     *
     * @param request 移动请求参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/08/02 18:14
     */
    @ApiOperation("移动")
    @PutMapping("/move")
    public AjaxResult move(@Validated @RequestBody WeSubstituteCustomerOrderCatalogueMoveRequest request) {
        weSubstituteCustomerOrderCatalogueService.move(request);
        return AjaxResult.success();
    }


}

