package com.linkwechat.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
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

    public static Long getBeforeByDayLongTime(int day){
        LocalDateTime now = LocalDateTime.now();
        now = now.minus(-day, ChronoUnit.DAYS);
        return now.toEpochSecond(ZoneOffset.of("+8"));
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
     * 日期范围 - 切片
     *
     *
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @param type 切片类型 1年  2月  3日  4小时
     * @return          切片日期
     */
    public static List<String> sliceUpDateRange(String startDate, String endDate ,int type) {
        List<String> rs = new ArrayList<>();

        try {
            int dt = Calendar.DATE;
            String pattern = "yyyy-MM-dd";
            if (type==1) {
                pattern = "yyyy";
                dt = Calendar.YEAR;
            } else if (type==2) {
                pattern = "yyyy-MM";
                dt = Calendar.MONTH;
            } else if (type==3) {
                pattern = "yyyy-MM-dd";
                dt = Calendar.DATE;
            }else if (type==4) {
                pattern = "yyyy-MM-dd HH";
                dt = Calendar.HOUR_OF_DAY;
            }
            SimpleDateFormat sd = new SimpleDateFormat(pattern);
            Calendar sc = Calendar.getInstance();
            Calendar ec = Calendar.getInstance();
            sc.setTime(sd.parse(startDate));
            ec.setTime(sd.parse(endDate));
            while (sc.compareTo(ec) < 1) {
                rs.add(sd.format(sc.getTime()));
                sc.add(dt, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**判断是否超过多少小时 如：24
     *
     * @param tableTime 业务时间
     * @param hour 多少小时
     * @return boolean true未超过 false超过
     * @throws Exception
     */
    public static boolean judgmentDate(String tableTime, Integer hour) throws Exception {
        String currentTime = dateTimeNow(YYYY_MM_DD_HH_MM_SS);//当前时间

        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);

        Date start = sdf.parse(tableTime);//业务时间

        Date end = sdf.parse(currentTime);//当前时间

        long cha = end.getTime() - start.getTime();

        if (cha < 0) {

            return false;
        }

        double result = cha * 1.0 / (1000 * 60 * 60);

        if (result <= hour) {

            return true;//是小于等于 hour 小时

        } else {

            return false;

        }

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
     * 当前时间向前推n分钟
     * @param minute
     * @return
     */
    public static  String getBeforeByMinute(int minute){

        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -minute);// n分钟之前的时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(beforeTime.getTime());
    }


    /**
     * 获取二个日期见相差多少天
     * @param startdate
     * @param enddate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String startdate,String enddate) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat(YYYY_MM_DD);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(startdate));
        long start = calendar.getTimeInMillis();
        calendar.setTime(sdf.parse(enddate));
        long end = calendar.getTimeInMillis();
        long betweendays=(end-start)/(1000*3600*24);
        int days = Integer.parseInt(String.valueOf(betweendays));
        return days;
    }


    public static String initSqlBeginTime(String str){
        if(ObjectUtil.isEmpty(str)){
            str = dateTimeNow(YYYY_MM_DD_HH_MM_SS);
        }
        return str + " 00:00:00";
    }

    public static String initSqlEndTime(String str){
        if(ObjectUtil.isEmpty(str)){
            str = dateTimeNow(YYYY_MM_DD_HH_MM_SS);
        }
        return str + " 59:59:59";
    }


    /**
     *  获得指定时间的前几天或后几天是哪一天
     * @param oneday
     * @param amount n
     * @return
     */
    public static Date daysAgoOrAfter(Date oneday,int amount){
        Calendar mon = Calendar.getInstance();
        mon.setTime(oneday);
        mon.add(Calendar.DATE,amount);
        return mon.getTime();
    }


    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 秒转化为:n小时k分这种
     * @param diff
     * @return
     */
    public static String ShowTimeInterval(long diff) {


        if(diff<60){
            return diff+"秒";
        }


        long minutes = diff / 60;
        long min = minutes % 60;
        long hou = minutes / 60;

        return hou+"小时"+min+"分";
    }

    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
//        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
//        if(milliSecond > 0) {
//            sb.append(milliSecond+"毫秒");
//        }
        return sb.toString();
    }

    public static String getYesterday(String format){

        // 获取当前日期
        LocalDate today = LocalDate.now();

        // 计算昨天的日期
        LocalDate yesterday = today.minus(1, ChronoUnit.DAYS);

        // 创建日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        // 将昨天的日期转换为字符串
        return yesterday.format(formatter);
    }


    /**
     * 获取二个间的秒数
     * @param startTime
     * @param endTime
     * @return
     */
    public static long timeDifference(Date startTime,Date endTime){

        // 计算两个时间点之间的秒数
        Duration duration = Duration.between(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()));
       return duration.getSeconds();
    }





}
