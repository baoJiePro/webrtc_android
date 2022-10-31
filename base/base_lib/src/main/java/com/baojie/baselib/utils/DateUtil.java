package com.baojie.baselib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class DateUtil {

    public static final String SIMPLE_DATE = "yyyy-MM-dd";

    public static final SimpleDateFormat datetimeFormatWithD = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat mDatetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat dateFormat_ = new SimpleDateFormat("yyyy年MM月dd日");

    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("yy-MM-dd");

    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private static final SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm");

    private static final SimpleDateFormat mDataFormat_1 = new SimpleDateFormat("MM月dd日HH:mm");

    private static final int ChinaTimeZone = 8;

    private static void setChinaTimeZone() {
        datetimeFormatWithD.setTimeZone(getChinaTimeZone());
        datetimeFormat.setTimeZone(getChinaTimeZone());
        mDatetimeFormat.setTimeZone(getChinaTimeZone());
        dateFormat.setTimeZone(getChinaTimeZone());
        dateFormat_.setTimeZone(getChinaTimeZone());
        mDateFormat.setTimeZone(getChinaTimeZone());
        timeFormat.setTimeZone(getChinaTimeZone());
        mTimeFormat.setTimeZone(getChinaTimeZone());
        mDataFormat_1.setTimeZone(getChinaTimeZone());
    }

    /**
     * 根据日期获取时间戳，yyyy-MM-dd
     *
     * @param time
     * @return
     */
    public static long getTime(String time) {
        setChinaTimeZone();
        if (time.equals(""))
            return 0;
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 根据字符串获取时间戳，格式自定义
     *
     * @param dateFormat 格式
     * @param time
     * @return
     */
    public static long getTime(SimpleDateFormat dateFormat, String time) {
        dateFormat.setTimeZone(getChinaTimeZone());
        if (time.equals(""))
            return 0;
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 根据时间戳判断是否是今天
     *
     * @param time
     * @return
     */
    public static boolean isToday(Long time) {
        setChinaTimeZone();
        Calendar cal = Calendar.getInstance();
        int thisDay = cal.get(Calendar.DAY_OF_MONTH);
        int thisYear = cal.get(Calendar.YEAR);
        int thisMonth = cal.get(Calendar.MONTH);
        cal.setTimeInMillis(time);
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH);

        if (thisYear == currentYear && thisMonth == currentMonth && thisDay == currentDay) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取在当前月中某一天
     *
     * @param time
     * @return
     */
    public static int getDay(Long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);

        return currentDay;
    }

    /**
     * 获取在当前年中某个月
     *
     * @param time
     * @return
     */
    public static int getMonth(Long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int currentMonth = cal.get(Calendar.MONTH);

        return currentMonth + 1;
    }

    /**
     * 获取年份
     *
     * @param time
     * @return
     */
    public static int getYear(Long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int currentYear = cal.get(Calendar.YEAR);

        return currentYear;
    }

    /**
     * 日期是否相等
     *
     * @param sDate
     * @param dDate
     * @return
     */
    private static boolean equlsDate(Date sDate, Date dDate) {
        if (sDate.getYear() == dDate.getYear() && sDate.getMonth() == dDate.getMonth()
                && sDate.getDate() == dDate.getDate()) {
            return true;
        }
        return false;
    }

    /**
     * 获取时分秒
     *
     * @param mills
     * @return
     */
    public static String getHMSWith00(long mills) {
        long s = mills / 1000;
        long m = s / 60;
        long h = m / 60;
        String str;
        if (h == 0 && m == 0 && s == 0) {
            str = "00:00:00";
            return str;
        }
        if (h == 0 && m == 0) {
            str = String.format("00:00:%02d", s);
            return str;
        }
        if (h == 0) {
            str = String.format("00:%02d:%02d", m, s % 60);
            // str = "00:" + m + ":" + s % 60 + "";
            return str;
        } else {
            str = String.format("%02d:%02d:%02d", h, m % 60, s % 60);
            return str;
        }
    }

    /**
     * 获得当前日期时间
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String currentDatetime() {
        setChinaTimeZone();
        return datetimeFormat.format(now());
    }

    /**
     * 获得当前日期时间
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static String mformatDatetime() {
        setChinaTimeZone();
        return mDatetimeFormat.format(now());
    }

    /**
     * 格式化日期时间
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formatDatetime(Date date) {
        setChinaTimeZone();
        return datetimeFormat.format(date);
    }

    /**
     * @Title: formatAdd23h59m59m
     * @Description: 为当前的时间加23小时59分59s
     * @param: @param date
     * @param: @return 设定文件
     * @return: String 返回类型
     * @date: 2014-12-5 上午10:40:29
     */
    public static String formatAdd23h59m59s(long date) {
        long time = 24 * 60 * 60 * 1000 - 1000 + date;
        return time + "";
    }

    /**
     * @Title: formatAdd23h59m59m
     * @Description: 为当前的时间加23小时59分59s
     * @param: @param date
     * @param: @return 设定文件
     * @return: String 返回类型
     * @date: 2014-12-5 上午10:40:29
     */
    public static long formatAdd23h59m59sLong(long date) {
        long time = 24 * 60 * 60 * 1000 - 1000 + date;
        return time;
    }

    /**
     * @param timestamp
     * @return
     */
    public static String formatDatetime(Long timestamp) {
        return formatDatetime(new Date(timestamp));
    }

    /**
     * 格式化日期时间
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static String mformatDatetime(Date date) {
        setChinaTimeZone();
        return mDatetimeFormat.format(date);
    }

    private static TimeZone getChinaTimeZone() {
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(ChinaTimeZone * 60 * 60 * 1000);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(ChinaTimeZone * 60 * 60 * 1000, ids[0]);
        }

        return timeZone;
    }

    /**
     * 格式化日期时间
     *
     * @param date
     * @param pattern 格式化模式，详见{@link SimpleDateFormat}构造器
     *                <code>SimpleDateFormat(String pattern)</code>
     * @return
     */
    public static String formatDatetime(Date date, String pattern) {
        SimpleDateFormat customFormat = (SimpleDateFormat) datetimeFormat.clone();
        customFormat.applyPattern(pattern);
        customFormat.setTimeZone(getChinaTimeZone());
        return customFormat.format(date);
    }

    public static String formatDatetime(Long timestamp, String pattern) {
        return formatDatetime(new Date(timestamp), pattern);
    }

    /**
     * 获得当前日期
     * <p>
     * 日期格式yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrentDate() {
        setChinaTimeZone();
        return dateFormat.format(now());
    }

    /**
     * 格式化日期
     * <p>
     * 日期格式yyyy-MM-dd
     *
     * @return
     */
    public static String formatDate(Date date) {
        setChinaTimeZone();
        return dateFormat.format(date);
    }

    public static String formatDate(Long timestamp) {
        return formatDate(new Date(timestamp));
    }

    /**
     * 格式化日期
     * <p>
     * 日期格式yy-MM-dd
     *
     * @return
     */
    public static String mformatDate(Date date) {
        setChinaTimeZone();
        return mDateFormat.format(date);
    }

    /**
     * 获得当前时间
     * <p>
     * 时间格式HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime() {
        setChinaTimeZone();
        return timeFormat.format(now());
    }

    /**
     * 获得当前时间
     * <p>
     * 时间格式HH:mm
     *
     * @return
     */
    public static String mCurrentTime() {
        setChinaTimeZone();
        return mTimeFormat.format(now());
    }

    /**
     * 格式化时间
     * <p>
     * 时间格式HH:mm:ss
     *
     * @return
     */
    public static String formatTime(Date date) {
        setChinaTimeZone();
        return timeFormat.format(date);
    }

    /**
     * 格式化时间
     * <p>
     * 时间格式HH:mm
     *
     * @return
     */
    public static String mformatTime(Date date) {
        setChinaTimeZone();
        return mTimeFormat.format(date);
    }

    /**
     * 获得当前时间的<code>java.util.Date</code>对象
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    public static Calendar calendar() {
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        cal.setFirstDayOfWeek(Calendar.MONDAY);

        return cal;
    }

    public static String nextDay(int i) {
        Date date = now();
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_YEAR, +i);
        Date date2 = cal.getTime();
        return formatDatetime(date2);
    }

    public static Long getDayFromLeftTime(Long leftTime) {
        return (leftTime / (24 * 60 * 60 * 1000));
    }

    public static Long getHourFromLeftTime(Long leftTime) {
        return (leftTime % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
    }

    public static Long getMinuteFromLeftTime(Long leftTime) {
        return (leftTime % (60 * 60 * 1000)) / (60 * 1000);
    }

    public static Long getSecondFromLeftTime(Long leftTime) {
        return (leftTime % (60 * 1000)) / (1000);
    }

    public static String getFromNowTime(Long timestamp) {
        Long now = new Date().getTime();
        Long timeDiff = now - timestamp;
        if (timeDiff < 60 * 60 * 1000) {
            return (timeDiff / (60 * 1000)) + "分钟前";
        } else if (timeDiff < 24 * 60 * 60 * 1000) {
            return (timeDiff / (60 * 60 * 1000)) + "小时前";
        } else {
            return (timeDiff / (24 * 60 * 60 * 1000)) + "天前";
        }
    }

    public static String getFromNowTime(Long timestamp, Long nowTime) {
        Long timeDiff = nowTime - timestamp;
        if (timeDiff < 60 * 60 * 1000) {
            return (timeDiff / (60 * 1000)) + "分钟前";
        } else if (timeDiff < 24 * 60 * 60 * 1000) {
            return (timeDiff / (60 * 60 * 1000)) + "小时前";
        } else {
            return (timeDiff / (24 * 60 * 60 * 1000)) + "天前";
        }
    }

    /**
     * 获得当前时间的毫秒数
     * <p>
     * 详见{@link System#currentTimeMillis()}
     *
     * @return
     */
    public static long millis() {
        return System.currentTimeMillis();
    }

    /**
     * 获得当前Chinese月份
     *
     * @return
     */
    public static int month() {
        return calendar().get(Calendar.MONTH) + 1;
    }

    /**
     * 获得月份中的第几天
     *
     * @return
     */
    public static int dayOfMonth() {
        return calendar().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 今天是星期的第几天
     *
     * @return
     */
    public static int dayOfWeek() {
        return calendar().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 今天是年中的第几天
     *
     * @return
     */
    public static int dayOfYear() {
        return calendar().get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 判断原日期是否在目标日期之前
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean isBefore(Date src, Date dst) {
        return src.before(dst);
    }

    /**
     * 判断原日期是否在目标日期之后
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean isAfter(Date src, Date dst) {
        return src.after(dst);
    }

    /**
     * 判断两日期是否相同
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isEqual(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }

    /**
     * 判断某个日期是否在某个日期范围
     *
     * @param beginDate 日期范围开始
     * @param endDate   日期范围结束
     * @param src       需要判断的日期
     * @return
     */
    public static boolean between(Date beginDate, Date endDate, Date src) {
        return beginDate.before(src) && endDate.after(src);
    }

    /**
     * 获得当前月的最后一天
     * <p>
     * HH:mm:ss为0，毫秒为999
     *
     * @return
     */
    public static Date lastDayOfMonth() {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_MONTH, 0); // M月置零
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);// 月份+1
        cal.set(Calendar.MILLISECOND, -1);// 毫秒-1
        return cal.getTime();
    }

    /**
     * 获得当前月的第一天
     * <p>
     * HH:mm:ss SS为零
     *
     * @return
     */
    public static Date firstDayOfMonth() {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_MONTH, 1); // M月置1
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        return cal.getTime();
    }

    private static Date weekDay(int week) {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_WEEK, week);
        return cal.getTime();
    }

    /**
     * 获得周五日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    public static Date friday() {
        return weekDay(Calendar.FRIDAY);
    }

    /**
     * 获得周六日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    public static Date saturday() {
        return weekDay(Calendar.SATURDAY);
    }

    /**
     * 获得周日日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    public static Date sunday() {
        return weekDay(Calendar.SUNDAY);
    }

    /**
     * 将字符串日期时间转换成java.util.Date类型
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @param datetime
     * @return
     */
    public static Date parseDatetime(String datetime) throws ParseException {
        setChinaTimeZone();
        return datetimeFormat.parse(datetime);
    }

    /**
     * 将字符串日期转换成java.util.Date类型
     * <p>
     * 日期时间格式yyyy-MM-dd
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        setChinaTimeZone();
        return dateFormat.parse(date);
    }

    /**
     * 将字符串日期转换成java.util.Date类型
     * <p>
     * 时间格式 HH:mm:ss
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date parseTime(String time) throws ParseException {
        setChinaTimeZone();
        return timeFormat.parse(time);
    }

    /**
     * 根据自定义pattern将字符串日期转换成java.util.Date类型
     *
     * @param datetime
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseDatetime(String datetime, String pattern) throws ParseException {
        setChinaTimeZone();
        SimpleDateFormat format = (SimpleDateFormat) datetimeFormat.clone();
        format.applyPattern(pattern);
        return format.parse(datetime);
    }

    /**
     * 获取当前系统时间
     * <p>
     * 自定义格式
     *
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static String currDate(String pattern) throws ParseException {
        return formatDatetime(new Date(), pattern);
    }

    /**
     * 获取当前系统时间
     * <p>
     * 简单格式 yyyy-MM-dd
     *
     * @return
     * @throws ParseException
     */
    public static String currDate() throws ParseException {
        return formatDatetime(new Date(), SIMPLE_DATE);
    }

    /**
     * @param @return
     * @param @throws ParseException
     * @return String
     * @throws
     * @Title:TomorrowDate
     * @Description:获取明天的日期
     */
    public static String TomorrowDate() throws ParseException {
        setChinaTimeZone();
        Date date = new Date();// 取时间
        Calendar calendar = GregorianCalendar.getInstance(Locale.CHINESE);
        calendar.setTime(date);
        calendar.add(GregorianCalendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        return dateFormat.format(date);
    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title:getMondayOFWeek
     * @Description:获取本周周一，第一天
     */
    public static String getMondayOFWeek() {
        int mondayPlus = getMondayPlus();
        Calendar calendar = GregorianCalendar.getInstance(Locale.CHINESE);
        calendar.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = calendar.getTime();

        String preMonday = formatDate(monday);
        return preMonday;
    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title:getSundayOFWeek
     * @Description:获取本周周日，最后一天
     */
    public static String getSundayOFWeek() {
        int mondayPlus = getMondayPlus();
        Calendar calendar = GregorianCalendar.getInstance(Locale.CHINESE);
        calendar.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date sunday = calendar.getTime();

        String preMonday = formatDate(sunday);
        return preMonday;
    }

    /**
     * @param @return
     * @return int
     * @throws
     * @Title:getMondayPlus
     * @Description:获取周一与今天差值
     */
    private static int getMondayPlus() {
        Calendar calendar = GregorianCalendar.getInstance(Locale.CHINESE);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return 1 - dayOfWeek;
    }

    public static long getTimesmorning() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.set(Calendar.DAY_OF_YEAR, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long log = calendar.getTimeInMillis();
        return (calendar.getTimeInMillis());
    }

    public static long getTimeStemp(String time) {

        Date date;
        long timeStemp = 0L;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            simpleDateFormat.setTimeZone(getChinaTimeZone());
            date = simpleDateFormat.parse(time);
            timeStemp = (long) date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeStemp;
    }

    public static String getSimpleFormatStr(Date date) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sf1.setTimeZone(getChinaTimeZone());
        try {
            return sf1.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFormatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        df.setTimeZone(getChinaTimeZone());
        return df.format(date);
    }

    public static String getFormatDateInChinese(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        df.setTimeZone(getChinaTimeZone());
        return df.format(date);
    }

    public static String getFormatDateyyyyMMdd(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(getChinaTimeZone());
        return df.format(date);
    }

    public static Date getFormatDateTime(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(getChinaTimeZone());
        Date newdate = null;
        try {
            newdate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newdate;
    }

    public static Date getFormatDateNoMds(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(getChinaTimeZone());
        Date newdate = null;
        try {
            newdate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newdate;
    }

    public static String getFormatDateStrYYR(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(getChinaTimeZone());
        return df.format(date);
    }

    public static String getCreateAt(Date date) {
        Calendar c = Calendar.getInstance();
        if (c.get(Calendar.YEAR) - (date.getYear() + 1900) > 0) {
            // int i = c.get(Calendar.YEAR)-date.getYear();
            // return i+"年前";
            return DateUtil.getFormatDateStr(date);
        } else if (c.get(Calendar.MONTH) - date.getMonth() > 0) {
            // int i = c.get(Calendar.MONTH)-date.getMonth();
            // return i+"月前";
            return DateUtil.getFormatDateStr(date);
        } else if (c.get(Calendar.DAY_OF_MONTH) - date.getDate() > 0) {
            int i = c.get(Calendar.DAY_OF_MONTH) - date.getDate();
            return i + "天前";
        } else if (c.get(Calendar.HOUR_OF_DAY) - date.getHours() > 0) {
            int i = c.get(Calendar.HOUR_OF_DAY) - date.getHours();
            return i + "小时前";
        } else if (c.get(Calendar.MINUTE) - date.getMinutes() > 0) {
            int i = c.get(Calendar.MINUTE) - date.getMinutes();
            return i + "分钟前";
        } else if (c.get(Calendar.SECOND) - date.getSeconds() > 0) {
            int i = c.get(Calendar.SECOND) - date.getSeconds();
            return i + "秒前";
        } else {
            return "刚刚";
        }
    }

    /**
     * 获取消息显示时间
     *
     * @param millis
     * @return
     */
    public static String getMessageTime(Long millis) {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTimeInMillis(Long.valueOf(millis));
        Calendar currentCalendar = Calendar.getInstance();
        int intervalDay = currentCalendar.get(Calendar.DAY_OF_MONTH) - calendarDate.get(Calendar.DAY_OF_MONTH);
        if (currentCalendar.get(Calendar.YEAR) - calendarDate.get(Calendar.YEAR) > 0) {
            return formatDatetime(millis, "yyyy-MM-dd HH:mm");
        } else if (currentCalendar.get(Calendar.MONTH) - calendarDate.get(Calendar.MONTH) > 0) {
            return formatDatetime(millis, "MM-dd HH:mm");
        } else if (intervalDay > 1) {
            return formatDatetime(millis, "MM-dd HH:mm");
        } else if (intervalDay > 0) {
            return formatDatetime(millis, "MM-dd HH:mm");
        } else {
            return formatDatetime(millis, "HH:mm");
        }
    }

    /**
     * 获取是上午还是下午，晚上
     *
     * @return
     */
    public static String getTimeQuantum() {
        String AM_END_POINT = "12:00";
        String PM_END_POINT = "18:00";
        String currentTime = formatDatetime(System.currentTimeMillis(), "HH:mm");
        if (currentTime.compareTo(AM_END_POINT) < 0) {
            return "上午好，";
        } else if (currentTime.compareTo(PM_END_POINT) < 0) {
            return "下午好，";
        } else {
            return "晚上好，";
        }

    }

    public static String getFormatDateStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        df.setTimeZone(getChinaTimeZone());
        return df.format(date);
    }
}
