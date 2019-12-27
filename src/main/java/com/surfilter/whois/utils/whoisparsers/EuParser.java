package com.surfilter.whois.utils.whoisparsers;

import com.surfilter.whois.models.WhoisModel;
import com.surfilter.whois.utils.IpUtil;

import java.util.regex.Pattern;
/**
 * Created by dell on 17-11-18.
 */

/**
 * % The WHOIS service offered by EURid and the access to the records
 * % in the EURid WHOIS database are provided for information purposes
 * % only. It allows persons to check whether a specific domain name
 * % is still available or not and to obtain information related to
 * % the registration records of existing domain names.
 * %
 * % EURid cannot, under any circumstances, be held liable in case the
 * % stored information would prove to be wrong, incomplete or not
 * % accurate in any sense.
 * %
 * % By submitting a query you agree not to use the information made
 * % available to:
 * %
 * % - allow, enable or otherwise support the transmission of unsolicited,
 * %   commercial advertising or other solicitations whether via email or
 * %   otherwise;
 * % - target advertising in any possible way;
 * %
 * % - to cause nuisance in any possible way to the registrants by sending
 * %   (whether by automated, electronic processes capable of enabling
 * %   high volumes or other possible means) messages to them.
 * %
 * % Without prejudice to the above, it is explicitly forbidden to extract,
 * % copy and/or use or re-utilise in any form and by any means
 * % (electronically or not) the whole or a quantitatively or qualitatively
 * % substantial part of the contents of the WHOIS database without prior
 * % and explicit permission by EURid, nor in any attempt hereof, to apply
 * % automated, electronic processes to EURid (or its systems).
 * %
 * % You agree that any reproduction and/or transmission of data for
 * % commercial purposes will always be considered as the extraction of a
 * % substantial part of the content of the WHOIS database.
 * %
 * % By submitting the query you agree to abide by this policy and accept
 * % that EURid can take measures to limit the use of its WHOIS services
 * % in order to protect the privacy of its registrants or the integrity
 * % of the database.
 * %
 * % The EURid WHOIS service on port 43 (textual whois) never
 * % discloses any information concerning the registrant.
 * % Registrant and onsite contact information can be obtained through use of the
 * % webbased whois service available from the EURid website www.eurid.eu
 * %
 * % WHOIS europa.eu
 * Domain: europa.eu
 * <p>
 * Registrant:
 * NOT DISCLOSED!
 * Visit www.eurid.eu for webbased whois.
 * <p>
 * Onsite(s):
 * NOT DISCLOSED!
 * Visit www.eurid.eu for webbased whois.
 * <p>
 * Technical:
 * Name: Proximus DNS Masters
 * Organisation: Proximus sa/nv
 * Language: en
 * Phone: +32.80023452
 * Fax: +32.22403632
 * Email: dnsmaster@proximus.com
 * <p>
 * Registrar:
 * Name: Proximus nv/sa
 * Website: https://www.proximus.be
 * <p>
 * Name servers:
 * ns1bru.europa.eu (147.67.250.2)
 * ns2eu.bt.net
 * ns3bru.europa.eu (2a01:7080:14:101::2)
 * ns2lux.europa.eu (147.67.12.3)
 * ns1lux.europa.eu (147.67.12.2)
 * ns2bru.europa.eu (147.67.250.3)
 * ns1.be.colt.net
 * ns1.bt.net
 * ns3lux.europa.eu (2a01:7080:24:101::2)
 * <p>
 * Please visit www.eurid.eu for more info.
 */
public class EuParser extends AParser {
    private EuParser() {
    }

    private static EuParser instance = null;

    public static EuParser getInstance() {
        if (instance == null) {
            instance = new EuParser();
        }
        return instance;
    }

    private final String DOMAINREG = "\\s*Domain:\\s*[^\\n]+";
    private final String EMAILREG = "\\s*Email:\\s*[^\\n]+";
    private final String PHONEREG = "\\s*Phone:\\s*[^\\n]+";
    private Pattern domainPattern = Pattern.compile( DOMAINREG );
    private Pattern emailPattern = Pattern.compile( EMAILREG );
    private Pattern phonePattern = Pattern.compile( PHONEREG );

    public WhoisModel parseWhois(String whoisResponse) {
        WhoisModel whoisModel = new WhoisModel();
        try {
            String domain = getFieldValue( getMatchField( domainPattern, whoisResponse ), ":" );
            whoisModel.setDomain( domain );
            whoisModel.setIp( IpUtil.getIpByDomain( domain ) );
            String email = getFieldValue( getMatchField( emailPattern, whoisResponse ), ":" );
            String phone = getFieldValue( getMatchField( phonePattern, whoisResponse ), ":" );
            whoisModel.setEmail( email );
            whoisModel.setPhone( phone );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return whoisModel;
    }
}
