package com.removewatermark.rmmark.service;

import com.removewatermark.rmmark.bean.RmMarkResponse;
import org.springframework.stereotype.Service;


public interface VideoParseUrlService {

    RmMarkResponse parseUrl(String url) throws Exception;
}
