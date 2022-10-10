package com.linkwechat.common.utils.file;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.aspectj.weaver.ast.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件处理工具类
 *
 * @author ruoyi
 */
@Slf4j
public class FileUtils extends org.apache.commons.io.FileUtils {
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";
    /**
     * 字符常量：斜杠 {@code '/'}
     */
    public static final char SLASH = '/';

    /**
     * 字符常量：反斜杠 {@code '\\'}
     */
    public static final char BACKSLASH = '\\';

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 批量下载文件，打包为zip。
     *
     * @param fileList 文件列表，每个元素应包含fileName、url，前者即为文件名，后者为下载所需链接
     * @param os       输出流
     */
    public static void batchDownloadFile(List<FileEntity> fileList, OutputStream os) {
        try {
            ZipOutputStream zos = new ZipOutputStream(os);
//            List<String> complateNames=new ArrayList<>();
            for (FileEntity fileInfo : fileList) {
//                String fileName=checkZipName(complateNames,fileList.stream().map(FileEntity::getFileName).collect(Collectors.toList()), fileInfo.getFileName(), 1)
//                        +fileInfo.getSuffix();
                String fileName=fileInfo.getFileName()+fileInfo.getSuffix();
                String url = fileInfo.getUrl();
                // 跳过不包含指定键位的对象
                if (StringUtils.isEmpty(url) || StringUtils.isEmpty(fileName)) {
                    continue;
                }
                URL _url = new URL(url);
                new ZipEntry(fileName);

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
     * @param urlPath 下载路径
     * @param os      输出流
     * @return 返回下载文件
     * @throws Exception
     */
    public static void downloadFile(String urlPath, OutputStream os) {
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
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            inputStream.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
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
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
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
    public static boolean isValidFilename(String filename) {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains("Chrome")) {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    /**
     * 返回文件名
     *
     * @param filePath 文件
     * @return 文件名
     */
    public static String getName(String filePath) {
        if (null == filePath) {
            return null;
        }
        int len = filePath.length();
        if (0 == len) {
            return filePath;
        }
        if (isFileSeparator(filePath.charAt(len - 1))) {
            // 以分隔符结尾的去掉结尾分隔符
            len--;
        }

        int begin = 0;
        char c;
        for (int i = len - 1; i > -1; i--) {
            c = filePath.charAt(i);
            if (isFileSeparator(c)) {
                // 查找最后一个路径分隔符（/或者\）
                begin = i + 1;
                break;
            }
        }

        return filePath.substring(begin, len);
    }


    /**
     * 是否为Windows或者Linux（Unix）文件分隔符<br>
     * Windows平台下分隔符为\，Linux（Unix）为/
     *
     * @param c 字符
     * @return 是否为Windows或者Linux（Unix）文件分隔符
     */
    public static boolean isFileSeparator(char c) {
        return SLASH == c || BACKSLASH == c;
    }


    /**
     * InputStream转成byte[]
     * @param inStream
     * @return
     * @throws IOException
     */
    public static final byte[] inputTobyte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] bytes= swapStream.toByteArray();
        return bytes;
    }

    /**
     * 根据地址获得数据的输入流
     * @param strUrl 网络连接地址
     * @return url的输入流
     */
    public static InputStream getInputStreamByUrl(String strUrl){
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(),output);
            return  new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            log.error(e+"");
        }finally {
            try{
                if (conn != null) {
                    conn.disconnect();
                }
            }catch (Exception e){
                log.error(e+"");
            }
        }
        return null;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FileEntity{
        private String fileName;
        private String url;
        private String suffix;
    }

    /**
     * 文件名重复机制（1）（2）的形式输出
     * @param fileNames 文件名集合
     * @param fileName 当前文件名
     * @param j 开始下标，大部分传1
     * @return
     */
    private static String checkZipName(List<String> complateNames,List<String> fileNames,String fileName, int j){
        if(!complateNames.contains(fileName)) {
            return fileName;
        }
        if (decide(fileName)) { //文件名字没有(2)的形式
            fileName = checkZipName(complateNames,fileNames,fileName + "(" + j +")", j++); //递归生成文件名字
            complateNames.add(fileName);
            return fileName;
        }
        String numCountString = fileName.substring(fileName.lastIndexOf("(") + 1, fileName.lastIndexOf(")"));
        int numCount = -1;
        if(org.apache.commons.lang3.StringUtils.isNumeric(numCountString)) {
            numCount = Integer.parseInt(numCountString);
        }
        if (numCount >= 1) { //是数字，不是字符串，截取(x)之前的字符  在numCount这个数字的基础上实现自增
            String realName = fileName.substring(0, fileName.lastIndexOf("("));
            fileName = checkZipName(complateNames,fileNames,realName + "(" + ++numCount +")", 2); //递归生成文件名字
            complateNames.add(fileName);
            return fileName;
        }
        fileName = checkZipName(complateNames,fileNames,fileName + "(" + j +")", j++); //递归生成文件名字
        complateNames.add(fileName);
        return fileName;
    }

    public static boolean decide(String fileName){
        if(fileName.lastIndexOf("(") < 0){return true;}
        if(fileName.lastIndexOf(")") < 0){return true;}
        if((fileName.length() - 1) > fileName.lastIndexOf(")")){return true;}
        if((fileName.lastIndexOf(")") - fileName.lastIndexOf("(")) <= 1){return true;}
        return false;
    }
}
