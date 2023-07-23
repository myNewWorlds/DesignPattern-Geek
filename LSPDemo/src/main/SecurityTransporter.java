package src.main;

import java.net.http.HttpClient;

public class SecurityTransporter extends Transporter {
    private final String appId;
    private final String appToken;

    public SecurityTransporter(HttpClient httpClient, String appId, String appToken) {
        super(httpClient);
        this.appId = appId;
        this.appToken = appToken;
    }

    @Override
    public void sendRequest(String request) {
        if (appId != null && appToken != null) {
            request += appId + appToken;
        }
        super.sendRequest(request);
    }
}
