package com.linkwechat.common.utils;

import cn.hutool.core.date.DateUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 周相关日期
 */
public class WeekDateUtils {
    public static void main(String[] args) {
        GetCurrentWeekAllDate();
//        GetSomedayWeekAllDate();
    }


    // 获取当前时间周内一周所有时间(key:每周具体日期的下表;value:具体日期)
    public static  Map<Integer,String> GetCurrentWeekAllDate(){

        Map<Integer,String> weekMaps=new HashMap<>();

        // 获取当天的毫秒时间
        long currentTimeMillis = System.currentTimeMillis();

        List<String> weeks = getAllWeekDayDateByMillis(currentTimeMillis);

        for(String week:weeks) {
            weekMaps.put(DateUtil.dayOfWeek(DateUtils.parseDate(week)),week);
        }

        return weekMaps;
    }

    // 获取指定任意日期当周的一星期的日期
    private static void GetSomedayWeekAllDate(){

        // 指定天的毫秒时间
        long currentTimeMillis = getWhatDaySomeDateMillis("2019-10-15");

        List<String> list = getAllWeekDayDateByMillis(currentTimeMillis);

        for(String date:list) {

            System.out.println("date : "+date);
        }
    }

    // someDataStr 格式："yyyy-MM-dd"
    // 返回 时间毫秒
    private static long getWhatDaySomeDateMillis(String someDataStr) {

        Date date= new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(someDataStr);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // 获取指定日期毫秒时间得到 星期几
    private static String getWhatDaySomeday(long timeMillis) {

        Date toDayDate = new Date(timeMillis);
        SimpleDateFormat formatE = new SimpleDateFormat("E");
        String week =null;
        try {
            week = formatE.format(toDayDate);
            System.out.println(week);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return week;
    }

    // 根据 星期几到获取与 星期一 相差几天
    private static int getHowManyDayFromMonday(String someDay) {
        int day = 0;
        switch(someDay) {
            case "星期一":
                day=0;
                break;
            case "星期二":
                day=1;
                break;
            case "星期三":
                day=2;
                break;
            case "星期四":
                day=3;
                break;
            case "星期五":
                day=4;
                break;
            case "星期六":
                day=5;
                break;
            case "星期日":
                day=6;
                break;
            default:
                System.out.println("不存在这样的星期几 : "+ someDay);
                break;
        }

        return day;
    }

    // 获取指定日期毫秒时间的当周一星期的日期
    private static List<String> getAllWeekDayDateByMillis(long timeMills){
        List<String> list = new ArrayList<String>();

        // 得到指定时间是周几
        String week = getWhatDaySomeday(timeMills);
        System.out.println("日期是：" + week);

        // 记录与周一的间隔天数
        int dayFromMonday=getHowManyDayFromMonday(week);
        System.out.println("dayFromMonday : "+ dayFromMonday);

        // 获取这周第一天毫秒值
        long dayMillis = 24*60*60*1000;
        // 获取这周第一天的日子
        long firstOfWeekMillis = timeMills - dayFromMonday * dayMillis;

        // 使用 for 循环得到当前一周的日子（7天的日子）
        for(long i = firstOfWeekMillis;i<firstOfWeekMillis+ 7 *dayMillis;i+=dayMillis) {
            Date targetDate = new Date(i);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String targetDay = format.format(targetDate);
            list.add(targetDay);
        }

        return list;
    }



    public static List<Date> dateToWeek(Date mdate) {

        int b = mdate.getDay();

        Date fdate;

        List<Date> list = new ArrayList<Date>();

        Long fTime = mdate.getTime() - b * 24 * 3600000;

        for (int a = 1; a <= 7; a++) {

            fdate = new Date();

            fdate.setTime(fTime + (a * 24 * 3600000));

            list.add(a-1, fdate);

        }

        return list;

    }


}
