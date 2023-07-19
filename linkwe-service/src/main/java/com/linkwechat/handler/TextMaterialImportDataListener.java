package com.linkwechat.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.vo.TextMaterialExportVo;
import com.linkwechat.service.IWeMaterialService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本素材导入监听器
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/12/16 14:27
 */
@Slf4j
public class TextMaterialImportDataListener implements ReadListener<TextMaterialExportVo> {


    private IWeMaterialService weMaterialService;

    public TextMaterialImportDataListener(IWeMaterialService weMaterialService) {
        this.weMaterialService = weMaterialService;
    }


    /**
     * 每隔100条存储数据库，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    /**
     * 缓存的数据
     */
    private List<WeMaterial> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 失败导入数据的行号
     */
    public List<Integer> errorIndex = new ArrayList<>();
    /**
     * 成功导出的数量
     */
    public Integer amount = 0;

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(TextMaterialExportVo data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        String title = data.getTitle();
        String content = data.getContent();
        if (StrUtil.isBlank(title) || StrUtil.isBlank(content)) {
            errorIndex.add(context.readRowHolder().getRowIndex() + 1);
        } else {
            WeMaterial weMaterial = new WeMaterial();
            //默认分组
            weMaterial.setCategoryId(1L);
            weMaterial.setModuleType(1);
            weMaterial.setContent(content);
            weMaterial.setMaterialName(title);
            weMaterial.setMediaType("4");
            cachedDataList.add(weMaterial);
            amount = amount + 1;
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (cachedDataList.size() >= BATCH_COUNT) {
                saveData();
                // 存储完成清理 list
                cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            }
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        this.weMaterialService.saveBatch(cachedDataList);
        log.info("存储数据库成功！");
    }

}
