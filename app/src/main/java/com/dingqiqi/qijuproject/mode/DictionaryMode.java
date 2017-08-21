package com.dingqiqi.qijuproject.mode;

/**
 * 新华字典mode
 * Created by dingqiqi on 2017/6/29.
 */
public class DictionaryMode extends BaseMode {
    //返回结果集
    private DictionaryMode result;
    //部首
    private String bihua;
    //去部首后笔画数
    private String bihuaWithBushou;
    //简介
    private String brief;
    //部首
    private String bushou;
    //明细
    private String detail;
    //汉字
    private String name;
    //拼音
    private String pinyin;
    //五笔
    private String wubi;

    public DictionaryMode getResult() {
        return result;
    }

    public void setResult(DictionaryMode result) {
        this.result = result;
    }

    public String getBihua() {
        return bihua;
    }

    public void setBihua(String bihua) {
        this.bihua = bihua;
    }

    public String getBihuaWithBushou() {
        return bihuaWithBushou;
    }

    public void setBihuaWithBushou(String bihuaWithBushou) {
        this.bihuaWithBushou = bihuaWithBushou;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getBushou() {
        return bushou;
    }

    public void setBushou(String bushou) {
        this.bushou = bushou;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getWubi() {
        return wubi;
    }

    public void setWubi(String wubi) {
        this.wubi = wubi;
    }
}
