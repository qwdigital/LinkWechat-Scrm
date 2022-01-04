package com.linkwechat.common.utils.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.utils.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;

/**
 * 文件处理工具类
 *
 * @author ruoyi
 */
public class FileUtils extends org.apache.commons.io.FileUtils
{
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os 输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 批量下载文件，打包为zip。
     * @param fileList 文件列表，每个元素应包含fileName、url，前者即为文件名，后者为下载所需链接
     * @param os 输出流
     */
    public static void batchDownloadFile(List<Map<String, String>> fileList, OutputStream os) {
        try {
            Map<String, Integer> fileNameCountMap = fileList.stream().collect(Collectors
                    .toMap(item -> item.get("fileName"), item -> 0, (key, value) -> key));
            ZipOutputStream zos = new ZipOutputStream(os);
            for (Map<String, String> fileInfo: fileList) {
                String fileName = fileInfo.get("fileName");
                String url = fileInfo.get("url");
                if(fileNameCountMap.containsKey(fileName)){
                    Integer count = fileNameCountMap.get(fileName);
                    if(count > 0){
                        String tempName = FileUtil.getPrefix(fileName) + "(" + count + ")";
                        fileName = fileName.replaceAll(FileUtil.getPrefix(fileName),tempName);
                    }
                    fileNameCountMap.put(fileName,count+1);
                }
                // 跳过不包含指定键位的对象
                if (StringUtils.isEmpty(url) || StringUtils.isEmpty(fileName)){
                    continue;
                }
                URL _url = new URL(url);
                zos.putNextEntry(new ZipEntry(fileName));
                InputStream fis = _url.openConnection().getInputStream();
                byte[] buffer = new byte[1024];
                int r = 0;
                while ((r = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, r);
                }
                fis.close();
            }
            //关闭zip输出流
            zos.flush();
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param urlPath 下载路径
     * @param os  输出流
     * @return 返回下载文件
     * @throws Exception
     */
    public static void downloadFile(String urlPath,OutputStream os){
        InputStream inputStream = null;
        try {
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设定请求的方法，默认是GET（对于知识库的附件服务器必须是GET，如果是POST会返回405。流程附件迁移功能里面必须是POST，有所区分。）
            httpURLConnection.setRequestMethod("GET");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.getResponseCode();

            inputStream = httpURLConnection.getInputStream();

            byte[] b = new byte[1024];
            int length;
            while ((length = inputStream.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
            inputStream.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }


        /**
         * 删除文件
         *
         * @param filePath 文件
         * @return
         */
    public static boolean deleteFile(String filePath)
    {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件名称验证
     * 
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename)
    {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 下载文件名重新编码
     * 
     * @param request 请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException
    {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE"))
        {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        else if (agent.contains("Firefox"))
        {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        }
        else if (agent.contains("Chrome"))
        {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        else
        {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }


    /**
     * MultipartFile 转化为file
     * @param multiFile
     * @return
     */
    public static File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码

        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
