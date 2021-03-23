package com.removewatermark.rmmark.bean;

/**
 * @author SemperFi
 * @Title: null.java
 * @Package rmmark
 * @Description:
 * @date 2021-03-22 21:53
 */
public class RmMarkResponse {

    private String title;

    private String url;

    public RmMarkResponse() {
    }

    public RmMarkResponse(String title, String url) {
        this.title = title;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Response{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
