package com.linkwechat.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
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
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.common.utils.poi.ExcelUtil;
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
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
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
        return AjaxResult.success(materialService.addOrUpdate(material));
    }


    @Log(title = "更新素材信息", businessType = BusinessType.UPDATE)
    @PutMapping("/material")
    @ApiOperation("更新素材信息")
    public AjaxResult edit(@RequestBody WeMaterial material) {
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
    public AjaxResult insert(@RequestBody WePoster poster) {
        WeMaterial material = materialService.generateSimpleImg(poster);
        material.setMediaType(MediaType.POSTER.getType());
        material.setModuleType(poster.getModuleType());
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
    private AjaxResult<FileEntity> createPoster(@RequestBody PurePoster purePoster) {
        FileEntity poster = materialService.createPoster(purePoster);
        return AjaxResult.success(poster);
    }


    @PutMapping(value = "/poster/update")
    @ApiOperation("修改海报")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult update(@RequestBody WePoster poster) {
        if (poster.getId() == null) {
            return AjaxResult.error("id为空");
        }
        poster.setMediaType(null);
        WeMaterial material = materialService.generateSimpleImg(poster);
        materialService.saveOrUpdate(material);
        return AjaxResult.success(material);
    }


    @GetMapping(value = "/poster/entity/{id}")
    @ApiOperation("查询海报详情")
    public AjaxResult entity(@PathVariable Long id) {
        WeMaterial material = materialService.getById(id);
        WePosterVo vo = BeanUtil.copyProperties(material, WePosterVo.class);
        vo.setTitle(material.getMaterialName());
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

    @GetMapping("/material/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<WeMaterial> util = new ExcelUtil<WeMaterial>(WeMaterial.class);
        return util.importTemplateExcel("话术语模板");
    }

    @PostMapping("/material/importData")
    public AjaxResult importData(WeMaterial weMaterial, MultipartFile file) throws Exception {
        ExcelUtil<WeMaterial> util = new ExcelUtil<WeMaterial>(WeMaterial.class);
        List<WeMaterial> weMaterialList = util.importExcel(file.getInputStream());
        String tip = new String("成功导入{0}条，去重复{1}条");
        if (ObjectUtil.isEmpty(weMaterialList)) {
            return AjaxResult.error("导入数据不能为空！");
        }
        List<WeMaterial> list = weMaterialList.stream().map(item -> {
            String materialName = item.getMaterialName();
            String content = item.getContent();
            if (ObjectUtil.isNotEmpty(materialName) && ObjectUtil.isNotEmpty(content) && materialName.length() <= 50 && content.length() <= 2000) {
                item.setMediaType(weMaterial.getMediaType());
                item.setCategoryId(weMaterial.getCategoryId());
                return item;
            }
            return null;
        }).collect(Collectors.toList());
        if (ObjectUtil.isEmpty(list)) {
            return AjaxResult.error("导入数据不满足要求！");
        }
        materialService.saveBatch(list);
        tip = MessageFormat.format(tip, new Object[]{new Integer(list.size()).toString(),
                new Integer(weMaterialList.size() - list.size()).toString()});
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
        List<ContentDataDetailVo> weMaterialDataCount = materialService.getWeMaterialDataCount(contentDetailQuery);
        startPage();
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

        //素材类型：需要参考 CategoryMediaType类中的定义
        //由于枚举类定义了很多不属于素材中的类型，所以需要把用到的素材类型挑选出来
        List<MaterialMediaTypeVO> result = new ArrayList<>();
        result.add(MaterialMediaTypeVO.builder().type(4).name("文本").sort(1).build());
        result.add(MaterialMediaTypeVO.builder().type(0).name("图片").sort(2).build());
        result.add(MaterialMediaTypeVO.builder().type(9).name("图文").sort(3).build());
        result.add(MaterialMediaTypeVO.builder().type(11).name("小程序").sort(4).build());
        result.add(MaterialMediaTypeVO.builder().type(12).name("文章").sort(5).build());
        result.add(MaterialMediaTypeVO.builder().type(2).name("视频").sort(6).build());
        result.add(MaterialMediaTypeVO.builder().type(3).name("文件").sort(7).build());
        result.add(MaterialMediaTypeVO.builder().type(5).name("海报").sort(8).build());
        result = result.stream().sorted(Comparator.comparing(MaterialMediaTypeVO::getSort)).collect(Collectors.toList());
        return AjaxResult.success(result);
    }


}
