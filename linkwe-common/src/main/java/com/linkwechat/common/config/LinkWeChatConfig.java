package com.linkwechat.common.config;

import com.linkwechat.common.utils.OsUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author danmo
 */
@Component
@Data
@ConfigurationProperties(prefix = "linkwechat")
public class LinkWeChatConfig {
    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;

    /**
     * 是否可以修改密码
     */
    private boolean editPwd = true;

    /**
     * 是否是演示环境 true:是 false:不是
     */
    private boolean demoEnviron=false;
    /**
     * 上传路径
     */
    private static String profile;

    /**
     * 客户访问地址
     */
    private  String groupCodeUrl;


    /**
     * 客户公海访问地址
     */
    private String seasRedirectUrl;



    /**
     * 老客标签建群地址
     */
    private String tagRedirectUrl;


    /**
     * 客户sop h5跳转地址
     */
    private String customerSopRedirectUrl;


    /**
     * 客群sop h5跳转地址
     */
    private String groupSopRedirectUrl;



    /**
     * 流失客户地址
     */
    private String lostCustomerRedirectUrl;

    /**
     * 移动端域名
     */
    private String h5Domain;



    /**
     * JS SDK 身份校验url
     */
    private String authorizeUrl="https://open.weixin.qq.com/connect/oauth2/authorize";

    /**
     * 获取地址开关
     */
    private static boolean addressEnabled;

    private FileConfig file;

    /**
     * 匿名访问的URL
     */
    private String[] anonUrl;



    /**
     * 服务商id
     */
    private String serviceProviderId;

    /**
     * 扫码登陆授权url
     */
    private String wecomeLoginUrl;

    /**
     * 企业微信推广url
     */
    private String weExtensionRegisterUrl;

    /**
     * 推广企业微信模版id
     */
    private String weExtensionRegisterTempId;


    /**
     * 客服二维码
     */
    private String customerServiceQrUrl;

    /**
     * 第三方应用安装url
     */
    private String installThirdAppUrl;


    /**
     * 第三方应用的环境 true 正式授权， false 测试授权
     */
    private boolean installEnv=false;


    /**
     * 数据同步使劲啊间隔
     */
    private int dataSynchInterval=3;


    /**
     * 侧边栏
     */
    private WeSideBarConfig weSideBarConfig;

    /**
     * 百度地图开发者ak
     */
    private String baiduMapsAk;


    /**
     *  导购码
     */
    private String guideCodeUrl;

    /**
     * 导购群码
     */
    private String guideGroupUrl;




    private String taskFissionUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear) {
        this.copyrightYear = copyrightYear;
    }

    public static String getProfile() {
        if (OsUtils.isWindows()) {
            return "D:/linkWeChat/uploadPath";
        }else if(OsUtils.isMac()){
            return "/Users/robin/work/sr_project/lw/saas/file";
        }
        return "/usr/local/app/file";
    }

    public void setProfile(String profile) {
        LinkWeChatConfig.profile = profile;
    }

    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        LinkWeChatConfig.addressEnabled = addressEnabled;
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }

    /**
     * 获取会话存档素材下载路径
     */
    public static String getDownloadWeWorkPath() {
        return getProfile() + "/download/media_data/{}/{}";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getProfile() + "/upload";
    }

    public String[] getAnonUrl() {
        return anonUrl;
    }

    public void setAnonUrl(String[] anonUrl) {
        this.anonUrl = anonUrl;
    }

    public FileConfig getFile() {
        return file;
    }

    public void setFile(FileConfig file) {
        this.file = file;
    }

    public boolean isEditPwd() {
        return editPwd;
    }

    public void setEditPwd(boolean editPwd) {
        this.editPwd = editPwd;
    }


    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getWecomeLoginUrl() {
        return wecomeLoginUrl;
    }

    public void setWecomeLoginUrl(String wecomeLoginUrl) {
        this.wecomeLoginUrl = wecomeLoginUrl;
    }


    public String getWeExtensionRegisterUrl() {
        return weExtensionRegisterUrl;
    }

    public void setWeExtensionRegisterUrl(String weExtensionRegisterUrl) {
        this.weExtensionRegisterUrl = weExtensionRegisterUrl;
    }

    public String getWeExtensionRegisterTempId() {
        return weExtensionRegisterTempId;
    }

    public void setWeExtensionRegisterTempId(String weExtensionRegisterTempId) {
        this.weExtensionRegisterTempId = weExtensionRegisterTempId;
    }

    public String getCustomerServiceQrUrl() {
        return customerServiceQrUrl;
    }

    public void setCustomerServiceQrUrl(String customerServiceQrUrl) {
        this.customerServiceQrUrl = customerServiceQrUrl;
    }

    public String getInstallThirdAppUrl() {
        return installThirdAppUrl;
    }

    public void setInstallThirdAppUrl(String installThirdAppUrl) {
        this.installThirdAppUrl = installThirdAppUrl;
    }

    public String getGroupCodeUrl() {
        return groupCodeUrl;
    }

    public void setGroupCodeUrl(String groupCodeUrl) {
        this.groupCodeUrl = groupCodeUrl;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public void setAuthorizeUrl(String authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }

    public String getSeasRedirectUrl() {
        return seasRedirectUrl;
    }

    public void setSeasRedirectUrl(String seasRedirectUrl) {
        this.seasRedirectUrl = seasRedirectUrl;
    }

    public WeSideBarConfig getWeSideBarConfig() {
        return weSideBarConfig;
    }

    public void setWeSideBarConfig(WeSideBarConfig weSideBarConfig) {
        this.weSideBarConfig = weSideBarConfig;
    }

    public boolean isInstallEnv() {
        return installEnv;
    }

    public void setInstallEnv(boolean installEnv) {
        this.installEnv = installEnv;
    }

    public String getLostCustomerRedirectUrl() {
        return lostCustomerRedirectUrl;
    }

    public void setLostCustomerRedirectUrl(String lostCustomerRedirectUrl) {
        this.lostCustomerRedirectUrl = lostCustomerRedirectUrl;
    }

    public int getDataSynchInterval() {
        return dataSynchInterval;
    }

    public void setDataSynchInterval(int dataSynchInterval) {
        this.dataSynchInterval = dataSynchInterval;
    }


}
