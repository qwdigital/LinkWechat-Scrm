package com.linkwechat.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 计算两个时间差
     */
    public static long diffTime(Date endDate, Date nowDate){
      return endDate.getTime() - nowDate.getTime();
    }

    /**
     * 获取时间段内所有日期
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<Date> findDates(Date dBegin, Date dEnd)
    {
        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime()))
        {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }

    public static long getMillionSceondsBydate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        long millionSeconds = sdf.parse(date).getTime();//毫秒
        return millionSeconds;
    }




    public static  int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一
            }else{
                age--;//当前月份在生日之前，年龄减一
            } }
        return age;

    }


    /**
     * 获取当前时间的时间戳
     */
    public static int getCurrentTimeIntValue() {
        return (int) (System.currentTimeMillis() / 1000);
    }


    /**
     * 获取days天后的当前时间 时间戳
     */
    public static int addDaysTimeStamp(int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, days);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * 获取今日零点的时间戳
     */
    public static int getStartTimeStamp() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        long time = todayStart.getTimeInMillis()/1000;
        return (int)time;
    }

    /**
     * 获取今日23：59：59的时间戳
     */
    public static int getEndTimeStamp() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        long time = todayEnd.getTimeInMillis() / 1000;
        return (int) time;
    }

    /**
     * 获取指定时间零点的时间戳
     */
    public static int getStartTimeStamp(Date date) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(date);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        long time = todayStart.getTimeInMillis()/1000;
        return (int)time;
    }

    /**
     * 获取指定时间23：59：59的时间戳
     */
    public static int getEndTimeStamp(Date date) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(date);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        long time = todayEnd.getTimeInMillis() / 1000;
        return (int) time;
    }

    /**
     * 获取昨天零点的时间（字符串）
     */
    public static String getYesterDayStartTimeStamp() {
        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND,0); //这是将【秒】设置为0
        calendar.set(Calendar.MINUTE,0); //这是将【分】设置为0
        calendar.set(Calendar.HOUR_OF_DAY,0); //这是将【时】设置为0
        calendar.add(Calendar.DATE,-1); //当前日期加一
        String yesterday  = sdfYMD.format(calendar.getTime()); //获取昨天的时间 如2021-02-25 00:00:00
        return yesterday;
    }

    /**
     * 获取昨天零点的时间戳
     */
    public static Integer getBeforeStartTime(){
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        long time = todayStart.getTimeInMillis()/1000;
        return (int)time-86400;
    }

    /**
     * 获取昨天23:59:59的时间（字符串）
     */
    public static String getYesterDayEndTimeStamp() {
        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND,59); //这是将当天的【秒】设置为0
        calendar.set(Calendar.MINUTE,59); //这是将当天的【分】设置为0
        calendar.set(Calendar.HOUR_OF_DAY,23); //这是将当天的【时】设置为0
        calendar.add(Calendar.DATE,-1); //当前日期加一
        String yesterday  = sdfYMD.format(calendar.getTime()); //获取第二天的时间 2021-02-25 00:00:00
        return yesterday;
    }



    /**
     * 获取明天零点的时间戳
     */
    public static Integer getAfterStartime()
    {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        long time = todayStart.getTimeInMillis()/1000;
        return (int)time+86400;
    }

    /**
     * 将昨天凌晨时间转换为Date类型（下面设置成这样 new simpleDateFormat("yyyy-MM-dd HH:mm:ss") 数据中对应的字段类型就是DateTime）　　　* 这个方法中调用了上面的getYesterDayStartTimeStamp()方法哦
     */
    public static Date getDateTime()
    {
        Date dateTime = null;
        String yesterDayStartTimeStamp =  getYesterDayStartTimeStamp();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            dateTime = formatter.parse(yesterDayStartTimeStamp);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        //Sun May 09 00:00:00 CST 2021
        return dateTime;
    }


    /**
     * 当前时间向推几小时
     * @param ihour 小时
     * @return String
     */
    public static String getBeforeByHourTime(int ihour){
        String returnstr = "";
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        returnstr = df.format(calendar.getTime());
        return returnstr;
    }


    /**
     * 当前时间向推N天
     * @param day 小时
     * @return String
     */
    public static String getBeforeByDayTime(int day){
        LocalDateTime now = LocalDateTime.now();
        now = now.minus(-day, ChronoUnit.DAYS);


        return now.toString();
    }

    /**
     * 当前时间向推N天
     * 获取时间戳
     * @param day 小时
     * @return String
     */
    public static Long getBeforeByDayLongTime(int day){
        LocalDateTime now = LocalDateTime.now();
        now = now.minus(-day, ChronoUnit.DAYS);


        return now.toEpochSecond(ZoneOffset.of("+8"));
    }
}
