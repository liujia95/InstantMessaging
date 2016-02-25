package me.liujia95.instantmessaging.bean;

/**
 * Created by Administrator on 2016/2/12 16:51.
 */
public class ConversationBean {
    public String title;
    public String desc;
    public int    icon;
    public String date;

    public ConversationBean(String title, String desc, int icon, String date) {
        this.title = title;
        this.desc = desc;
        this.icon = icon;
        this.date = date;
    }
}
