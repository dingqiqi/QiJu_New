package com.dingqiqi.qijuproject.mode;

/**
 * 获取网络msg code  实体基类
 * Created by dingqiqi on 2017/6/13.
 */
public class BaseMode {
    //消息
    private String msg;
    //返回代码
    private String retCode;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }
}
