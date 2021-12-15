package com.linkwechat.common.utils.img;


import com.qcloud.cos.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Slf4j
public class ImageUtils {

    private final static JLabel J_LABEL = new JLabel();




    public static byte[] changeImageSize(InputStream inputStream, Integer size) {
        try {
            BufferedImage oldBufferedImage = ImageIO.read(inputStream);
            int oldWidth = oldBufferedImage.getWidth();
            int oldHeight = oldBufferedImage.getHeight();
            int newWidth = 0;
            int newHeight = 0;
            if (size >= 0) {
                newWidth = oldWidth * size;
                newHeight = oldHeight * size;
            } else {
                size = -size;
                newWidth = oldWidth / size;
                newHeight = oldHeight / size;
            }
            BufferedImage newBufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D newGraphics2D = newBufferedImage.createGraphics();
            newGraphics2D.setBackground(Color.WHITE);
            newGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            newGraphics2D.drawImage(oldBufferedImage, 0, 0, newWidth, newHeight, null);
            oldBufferedImage.flush();
            OutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(newBufferedImage, "jpg", outputStream);
            newBufferedImage.flush();
            byte[] bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
            outputStream.flush();
            outputStream.close();
            return bytes;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 切割图片
     *
     * @param inputStream
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static byte[] cropImage(InputStream inputStream, int x, int y, int width, int height) {
        BufferedImage bufferedImage = null;
        byte bytes[] = null;
        try {
            bufferedImage = ImageIO.read(inputStream);
            bytes = cropImage(bufferedImage, x, y, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bufferedImage.flush();
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                inputStream = null;
            }
        }
        return bytes;
    }


    public static byte[] cropImage(BufferedImage bufferedImage, int x, int y, int width, int height) {
        //BufferedImage newBufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        BufferedImage copyBufferedImage = bufferedImage.getSubimage(x, y, width, height);
        /*Graphics2D graphics2D = newBufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(copyBufferedImage,0,0,width,height,null);
        copyBufferedImage.flush();*/
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            ImageIO.write(copyBufferedImage, "jpg", outputStream);
            bytes = outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                outputStream = null;
            }
            //newBufferedImage.flush();

        }
        return bytes;

    }


    public static void inciseImage(InputStream inputStream, Integer direction, Integer directionValue, String fileName, String path) {
        try {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if (direction == 1) {
                int directionNum = height / directionValue;
                if (directionNum == 0) {
                    return;
                }
                int i = 0;
                for (i = 0; i < directionNum; i++) {
                    byte[] bytes = cropImage(bufferedImage, 0, i * directionValue, width, directionValue);
                    byteToFile(bytes, fileName + "-" + i, "jpg", path);
                }
                int directionSurplus = height % directionValue;
                if (directionSurplus > 0) {
                    byte[] bytes = cropImage(bufferedImage, 0, height - directionSurplus, width, directionSurplus);
                    byteToFile(bytes, fileName + "-" + (i + 1), "jpg", path);
                }
            } else if (direction == 2) {
                int directionNum = width / directionValue;
                if (directionNum == 0) {
                    return;
                }
                int i = 0;
                for (; i < directionNum; i++) {
                    byte[] bytes = cropImage(bufferedImage, i * directionValue, 0, directionValue, height);
                    byteToFile(bytes, fileName + "-" + i, "jpg", path);
                }
                int directionSurplus = width % directionValue;
                if (directionSurplus > 0) {
                    byte[]  bytes = cropImage(bufferedImage, height - directionSurplus, 0, directionSurplus, height);
                    byteToFile(bytes, fileName + "-" + (i + 1), "jpg", path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static BufferedImage filePathToBufferedImage(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.isFile()) {
            throw new FileNotFoundException("文件路径为目录");
        }
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在");
        }
        BufferedImage bufferedImage = ImageIO.read(file);
        return bufferedImage;
    }


    /**
     * 复制
     *
     * @param bufferedImage
     * @param bufferedImageType
     * @return
     */
    public static BufferedImage copyBufferedImage(BufferedImage bufferedImage, Integer bufferedImageType) {
        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImageType);
        Graphics2D graphics2D = newBufferedImage.createGraphics();
        graphics2D.drawImage(bufferedImage, null, 0, 0);
        return newBufferedImage;
    }

    /**
     * 设置透明度
     * @param bufferedImage
     * @param alpha
     * @param bufferedImageType
     * @return
     */
    public static BufferedImage setBufferedImageAlpha(BufferedImage bufferedImage,Integer alpha,Integer bufferedImageType){
        if(alpha < 0 || alpha > 255){
            throw new RuntimeException("范围错误[0,255]");
        }
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage newBufferedImage = new BufferedImage(width,height, bufferedImageType);
        int argb;
        int oa;
        int a;
        int rgb;
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                argb = bufferedImage.getRGB(x,y);
                rgb = argb & 0x00FFFFFF;
                oa = argb >>> 24;
                a = oa == 0?0:alpha<<24;
                argb = a^rgb;
                newBufferedImage.setRGB(x,y,argb);
            }
        }
        return newBufferedImage;
    }


