package com.dingqiqi.qijuproject.mode;

import java.util.List;

/**
 * 菜谱实体类
 * Created by dingqiqi on 2017/7/25.
 */
public class MenuMode extends BaseMode {
    //结果集
    private MenuMode result;
    //数据列表
    private List<MenuMode> list;
    //总条数
    private int total;
    //当前页
    private int curPage;
    //分类ID
    private List<String> ctgIds;
    //分类标签
    private String ctgTitles;
    //菜谱id
    private String menuId;
    //菜谱名称
    private String name;
    //制作步骤
    private MenuDetailMode recipe;
    //预览图地址
    private String thumbnail;
    //数据库主键,记录顺序
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MenuMode getResult() {
        return result;
    }

    public void setResult(MenuMode result) {
        this.result = result;
    }

    public List<MenuMode> getList() {
        return list;
    }

    public void setList(List<MenuMode> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public List<String> getCtgIds() {
        return ctgIds;
    }

    public void setCtgIds(List<String> ctgIds) {
        this.ctgIds = ctgIds;
    }

    public String getCtgTitles() {
        return ctgTitles;
    }

    public void setCtgTitles(String ctgTitles) {
        this.ctgTitles = ctgTitles;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public MenuDetailMode getRecipe() {
        return recipe;
    }

    public void setRecipe(MenuDetailMode recipe) {
        this.recipe = recipe;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
