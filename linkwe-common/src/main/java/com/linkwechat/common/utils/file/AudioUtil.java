package com.linkwechat.common.utils.file;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

public class AudioUtil {



    /**
     * 获取语音文件播放时长(秒) 支持wav 格式
     * @param filePath
     * @return
     */
    public static Float getDuration(String filePath){
        try{

            File destFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(destFile);
            AudioFormat format = audioInputStream.getFormat();
            long audioFileLength = destFile.length();
            int frameSize = format.getFrameSize();
            float frameRate = format.getFrameRate();
            float durationInSeconds = (audioFileLength / (frameSize * frameRate));
            return durationInSeconds;

        }catch (Exception e){
            e.printStackTrace();
            return 0f;
        }

    }

    /**
     * 获取mp3语音文件播放时长(秒) mp3
     * @param filePath
     * @return
     */
    public static Float getMp3Duration(String filePath){

        try {
            File mp3File = new File(filePath);
            MP3File f = (MP3File) AudioFileIO.read(mp3File);
            MP3AudioHeader audioHeader = (MP3AudioHeader)f.getAudioHeader();
            return Float.parseFloat(audioHeader.getTrackLength()+"");
        } catch(Exception e) {
            e.printStackTrace();
            return 0f;
        }
    }


    /**
     * 获取mp3语音文件播放时长(秒)
     * @param mp3File
     * @return
     */
    public static Float getMp3Duration(File mp3File){

        try {
            //File mp3File = new File(filePath);
            MP3File f = (MP3File) AudioFileIO.read(mp3File);
            MP3AudioHeader audioHeader = (MP3AudioHeader)f.getAudioHeader();
            return Float.parseFloat(audioHeader.getTrackLength()+"");
        } catch(Exception e) {
            e.printStackTrace();
            return 0f;
        }
    }


    /**
     * 得到pcm文件的毫秒数
     *
     * pcm文件音频时长计算
     * 同图像bmp文件一样，pcm文件保存的是未压缩的音频信息。 16bits 编码是指，每次采样的音频信息用2个字节保存。可以对比下bmp文件用分别用2个字节保存RGB颜色的信息。 16000采样率 是指 1秒钟采样 16000次。常见的音频是44100HZ，即一秒采样44100次。 单声道： 只有一个声道。
     *
     * 根据这些信息，我们可以计算： 1秒的16000采样率音频文件大小是 2*16000 = 32000字节 ，约为32K 1秒的8000采样率音频文件大小是 2*8000 = 16000字节 ，约为 16K
     *
     * 如果已知录音时长，可以根据文件的大小计算采样率是否正常。
     * @param filePath
     * @return
     */
    public static long getPCMDurationMilliSecond(String filePath) {
        File file = new File(filePath);

        //得到多少秒
        long second = file.length() / 32000 ;

        long milliSecond = Math.round((file.length() % 32000)   / 32000.0  * 1000 ) ;

        return second * 1000 + milliSecond;
    }
}

