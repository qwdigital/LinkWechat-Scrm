package com.linkwechat.fileservice.utils;

import cn.hutool.core.util.ObjectUtil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * @Author 狗头军师
 * @Description TODO
 * @Date 2022/10/17 10:36
 **/
public class VideoFirstFrameUtils {


    public static void cutUrl(String imageUrl, int width, int height) throws Exception {
        URL url = new URL(imageUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        /**设置请求方式为"GET"*/
        httpConn.setRequestMethod("GET");
        /**超时响应时间为5秒*/
        httpConn.setConnectTimeout(5 * 1000);
        httpConn.connect();
        InputStream is = httpConn.getInputStream();
        // 读取图片
        BufferedImage bufImage = ImageIO.read(is);
        BufferedImage tag=new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        //缩放图片
        tag.getGraphics().drawImage(bufImage, 0, 0, width, height, null);
        File imgFile = new File("bb.jpg");
        ImageIO.write(tag, "JPEG", imgFile);
        /**
         * 裁剪图片
         * @param x   起始x坐标
         * @param y   起始y坐标
         * @param w  要裁剪的图片的宽度
         * @param h  要裁剪的图片的高度
         */
        BufferedImage bufferedImage = bufImage.getSubimage(0,0,300,300);
        File imgCutFile = new File("bb2.jpg");
        ImageIO.write(bufferedImage, "JPEG", imgCutFile);

        //缩放图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        Image img = bufImage.getScaledInstance(width , height, Image.SCALE_DEFAULT);
        graphics.drawImage(img, 0, 0,null);
        //一定要释放资源
        graphics.dispose();
        File imgZoomFile = new File("bb3.jpg");
        ImageIO.write(image, "JPEG", imgZoomFile);


    }


    public static RenderedImage getFirstFrameRenderedImageByUrl(String imageUrl){

        FFmpegFrameGrabber ff = null;

        try {
            //图片的名称
            String uuid = UUID.randomUUID().toString();
            //图片的路径
            URL url = new URL(imageUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            /**设置请求方式为"GET"*/
            httpConn.setRequestMethod("GET");
            /**超时响应时间为5秒*/
            httpConn.setConnectTimeout(5 * 1000);
            httpConn.connect();
            InputStream is = httpConn.getInputStream();

            if (ObjectUtil.isNotEmpty(is)) {
                ff = new FFmpegFrameGrabber(is);
                ff.start();
                int ftp = ff.getLengthInFrames();
                int flag=0;
                Frame frame = null;
                while (flag <= ftp) {
                    //获取帧
                    frame = ff.grabImage();
                    //过滤前3帧，避免出现全黑图片
                    if ((flag>6)&&(frame != null)) {
                        break;
                    }
                    flag++;
                }
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage fecthedImage = converter.getBufferedImage(frame);
                BufferedImage bi = new BufferedImage(400, 400, BufferedImage.TYPE_3BYTE_BGR);
                bi.getGraphics().drawImage(fecthedImage.getScaledInstance(400, 400, Image.SCALE_SMOOTH),0, 0, null);
                return bi;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(ff != null){
                    ff.close();
                    ff.stop();
                }
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取视频第一帧
     * @param filePath  视频路径
     * @return 视频第一帧图片路径
     */
    public static String getFirstFrame(String filePath){
        String firstFrameImagePath = null;
        FFmpegFrameGrabber ff = null;

        try {
            //图片的名称
            String uuid = UUID.randomUUID().toString();
            //图片的路径
            String localPath="D:/imageTest01/"+uuid+".png";
            File file = new File(localPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            File fileVideo = new File(filePath);
            if (fileVideo.exists()) {
                System.out.println("文件存在，路径正确！");
                ff = new FFmpegFrameGrabber(fileVideo);
                ff.start();
                int ftp = ff.getLengthInFrames();
                int flag=0;
                Frame frame = null;
                while (flag <= ftp) {
                    //获取帧
                    frame = ff.grabImage();
                    //过滤前3帧，避免出现全黑图片
                    if ((flag>6)&&(frame != null)) {
                        break;
                    }
                    flag++;
                }

                //ImageIO.write(FrameToBufferedImage(frame), "jpg", file);
                FrameToBufferedImage1(frame,file);
                try {
                    //将本地的图片上传到七牛服务器

                    System.out.println("firstFrameImagePath :" + firstFrameImagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(ff != null){
                    ff.close();
                    ff.stop();
                }
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
        return firstFrameImagePath;
    }

    private static RenderedImage FrameToBufferedImage1(Frame frame,File file) throws IOException {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage fecthedImage = converter.getBufferedImage(frame);
        BufferedImage bi = new BufferedImage(400, 400, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(fecthedImage.getScaledInstance(400, 400, Image.SCALE_SMOOTH),0, 0, null);
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        try {
            ImageIO.write(bi, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }


    private static RenderedImage FrameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }



}
