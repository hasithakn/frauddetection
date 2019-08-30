package com.hsenid.frauddetection.solrsearch.functions;

import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import org.apache.solr.common.SolrDocument;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class TimeFunctions {
    public static String timestampToISO(Date date) {
        try {
            String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String isoTS = sdf.format(date);
            return isoTS;
        } catch (Exception e) {
            if (date != null) {
                return date.toString();
            } else {
                return "";
            }
        }
    }

//    public static String ISODateStringToLocalDate(String date) {
//        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
//            LocalDateTime t = LocalDateTime.parse(date, formatter);
//            Instant instant = t.toInstant(ZoneOffset.UTC);
//            Date from = Date.from(instant);
////            int year = t.getYear();
////            int month= t.getMonthValue();
////            int day = t.getDayOfMonth();
////            int hour = t.getHour();
////            int min = t.getMinute();
////            int sec = t.getSecond();
////            Date date1 = new Date(Date.UTC(year,month,day,hour,min,sec));
//            return "";
//        } catch (Exception e) {
//            return "";
//        }
//    }

    public static String timestampToISOWithOutUTC(Date date) {
        try {
            String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            String isoTS = sdf.format(date);
            return isoTS;
        } catch (Exception e) {
            if (date != null) {
                return date.toString();
            } else {
                return "";
            }
        }
    }

    public static String getFilterDate(String timeFilter1, String timeFilter2, String timeTemp) {
        StringBuffer temp = new StringBuffer();
        temp.append("datetime: [");
        temp.append(timeTemp);
        temp.append(timeFilter1);
        temp.append(" TO ");
        temp.append(timeTemp + timeFilter2 + " ]");
        return temp.toString();
    }

    public static String addTimeFilter(SolrDocument entrie, String timeFilter1, String timeFilter2) {
        String timeTemp = timestampToISO((Date) entrie.getFieldValue("datetime"));
        return getFilterDate(timeFilter1, timeFilter2, timeTemp);
    }

    public static String addTimeFilterDateXTillDocTime(SolrEntity entrie, String x) {
        StringBuffer temp = new StringBuffer();
        temp.append("datetime: [");
        temp.append(x);
        temp.append(" TO ");
        temp.append(DateTimeFormatter.ISO_INSTANT.format(entrie.getDatetime()) + " ]");
        return temp.toString();
    }

    public static String getDateTimeWithXY(String x, String y) {
        StringBuffer temp = new StringBuffer();
        temp.append("datetime: [");
        temp.append(x);
        temp.append(" TO ");
        temp.append(y + " ]");
        return temp.toString();
    }
}
