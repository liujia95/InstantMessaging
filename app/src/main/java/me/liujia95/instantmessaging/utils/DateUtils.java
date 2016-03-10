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
    public static String getDateFormat(long millis) {
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
