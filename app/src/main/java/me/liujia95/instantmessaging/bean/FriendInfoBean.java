package me.liujia95.instantmessaging.bean;

import me.liujia95.instantmessaging.utils.CharacterParser;

/**
 * Created by Administrator on 2016/3/2 19:48.
 */
public class FriendInfoBean {
    public int    avatar;
    public String name;

    public static final int TYPE_CHARACTER = 0; //首字母item的类型
    public static final int TYPE_DATA = 1;      //数据类型
    public int item_type;                      //类型
    public String item_en;

    public FriendInfoBean() {
    }

    public FriendInfoBean(int avatar, String name) {
        this.avatar = avatar;

        CharacterParser parser = CharacterParser.getInstance();
        this.name = name;
        this.item_en = parser.getSelling(name).toUpperCase().trim();
        if(!item_en.matches("[A-Z]+")){
            item_en = "#"+item_en;
        }
    }

    public FriendInfoBean(int avatar, String name,int type) {
        this.avatar = avatar;

        CharacterParser parser = CharacterParser.getInstance();
        this.name = name;
        this.item_type = type;
        this.item_en = parser.getSelling(name).toUpperCase().trim();
        if(!item_en.matches("[A-Z]+")){
            item_en = "#"+item_en;
        }
    }

}
