package com.ljt;

import com.ljt.inner.StatViewer;

import java.util.Map;

public class EmailViewer implements StatViewer {
    @Override
    public void output(Map<String, RequestStat> requestStats, long startTimeInMillis, long endTimeInMills) {
        //发送邮件
    }
}
