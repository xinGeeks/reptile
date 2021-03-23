package com.removewatermark.rmmark.controller;



import com.removewatermark.rmmark.bean.RmMarkResponse;
import com.removewatermark.rmmark.common.ParseCommonUtil;
import com.removewatermark.rmmark.service.VideoParseUrlService;
import com.removewatermark.rmmark.service.impl.DouYinParseUrlServiceImpl;
import com.removewatermark.rmmark.service.impl.HuoShanParseUrlServiceImpl;
import com.removewatermark.rmmark.service.impl.KuaiShouParseUrlServiceImpl;
import com.removewatermark.rmmark.service.impl.PipixiaParseUrlServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


/**
 * @author xzhao
 * @description 抖音无水印视频下载
 * @date 2020/9/15 18:44
 */
@Slf4j
@Controller
public class RemoveWaterMarkController {

    private static final Logger logger = LoggerFactory.getLogger(RemoveWaterMarkController.class);

    @Autowired
    private HuoShanParseUrlServiceImpl huoShanParseUrlService;

    @Autowired
    private DouYinParseUrlServiceImpl douYinParseUrlService;

    @Autowired
    private PipixiaParseUrlServiceImpl pipixiaParseUrlService;

    @Autowired
    private KuaiShouParseUrlServiceImpl kuaiShouParseUrlService;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }


    /**
     * @param url
     * @author xzhao
     * @description 解析无水印视频url
     * @date 2020/9/15 12:43
     */
    @RequestMapping("/parse")
    @ResponseBody
    public RmMarkResponse parseVideoUrl(@RequestParam String url) throws Exception {
        RmMarkResponse response = new RmMarkResponse();

        String decodeUrl = URLDecoder.decode(url, "utf-8");
        url = decodeUrl.replace("url=", "");

        try {
            if (url.contains(ParseCommonUtil.HUO_SHAN_DOMAIN)) {
                logger.debug("解析火山视频");
                response = huoShanParseUrlService.parseUrl(url);
            } else if (url.contains(ParseCommonUtil.DOU_YIN_DOMAIN)) {
                logger.debug("解析抖音");
                response = douYinParseUrlService.parseUrl(url);
            } else if (url.contains(ParseCommonUtil.PIPIXIA_DOMAIN)) {
                logger.debug("解析皮皮虾");
                response = pipixiaParseUrlService.parseUrl(url);
            } else if (url.contains(ParseCommonUtil.KUAI_SHOU)) {
                logger.debug("解析快手");
                response = kuaiShouParseUrlService.parseUrl(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
