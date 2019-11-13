package com.surfilter.util;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpUtil {

    /**
     * 根据url判断服务是否存在
     * @param url
     * @return
     */
    public static boolean httpConnTest(String url){
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
//            Long totalSize = Long.parseLong(httpURLConnection.getHeaderField("Content-Length"));
            int code = httpURLConnection.getResponseCode();
            if (Integer.parseInt(String.valueOf(code).substring(0,1)) == 4 || Integer.parseInt(String.valueOf(code).substring(0,1)) == 5){
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isSocketAliveUitlitybyCrunchify(String hostName, int port) {
        boolean isAlive = false;

        // 创建一个套接字
        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        Socket socket = new Socket();

        // 超时设置，单位毫秒
        int timeout = 2000;

        try {
            socket.connect(socketAddress, timeout);
            socket.close();
            isAlive = true;

        } catch (SocketTimeoutException exception) {

        } catch (IOException exception) {
        }
        return isAlive;
    }

    private static void log(String string) {
        System.out.println(string);
    }

    private static void log(boolean isAlive) {
        System.out.println("是否真正在使用: " + isAlive + "\n");
    }



    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[] { new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

}