    /**
     * 放大缩小
     * @param bufferedImage
     * @param enlargementTimes
     * @return
     */
    public static BufferedImage enlargementBufferedImage(BufferedImage bufferedImage, double enlargementTimes, Integer bufferedImageType) {
        int newWidth = (int) (bufferedImage.getWidth() * enlargementTimes);
        int newHeight = (int) (bufferedImage.getHeight() * enlargementTimes);
        BufferedImage newBufferedImage = new BufferedImage(newWidth, newHeight, bufferedImageType);
        Graphics2D graphics2D = newBufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(bufferedImage, 0, 0, newWidth, newHeight, null);
        return newBufferedImage;
    }

    /**
     * 将图片改为固定尺寸
     *
     * @param bufferedImage
     * @param bufferedImageType
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage fixedDimensionBufferedImage(BufferedImage bufferedImage, Integer bufferedImageType, Integer width, Integer height) {
        BufferedImage newBufferedImage = new BufferedImage(width, height, bufferedImageType);
        Graphics2D graphics2D = newBufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(bufferedImage, 0, 0, width, height, null);
        return newBufferedImage;
    }


    /**
     * 合并两个图层
     *
     * @param backBufferedImage
     * @param frontBufferedImage
     * @param pointX
     * @param pointY
     * @return
     */
    public static BufferedImage mergeBufferedImage(BufferedImage backBufferedImage, BufferedImage frontBufferedImage, Integer pointX, Integer pointY) {
        Graphics2D graphics2D = backBufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(frontBufferedImage, pointX, pointY, frontBufferedImage.getWidth(), frontBufferedImage.getHeight(), null);
        return backBufferedImage;
    }

    /**
     * 在图层上写字
     *
     * @param bufferedImage
     * @param str
     * @param pointX
     * @param pointY
     * @param font
     * @param color
     * @return
     */
    public static BufferedImage writeFontBufferedImage(BufferedImage bufferedImage, String str, Integer pointX, Integer pointY, Font font, Color color) {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        /*graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(color);
        graphics2D.setFont(font);
        graphics2D.drawString(str, pointX, pointY);*/
        writeFontBufferedImage(graphics2D,str,pointX,pointY,font,color);
        return bufferedImage;
    }

