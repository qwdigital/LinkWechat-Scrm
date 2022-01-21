package com.linkwechat.common.config;

import com.linkwechat.common.utils.OsUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 * 
 * @author ruoyi
 */
@Component
@ConfigurationProperties(prefix = "ruoyi")
public class RuoYiConfig
{
    /** 项目名称 */
    private String name;

    /** 版本 */
    private String version;

    /** 版权年份 */
    private String copyrightYear;

    /** 实例演示开关 */
    private boolean demoEnabled;

    /** 上传路径 */
    private static String profile;

    /** 获取地址开关 */
    private static boolean addressEnabled;

    /** 企业微信账号登录系统默认密码 */
    private static String weUserDefaultPwd="123456";


    private FileConfig file;


    /**是否开启多租户*/
    private boolean startTenant=false;


    /**是否可以修改密码*/
    private boolean editPwd=true;


    /**无需同步的用户*/
    private String[] noSyncWeUser;



    /**匿名访问的URL*/
    private String[] anonUrl;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getCopyrightYear()
    {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear)
    {
        this.copyrightYear = copyrightYear;
    }

    public boolean isDemoEnabled()
    {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled)
    {
        this.demoEnabled = demoEnabled;
    }

    public static String getProfile()
    {
        if(OsUtils.isWindows()){
            return "D:/linkWeChat/uploadPath";

        }
        return "/Users/robin/work/sr_project/lw";
    }

    public void setProfile(String profile)
    {
        RuoYiConfig.profile = profile;
    }

    public static boolean isAddressEnabled()
    {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
        RuoYiConfig.addressEnabled = addressEnabled;
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取会话存档素材下载路径
     */
    public static String getDownloadWeWorkPath()
    {
        return getProfile() + "/download/media_data/{}/{}";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }

    public  String getWeUserDefaultPwd() {
        return weUserDefaultPwd;
    }

    public  void setWeUserDefaultPwd(String weUserDefaultPwd) {
        RuoYiConfig.weUserDefaultPwd = weUserDefaultPwd;
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

    public boolean isStartTenant() {
        return startTenant;
    }

    public void setStartTenant(boolean startTenant) {
        this.startTenant = startTenant;
    }

    public boolean isEditPwd() {
        return editPwd;
    }

    public void setEditPwd(boolean editPwd) {
        this.editPwd = editPwd;
    }


    public String[] getNoSyncWeUser() {
        return noSyncWeUser;
    }

    public void setNoSyncWeUser(String[] noSyncWeUser) {
        this.noSyncWeUser = noSyncWeUser;
    }
}
