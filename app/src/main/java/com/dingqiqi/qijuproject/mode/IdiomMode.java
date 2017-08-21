package com.dingqiqi.qijuproject.mode;

/**
 * 成语大全
 * Created by dingqiqi on 2017/6/29.
 */
public class IdiomMode extends BaseMode{
    //返回结果集
    private IdiomMode result;
    //成语
    private String name;
    //拼音
    private String pinyin;
    //释义
    private String pretation;
    //示例
    private String sample;
    //示例出自
    private String sampleFrom;
    //出自
    private String source;

    public IdiomMode getResult() {
        return result;
    }

    public void setResult(IdiomMode result) {
        this.result = result;
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

    public String getPretation() {
        return pretation;
    }

    public void setPretation(String pretation) {
        this.pretation = pretation;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getSampleFrom() {
        return sampleFrom;
    }

    public void setSampleFrom(String sampleFrom) {
        this.sampleFrom = sampleFrom;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
