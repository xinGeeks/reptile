package com.removewatermark.rmmark.service.impl;

import com.alibaba.fastjson.JSON;
import com.removewatermark.rmmark.bean.HSResult;
import com.removewatermark.rmmark.bean.RmMarkResponse;
import com.removewatermark.rmmark.common.ParseCommonUtil;
import com.removewatermark.rmmark.service.VideoParseUrlService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package rmmark
 * @Description: 测试url=https://share.huoshan.com/hotsoon/s/oUEFyvRFQg8/
 * @date 2021-03-22 22:20
 */
@Service
public class HuoShanParseUrlServiceImpl implements VideoParseUrlService {

    @Override
    public RmMarkResponse parseUrl(String url) throws Exception {
        String redirectUrl = ParseCommonUtil.getLocation(url);

        RmMarkResponse response = new RmMarkResponse();

        if (redirectUrl.isEmpty()) {
            return null;
        }

        /**
         * 1、拿到itemId
         */
        String itemId = ParseCommonUtil.hSMatchNo(redirectUrl);

        StringBuilder sb = new StringBuilder();
        sb.append(ParseCommonUtil.HUO_SHAN_BASE_URL).append(itemId);

        /**
         * 2、itemId 拼接视频详情接口
         */
        String videoResult = ParseCommonUtil.httpGet(sb.toString());

        HSResult hsResult = JSON.parseObject(videoResult, HSResult.class);

        /**
         * 3、替换URL地址
         */
        String replace = hsResult.getData().getItem_info().getUrl().replace("_reflow", "_playback");

        response.setUrl(replace.substring(0, replace.indexOf("&")));

        response.setTitle("火山小视频");

        return response;
    }
}
