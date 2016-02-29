package me.liujia95.instantmessaging;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.liujia95.instantmessaging.utils.LogUtils;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test(){
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE,-1); // 减一就是昨天

        String strYesterday = new SimpleDateFormat( "yyyy-MM-dd ").format(yesterday.getTime());
        LogUtils.d("-------------");
        LogUtils.d("yesterday is:"+strYesterday);
    }
}