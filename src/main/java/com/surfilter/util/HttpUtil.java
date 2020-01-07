package com.surfilter.util;

import com.surfilter.enums.Param;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    private static String User_Agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22";
    private static String Accept = "text/html";
    private static String Accept_Charset = "utf-8";
    private static String Accept_EnCoding = "gzip";
    private static String Accept_Language = "en-Us,en";

    /**
     * 根据url判断服务是否存在
     *
     * @param url
     * @return
     */
    public static boolean httpConnTest(String url) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL( url ).openConnection();
//            Long totalSize = Long.parseLong(httpURLConnection.getHeaderField("Content-Length"));
            int code = httpURLConnection.getResponseCode();
            if (Integer.parseInt( String.valueOf( code ).substring( 0, 1 ) ) == 4 || Integer.parseInt( String.valueOf( code ).substring( 0, 1 ) ) == 5) {
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
     * @param hostName
     * @param port
     * @return
     */
    public static boolean isSocketAliveUitlitybyCrunchify(String hostName, int port) {
        boolean isAlive = false;
        // 创建一个套接字
        SocketAddress socketAddress = new InetSocketAddress( hostName, port );
        Socket socket = new Socket();
        // 超时设置，单位毫秒
        int timeout = 2000;
        try {
            socket.connect( socketAddress, timeout );
            isAlive = true;
        } catch (Exception exception) {
           // exception.printStackTrace();
        } finally {
            try {
                socket.close();
                socketAddress = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isAlive;
    }

    private static void log(String string) {
        System.out.println( string );
    }

    private static void log(boolean isAlive) {
        System.out.println( "是否真正在使用: " + isAlive + "\n" );
    }


    /**
     *
     */
    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            } );

            SSLContext context = SSLContext.getInstance( "TLS" );
            context.init( null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom() );
            HttpsURLConnection.setDefaultSSLSocketFactory( context.getSocketFactory() );
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }


    /**
     * 爬取url
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static Document getDocByUrl(String url, int timeout) throws IOException {
        Map<String, String> header = new HashMap<>();
        header.put( "User-Agent", "  Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0" );
        header.put( "Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" );
        header.put( "Accept-Language", "zh-cn,zh;q=0.5" );
        header.put( "Accept-Encoding", "gzip, deflate, sdch" );
        header.put( "Connection", "keep-alive" );
        Document doc = null;
        try {
            doc = Jsoup.connect( url )
                    .headers( header )
                    .ignoreContentType( true )
                    .timeout( timeout ).get();
        } catch (Exception e) {
        //    e.printStackTrace();
        }
        return doc;
    }

    public static void main(String[] args) {
        try {
            Document doc = getDocByUrl( "http://681444.com", 10000 );
            System.out.println( doc.title() );
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static String getNewUrl(String url) {
        String newUrl = null;
        if (!url.contains( Param.HTTP_PORT.getMsg() ) && !url.contains( Param.HTTPS_PORT.getMsg() )) {
            if (HttpUtil.isSocketAliveUitlitybyCrunchify( url, Param.HTTPS_PORT.getCode() )) {
                newUrl = Param.HTTPS_PORT.getMsg() + "://" + url;
            } else if (HttpUtil.isSocketAliveUitlitybyCrunchify( url, Param.HTTP_PORT.getCode() )) {
                newUrl = Param.HTTP_PORT.getMsg() + "://" + url;
            } else {
                newUrl = url;
            }
        } else {
            newUrl = url;
        }
        return newUrl;
    }


    public static String getPageContent_addHeader(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet( url );
            httpget.addHeader( "Accept", Accept );
            httpget.addHeader( "Accept-Charset", Accept_Charset );
            httpget.addHeader( "Accept-Encoding", Accept_EnCoding );
            httpget.addHeader( "Accept-Language", Accept_Language );
            httpget.addHeader( "User-Agent", User_Agent );
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        System.out.println( status );
                        return entity != null ? EntityUtils.toString( entity ) : null;
                    } else {
                        System.out.println( status );
                        Date date = new Date();
                        System.out.println( date );
                        System.exit( 0 );
                        throw new ClientProtocolException( "Unexpected response status: " + status );
                    }
                }
            };
            String responseBody = httpclient.execute( httpget, responseHandler );
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Document getPageContent(String url) {
        Document document = Jsoup.parse( getPageContent_addHeader( url ) );
        return document;
    }

}
