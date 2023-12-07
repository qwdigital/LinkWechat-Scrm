package com.linkwechat.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.enums.CategoryMediaType;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.material.ao.PurePoster;
import com.linkwechat.domain.material.ao.WePoster;
import com.linkwechat.domain.material.ao.WePosterFontAO;
import com.linkwechat.domain.material.dto.ResetCategoryDto;
import com.linkwechat.domain.material.dto.TemporaryMaterialDto;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.query.ContentDetailQuery;
import com.linkwechat.domain.material.query.LinkMediaQuery;
import com.linkwechat.domain.material.vo.*;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.service.IWeMaterialService;
import com.linkwechat.service.IWeTagService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 素材管理/海报管理/海报字体管理
 *
 * @author leejoker
 * @date 2022/3/29 22:50
 */
@RestController
public class WeMaterialController extends BaseController {
    @Resource
    private IWeMaterialService materialService;

    @Autowired
    private IWeTagService iWeTagService;

    @GetMapping("/material/list")
    @ApiOperation("查询素材列表")
    public TableDataInfo list(LinkMediaQuery query) {
        startPage();
        List<WeMaterialNewVo> weMaterialNewVos = materialService.selectListByLkQuery(query);
        return getDataTable(weMaterialNewVos);
    }


    @GetMapping("/material/{id}")
    @ApiOperation("查询素材详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(materialService.findWeMaterialById(id));
    }


    @Log(title = "添加素材信息", businessType = BusinessType.INSERT)
    @PostMapping("/material")
    @ApiOperation("添加素材信息")
    public AjaxResult add(@RequestBody WeMaterial material) {
        Integer moduleType = material.getModuleType();
        if (moduleType.equals(1) && material.getCategoryId() == null) {
            throw new CustomException("请先选择素材分组！");
        }
        return AjaxResult.success(materialService.addOrUpdate(material));
    }


    @Log(title = "更新素材信息", businessType = BusinessType.UPDATE)
    @PutMapping("/material")
    @ApiOperation("更新素材信息")
    public AjaxResult edit(@RequestBody WeMaterial material) {
        Integer moduleType = material.getModuleType();
        if (moduleType.equals(1) && material.getCategoryId() == null) {
            throw new CustomException("请先选择素材分组！");
        }
        materialService.addOrUpdate(material);
        return AjaxResult.success();
    }


    @Log(title = "删除素材信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/material/{ids}")
    @ApiOperation("删除素材信息")
    public AjaxResult remove(@PathVariable Long[] ids) {
        materialService.deleteWeMaterialByIds(ids);
        return AjaxResult.success();
    }


    @Log(title = "上传素材信息", businessType = BusinessType.OTHER)
    @PostMapping("/material/upload")
    @ApiOperation("上传素材信息")
    public AjaxResult upload(@RequestParam(value = "file") MultipartFile file,
                             @RequestParam(value = "mediaType") String mediaType) {
        WeMaterialFileVo weMaterialFileVo = materialService.uploadWeMaterialFile(file, mediaType);
        return AjaxResult.success(weMaterialFileVo);
    }


    @Log(title = "更换分组", businessType = BusinessType.OTHER)
    @PutMapping("/material/resetCategory")
    @ApiOperation("更换分组")
    public AjaxResult resetCategory(@RequestBody ResetCategoryDto resetCategoryDto) {
        materialService.resetCategory(resetCategoryDto.getCategoryId(), resetCategoryDto.getMaterials());
        return AjaxResult.success();
    }

    @Log(title = "获取素材media_id", businessType = BusinessType.OTHER)
    @GetMapping("/material/temporaryMaterialMediaId")
    @ApiOperation("H5端发送获取素材media_id")
    public AjaxResult temporaryMaterialMediaId(String url, String type, String name) {

        if (StringUtils.isEmpty(name)) {
            name = SnowFlakeUtil.nextId().toString();
        }

        WeMediaVo weMediaDto = materialService.uploadTemporaryMaterial(url, type,
                name + "." + url.substring(url.lastIndexOf(".") + 1, url.length()));
        return AjaxResult.success(weMediaDto);
    }


