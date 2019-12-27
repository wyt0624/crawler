package com.surfilter.whois.utils.whoisparsers;

import com.surfilter.whois.models.WhoisModel;
import com.surfilter.whois.utils.IpUtil;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
/**
 * Created by dell on 2017/11/17.
 */

/**
 * Domain Name: shou.org.cn
 * ROID: 20100709s10051s53120303-cn
 * Domain Status: ok
 * Registrant ID: hc099798798-cn
 * Registrant: 上海电视大学
 * Registrant Contact Email: jichenjun@shtvu.edu.cn
 * Sponsoring Registrar: 阿里云计算有限公司（万网）
 * Name Server: dns13.hichina.com
 * Name Server: dns14.hichina.com
 * Registration Time: 2010-07-09 14:36:16
 * Expiration Time: 2019-07-09 14:36:16
 * DNSSEC: unsigned
 */
public class CnParser extends AParser {
    private CnParser() {
    }

    private static CnParser instance = null;

    public static CnParser getInstance() {
        if (instance == null) {
            instance = new CnParser();
        }
        return instance;
    }

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
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss'Z'" );

    @Override
    public WhoisModel parseWhois(String whoisResponse) {
        WhoisModel whoisModel = new WhoisModel();
        try {
            String domain = getFieldValue( getMatchField( domainPattern, whoisResponse ), ":" );
            whoisModel.setDomain( domain );
            whoisModel.setIp( IpUtil.getIpByDomain( domain ) );
            String ctime = getFieldValue( getMatchField( ctimePattern, whoisResponse ), ":" );
            whoisModel.setCtime( simpleDateFormat.parse( ctime ).getTime() );
            String utime = getFieldValue( getMatchField( utimePattern, whoisResponse ), ":" );
            whoisModel.setUtime( simpleDateFormat.parse( utime ).getTime() );
            String etime = getFieldValue( getMatchField( etimePattern, whoisResponse ), ":" );
            whoisModel.setEtime( simpleDateFormat.parse( etime ).getTime() );
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
