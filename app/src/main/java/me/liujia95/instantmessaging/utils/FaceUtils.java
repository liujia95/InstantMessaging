package me.liujia95.instantmessaging.utils;

/**
 * Created by Administrator on 2016/3/11 15:23.
 */
public class FaceUtils {

    /**
     * 把表情的uri进行打包
     * #[bigface:xxx]#
     *
     * @return
     */
    public static String pack(String s) {
        return "#[bigface:" + s + "]#";
    }

    /**
     * 判断是不是表情
     *
     * @return
     */
    public static boolean isFace(String s) {
        return s.startsWith("#[bigface:") && s.endsWith("]#");
    }

    /**
     * 把打好包的表情uri解包
     *
     * @return
     */
    public static String unpack(String s) {
        return s.replace("#[bigface:", "").replace("]#", "");
    }
}
