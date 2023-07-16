package com.example;

import com.example.inter.ApiAuthenticator;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestAuth {
    @Test
    public void test() {
        byte[] byteArray = {72, 101, 108, 108, 111}; // 假设这是一个字节数组
        System.out.println("第一");
        System.out.println(new String(byteArray));
        System.out.println("第二");
        System.out.println(new String(byteArray, StandardCharsets.UTF_8));
        System.out.println("第三");
        System.out.println(new String(Base64.getEncoder().encode(byteArray)));
    }

    @Test
    public void testAuth() {
        String url = "www.bai.com";
        String appId = "id1";
        String password = "123456";
        long timeStamp = System.currentTimeMillis();
        AuthToken authToken = AuthToken.create(url, appId, password, timeStamp);
        String token = authToken.getToken();
        String finalUrl = url + "?token=" + token + "&appId=" + appId + "&timestamp=" + timeStamp;

        ApiAuthenticator apiAuthencator = new DefaultApiAuthencatorImpl();
        apiAuthencator.auth(finalUrl);
        System.out.println("a");
    }
}
