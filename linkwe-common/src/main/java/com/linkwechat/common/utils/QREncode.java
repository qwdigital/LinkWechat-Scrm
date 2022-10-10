package com.linkwechat.common.utils;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.linkwechat.common.constant.Constants;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class QREncode {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    /**
     * 默认宽度
     */
    private static final Integer WIDTH = 140;
    /**
     * 默认高度
     */
    private static final Integer HEIGHT = 140;

    /**
     * LOGO 默认宽度
     */
    private static final Integer LOGO_WIDTH = 22;
    /**
     * LOGO 默认高度
     */
    private static final Integer LOGO_HEIGHT = 22;

    /**
     * 图片格式
     */
    private static final String IMAGE_FORMAT = "png";


    private QREncode() {
    }


    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    /**
     * 生成带头像的二维码
     *
     * @param content 内容
     * @param logoUrl logo在线地址
     * @return
     */
    public static BufferedImage crateQRCode(String content, String logoUrl) {
        if (StringUtils.isNotBlank(content)) {
            ServletOutputStream stream = null;
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>(4);
            // 指定字符编码为utf-8
            hints.put(EncodeHintType.CHARACTER_SET, Constants.UTF8);
            // 指定二维码的纠错等级为中级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            // 设置图片的边距
            hints.put(EncodeHintType.MARGIN, 2);
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
                BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < WIDTH; x++) {
                    for (int y = 0; y < HEIGHT; y++) {
                        bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                    }
                }
                if (StringUtils.isNotBlank(logoUrl)) {
                    insertLogo(bufferedImage, logoUrl);
                }
                return bufferedImage;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.flush();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 二维码插入logo
     *
     * @param source     二维码
     * @param logoUrl    logo 在线地址
     * @throws Exception
     */
    private static void insertLogo(BufferedImage source, String logoUrl) throws Exception {
        // logo 源可为 File/InputStream/URL
        Image src = ImageIO.read(new URL(logoUrl));
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (WIDTH - LOGO_WIDTH) / 2;
        int y = (HEIGHT - LOGO_HEIGHT) / 2;
        graph.drawImage(src, x, y, LOGO_WIDTH, LOGO_HEIGHT, null);
        Shape shape = new RoundRectangle2D.Float(x, y, LOGO_WIDTH, LOGO_HEIGHT, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成带二维码并生成流文件进行传输
     *
     * @param content 内容
     * @param output  输出流
     * @throws Exception
     */
    public void getQRCode(String content, OutputStream output) throws Exception {
        BufferedImage image = crateQRCode(content, null);
        if (StringUtils.isNotNull(image)) {
            ImageIO.write(image, IMAGE_FORMAT, output);
        }
    }

    /**
     * 生成带logo的二维码并生成流文件进行传输
     *
     * @param content 内容
     * @param logoUrl logo资源
     * @param output  输出流
     * @throws Exception
     */
    public void getQRCode(String content, String logoUrl, OutputStream output) throws Exception {
        BufferedImage image = crateQRCode(content, logoUrl);
        if (StringUtils.isNotNull(image)) {
            ImageIO.write(image, IMAGE_FORMAT, output);
        }
    }

    public static InputStream writeToInputStream(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, IMAGE_FORMAT, os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public static MultipartFile getQRCodeMultipartFile(String content, String logoUrl) throws IOException {
        BufferedImage bufferedImage = crateQRCode(content, logoUrl);
        //读取图片转换为 BufferedImage
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( bufferedImage, "jpg", baos );
        //转换为MultipartFile
        return new MockMultipartFile("groupQrCode","groupQrCode.jpg","text/plain", baos.toByteArray());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void main(String[] args) throws Exception {
        String text = "http://open.weixin.qq.com/qr/code/?username=LinkWeChat2021";
        int width = 300;
        int height = 300;
        String format = "png";
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, Constants.UTF8);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        File outputFile = new File("d:" + File.separator + "qr.png");
        QREncode.writeToFile(bitMatrix, format, outputFile);
    }
}
