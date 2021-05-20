package com.linkwechat.framework.web.service;


import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.utils.OsUtils;
import com.linkwechat.common.utils.file.FileUploadUtils;
import com.linkwechat.framework.web.domain.server.SysFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * 文件服务
 */
@Component
public class FileService {


    @Autowired
    private RuoYiConfig ruoYiConfig;


    private static final String WINDOWSFILEPATH="D:/linkWeChat/uploadPath";


    private static final String LINUXFILEPATH="/linkWeChat/uploadPath";



    /**
     * 文件上传
     * @param file
     * @return
     * @throws IOException
     */
     public SysFile upload(MultipartFile file) throws IOException {
        String fileName="";
        if(ruoYiConfig.getFile().isStartCosUpload()){//开启云上传
             //开启云上传开关则云上传，不然上传本地
             fileName = FileUploadUtils.upload2Cos(file, ruoYiConfig.getFile().getCos());
         }else {//本地上传
            File osFile=OsUtils.isWindows()?new File(WINDOWSFILEPATH):new File(LINUXFILEPATH);
            if(!osFile.exists()){
                osFile.mkdirs();
            }
            fileName = FileUploadUtils.upload(osFile.getPath(), file);
        }
        return SysFile.builder()
                .fileName(fileName)
                .imgUrlPrefix(ruoYiConfig.getFile().getImgUrlPrefix())
                .build();
    }



    public void findFile(String fileName, HttpServletResponse rp){
        String fileDownUrl="";
//        if(ruoYiConfig.getFile().isStartCosUpload()) {//开启云上传
//            fileDownUrl= ruoYiConfig.getFile().getImgUrlPrefix();
//        }else{
            fileDownUrl=OsUtils.isWindows()?WINDOWSFILEPATH+fileName:LINUXFILEPATH+fileName;
//        }
        File file=new File(fileDownUrl);
        if (file.exists()) {
            FileInputStream fis = null;
            OutputStream os = null;
            try {
                fis = new FileInputStream(file);
                os = rp.getOutputStream();
                int count = 0;
                byte[] buffer = new byte[1024 * 8];
                while ((count = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                    os.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
