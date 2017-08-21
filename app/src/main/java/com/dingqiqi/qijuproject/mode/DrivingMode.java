package com.dingqiqi.qijuproject.mode;

import java.util.List;

/**
 * 驾照试题mode
 * Created by dingqiqi on 2017/6/14.
 */
public class DrivingMode extends BaseMode {
    //当前页
    private int curPage;
    //总题数
    private int total;
    //结果集
    private DrivingMode result;
    //题目列表
    private List<Mode> list;

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

    public DrivingMode getResult() {
        return result;
    }

    public void setResult(DrivingMode result) {
        this.result = result;
    }

    public List<Mode> getList() {
        return list;
    }

    public void setList(List<Mode> list) {
        this.list = list;
    }

    public class Mode {
        //选项A
        private String a;
        private String b;
        private String c;
        private String d;
        //答案解释
        private String explainText;
        //图片文件链接url
        private String file;
        //题库id
        private String id;
        //题库类型:判断题-judge ; 选择题-select
        private String tikuType;
        //标题
        private String title;
        //答案选项（如果是选择题，则1-a,2-b,3-c,4-d；如果是判断题，则1=正确，2=错误）
        private String val;
        //选中的答案 默认是0
        private int selectVal;
        //科目一 还是科目四
        private String type;

        public int getSelectVal() {
            return selectVal;
        }

        public void setSelectVal(int selectVal) {
            this.selectVal = selectVal;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getD() {
            return d;
        }

        public void setD(String d) {
            this.d = d;
        }

        public String getExplainText() {
            return explainText;
        }

        public void setExplainText(String explainText) {
            this.explainText = explainText;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTikuType() {
            return tikuType;
        }

        public void setTikuType(String tikuType) {
            this.tikuType = tikuType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }

}
