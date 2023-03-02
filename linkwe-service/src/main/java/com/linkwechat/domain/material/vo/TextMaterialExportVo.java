package com.linkwechat.domain.material.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文本素材导出导入
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/12/15 15:53
 */
@Data
@EqualsAndHashCode
@ColumnWidth(50)
public class TextMaterialExportVo {

    @ExcelProperty(value = {"填写说明：\n" +
            "1、带*标记为必填；\n" +
            "2、文本标题不超过50字；\n" +
            "3、文本内容不超过2000字；\n", "*文本标题"})
    private String title;

    @ExcelProperty(value = {"填写说明：\n" +
            "1、带*标记为必填；\n" +
            "2、文本标题不超过50字；\n" +
            "3、文本内容不超过2000字；\n", "*文本内容"})
    private String content;


}
