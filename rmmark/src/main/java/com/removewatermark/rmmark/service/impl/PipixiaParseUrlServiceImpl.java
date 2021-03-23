package com.removewatermark.rmmark.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.removewatermark.rmmark.bean.RmMarkResponse;
import com.removewatermark.rmmark.common.ParseCommonUtil;
import com.removewatermark.rmmark.service.VideoParseUrlService;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package rmmark
 * @Description: 目标APP：皮皮虾
 * 目标url：APP短视频分享链接
 * 爬取思路：
 * 1. 通过APP里的分享获取视频url：https://h5.pipix.com/s/JAtW8Yg/
 * 2. url重定向到真实跳转地址：简化后.,https://h5.pipix.com/item/6869230768778909965
 * 3. 获取重定向后的url的item_id,携带其发送get请求
 * 原理：
 * 1. 任意分享的视频链接如下：
 * - https://h5.pipix.com/s/3asShh（✅）
 * - https://h5.pipix.com/s/JRjEVyT（✖）
 * - https://h5.pipix.com/s/JAtW8Yg（✖）
 * - https://h5.pipix.com/s/rR5Ppu（✅）
 * 2. 发现有些视频链接，获取访问结果的json中的“comment”字段里居然藏有无水印的视频url
 * 3. 有些分享链接的comment字段为空数组，有些又有值。想通过app分享链接百分百拿到无水印视频url感觉有点困难，除非知道内部视频数据api
 * 视频分享链接有些是可以获取，有些无法获取，原因在于其【视频是否有评论】，我们的无水印解析基础是通过评论里获取的
 * 皮皮虾的短视频【推荐】板块是每次post请求，返回6条数据，滑动6个视频后会重新请求，根据这个api好像不能查询某个具体的视频，
 * 于是我就点击详细页，发现了评论的api中找到了这个无水印的短视频URL。美中不足的是：假如某个视频是没有评论的，返回的就是一个空数组
 * @date 2021-03-22 22:50
 */
@Service
public class PipixiaParseUrlServiceImpl implements VideoParseUrlService {

    private static final Logger logger = LoggerFactory.getLogger(PipixiaParseUrlServiceImpl.class);


    @Override
    public RmMarkResponse parseUrl(String url) throws Exception {
        RmMarkResponse response = new RmMarkResponse();
        String redirectUrl = ParseCommonUtil.getLocation(url);
        if (redirectUrl.isEmpty()) {
            logger.warn("PipixiaParseUrl redirect url is not exist");
            return null;
        }
        String itemId = ParseCommonUtil.PpxMatchNo(redirectUrl);
        String format = String.format(ParseCommonUtil.PIPIXIA_BASE_URL, itemId);
        String result = getPipixi(format);
        if (result.isEmpty()) {
            logger.warn("PipixiaParseUrl result is null, url = " + url);
            return null;
        }
        JSONObject object = JSONObject.parseObject(result);
        JSONObject data =  (JSONObject) object.get("data");
        JSONObject item = (JSONObject) data.get("item");
        JSONArray comments = (JSONArray) item.get("comments");
        //判断comments是否为空
        if (comments.size() > 0) {
            JSONObject jsonObject =  (JSONObject) comments.get(0);
            JSONObject itemJsonObject = (JSONObject) jsonObject.get("item");
            JSONObject video = (JSONObject) itemJsonObject.get("video");
            JSONObject videoHigh = (JSONObject) video.get("video_high");
            JSONArray urlList = (JSONArray) videoHigh.get("url_list");
            JSONObject urlJSONObject = (JSONObject) urlList.get(0);
            String urlString = urlJSONObject.get("url").toString();
            JSONObject share = (JSONObject) item.get("share");

            response.setTitle(share.get("title").toString());
            response.setUrl(urlString);
        } else {
            String pipixiaBaseUrl1 = ParseCommonUtil.PIPIXIA_BASE_URL_1;
            String pipixiResult = getPipixi(pipixiaBaseUrl1);
            if (pipixiResult.isEmpty()) {
                logger.warn("PipixiaParseUrl result is null, url = " + url);
                return null;
            }
            JSONObject dataObject =  (JSONObject) object.get("data");
            JSONObject itemObject = (JSONObject) dataObject.get("item");

        }

        return response;
    }

    private static String getPipixi(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/86.0.4240.193 Safari/537.36 ")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            return null;
        }
        return response.body().string();
    }
}
