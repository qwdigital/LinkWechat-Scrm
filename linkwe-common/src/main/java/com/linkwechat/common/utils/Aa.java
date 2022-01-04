package com.linkwechat.common.utils;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Aa {

    /**
     * 模拟上传文件(远程URL)
     *
     * @Title uploadFile
     * @param uploadUrl   上传路径
     * @param picUrl       远程图片URL
     * @param params       参数内容
     * @return
     * @throws IOException
     * @author zh
     * @date 2016年6月30日 下午4:22:10
     */
    public static String uploadFile(String uploadUrl, URL picUrl, Map<String,String> params) throws IOException {
        return uploadFile(uploadUrl, picUrl.openStream(), "回话存档", params);
    }

    /**
     * 模拟上传文件(本地文件)
     *
     * @Title uploadFile
     * @param uploadUrl   上传路径
     * @param params       参数内容
     * @return
     * @throws IOException
     * @author zh
     * @date 2016年6月30日 下午4:23:15
     */
    public static String uploadFile(String uploadUrl, File file, Map<String,String> params) throws IOException {
        return uploadFile(uploadUrl, new FileInputStream(file), file.getName(), params);
    }

    /**
     * 模拟上传文件（输入流）
     *
     * @Title uploadFile
     * @param uploadUrl     上传链接
     * @param is            输入流
     * @param filename      文件名
     * @param params        参数
     * @return
     * @throws IOException
     * @author zh
     * @date 2016年6月30日 下午4:08:52
     */
    public static String uploadFile(String uploadUrl, InputStream is, String filename, Map<String,String> params) throws IOException {
        URL urlGet = new URL(uploadUrl);
        HttpURLConnection conn = (HttpURLConnection) urlGet.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "DEFAULT_USER_AGENT");
        conn.setRequestProperty("Charsert", "UTF-8");
        // 定义数据分隔线
        String BOUNDARY = "----WebKitFormBoundary7blBEBTiZtWOX8LI";
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        OutputStream out = new DataOutputStream(conn.getOutputStream());
        // 文件内容
        StringBuilder contentBody = new StringBuilder();
        contentBody.append("--").append(BOUNDARY).append("\r\n");
        contentBody.append("Content-Disposition: form-data;name=\"media\";filename=\""+ filename + "\"\r\n");
        contentBody.append("Content-Type:application/octet-stream\r\n\r\n");
        System.out.println(contentBody);
        out.write(contentBody.toString().getBytes());
        DataInputStream fs = new DataInputStream(is);
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = fs.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        IOUtils.closeQuietly(fs);
        // 参数内容
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                StringBuilder paramData = new StringBuilder();
                paramData.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                paramData.append("Content-Disposition: form-data;name=\""+key+"\";");
                paramData.append("\r\n");
                paramData.append("\r\n");
                paramData.append("\""+params.get(key)+"\"");
                System.out.println(paramData);
                out.write(paramData.toString().getBytes());
            }
        }
        // 最后一个片段结尾要用--表示
        byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
        out.write(end_data);
        out.flush();
        IOUtils.closeQuietly(out);
        // 定义BufferedReader输入流来读取URL的响应
        InputStream in = conn.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in, Charsets.UTF_8));
        String valueString = null;
        StringBuffer bufferRes = null;
        bufferRes = new StringBuffer();
        while ((valueString = read.readLine()) != null){
            bufferRes.append(valueString);
        }
        IOUtils.closeQuietly(in);
        // 关闭连接
        if (conn != null) {
            conn.disconnect();
        }

        return bufferRes.toString();
    }


    public static void main(String[] args) throws Exception {
//        File file = new File("E:\\中文图片.jpg");
//        String access_token = "AasbMO9SCcF3HO0jVLnddQuZo4pUmSYHieqC56vQbBvQgqGWW8spQG5CtWT6Vcjh";
        String uploadUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?type=file&access_token=otMTtJ_G_cCjHs1JOU9sI90xC8xDUMBbvgiDSCfY__ZHIgIwnf3hSOWenZzUHEITOXU9EUILBcIdoFIPR4LMo7ngTw_3ty-sXTrm8BLw9He5AEJKykCw3detu3ckEt7QDzFL1aP3l9VswzWrBm8_I-JIflWSOy0WOhyWRK11zvQw8FpQ26PInGWjSS718A2LJexK6Rf2liA8sM7J11Ysqg";
//        // 本地文件
//        String upload_response = uploadFile(uploadUrl, file, null);
//        System.out.println(upload_response);

        // 远程URL文件提交上传
        URL url = new URL("https://link-wechat-1251309172.cos.ap-nanjing.myqcloud.com/2021/12/30/c3fb815b-68a1-4df9-8781-4b203b92a5bf.doc");
        String upload_response2 = uploadFile(uploadUrl, url, null);
        System.out.println(upload_response2);
    }


}
