package com.linkwechat.common.config;

import com.linkwechat.common.utils.OsUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

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
     * 是否开启文件预览 true:开启 false:不开启
     */
    private boolean enableFilePreview=true;
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
     * 新客拉群h5地址
     */
    private String communityNewGroupUrl;

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
     * 直播页面h5链接
     */
    private String liveUrl;




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
     * auth服务中授权微信端相关的授权的接口
     */
    private List<String> wxAuthUrl;

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
     * 识客码
     */
    private String knowCustomerUrl;

    /**
     * 导购群码
     */
    private String guideGroupUrl;



    /**
     * 裂变url
     */
    private String fissionUrl;

    /**
     * 短链域名
     */
    private String shortLinkDomainName;


    /**
     * 获客助手短链
     */
    private String customerShortLinkDomainName;

    /**
     * 短链小程序地址
     */
    private String shortAppletUrl;

    /**
     * 拉新活码H5地址
     */
    private String lxQrCodeUrl;

    /**
     * 短链推广-应用消息页面
     */
    private String appMsgUrl;


    /**
     * 素材中心素材详情
     */
    private String materialDetailUrl;

    /**
     * 朋友圈移动端列表页
     */
    private String momentsUrl;

    /**
     * 线索中心移动端-待办任务-线索长时间未跟进-详情页
     */
    private String leadsDetailUrl;

    /**
     * 线索中心移动端-待办任务-线索约定事项待跟进-详情页
     */
    private String leadsCovenantWaitFollowUpUrl;



    private WeComeProxyConfig weComeProxyConfig;



    private FincaceProxyConfig fincaceProxyConfig;

    /**
     * 混元大模型秘钥ID
     */
    private String txAiSecretId;
    /**
     * 混元大模型秘钥
     */
    private String txAiSecretKey;

    /**
     * 混元大模型地区
     */
    private String txAiRegion;
    /**
     * 活码短链域名
     */
    private String qrShortLinkDomainName;

    /**
     * 群活码短链域名
     */
    private String qrGroupShortLinkDomainName;


    /**
     * 关键词群h5链接
     */
    private String keyWordGroupUrl;

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


    public boolean isEnableFilePreview() {
        return enableFilePreview;
    }

    public void setEnableFilePreview(boolean enableFilePreview) {
        this.enableFilePreview = enableFilePreview;
    }

    public WeComeProxyConfig getWeComeProxyConfig() {
        return weComeProxyConfig;
    }

    public void setWeComeProxyConfig(WeComeProxyConfig weComeProxyConfig) {
        this.weComeProxyConfig = weComeProxyConfig;
    }

    public FincaceProxyConfig getFincaceProxyConfig() {
        return fincaceProxyConfig;
    }

    public void setFincaceProxyConfig(FincaceProxyConfig fincaceProxyConfig) {
        this.fincaceProxyConfig = fincaceProxyConfig;
    }
}
