package com.service;

import com.bean.SearchRequest;
import com.util.FetchImgUtil;
import com.util.FolderUtil;
import com.util.HTMLTag;
import com.util.HtmlUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package java-reptile
 * @Description:
 * @date 2021-11-21 18:44
 */
public class Parsekeke2345 {

    public static final String baseUrl = "https://www.keke2345.com";

    private static final String BASE_PATH = FetchImgUtil.getBasePath();

    public Parsekeke2345() {
    }

    /**
     * 下载当前搜索页的所有detail页的图片
     * @param request
     * @return
     */
    public String downloadHtmlImage(SearchRequest request) {
        String searchName = request.getSearchMap().get("keyword");
        int pageNum = request.getPageNum();
        int pageSize = request.getPageSize();
        List<String> detailUrls = searchByName(searchName, pageSize, pageNum);

        for (String detailUrl : detailUrls) {
            Optional<Document> optional = HtmlUtil.getDocumentByUrl(detailUrl);
            if (optional.isPresent()) {
                Document document = optional.get();
                List<String> urls = allImgHtmlUrl(document);
                parseHtml(urls, searchName);
            }
            System.out.println(detailUrl + "下载完成");
        }
        System.out.println("===============全部下载完成================");
        return null;
    }

    /**
     * 返回查询到的连接
     * @param name
     * @return
     */
    public static List<String> searchByName(String name, int pageSize, int pageNo) {
        List<String> urls = new ArrayList<>();
        String encode = HtmlUtil.getGBKURLEncoderString(name);
        String url = "https://www.keke2345.com/plus/search.php?keyword="+ encode +"&searchtype=titlekeyword&channeltype=0&orderby=&kwtype=0&pagesize="+ pageSize +"&PageNo=" + pageNo;
        Optional<Document> optional = HtmlUtil.getDocumentByUrl(url);
        if (optional.isPresent()) {
            Document document = optional.get();
            Elements elements = document.select(".t>a");
            for (Element element : elements) {
                urls.add(baseUrl + element.attr(HTMLTag.HREF.name()));
            }
        }
        return urls;
    }

    /**
     * 解析一个详情页面的所有图片
     * @param url
     * @param folderName
     */
    public void parseDetailHtml(String url, String folderName) {
        parseHtml(Collections.singletonList(url), folderName);
    }

    public void parseHtml(List<String> urls, String folderName) {
        urls.parallelStream().forEach(url -> {
            Optional<Document> optional = HtmlUtil.getDocumentByUrl(url);
            if (optional.isPresent()) {
                Elements titleEle = optional.get().getElementsByTag("h2");
                Elements contentEle = optional.get().getElementsByClass("content");
                Elements selectImg = contentEle.select(HTMLTag.IMG.name());
                String title = titleEle.text().trim();

                for (Element element : selectImg) {
                    File folder = FolderUtil.createFolder(BASE_PATH + File.separator + folderName);
                    String imgUrl = element.attr(HTMLTag.SRC.name());
                    FetchImgUtil.downImages(folder.getAbsolutePath(), imgUrl);
                }
            }
        });
    }

    private List<String> allImgHtmlUrl(Document document) {
        List<String> allUrl = new ArrayList<>();
        String currentUrl = document.location();
        allUrl.add(currentUrl);

        Elements pageEle = document.getElementsByClass("page");
        String pageSizeText = pageEle.first().text();
        //Pattern pattern = Pattern.compile("(?<=共).*?(?=页)");
        //Matcher m = pattern.matcher(pageSizeStr);
        //int pageSize = Integer.parseInt(m.group());
        String pageSizeStr = pageSizeText.split("页")[0].split("共")[1];
        int pageSize = Integer.parseInt(pageSizeStr);
        for (int i = 2; i <= pageSize; i++) {
            allUrl.add(currentUrl.replace(".html", "_" + i + ".html"));
        }
        return allUrl;
    }
}
