package com.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package java-reptile
 * @Description:
 * @date 2021-11-21 18:45
 */
public class HtmlUtil {

    public static Optional<Document> getDocumentByUrl(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            return Optional.ofNullable(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static String getGBKURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String URLGBKDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
