package com.surfilter.util;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class MiTM implements TrustManager, X509TrustManager {
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public boolean isServerTrusted(X509Certificate[] certs) {
        return true;
    }

    public boolean isClientTrusted(X509Certificate[] certs) {
        return true;
    }

    public void checkServerTrusted(X509Certificate[] certs, String authType)
            throws CertificateException {
        return;
    }

    public void checkClientTrusted(X509Certificate[] certs, String authType)
            throws CertificateException {
        return;
    }


}
