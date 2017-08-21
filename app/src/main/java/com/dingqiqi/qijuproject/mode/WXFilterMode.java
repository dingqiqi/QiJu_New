package com.dingqiqi.qijuproject.mode;

import java.util.List;

/**
 * 精选文章实体类
 * Created by 丁奇奇 on 2017/8/3.
 */
public class WXFilterMode extends BaseMode {
    //当前页
    private int curPage;
    //总条数
    private int total;
    //结果集
    private WXFilterMode result;
    //结果列表
    private List<WXFilterMode> list;
    //分类Id
    private String cid;
    //文章id
    private String id;
    //文章的发布时间
    private String pubTime;
    //文章来源
    private String sourceUrl;
    //文章副标题
    private String subTitle;
    //文章标题
    private String title;
    //导航缩略图，多个图片用$分割
    private String thumbnails;
    //所有图片地址
    private List<String> images;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public WXFilterMode getResult() {
        return result;
    }

    public void setResult(WXFilterMode result) {
        this.result = result;
    }

    public List<WXFilterMode> getList() {
        return list;
    }

    public void setList(List<WXFilterMode> list) {
        this.list = list;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