    @Log(title = "获取素材media_id", businessType = BusinessType.OTHER)
    @PostMapping("/material/temporaryMaterialMediaIdForWeb")
    @ApiOperation("web端发送获取素材media_id")
    public AjaxResult temporaryMaterialMediaIdForWeb(@RequestBody TemporaryMaterialDto temporaryMaterialDto) {
        WeMediaVo weMediaDto = materialService.uploadTemporaryMaterial(temporaryMaterialDto.getUrl(),
                MediaType.of(temporaryMaterialDto.getType()).get().getMediaType(), temporaryMaterialDto.getName());
        return AjaxResult.success(weMediaDto);
    }


    @Log(title = "上传素材图片", businessType = BusinessType.OTHER)
    @PostMapping("/material/uploadimg")
    @ApiOperation("上传素材图片")
    public AjaxResult<WeMediaVo> uploadImg(MultipartFile file) {
        WeMediaVo weMediaVo = materialService.uploadImg(file);
        return AjaxResult.success(weMediaVo);
    }

    @PostMapping(value = "/poster/insert")
    @ApiOperation("创建海报")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult insert(@RequestBody WePoster poster) throws Exception {
        String materialName = poster.getMaterialName();
        if (materialName != null && materialName.length() > 60) {
            throw new CustomException("海报标题不可超过60个字符！");
        }
        String digest = poster.getDigest();
        if (digest != null && digest.length() > 100) {
            throw new CustomException("海报描述不可超过100个字符！");
        }
        WeMaterial material = materialService.builderSimpleImg(poster);
        material.setMediaType(MediaType.POSTER.getType());
        material.setModuleType(poster.getModuleType());
        material.setTagIds(poster.getTagIds());
        boolean b = materialService.saveOrUpdate(material);
        WeMaterialVo weMaterialVo = new WeMaterialVo();
        BeanUtils.copyProperties(material, weMaterialVo);
        return AjaxResult.success(weMaterialVo);
    }

    /**
     * 纯创建海报，数据未写入到素材中心
     *
     * @param purePoster
     * @return
     */
    @PostMapping(value = "/pure/poster/insert")
    @ApiOperation("纯创建海报")
    private AjaxResult<FileEntity> createPoster(@RequestBody PurePoster purePoster) throws Exception {
        FileEntity poster = materialService.builderPoster(purePoster);
        return AjaxResult.success(poster);
    }


    @PutMapping(value = "/poster/update")
    @ApiOperation("修改海报")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult update(@RequestBody WePoster poster) throws Exception {
        if (poster.getId() == null) {
            return AjaxResult.error("id为空");
        }
        String materialName = poster.getMaterialName();
        if (materialName != null && materialName.length() > 60) {
            throw new CustomException("海报标题不可超过60个字符！");
        }
        String digest = poster.getDigest();
        if (digest != null && digest.length() > 100) {
            throw new CustomException("海报描述不可超过100个字符！");
        }
        poster.setMediaType(null);
        WeMaterial material = materialService.builderSimpleImg(poster);
        material.setTagIds(poster.getTagIds());
        materialService.saveOrUpdate(material);
        return AjaxResult.success(material);
    }


