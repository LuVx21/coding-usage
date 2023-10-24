package org.luvx.coding;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTest {
    @Test
    void m1() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate now = LocalDate.now();
        System.out.println(dateTimeFormatter.format(now));
    }
}
