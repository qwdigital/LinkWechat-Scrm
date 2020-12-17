package com.linkwechat.common.utils.img;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.WebClient;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author ws
 */
public class NetFileUtils {

    private static final Vertx vertx = Vertx.vertx();

    private static final WebClient webClient = WebClient.create(vertx);

    public static void main(String[] args) {
        FileCallable fileCallable = getNetFile("https://pc-index-skin.cdn.bcebos.com/hiphoto/51631423522.jpg?x-bce-process=image/crop,x_0,y_13,w_1999,h_1250");
        getByteArrayOutputStream(fileCallable,true);
    }


    public static FileCallable getNetFile(String urlPath){
        FileCallable fileCallable = new FileCallable();

        webClient.getAbs(urlPath).send().onSuccess(response -> {
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


}
