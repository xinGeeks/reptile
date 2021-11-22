package com.controller;

import com.bean.SearchRequest;
import com.service.Parsekeke2345;
import com.util.HtmlUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package java-reptile
 * @Description:
 * @date 2021-11-21 18:44
 */
public class ParseHTML {

    public static void main(String[] args) {
        Parsekeke2345 parsekeke2345 = new Parsekeke2345();

        String searchName = "玉兔miki";
        Map<String, String> map = new HashMap<>();
        map.put("keyword", searchName);
        SearchRequest request = new SearchRequest(999, 1, map);
        parsekeke2345.downloadHtmlImage(request);

        //parsekeke2345.parseDetailHtml("https://www.keke2345.com/gaoqing/cn/xiuren/2021/1121/41607.html", "陈舒羽");

    }


}
