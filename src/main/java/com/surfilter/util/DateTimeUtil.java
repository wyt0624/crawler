package com.surfilter.util;

import java.sql.Timestamp;

public class DateTimeUtil {
    public static Timestamp dateToTimstamp(Long date) {
        Timestamp fromTS1  = null;
        try {
//            SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
//                    "yyyy-MM-dd hh:mm:ss" );
//            Date lFromDate1 = datetimeFormatter1.parse( date );
//            fromTS1 = new Timestamp( lFromDate1.getTime() );
//            return fromTS1;
            fromTS1 = new Timestamp( date );
            return fromTS1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
