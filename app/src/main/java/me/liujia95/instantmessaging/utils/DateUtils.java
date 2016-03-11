package me.liujia95.instantmessaging.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/2/29 10:48.
 */
public class DateUtils {
    private static Calendar mCalendar = Calendar.getInstance();

    public static final long FIVE_MINUTE = 5 * 60 * 1000; //5分钟

    /**
     * 昨天：前一天的00：00 到 前一天 23：59之间
     * TODO:判断年份
     *
     * @param millis
     * @return
     */
    public static String getDateFormatToChatList(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        String date = null;

        if (isYesterday(millis)) {
            LogUtils.d("YESTERDAY");
            date = "昨天";
        } else if (millis > getYesterdayMaxTimeMillis()) {
            //如果时间大于昨天的最大值，就是今天
            LogUtils.d("TODAY");
            date = new SimpleDateFormat("HH:mm").format(millis);
        } else {
            LogUtils.d("LATER");
            //其余就是更早的时候，输出月份
            date = new SimpleDateFormat("MM-dd").format(calendar.getTime());
        }

        LogUtils.d("date:" + date);
        return date;
    }

    public static String getDateFormatToChatting(long millis) {
        Calendar canlendar = Calendar.getInstance();
        canlendar.setTimeInMillis(millis);

        String date = null;
        if (isYesterday(millis)) {
            date = "昨天 " + new SimpleDateFormat("HH:mm").format(millis);
        } else if (millis > getYesterdayMaxTimeMillis()) {
            date = new SimpleDateFormat("HH:mm").format(millis);
        } else if (millis > getWeekMinTimeMillis()) {
            //如果大于这周最小值
            date = getWeekday(millis) +" "+ new SimpleDateFormat("HH:mm").format(millis);
        } else {
            date = canlendar.get(Calendar.MONTH) + "月" + canlendar.get(Calendar.DAY_OF_MONTH) + "日 " + getEarlyOrNight(canlendar) + new SimpleDateFormat("HH:mm").format(millis);
        }
        return date;
    }

    /**
     * 获取早中晚
     *
     * @param canlendar
     * @return
     */
    public static String getEarlyOrNight(Calendar canlendar) {
        int hourOfDay = canlendar.get(Calendar.HOUR_OF_DAY);
        String s = null;
        if (hourOfDay < 6) {
            s = "凌晨";
        } else if (hourOfDay < 12) {
            s = "早上";
        } else if (hourOfDay < 18) {
            s = "下午";
        } else if (hourOfDay < 24) {
            s = "晚上";
        }
        return s;
    }

    /**
     * 获取昨天时间的最大值
     *
     * @return
     */
    public static long getYesterdayMaxTimeMillis() {
        long currTime = System.currentTimeMillis();
        mCalendar.setTime(new Date(currTime));

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        day -= 1;

        mCalendar.set(year, month, day, 23, 59, 59);

        return mCalendar.getTimeInMillis();
    }

    /**
     * 获取这周的时间最小值
     * 先得到今天是这周第几天T，拿现在的时间减去T，得到这周一的时间，设置
     *
     * @return 本周最小值
     */
    public static long getWeekMinTimeMillis() {
        long currTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(currTime);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == 1) {
            //1是星期天
            dayOfWeek = 7;
        } else {
            dayOfWeek -= 1;
        }

        day -= dayOfWeek;

        calendar.set(year, month, day, 0, 0, 0);

        return calendar.getTimeInMillis();
    }


    public static String getWeekday(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        String weekday = null;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                weekday = "周日";
                break;
            case 2:
                weekday = "周一";
                break;
            case 3:
                weekday = "周二";
                break;
            case 4:
                weekday = "周三";
                break;
            case 5:
                weekday = "周四";
                break;
            case 6:
                weekday = "周五";
                break;
            case 7:
                weekday = "周六";
                break;
        }
        return weekday;
    }


    /**
     * 获取昨天时间的最小值
     *
     * @return
     */
    public static long getYesterdayMinTimeMillis() {
        long currTime = System.currentTimeMillis();
        mCalendar.setTime(new Date(currTime));

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        day -= 1;

        mCalendar.set(year, month, day, 0, 0, 0);

        return mCalendar.getTimeInMillis();
    }

    /**
     * 判断是否是昨天
     *
     * @return
     */
    public static boolean isYesterday(long time) {
        if (time <= getYesterdayMaxTimeMillis() && time >= getYesterdayMinTimeMillis()) {
            return true;
        }
        return false;
    }

    /**
     * 时间是否要显示
     *
     * @param preTime  上一条的时间
     * @param lastTime 最新的时间
     * @return
     */
    public static boolean isShowTime(long preTime, long lastTime) {
        return (lastTime - preTime) >= FIVE_MINUTE;
    }
}
