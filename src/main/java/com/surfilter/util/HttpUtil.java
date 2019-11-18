package com.surfilter.util;

import com.surfilter.enums.Param;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

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

    /**
     *
     * @param hostName
     * @param port
     * @return
     */
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


    /**
     *
     */
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


    /**
     * 爬取url
     * @param url
     * @return
     * @throws IOException
     */
    public static Document getDocByUrl(String url) throws IOException {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", "  Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0");
        header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        header.put("Accept-Language", "zh-cn,zh;q=0.5");
        header.put("Accept-Encoding", "gzip, deflate, sdch");
        header.put("Connection", "keep-alive");
        Document doc = Jsoup.connect(url)
                .headers(header)
                .ignoreContentType(true)
                .timeout(5000).get();
        return doc;
    }

    public static String getNewUrl(String url) {
        String newUrl = null;
        if (!url.contains(Param.HTTP_PORT.getMsg()) && !url.contains(Param.HTTPS_PORT.getMsg())) {
            if (HttpUtil.isSocketAliveUitlitybyCrunchify(url,Param.HTTPS_PORT.getCode())) {
                newUrl = Param.HTTPS_PORT.getMsg() + "://" + url;
            } else if (HttpUtil.isSocketAliveUitlitybyCrunchify(url,Param.HTTP_PORT.getCode())) {
                newUrl = Param.HTTP_PORT.getMsg() + "://" + url;
            }  else {
                newUrl = url;
            }
        } else {
            newUrl = url;
        }
        return newUrl;
    }

}
