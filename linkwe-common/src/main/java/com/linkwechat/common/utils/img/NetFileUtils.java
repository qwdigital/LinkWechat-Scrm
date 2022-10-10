package com.linkwechat.common.utils.img;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.WebClient;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author ws
 */
public class NetFileUtils {

    private static final Vertx vertx = Vertx.vertx();

    private static final WebClient webClient = WebClient.create(vertx);

    public static FileCallable getNetFile(String urlPath){
        FileCallable fileCallable = new FileCallable();

        webClient.getAbs(urlPath).timeout(1000L*60L*10L).send().onSuccess(response -> {
            if(response.statusCode() == 200 || response.statusCode() == 302){
                Buffer buffer = response.body();
                buffer.getByteBuf();
                byte[] bytes = buffer.getBytes();
                fileCallable.setFile(bytes);
            }else {
                fileCallable.error();
            }
        }).onFailure(throwable -> {
            fileCallable.error();
            throwable.printStackTrace();
        });
        return fileCallable;


    }

    public static ByteArrayOutputStream getByteArrayOutputStream(FileCallable fileCallable,boolean clearBuf){
        try {
            ByteBuf byteBuf = fileCallable.call();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int byteLength = 1024;
            byte[] bytes = new byte[byteLength];
            int length = 0;
            while ((length = byteBuf.readableBytes()) > 0){
                byteBuf.readBytes(bytes, 0, Math.min(length, byteLength));
                byteArrayOutputStream.write(bytes,0, Math.min(length, byteLength));
            }
            if(!clearBuf){
                byteBuf.resetReaderIndex();
            }else {
                byteBuf.clear();
            }
            return byteArrayOutputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static class FileCallable implements Callable<ByteBuf>{

        private final CountDownLatch countDownLatch = new CountDownLatch(1);

        private volatile ByteBuf byteBuf;

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        public ByteBuf call() throws Exception {
            boolean k = countDownLatch.await(10,TimeUnit.MINUTES);
            if(k && byteBuf != null){
                return byteBuf;
            }else {
                throw new RuntimeException("获取失败");
            }

        }

        public void setFile(byte[] bytes){
            ByteBuf byteBuf = Unpooled.directBuffer();
            byteBuf.writeBytes(bytes);
            this.byteBuf = byteBuf;
            countDownLatch.countDown();
        }

        public void error(){
            countDownLatch.countDown();
        }
    }

    public static class StreamMultipartFile implements MultipartFile{

        private final String filename;

        private final byte[] bytes;

        public StreamMultipartFile(String filename,byte[] bytes){
            this.filename = filename;
            this.bytes = bytes;
        }

        @Override
        public String getName() {
            return filename;
        }

        @Override
        public String getOriginalFilename() {
            return filename;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return bytes==null || bytes.length == 0;
        }

        @Override
        public long getSize() {
            return bytes.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return this.bytes;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(bytes);
        }

        @Override
        public void transferTo(File file) throws IOException, IllegalStateException {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            FileChannel fileChannel = fileOutputStream.getChannel();
            ReadableByteChannel channel = Channels.newChannel(new ByteArrayInputStream(bytes));
            fileChannel.transferFrom(channel,0,bytes.length);
            fileChannel.close();
            channel.close();
            fileOutputStream.close();
        }
    }

}
