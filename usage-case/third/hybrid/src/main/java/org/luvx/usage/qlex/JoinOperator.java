package org.luvx.usage.qlex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Lists;
import com.ql.util.express.Operator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JoinOperator extends Operator {
    @Override
    public Object executeInner(Object[] array) {
        log.info("参数:{}", Arrays.toString(array));

        if (ArrayUtils.isEmpty(array)) {
            return Collections.emptyList();
        }
        List<Object> result = Lists.newArrayList();
        Object first = array[0];
        if (first instanceof List) {
            result.addAll((List) first);
        } else {
            result.add(first);
        }

        for (int i = 1; i < array.length; i++) {
            result.add(array[i]);
        }

        return result;
    }
}
