package com.dingqiqi.qijuproject.mode;

import java.util.List;

/**
 * 菜谱筛选实体类
 * Created by 丁奇奇 on 2017/7/20.
 */
public class MenuFilterMode extends BaseMode {
    //返回结果集
    private MenuFilterMode result;
    //子集列表
    private List<MenuFilterMode> childs;
    //分类数据集
    private MenuFilterMode categoryInfo;
    //分类ID
    private String ctgId;
    //分类描述
    private String name;
    //上层分类ID
    private String parentId;

    public MenuFilterMode getResult() {
        return result;
    }

    public void setResult(MenuFilterMode result) {
        this.result = result;
    }

    public List<MenuFilterMode> getChilds() {
        return childs;
    }

    public void setChilds(List<MenuFilterMode> childs) {
        this.childs = childs;
    }

    public MenuFilterMode getCategoryInfo() {
        return categoryInfo;
    }

    public void setCategoryInfo(MenuFilterMode categoryInfo) {
        this.categoryInfo = categoryInfo;
    }

    public String getCtgId() {
        return ctgId;
    }

    public void setCtgId(String ctgId) {
        this.ctgId = ctgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