    public static void writeFontBufferedImage(Graphics2D graphics2D, String str, Integer pointX, Integer pointY, Font font, Color color) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(color);
        graphics2D.setFont(font);
        graphics2D.drawString(str, pointX, pointY);
    }


    /**
     * byte数组转成文件
     *
     * @param bytes
     * @param fileName
     * @param fileType
     * @param filePath
     */
    public static void byteToFile(byte bytes[], String fileName, String fileType, String filePath) {
        File fileFloder = new File(filePath);
        if (!fileFloder.exists()) {
            fileFloder.mkdirs();
        }
        fileFloder = null;
        ///fileName = WsStringUtils.stringTrim(fileName);
        filePath = filePath + fileName + "." + fileType;
        File file = new File(filePath);
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            fileChannel = fileOutputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            fileChannel.write(byteBuffer);
            byteBuffer.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileChannel != null) {
                    fileChannel.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 为图片打马赛克
     * @param bufferedImage
     * @param pointX
     * @param pointY
     * @param width
     * @param height
     * @param level
     * @return
     */
    public static BufferedImage createMosaic(BufferedImage bufferedImage, Integer pointX, Integer pointY, Integer width, Integer height, Integer level) {
        int bufferImageWidth = bufferedImage.getWidth();
        int bufferImageHeight = bufferedImage.getHeight();
        Integer x = pointX;
        Integer y = pointY;
        int chunkWidth = width / level;
        int chunkHeight = height / level;
        chunkWidth = chunkWidth == 0 ? 1 : chunkWidth;
        chunkHeight = chunkHeight == 0 ? 1 : chunkHeight;

        x -= chunkWidth;
        y -= chunkHeight;
        Random random = new Random();
        Graphics2D graphics2D = bufferedImage.createGraphics();
        /*Rectangle rectangle = new Rectangle();
        rectangle.setRect(pointX,pointY,chunkWidth,chunkHeight);*/
        while (x < pointX + width) {
            x += chunkWidth;
            while (y < pointY + height) {
                y += chunkHeight;
                Integer randomX = random.nextInt(chunkWidth);
                Integer randomY = random.nextInt(chunkWidth);
                int modificationX = 0;
                int modificationY = 0;
                if (x + randomX >= bufferImageWidth) {
                    modificationX = (x + randomX) - bufferImageWidth + 1;
                }
                if (y + randomY >= bufferImageHeight) {
                    modificationY = (y + randomY) - bufferImageHeight + 1;
                }

                System.out.println("(" + (x + randomX - modificationX) + "," + (y + randomY - modificationY) + ")(" + x + "," + y + ")");

                int rgb = bufferedImage.getRGB(x + randomX - modificationX, y + randomY - modificationY);
                int a = rgb >>> 24;
                int r = (rgb << 8) >>> 24;
                int g = (rgb << 16) >>> 24;
                int b = (rgb << 24) >>> 24;
                //System.out.println("a:"+a+" r:"+r+" g:"+g+" b:"+b);
                Color color = new Color(r, g, b);
                graphics2D.setColor(color);
                graphics2D.fillRect(x, y, chunkWidth, chunkHeight);


            }
            y = pointY - chunkHeight;
        }
        return bufferedImage;
    }


    /**
     * bufferedImage转成byte数组
     *
     * @param bufferedImage
     * @param type
     * @return
     */
    public static byte[] bufferedImageToByteArray(BufferedImage bufferedImage, String type) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            ImageIO.write(bufferedImage, type, byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
            bufferedImage.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;

    }

    /**
     * 把字符串拆成行
     * @param context 文本内容
     * @param fontMetrics
     * @param pointX 开始点的x轴坐标
     * @param pointY 开始点的y轴坐标
     * @param width 文本域宽度
     * @param height 文本域高度
     * @param wordSpace 字间距
     * @param lineSpace 行间距
     * @param horizontalType 1 右对齐 2 居中 3 左对齐
     * @return
     */
    public static List<LineText> splitContext(String context,FontMetrics fontMetrics,int pointX,int pointY,int width,int height,int wordSpace,int lineSpace,Integer horizontalType,Integer verticalType){
        context = new String(context.getBytes(StandardCharsets.UTF_8));
        Font font = fontMetrics.getFont();
        char[] chars = context.toCharArray();
        List<LineText> returnLineList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        //当前的行宽度
        int currentLineWidth = 0;
        //当前的高度
        int currentHeight = font.getSize();
        //字体宽度
        int charSize = 0;
        List<CharText> charTextList = new ArrayList<>();
        for(char c:chars){
            charSize = fontMetrics.charWidth(c);
            currentLineWidth += charSize;
            if(currentLineWidth >= width){
                returnLineList.add(new LineText(pointX,width,currentHeight,sb.toString(),currentLineWidth - charSize - wordSpace,charTextList,horizontalType));
                currentHeight += font.getSize();
                currentHeight += lineSpace;
                currentLineWidth = charSize;
                charTextList = new ArrayList<>();
                sb = new StringBuilder();
            }
            charTextList.add(new CharText(currentLineWidth - charSize,currentHeight,c));
            currentLineWidth += wordSpace;
            sb.append(c);
            if(currentHeight > height){
                sb = new StringBuilder();
                break;
            }
        }
        if(sb.length() > 0){
            returnLineList.add(new LineText(pointX,width,currentHeight,sb.toString(),currentLineWidth - wordSpace,charTextList,horizontalType));
        }
        if(verticalType.equals(2)){
            int allHeight = returnLineList.size()* font.getSize() + lineSpace * returnLineList.size() - 1;
            pointY = (height - allHeight)/2 + pointY;
        }else if (verticalType.equals(3)){
            int allHeight = returnLineList.size()* font.getSize() + lineSpace * returnLineList.size() - 1;
            pointY = pointY + height - allHeight;
        }
        int finalPointY = pointY;
        returnLineList.forEach(lineText -> {
            lineText.setPointY(finalPointY + lineText.getPointY());
            lineText.getCharTextList().forEach(charText -> {
                charText.setPointY(lineText.getPointY());
            });
        });
        return returnLineList;

    }

    /**
     * 旋转图片
     * @param bufferedImage
     * @param angle
     * @return
     */
    public static BufferedImage rotateImage(BufferedImage bufferedImage,double angle){
        Rectangle rectangle = getRotateRectangle(bufferedImage.getWidth(),bufferedImage.getHeight(),angle);
        BufferedImage image = new BufferedImage((int) rectangle.getWidth(),(int) rectangle.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.translate((rectangle.getWidth() - bufferedImage.getWidth()) / 2,(rectangle.getHeight() - bufferedImage.getHeight()) / 2);
        graphics2D.rotate(Math.toRadians(angle), BigDecimal.valueOf(bufferedImage.getWidth()/2).doubleValue(),BigDecimal.valueOf(bufferedImage.getHeight()/2).doubleValue());
        graphics2D.drawImage(bufferedImage,null,0,0);
        return image;
    }


    public static Rectangle getRotateRectangle(double width,double height,double angle){
        if (angle >= 90) {
            if((int) angle / 90 % 2 == 1){
                double temp = height;
                height = width;
                width = temp;
            }
            angle = angle % 90;
        }
        double j1Angle = Math.atan(height / width);
        angle = Math.toRadians(angle);
        double r = Math.sqrt(Math.pow(width,2)+Math.pow(height,2)) / 2;
        double newWidth = Math.cos(j1Angle - angle) * r * 2;
        double newHeight = Math.sin(j1Angle + angle) * r * 2;
        newHeight = Math.abs(newHeight);
        newWidth = Math.abs(newWidth);
        return new Rectangle(0,0,(int) newWidth,(int) newHeight);
    }


    /**
     * 解析16进制颜色
     * @param value
     * @return
     */
    public static Color getColor(String value){
        if(!value.startsWith("#")){
            throw new RuntimeException("格式错误");
        }
        return new Color(Integer.parseInt(value.substring(1,3),16),Integer.parseInt(value.substring(3,5),16),Integer.parseInt(value.substring(5,7),16));
    }

    public static Color getColor(String value,Integer alpha){
        if(alpha == null){
            return getColor(value);
        }
        if(!value.startsWith("#")){
            throw new RuntimeException("格式错误");
        }
        return new Color(Integer.parseInt(value.substring(1,3),16),Integer.parseInt(value.substring(3,5),16),Integer.parseInt(value.substring(5,7),16),alpha);
    }


    public static FontMetrics getFontMetrics(Font font){
        return J_LABEL.getFontMetrics(font);
    }

    public static class LineText{

        private Integer pointX;

        private Integer pointY;

        private String text;

        private Integer length;

        private List<CharText> charTextList;

        public LineText(Integer pointX,Integer width,Integer pointY,String text,Integer length,List<CharText> charTextList,Integer type){
            this.text = text;
            this.length = length;
            this.pointY = pointY;
            if(type.equals(1)){
                this.pointX = pointX;
            }else if(type.equals(2)){
                this.pointX = pointX+(width - length)/2;
            }else if(type.equals(3)){
                this.pointX = pointX + width - length;
            }else {
                throw new RuntimeException("不支持的类型");
            }
            charTextList.forEach(charText -> {
                charText.setPointX(this.pointX + charText.getPointX());
            });
            this.charTextList = charTextList;
        }

        public Integer getPointX() {
            return pointX;
        }

        public void setPointX(Integer pointX) {
            this.pointX = pointX;
        }

        public Integer getPointY() {
            return pointY;
        }

        public void setPointY(Integer pointY) {
            this.pointY = pointY;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public List<CharText> getCharTextList() {
            return charTextList;
        }

        public void setCharTextList(List<CharText> charTextList) {
            this.charTextList = charTextList;
        }
    }

    public static class CharText{

        private Integer pointX;

        private Integer pointY;

        private Character value;

        public CharText(Integer pointX,Integer pointY,Character value){
            this.pointX = pointX;
            this.pointY = pointY;
            this.value = value;
        }


        public Integer getPointX() {
            return pointX;
        }

        public void setPointX(Integer pointX) {
            this.pointX = pointX;
        }

        public Integer getPointY() {
            return pointY;
        }

        public void setPointY(Integer pointY) {
            this.pointY = pointY;
        }

        public Character getValue() {
            return value;
        }

        public void setValue(Character value) {
            this.value = value;
        }
    }

    /**
     * 根据url 拉取文件
     * @param url
     * @return
     * @throws Exception
     */
    public static File getFile(String url) throws Exception {
        //对本地文件命名
        String fileName = url.substring(url.lastIndexOf("."),url.length());
        File file = null;

        URL urlfile;
        InputStream inStream = null;
        OutputStream os = null;
        try {
            file = File.createTempFile("fwj_url", fileName);
            //下载
            urlfile = new URL(url);
            inStream = urlfile.openStream();
            os = new FileOutputStream(file);

            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != inStream) {
                    inStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }


    /**
     * 根据url获取图片并转换为multipartFile类型
     * @param url
     * @return
     */
    public static MultipartFile getMultipartFile(String url) {
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File file = getFile(url);
            System.out.println(file.toPath());
            FileItem fileItem = new DiskFileItem("formFieldName",//form表单文件控件的名字随便起
                    Files.probeContentType(file.toPath()),//文件类型
                    false, //是否是表单字段
                    file.getName(),//原始文件名
                    (int) file.length(),//Interger的最大值可以存储两部1G的电影
                    file.getParentFile());//文件会在哪个目录创建
            //为DiskFileItem的OutputStream赋值
            inputStream = new FileInputStream(file);
            outputStream = fileItem.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            return new CommonsMultipartFile(fileItem);
        } catch (Exception e) {
            log.error("文件类型转换失败" + e.getMessage());
            return null;
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }

                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error(">>文件流关闭失败" + e.getMessage());
            }
        }

    }
}

