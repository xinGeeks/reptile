package com.removewatermark.rmmark.common;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package rmmark
 * @Description:
 * @date 2021-03-22 22:03
 */
public class ParseCommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(ParseCommonUtil.class);

    public final static String DOU_YIN_DOMAIN = "douyin";

    public final static String HUO_SHAN_DOMAIN = "huoshan";

    public final static String PIPIXIA_DOMAIN = "pipix";

    public final static String KUAI_SHOU="kuaishou";

    public static String DOU_YIN_BASE_URL = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";

    public static String HUO_SHAN_BASE_URL = " https://share.huoshan.com/api/item/info?item_id=";

    public static String PIPIXIA_BASE_URL = "https://h5.pipix.com/bds/webapi/item/detail/?item_id=%s&source=share";

    public static String PIPIXIA_BASE_URL_1 = "https://i-lq.snssdk.com/bds/cell/cell_comment/";

    public static String KUAI_SHOU_BASE_URL = "https://video.kuaishou.com/graphql";

    public static String getLocation(String url) {
        try {
            URL serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("User-agent", "ua");//模拟手机连接
            conn.connect();
            String location = conn.getHeaderField("Location");
            return location;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String httpGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "text/json;charset=utf-8");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuffer buf = new StringBuffer();
        String inputLine = in.readLine();
        while (inputLine != null) {
            buf.append(inputLine).append("\r\n");
            inputLine = in.readLine();
        }
        in.close();
        return buf.toString();
    }

    /**
     * 获取快手视频唯一标识
     * @param redirectUrl
     * @return
     */
    public static String matchNo(String redirectUrl) {
        List<String> results = new ArrayList<>();
        Pattern p = Pattern.compile("video/([\\w/\\.]*)/");
        Matcher m = p.matcher(redirectUrl);
        while (!m.hitEnd() && m.find()) {
            results.add(m.group(1));
        }
        return results.get(0);
    }

    /**
     * 获取火山视频唯一标识
     * @param redirectUrl
     * @return
     */
    public static String hSMatchNo(String redirectUrl) {
        List<String> results = new ArrayList<>();
        Pattern p = Pattern.compile("item_id=([\\w/\\.]*)&");
        Matcher m = p.matcher(redirectUrl);
        while (!m.hitEnd() && m.find()) {
            results.add(m.group(1));
        }
        return results.get(0);
    }

    /**
     * 获取快手 视频唯一标识
     * @param url
     * @return
     */
    public static String kSMatchNo(String url) {
        return getUrlParam(url, "(?<=(photo/)).{1,100}(?=(\\?))");
    }

    /**
     * 获取皮皮虾 视频唯一标识
     * @param url
     * @return
     */
    public static String PpxMatchNo(String url) {
        return getUrlParam(url, "(?<=item/)[0-9]+(?=(\\?))");
    }

    private static String getUrlParam(String url, String pattern) {
        List<String> results = new ArrayList<>();
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(url);
        if (!m.hitEnd() && m.find()) {
            return m.group();
        }
        return null;
    }

}
