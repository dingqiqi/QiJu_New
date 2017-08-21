package com.dingqiqi.qijuproject.mode;

import java.util.List;

/**
 * 精选标题实例
 * Created by 丁奇奇 on 2017/8/2.
 */
public class WXTitleMode extends BaseMode{

    private String cid;
    private String name;

    private List<WXTitleMode> result;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WXTitleMode> getResult() {
        return result;
    }

    public void setResult(List<WXTitleMode> result) {
        this.result = result;
    }
}
