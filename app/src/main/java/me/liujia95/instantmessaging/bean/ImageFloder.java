package me.liujia95.instantmessaging.bean;

/**
 * 首先对手机中图片进行扫描，拿到图片数量最多的，直接显示在GridView上；
 * 并且扫描结束，得到一个所有包含图片的文件夹信息的List；
 * 对于文件夹信息，我们单独创建了一个Bean:
 */
public class ImageFloder {
    /**
     * 图片的文件夹路径
     */
    private String dir;

    /**
     * 第一张图片的路径
     */
    private String firstImagePath;

    /**
     * 文件夹的名称
     */
    private String name;

    /**
     * 图片的数量
     */
    private int count;

    public String getDir() {
        return dir;
    }

    //自动提取文件夹的名称
    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf);
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}  