package com.edu.tmall.util;

/**
 * Created by 何腾飞 on 17/11/27.
 * 分页的工具类
 */
public class Page {
    //开始位置
    private int start;
    //每页显示的记录数
    private int count;
    //总共多少条记录(查数据库)
    private int total;
    //参数(这个属性在后续有用到，但是分类的分页查询里并没有用到，请忽略)
    private String param;
    //默认显示5页
    private static final int defaultCount = 5;

    //总共多少页
    public int getTotalPage() {
        int totalPage;
        if (total % count == 0)
            totalPage = total / count;
        else
            totalPage = total / count + 1;
        if (totalPage==0)
            totalPage = 1;
        return totalPage;
    }

    //最后一页的数值是从多少开始
    public int getLast() {
        int last;
        if (total % count==0)
            last = total - count;
        else
            last = total - total % count;
        last = last<0?0:last;
        return last;
    }

    //是否有上一页
    public boolean isHasPreviouse() {
        return start!=0;
    }

    //是否有下一页
    public boolean isHasNext() {
        return start!=getLast();
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Page() {
        count = defaultCount;
    }

    public Page(int start, int count) {
        this.start = start;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Page{" +
                "start=" + start +
                ", count=" + count +
                ", total=" + total +
                ", param='" + param + '\'' +
                '}';
    }
}
