package com.linkwechat.domain.leads.sea.query;

import lombok.Data;

import java.util.List;

/**
 * 公海可见范围
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/03 14:42
 */
@Data
public class VisibleRange {

    public static final int dept = 0;
    public static final int post = 1;
    public static final int user = 2;

    @Data
    public static class DeptRange {
        /**
         * 是否被选中
         */
        private boolean isSelect = false;
        /**
         * 部门Id集合
         */
        private List<Long> deptIds;
        /**
         * 部门名称集合
         */
        private List<String> deptNames;
    }

    @Data
    public static class PostRange {
        /**
         * 是否被选中
         */
        private boolean isSelect = false;
        /**
         * 部门集合
         */
        private List<String> posts;
    }

    @Data
    public static class UserRange {
        /**
         * 是否被选中
         */
        private boolean isSelect = false;
        /**
         * 企微Id集合
         */
        private List<String> weUserIds;
        /**
         * 员工名称集合
         */
        private List<String> userNames;
    }

}
