package com.ljt;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestEvery {

    @Test
    public void a() {
        RequestInfo requestInfo1 = new RequestInfo();
        requestInfo1.setResponseTime(100);
        requestInfo1.setApiName("1");
        RequestInfo requestInfo2 = new RequestInfo();
        requestInfo2.setResponseTime(200);
        requestInfo2.setApiName("2");
        List<RequestInfo> requestInfos = new ArrayList<>();
        requestInfos.add(requestInfo1);
        requestInfos.add(requestInfo2);

        requestInfos.sort((o1, o2) -> {
            //升序 前面的 - 后面的
            //降序 后面的 - 前面的
            double diff = o1.getResponseTime() - o2.getResponseTime();
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            } else {
                return 0;
            }
        });

        for (RequestInfo requestInfo : requestInfos) {
            System.out.println(requestInfo.getApiName());
        }
    }
}
