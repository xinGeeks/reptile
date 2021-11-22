package com.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package java-reptile
 * @Description:
 * @date 2021-11-22 22:22
 */
public class SearchRequest {

    private int pageSize;

    private int pageNum;

    private Map<String, String> searchMap;

    public SearchRequest() {
    }

    public SearchRequest(int pageSize, int pageNum, Map<String, String> searchMap) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.searchMap = searchMap;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public Map<String, String> getSearchMap() {
        return searchMap;
    }

    public void setSearchMap(Map<String, String> searchMap) {
        this.searchMap = searchMap;
    }
}
