package com.hsenid.frauddetection.solrsearch.functions;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class TimeFunctionsTest {


    @Test
    public void timestampToISO() {
        LocalDateTime localDateTime = LocalDateTime.parse("2019-03-03T00:00:02");
        System.out.println();
    }

    @Test
    public void timestampToISOWithOutUTC() {
    }

    @Test
    public void getFilterDate() {
    }

    @Test
    public void addTimeFilter() {
    }

    @Test
    public void addTimeFilterDateXTillDocTime() {

    }

    @Test
    public void getDateTimeWithXY() {
    }

    @Test
    public void ISODateStringToLocalDate() {
//        TimeFunctions.ISOD!);
    }
}