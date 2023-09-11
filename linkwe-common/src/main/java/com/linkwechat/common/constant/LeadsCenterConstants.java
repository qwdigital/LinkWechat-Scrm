package com.linkwechat.common.constant;

/**
 * @author zhaoyijie
 * @since 2023/4/12 16:23
 */
public class LeadsCenterConstants {

    private LeadsCenterConstants() {
        throw new IllegalStateException("Construct LeadsCenterConstants");
    }

    public final static String NAME = "姓名";

    public final static String PHONE = "电话号码";

    public final static String SEX = "性别";

    public final static String LINK = "链接";

    /**
     * excel文件扩展名
     */
    public final static String XLSX_FILE_EXTENSION = ".xlsx";

    /**
     * excel文件响应头
     */
    public final static String XLSX_FILE_HEAD = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /**
     * 线索
     */
    public static final String LEADS = "leads:";
}
