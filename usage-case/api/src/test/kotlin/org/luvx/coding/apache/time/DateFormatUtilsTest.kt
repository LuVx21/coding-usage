package org.luvx.coding.apache.time

import org.apache.commons.lang3.time.DateFormatUtils
import org.junit.jupiter.api.Test
import org.luvx.coding.common.enhancer.out
import java.util.*

internal class DateFormatUtilsTest {
    @Test
    fun m1() {
        DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(Date()).out()
    }
}
