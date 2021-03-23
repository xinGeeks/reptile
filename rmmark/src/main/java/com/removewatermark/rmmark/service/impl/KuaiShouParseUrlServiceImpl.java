package com.removewatermark.rmmark.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.removewatermark.rmmark.bean.RmMarkResponse;
import com.removewatermark.rmmark.common.ParseCommonUtil;
import com.removewatermark.rmmark.service.VideoParseUrlService;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package rmmark
 * @Description:目标APP：快手
 * 目标url： APP视频分享链接/web端
 *   - APP端：
 *     1. 通过APP里的分享获取视频url：https://v.kuaishou.com/5FGpDa
 *     2. url重定向到真实跳转地址：简化后.,
 *         "https://c.kuaishou.com/fw/photo/3xbjz7dvcx6qj3a?fid=2096594305&cc=share_copylink&followRefer=151&photoId=3xbjz7dvcx6qj3a&userId=3xkvt27kw2i6xbe"
 *     3. 提取重定向后的url地址中的photoId的值
 *     4. 对 https://video.kuaishou.com/graphql发送post请求，目标数据就在返回的json数据中
 *
 *   - WEB端：
 *     1. web端的url形如：https://video.kuaishou.com/short-video/3x39cpt7868qp2i?authorId=3xk4tvmdvq72fqs&streamSource=find
 * @date 2021-03-23 21:44
 */
@Service
public class KuaiShouParseUrlServiceImpl implements VideoParseUrlService {

    @Override
    public RmMarkResponse parseUrl(String url) throws Exception {
        RmMarkResponse response = new RmMarkResponse();
        String redirectUrl = ParseCommonUtil.getLocation(url);
        if (redirectUrl.isEmpty()) {
            return null;
        }
        String itemId = ParseCommonUtil.kSMatchNo(redirectUrl);
        String newUrl = ParseCommonUtil.getLocation(url);
        String refererUrl = ParseCommonUtil.getLocation(newUrl);
        String result = postKuaiShou(itemId, refererUrl);
        JSONObject resultJson = (JSONObject) JSONObject.parse(result);
        JSONObject dataJson = (JSONObject) resultJson.get("data");
        JSONObject visionVideoDetail = (JSONObject) dataJson.get("visionVideoDetail");
        JSONObject photo = (JSONObject) visionVideoDetail.get("photo");
        String description = photo.get("caption").toString();
        String photoUrl = photo.get("photoUrl").toString();
        response.setTitle(description);
        response.setUrl(photoUrl);
        return response;
    }

    private static String postKuaiShou(String itemId, String refererUrl) {
        OkHttpClient client = new OkHttpClient();
        String templet = "{\n" +
                "\"operationName\": \"visionVideoDetail\",\n" +
                "\"variables\": {\n" +
                "\"photoId\": \"%s\",\n" +
                "\"page\": \"detail\"\n" +
                "},\n" +
                "\"query\": \"query visionVideoDetail($photoId: String, $type: String, $page: String, $webPageArea: String) {\\n  visionVideoDetail(photoId: $photoId, type: $type, page: $page, webPageArea: $webPageArea) {\\n    status\\n    type\\n    author {\\n      id\\n      name\\n      following\\n      headerUrl\\n      __typename\\n    }\\n    photo {\\n      id\\n      duration\\n      caption\\n      likeCount\\n      realLikeCount\\n      coverUrl\\n      photoUrl\\n      liked\\n      timestamp\\n      expTag\\n      llsid\\n      viewCount\\n      videoRatio\\n      stereoType\\n      __typename\\n    }\\n    tags {\\n      type\\n      name\\n      __typename\\n    }\\n    commentLimit {\\n      canAddComment\\n      __typename\\n    }\\n    llsid\\n    __typename\\n  }\\n}\\n\"\n" +
                "}";
        String requestJson = String.format(templet, itemId);
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody requestBody = RequestBody.create(requestJson,mediaType);


        final Request request =  new Request.Builder()
                .addHeader("Host", "video.kuaishou.com")
                .addHeader("Origin", "https://video.kuaishou.com")
                .addHeader("content-type", "application/json")
                .addHeader("user-agent","Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, " +
        "like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1")
                .addHeader("Content-Length","863")
                .addHeader("Referer", refererUrl)
                .post(requestBody)
                .url(ParseCommonUtil.KUAI_SHOU_BASE_URL)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
