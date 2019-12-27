package com.surfilter.whois.utils.whoisparsers;

import com.surfilter.whois.models.WhoisModel;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
/**
 * Created by dell on 17-11-18.
 */

/**
 * Domain Name: BAIDU.COM
 * Registry Domain ID: 11181110_DOMAIN_COM-VRSN
 * Registrar WHOIS Server: whois.markmonitor.com
 * Registrar URL: http://www.markmonitor.com
 * Updated Date: 2017-07-28T02:36:28Z
 * Creation Date: 1999-10-11T11:05:17Z
 * Registry Expiry Date: 2026-10-11T11:05:17Z
 * Registrar: MarkMonitor Inc.
 * Registrar IANA ID: 292
 * Registrar Abuse Contact Email: abusecomplaints@markmonitor.com
 * Registrar Abuse Contact Phone: +1.2083895740
 * Domain Status: clientDeleteProhibited https://icann.org/epp#clientDeleteProhibited
 * Domain Status: clientTransferProhibited https://icann.org/epp#clientTransferProhibited
 * Domain Status: clientUpdateProhibited https://icann.org/epp#clientUpdateProhibited
 * Domain Status: serverDeleteProhibited https://icann.org/epp#serverDeleteProhibited
 * Domain Status: serverTransferProhibited https://icann.org/epp#serverTransferProhibited
 * Domain Status: serverUpdateProhibited https://icann.org/epp#serverUpdateProhibited
 * Name Server: DNS.BAIDU.COM
 * Name Server: NS2.BAIDU.COM
 * Name Server: NS3.BAIDU.COM
 * Name Server: NS4.BAIDU.COM
 * Name Server: NS7.BAIDU.COM
 * DNSSEC: unsigned
 * URL of the ICANN Whois Inaccuracy Complaint Form: https://www.icann.org/wicf/
 * >>> Last update of whois database: 2017-11-18T11:29:05Z <<<
 */
public class CrParser extends AParser {
    private CrParser() {
    }

    private static CrParser instance = null;

    public static CrParser getInstance() {
        if (instance == null) {
            instance = new CrParser();
        }
        return instance;
    }


//    private String domain;
//    private String ip;
//    private String contacts;
//    private String phone;
//    private String email;
//    private long ctime;
//    private long utime;
//    private long etime;

    private final String DOMAINREG = "\\s*Domain Name:\\s*[^\\n]+";
    private final String CTIMEREG = "\\s*Creation Date:\\s*[^\\n]+";
    private final String UTIMEREG = "\\s*Updated Date:\\s*[^\\n]+";
    private final String ETIMEREG = "\\s*Expiry Date:\\s*[^\\n]+";
    private final String EMAIL = "\\s*Contact Email:\\s*[^\\n]+";
    private final String PHONE = "\\s*Contact Phone:\\s*[^\\n]+";

    private Pattern domainPattern = Pattern.compile( DOMAINREG );
    private Pattern ctimePattern = Pattern.compile( CTIMEREG );
    private Pattern utimePattern = Pattern.compile( UTIMEREG );
    private Pattern etimePattern = Pattern.compile( ETIMEREG );
    private Pattern emailPattern = Pattern.compile( EMAIL );
    private Pattern phonePattern = Pattern.compile( PHONE );

    private SimpleDateFormat simpleDateFormatZ = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'" );
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );

    public WhoisModel parseWhois(String whoisResponse) {
        WhoisModel whoisModel = new WhoisModel();
        try {
            String domain = getFieldValue( getMatchField( domainPattern, whoisResponse ), ":" );
            whoisModel.setDomain( domain );
            //whoisModel.setIp( IpUtil.getIpByDomain(domain));
            String ctime = getFieldValue( getMatchField( ctimePattern, whoisResponse ), ":" );
            if (ctime.endsWith( "'Z'" )) {
                whoisModel.setCtime( simpleDateFormatZ.parse( ctime ).getTime() );
            } else {
                whoisModel.setCtime( simpleDateFormat.parse( ctime ).getTime() );
            }
            String utime = getFieldValue( getMatchField( utimePattern, whoisResponse ), ":" );
            if (ctime.endsWith( "'Z'" )) {
                whoisModel.setUtime( simpleDateFormatZ.parse( utime ).getTime() );
            } else {
                whoisModel.setUtime( simpleDateFormat.parse( utime ).getTime() );
            }
            String etime = getFieldValue( getMatchField( etimePattern, whoisResponse ), ":" );
            if (ctime.endsWith( "'Z'" )) {
                whoisModel.setEtime( simpleDateFormatZ.parse( etime ).getTime() );
            } else {
                whoisModel.setEtime( simpleDateFormat.parse( etime ).getTime() );
            }
            String email = getFieldValue( getMatchField( emailPattern, whoisResponse ), ":" );
            whoisModel.setEmail( email );
            String phone = getFieldValue( getMatchField( phonePattern, whoisResponse ), ":" );
            whoisModel.setPhone( phone );


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return whoisModel;
    }
}