    @GetMapping(value = "/poster/entity/{id}")
    @ApiOperation("查询海报详情")
    public AjaxResult entity(@PathVariable Long id) {
        WeMaterial material = materialService.getById(id);
        WePosterVo vo = BeanUtil.copyProperties(material, WePosterVo.class);
        if (StringUtils.isNotBlank(material.getMaterialName())) {
            vo.setTitle(material.getMaterialName());
        }
        if(StringUtils.isNotEmpty(vo.getTagIds())){
            List<WeTag> weTags = iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                    .in(WeTag::getTagId, ListUtil.toList(vo.getTagIds().split(","))));
            if(CollectionUtil.isNotEmpty(weTags)){
                vo.setTagNames(
                        weTags.stream().map(WeTag::getName).collect(Collectors.joining(","))
                );
            }

        }
        vo.setSampleImgPath(material.getMaterialUrl());
        vo.setBackgroundImgPath(material.getBackgroundImgUrl());
        return AjaxResult.success(vo);
    }

    @GetMapping(value = "/poster/page")
    @ApiOperation("分页查询海报")
    @DataScope(type = "2", value = @DataColumn(alias = "we_material", name = "create_by_id", userid = "user_id"))
    public AjaxResult page(Long categoryId, String name) {
        startPage();
        List<WeMaterial> materials = materialService.lambdaQuery()
                .eq(WeMaterial::getMediaType, MediaType.POSTER.getType()).eq(WeMaterial::getDelFlag, 0)
                .eq(categoryId != null, WeMaterial::getCategoryId, categoryId)
                .like(com.linkwechat.common.utils.StringUtils.isNotBlank(name), WeMaterial::getMaterialName, name)
                .orderByDesc(WeMaterial::getCreateTime).list();
        List<WePosterVo> posterList = materials.stream().map(m -> {
            WePosterVo vo = BeanUtil.copyProperties(m, WePosterVo.class);
            vo.setTitle(m.getMaterialName());
            vo.setSampleImgPath(m.getMaterialUrl());
            vo.setBackgroundImgPath(m.getBackgroundImgUrl());
            return vo;
        }).collect(Collectors.toList());
        return AjaxResult.success(getDataTable(posterList));
    }

    @DeleteMapping(value = "/poster/delete/{id}")
    @ApiOperation(value = "删除海报")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult deletePoster(@PathVariable Long id) {
        materialService.update(
                Wrappers.lambdaUpdate(WeMaterial.class).set(WeMaterial::getDelFlag, 1).eq(WeMaterial::getId, id));
        return AjaxResult.success("删除成功");
    }

    @PostMapping(value = "/posterFont")
    @ApiOperation("创建海报字体")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult insertPosterFont(@RequestBody WePosterFontAO posterFont) {
        posterFont.setMediaType(MediaType.POSTER_FONT.getType());
        WeMaterial material = BeanUtil.copyProperties(posterFont, WeMaterial.class);
        material.setMaterialName(posterFont.getFontName());
        material.setMaterialUrl(posterFont.getFontUrl());
        materialService.save(material);
        return AjaxResult.success("创建成功");
    }


    @PutMapping(value = "/posterFont")
    @ApiOperation("修改海报字体")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult updatePosterFont(@RequestBody WePosterFontAO posterFont) {
        if (posterFont.getId() == null) {
            return AjaxResult.error("id为空");
        }
        posterFont.setMediaType(null);
        WeMaterial material = BeanUtil.copyProperties(posterFont, WeMaterial.class);
        material.setMaterialName(posterFont.getFontName());
        material.setMaterialUrl(posterFont.getFontUrl());
        materialService.saveOrUpdate(material);
        return AjaxResult.success("修改成功");
    }

    @GetMapping(value = "/posterFont/posterFontList")
    @ApiOperation("列表查询海报字体")
    public AjaxResult selectPosterFontList(BaseEntity query) {
        return AjaxResult.success(materialService.getFontList(query));
    }

    @GetMapping(value = "/posterFont/posterFontPage")
    @ApiOperation("分页查询海报字体")
    public AjaxResult selectPosterFontPage(BaseEntity query) {
        startPage();
        return AjaxResult.success(getDataTable(materialService.getFontList(query)));
    }

    @DeleteMapping(value = "/posterFont/{id}")
    @ApiOperation("删除海报字体")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult deletePosterFont(@PathVariable Long id) {
        materialService.update(
                Wrappers.lambdaUpdate(WeMaterial.class).set(WeMaterial::getDelFlag, 1).eq(WeMaterial::getId, id));
        return AjaxResult.success("删除成功");
    }

    @ApiOperation("文本素材导出模板")
    @GetMapping("/material/importTemplate")
    public void importTemplate() throws IOException {
        materialService.importTemplate();
    }

    /**
     * 文本素材数据按模板导入
     *
     * @param weMaterial
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/material/importData")
    public AjaxResult importData(WeMaterial weMaterial, MultipartFile file) throws Exception {
        String tip = materialService.importData(file);
        return AjaxResult.success(tip);
    }


    // 统计 数据分析 共用 数据分析时 不需要传ContentId
    @ApiOperation(value = "素材详情数据统计", httpMethod = "GET")
    @Log(title = "素材详情数据统计", businessType = BusinessType.SELECT)
    @GetMapping("/material/count")
    public AjaxResult<WeContentCountVo> getWeMaterialCount(ContentDetailQuery contentDetailQuery) {
        WeContentCountVo WeContentCountVo = materialService.getWeMaterialCount(contentDetailQuery);
        return AjaxResult.success(WeContentCountVo);
    }


    @ApiOperation(value = "素材详情数据明细", httpMethod = "GET")
    @Log(title = "素材详情数据明细", businessType = BusinessType.SELECT)
    @GetMapping("/material/dataDetail")
    public TableDataInfo getWeMaterialDataCount(ContentDetailQuery contentDetailQuery) {
        startPage();
        List<ContentDataDetailVo> weMaterialDataCount = materialService.getWeMaterialDataCount(contentDetailQuery);
        return getDataTable(weMaterialDataCount);
    }


    @ApiOperation(value = "素材数据分析Top5", httpMethod = "GET")
    @Log(title = "素材数据分析Top5", businessType = BusinessType.SELECT)
    @GetMapping("/material/analyseTop")
    public AjaxResult getWeMaterialAnalyseTop(ContentDetailQuery contentDetailQuery) {
        List<WeMaterialAnalyseVo> list = materialService.getWeMaterialAnalyseTop(contentDetailQuery);
        return AjaxResult.success(list);
    }

    /**
     * 获取素材类型
     *
     * @param
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2022/10/21 14:24
     */
    @ApiOperation(value = "获取素材类型", httpMethod = "GET")
    @GetMapping("/material/media/type")
    public AjaxResult getMaterialMediaType() {

//        //素材类型：需要参考 CategoryMediaType类中的定义
//        //由于枚举类定义了很多不属于素材中的类型，所以需要把用到的素材类型挑选出来
//        List<MaterialMediaTypeVO> result = new ArrayList<>();
//        result.add(MaterialMediaTypeVO.builder().type(4).name("文本").sort(1).build());
//        result.add(MaterialMediaTypeVO.builder().type(0).name("图片").sort(2).build());
//        result.add(MaterialMediaTypeVO.builder().type(9).name("图文").sort(3).build());
//        result.add(MaterialMediaTypeVO.builder().type(11).name("小程序").sort(4).build());
//        result.add(MaterialMediaTypeVO.builder().type(12).name("文章").sort(5).build());
//        result.add(MaterialMediaTypeVO.builder().type(2).name("视频").sort(6).build());
//        result.add(MaterialMediaTypeVO.builder().type(3).name("文件").sort(7).build());
//        result.add(MaterialMediaTypeVO.builder().type(5).name("海报").sort(8).build());
//        result.add(MaterialMediaTypeVO.builder().type(18).name("智能表单").sort(9).build());
//        result = result.stream().sorted(Comparator.comparing(MaterialMediaTypeVO::getSort)).collect(Collectors.toList());
        return AjaxResult.success(
                CategoryMediaType.sideMaterialType()
        );
    }


}
