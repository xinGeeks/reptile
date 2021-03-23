package com.removewatermark.rmmark.service.impl;

import com.alibaba.fastjson.JSON;
import com.removewatermark.rmmark.bean.DYResult;
import com.removewatermark.rmmark.bean.RmMarkResponse;
import com.removewatermark.rmmark.common.ParseCommonUtil;
import com.removewatermark.rmmark.service.VideoParseUrlService;
import org.springframework.stereotype.Service;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package rmmark
 * @Description: url=https://v.douyin.com/eefSLTd/
 * @date 2021-03-22 22:11
 */
@Service
public class DouYinParseUrlServiceImpl implements VideoParseUrlService {

    @Override
    public RmMarkResponse parseUrl(String url) throws Exception {
        RmMarkResponse response = new RmMarkResponse();
        String redirectUrl = ParseCommonUtil.getLocation(url);
        if (redirectUrl.isEmpty()) {
            return null;
        }
        /**
         * 1、用 ItemId 拿视频的详细信息，包括无水印视频url
         */
        String itemId = ParseCommonUtil.matchNo(redirectUrl);

        StringBuilder sb = new StringBuilder();
        sb.append(ParseCommonUtil.DOU_YIN_BASE_URL).append(itemId);

        String videoResult = ParseCommonUtil.httpGet(sb.toString());

        DYResult dyResult = JSON.parseObject(videoResult, DYResult.class);

        /**
         * 2、无水印视频 url
         */
        String videoUrl = dyResult.getItem_list().get(0)
                .getVideo().getPlay_addr().getUrl_list().get(0)
                .replace("playwm", "play");
        String videoRedirectUrl = ParseCommonUtil.getLocation(videoUrl);

        response.setUrl(videoRedirectUrl);


        /**
         * 3、视频文案
         */
        String desc = dyResult.getItem_list().get(0).getDesc();
        response.setTitle(desc);
        return response;
    }


}
