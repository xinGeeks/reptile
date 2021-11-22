package com.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package java-reptile
 * @Description:
 * @date 2021-11-21 20:20
 */
public class FetchImgUtil {

    private static final String BASE_PATH = "D:\\tmp";
    /**
     * 下载图片到指定目录
     * @param downloadPath
     * @param imgUrl   图片URL
     */
    public static void downImages(String downloadPath, String imgUrl) {
        // 若指定文件夹没有，则先创建
        //String downloadPath = BASE_PATH + File.separator + folderName;
        //File folder = FolderUtil.createFolder(downloadPath);
        // 截取图片文件名
        String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.length());

        try {
            // 文件名里面可能有中文或者空格，所以这里要进行处理。但空格又会被URLEncoder转义为加号
            String urlTail = URLEncoder.encode(fileName, "UTF-8");
            // 因此要将加号转化为UTF-8格式的%20
            imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf('/') + 1) + urlTail.replaceAll("\\+", "\\%20");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 写出的路径
        File file = new File(downloadPath  + File.separator + fileName);
        if (file.exists()) {
            System.out.println("文件" + file + "已存在");
            return;
        }

        try {
            // 获取图片URL
            URL url = new URL(imgUrl);
            // 获得连接
            URLConnection connection = url.openConnection();
            // 设置10秒的相应时间
            connection.setConnectTimeout(10 * 1000);
            // 获得输入流
            InputStream in = connection.getInputStream();
            // 获得输出流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            // 构建缓冲区
            byte[] buf = new byte[1024];
            int size;
            // 写入到文件
            while (-1 != (size = in.read(buf))) {
                out.write(buf, 0, size);
            }
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBasePath() {
        return BASE_PATH;
    }
}
