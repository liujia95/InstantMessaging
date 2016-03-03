package me.liujia95.instantmessaging.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.liujia95.instantmessaging.bean.FriendInfoBean;

public class ListUtil {
    public static void sortList(List<FriendInfoBean> list) {
        List<FriendInfoBean> _List = new ArrayList<>();
        Collections.sort(list, new PinyinComparator());
        FriendInfoBean FriendInfoBean = new FriendInfoBean(0, getFirstCharacter(list.get(0).item_en), me.liujia95.instantmessaging.bean.FriendInfoBean.TYPE_CHARACTER);
        String currentCharacter = getFirstCharacter(list.get(0).item_en);
        _List.add(FriendInfoBean);
        _List.add(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            if (getFirstCharacter(list.get(i).item_en).compareTo(currentCharacter) != 0) {
                currentCharacter = getFirstCharacter(list.get(i).item_en);
                FriendInfoBean = new FriendInfoBean(0, currentCharacter, FriendInfoBean.TYPE_CHARACTER);
                _List.add(FriendInfoBean);
            }
            _List.add(list.get(i));
        }
        list.clear();
        for (FriendInfoBean bean : _List) {
            list.add(bean);
        }
    }

    public static String getFirstCharacter(String str) {
        return str.substring(0, 1);
    }
}