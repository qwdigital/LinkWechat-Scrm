package com.linkwechat.handler;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.SheetWriteHandlerContext;
import com.linkwechat.common.enums.leads.template.DataAttrEnum;
import com.linkwechat.common.enums.leads.template.DatetimeTypeEnum;
import com.linkwechat.domain.leads.template.vo.WeLeadsTemplateSettingsVO;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;

/**
 * 线索中心-线索导出模板-设置数据的有效性校验
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/13 17:04
 */
public class DataValidityWriteHandler implements SheetWriteHandler {

    /**
     * 头数据
     */
    private final List<WeLeadsTemplateSettingsVO> settings;

    public DataValidityWriteHandler(List<WeLeadsTemplateSettingsVO> settings) {
        this.settings = settings;
    }

    @Override
    public void afterSheetCreate(SheetWriteHandlerContext context) {
        for (int i = 0; i < settings.size(); i++) {
            CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, 10000, i, i);
            DataValidationHelper helper = context.getWriteSheetHolder().getSheet().getDataValidationHelper();
            DataValidationConstraint dataValidationConstraint = null;
            String errorMsg = null;
            WeLeadsTemplateSettingsVO vo = settings.get(i);
            if (vo.getTableEntryAttr().equals(DataAttrEnum.TEXT.getCode())) {
                if (vo.getDataAttr().equals(0)) {
                    //文本
                    Integer required = vo.getRequired();
                    String formula1 = required.equals(0) ? "0" : "1";
                    String formula2 = vo.getMaxInputLen().toString();
                    dataValidationConstraint = helper.createTextLengthConstraint(DataValidationConstraint.OperatorType.BETWEEN, formula1, formula2);
                    errorMsg = "字符长度不可超过" + formula2;
                } else if (vo.getDataAttr().equals(DataAttrEnum.NUMBER.getCode())) {
                    //数字
                    dataValidationConstraint = helper.createNumericConstraint(DataValidationConstraint.ValidationType.INTEGER, DataValidationConstraint.OperatorType.BETWEEN, String.valueOf(Integer.MIN_VALUE), String.valueOf(Integer.MAX_VALUE));
                    errorMsg = "请填写一个数字";
                } else if (vo.getDataAttr().equals(DataAttrEnum.DATE.getCode())) {
                    //日期
                    DatetimeTypeEnum of = DatetimeTypeEnum.of(vo.getDatetimeType());
                    dataValidationConstraint = helper.createDateConstraint(DataValidationConstraint.OperatorType.GREATER_THAN,"Date(1970,1,1)",null,of.getFormat());
                    errorMsg = "请填写日期格式数据";
                }
            }
            if (BeanUtil.isNotEmpty(dataValidationConstraint)) {
                //对单元格数据进行验证
                DataValidation dataValidation = helper.createValidation(dataValidationConstraint, cellRangeAddressList);
                //创建提示框 这里创建的是错误提示框
                dataValidation.createErrorBox("类型有误", errorMsg);
                dataValidation.setShowErrorBox(true);
                context.getWriteSheetHolder().getSheet().addValidationData(dataValidation);
            }
        }
    }
}
