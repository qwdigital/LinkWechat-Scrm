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

    public final static String AGE = "年龄";

    public final static String AGE_EN = "age";

    public final static String SYMPTOM = "症状";

    public final static String SEX = "性别";

    public final static String NOTES = "备注信息";

    public final static String KINSHIP = "亲属关系";

    public final static String KINSHIP_EN = "kinship";

    public final static String BELONG_CONSULTANT = "所属咨询师";

    public final static String BELONG_CONSULTANT_EN = "belongConsultant";

    @Deprecated
    public final static String SECONDARY_RELATION = "次级关系";

    public final static String LEAVE_PHONE_TIME = "留电时间";

    public final static String LINK = "链接";

    public final static String PRIMARY_CHANNEL = "一级渠道";

    public final static String SECONDARY_CHANNEL = "二级渠道";

    public final static String THREE_LEVEL_CHANNEL = "三级渠道";

    public final static String CONSULT = "咨询项目";

    public final static String CONSULT_EN = "consult";

    public final static String SUBJECT = "主题";

    public final static String USER_JOB_NAME = "引流员工姓名";

    public final static String USER_JOB_NUMBER = "引流员工工号";

    public final static String NEARBY_AREA = "就近院区";

    public final static String LABEL = "线索标签";

    /**
     * L1线索待分配的编码
     */
    public final static String DL1Code = "L1";

    /**
     * L2线索已分配的编码
     */
    public final static String DL2Code = "L2";

    /**
     * L6已上门编码
     */
    public final static String DL6Code = "L6";

    /**
     * 未接/关机/挂断（下发日起三天后仍然为接，归无效）的编码
     */
    public final static String DL46Code = "L46";

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
