package com.linkwechat.common.utils.file;


import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.utils.OsUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;


/**
 * 文件服务
 */
@Slf4j
public class FileUtil {


    private static RuoYiConfig ruoYiConfig = SpringUtils.getBean(RuoYiConfig.class);


    private static final String WINDOWSFILEPATH="D:/linkWeChat/uploadPath";


    private static final String LINUXFILEPATH="/linkWeChat/uploadPath";

    /**
     * 文件上传
     * @param file
     * @return
     * @throws IOException
     */
     public static JSONObject upload(MultipartFile file) throws Exception {

         try {
             FileUploadUtils.assertAllowed(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
             String fileName="";
             String imgUrlPrefix="";
             if(ruoYiConfig.getFile().isStartCosUpload()){//开启云上传
                 //开启云上传开关则云上传，不然上传本地
                 fileName = FileUploadUtils.upload2Cos(file, ruoYiConfig.getFile().getCos());
                 imgUrlPrefix = ruoYiConfig.getFile().getCos().getCosImgUrlPrefix();
             }else {//本地上传
                 File osFile=OsUtils.isWindows()?new File(WINDOWSFILEPATH):new File(LINUXFILEPATH);
                 if(!osFile.exists()){
                     osFile.mkdirs();
                 }
                 fileName = FileUploadUtils.upload(osFile.getPath(), file);
                 imgUrlPrefix = ruoYiConfig.getFile().getImgUrlPrefix();
             }
             JSONObject reust = new JSONObject();
             reust.put("fileName",fileName);
             reust.put("imgUrlPrefix",imgUrlPrefix);
             return reust;

         }catch (Exception e){
             throw e;
         }

    }


    /**
     * 获取图片(本地,或者网络图片)
     * @param fileName
     * @param rp
     */
    public void findImage(String fileName, HttpServletResponse rp){
         try {
             String fileDownUrl="";
             rp.setContentType("image/png");
             if(ruoYiConfig.getFile().isStartCosUpload()) {//开启云上传
                 fileDownUrl= ruoYiConfig.getFile().getCos().getCosImgUrlPrefix();
                 FileUtils.downloadFile(fileDownUrl+fileName,rp.getOutputStream());
             }else{
                 fileDownUrl=OsUtils.isWindows()?WINDOWSFILEPATH+"/"+fileName:LINUXFILEPATH+"/"+fileName;
                 FileUtils.writeBytes(fileDownUrl,rp.getOutputStream());
             }
         }catch (Exception e){
              log.error("图片获取异常:"+e.getMessage());

         }
    }


}
