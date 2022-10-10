package com.linkwechat.common.constant;


import com.linkwechat.common.utils.StringUtils;

/**
 * 权限通用常量
 *
 * @author danmo
 */
public class AuthorityConstants {

    /** 公共企业Id */
    public static final Long COMMON_ENTERPRISE = 0L;

    /** 公共企业Id */
    public static final Long COMMON_USER = 0L;

    /** 控制参数 */
    public static final Long DELETE_PARAM = 0L;

    /** 模块&菜单树 - 顶级节点Id */
    public static final Long SYSTEM_TOP_NODE = -1L;

    /** 菜单树 - 顶级节点Id */
    public static final Long MENU_TOP_NODE = 0L;

    /** 用户类型 */
    public enum UserType {

        ORDINARY("01", "普通用户"), ADMIN("00", "超管用户");

        private final String code;
        private final String info;

        UserType(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }

    /** 租户类型 */
    public enum TenantType {

        ORDINARY("N", "普通租户"), ADMIN("Y", "租管租户");

        private final String code;
        private final String info;

        TenantType(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }

    /** 角色类型 */
    public enum RoleType {

        NORMAL("0", "常规"),
        DERIVE_TENANT("1", "租户衍生"),
        DERIVE_ENTERPRISE("2", "企业衍生"),
        DERIVE_DEPT("3", "部门衍生"),
        DERIVE_POST("4", "岗位衍生"),
        DERIVE_USER("5", "用户衍生");

        private final String code;
        private final String info;

        RoleType(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

        public static RoleType getValue(String code){
            for(RoleType roleType : values()){
                if(StringUtils.equals(code, roleType.getCode())){
                    return roleType;
                }
            }
            return null;
        }
    }

    /** 公共数据 */
    public enum IsCommon {

        YES("Y", "是"), NO("N", "否");

        private final String code;
        private final String info;

        IsCommon(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }

    /** 数据范围 */
    public enum DataScope {

        ALL("1","全部数据权限"),
        CUSTOM("2","自定义数据权限"),
        DEPT("3","本部门数据权限"),
        DEPT_AND_CHILD("4","本部门及以下数据权限"),
        POST("5","本岗位数据权限"),
        SELF("6","仅本人数据权限");

        private final String code;
        private final String info;

        DataScope(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

        public static DataScope getValue(String code){
            for(DataScope scope : values()){
                if(StringUtils.equals(code, scope.getCode())){
                    return scope;
                }
            }
            return null;
        }
    }

    /** 菜单状态 */
    public enum Visible {

        YES("Y", "显示"), NO("N", "隐藏");

        private final String code;
        private final String info;

        Visible(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }

    /** 是否缓存 */
    public enum Cache {

        YES("Y", "是"), NO("N", "否");

        private final String code;
        private final String info;

        Cache(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }

    /** 是否菜单外链 */
    public enum Frame {

        YES("Y", "是"), NO("N", "否");

        private final String code;
        private final String info;

        Frame(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }

    /** 菜单类型 */
    public enum MenuType {

        DIR("M", "目录"), MENU("C", "菜单"), BUTTON("F", "按钮");

        private final String code;
        private final String info;

        MenuType(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }

    /** 组件标识 */
    public enum ComponentType {

        LAYOUT("Layout", "Layout"), PARENT_VIEW("ParentView", "ParentView"), INNER_LINK("InnerLink", "InnerLink");

        private final String code;
        private final String info;

        ComponentType(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }
}