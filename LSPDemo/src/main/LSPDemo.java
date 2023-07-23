package src.main;

import java.net.http.HttpClient;

public class LSPDemo {
    public static void main(String[] args) {
        LSPDemo lspDemo1 = new LSPDemo();
        lspDemo1.demoFun(new Transporter(HttpClient.newBuilder().build()));

        LSPDemo lspDemo2 = new LSPDemo();
        lspDemo2.demoFun(new SecurityTransporter(HttpClient.newBuilder().build(),"",""));
    }


    public void demoFun(Transporter transporter) {
        transporter.sendRequest("request");
    }
}
