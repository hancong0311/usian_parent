package com.usian.util;

/**
 * @Title: PageResult
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/10 15:28
 */
public class PageResult {

    private Integer pageIndex;//当前页
    private Integer totalPage;//总页
    private Object result;// 页的内容信息


    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
