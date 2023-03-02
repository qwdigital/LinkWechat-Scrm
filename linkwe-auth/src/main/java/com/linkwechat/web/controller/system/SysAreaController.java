package com.linkwechat.web.controller.system;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.vo.SysAreaVo;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.web.domain.SysArea;
import com.linkwechat.web.service.ISysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 行政区划信息
 *
 * @author danmo
 */
@RestController
@RequestMapping("/system/area")
public class SysAreaController extends BaseController {

    @Autowired
    private ISysAreaService sysAreaService;


    @Autowired
    private RedisService redisService;

    @GetMapping("/list")
    public AjaxResult list(SysArea sysArea) {

        List<Tree<Integer>> areaList = redisService.getCacheList("area");

        if(CollectionUtil.isNotEmpty(areaList)){
            return AjaxResult.success(areaList);
        }

        List<SysArea> list = sysAreaService.getList(sysArea);
        if (CollectionUtil.isEmpty(list)) {
            throw new WeComException("暂无数据");
        }


        List<TreeNode<Integer>> treeNodes = list.stream().map(area -> {
            return new TreeNode<>(area.getId(), area.getParentId(), area.getExtName(), area.getExtId());
        }).collect(Collectors.toList());
        List<Tree<Integer>> treeList = TreeUtil.build(treeNodes, 0);
        if(CollectionUtil.isNotEmpty(treeList)){
            redisService.setCacheList("area",treeList);
        }

        return AjaxResult.success(treeList);
    }

    @GetMapping("/getChildListById")
    public AjaxResult<SysAreaVo> getChildListById(@NotNull(message = "ID不允许为空") Integer id) {
        List<SysAreaVo> list = sysAreaService.getChildListById(id);
        return AjaxResult.success(list);
    }

    /**
     * 根据Id获取数据
     *
     * @param id
     * @return {@link AjaxResult< SysAreaVo>}
     * @author WangYX
     * @date 2022/10/17 14:40
     */
    @GetMapping("/getAreaById/{id}")
    public AjaxResult<SysAreaVo> getAreaById(@PathVariable("id") Integer id) {
        SysArea sysArea = sysAreaService.getById(id);
        return AjaxResult.success(sysArea);
    }

}
