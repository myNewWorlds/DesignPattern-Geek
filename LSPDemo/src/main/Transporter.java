package src.main;

import java.net.http.HttpClient;

public class Transporter {
    private final HttpClient httpClient;

    public Transporter(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void sendRequest(String request) {
        System.out.println(request);
        System.out.println("调用父类的方法" + httpClient.version());
    }
}
