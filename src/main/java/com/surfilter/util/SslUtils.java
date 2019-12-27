package com.surfilter.util;

import javax.net.ssl.*;

public class SslUtils {

    private static void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new MiTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance( "SSL" );
        sc.init( null, trustAllCerts, null );
        HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory() );
    }

    public static void ignoreSsl() throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                System.out.println( "Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost() );
                return true;
            }
        };
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier( hv );
    }
}
