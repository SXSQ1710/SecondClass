package com.SecondClass.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: ExcelListener
 * @Author SXSQ
 * @Description Excel导入工具
 * @Date 2022/11/25 9:19
 **/

public class ExcelListener<T> extends AnalysisEventListener {
    //可以通过实例获取该值
    private List<T> datas = new ArrayList<T>();

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        datas.add((T) o);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
        doSomething(o);//根据自己业务做处理
    }

    private void doSomething(Object object) {
        //1、入库调用接口
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // datas.clear();//解析结束销毁不用的资源
    }
}
