package com.dingqiqi.qijuproject.mode;

/**
 * 手机号码归属地
 * Created by dingqiqi on 2017/6/29.
 */
public class IPhoneMode extends BaseMode {
    //返回结果集
    private IPhoneMode result;
    //城市
    private String city;
    //城市代码
    private String cityCode;
    //手机号
    private String mobileNumber;
    //运营商
    private String operator;
    //省份
    private String province;
    //邮编
    private String zipCode;

    public IPhoneMode getResult() {
        return result;
    }

    public void setResult(IPhoneMode result) {
        this.result = result;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
