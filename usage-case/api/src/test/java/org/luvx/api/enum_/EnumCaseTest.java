package org.luvx.api.enum_;

import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.lang3.EnumUtils;
import org.junit.Test;
import org.luvx.api.enum_.EnumCase;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;

import io.vavr.API;

public class EnumCaseTest {

    @Test
    public void selectAll() {
        EnumCase sat = Enum.valueOf(EnumCase.class, "SAT");
        // API.println(sat.getCode());
        API.println(EnumUtils.getEnumList(EnumCase.class));

        for (EnumCase e : EnumCase.values()) {
            // System.out.println(e);
            // System.out.println(e.getDeclaringClass());
            // System.out.println(e.ordinal());
        }
    }

    /**
     * EnumSet
     */
    @Test
    public void EnumSetTest() {
        EnumSet<EnumCase> sets = EnumSet.allOf(EnumCase.class);
        for (EnumCase enumCase : sets) {
            System.out.println(enumCase);
        }
    }

    /**
     * EnumMap
     */
    @Test
    public void EnumMapTest() {
        EnumMap<EnumCase, String> enumMap = new EnumMap<>(EnumCase.class);
        enumMap.put(EnumCase.SAT, "6");
        for (Iterator<Map.Entry<EnumCase, String>> iter = enumMap.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<EnumCase, String> entry = iter.next();
            System.out.println(entry.getKey().name() + ":" + entry.getValue());
        }
    }


}
