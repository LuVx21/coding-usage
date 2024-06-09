package org.luvx.coding.data;

import com.google.common.collect.Lists;

import org.apache.poi.ss.util.CellReference;
import org.luvx.app.entity.Article;

import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

public class DataSource {

    private static final int row    = 10;
    private static final int column = 10;

    public static List<List<String>> header() {
        return IntStream.range(0, column)
                .mapToObj(i -> "列:" + CellReference.convertNumToColString(i))
                .map(List::of)
                .toList();
    }

    public static List<List<String>> data() {
        List<List<String>> data = Lists.newArrayListWithCapacity(row);
        for (int i = 0; i < row; i++) {
            List<String> line = Lists.newArrayListWithCapacity(column);
            for (int j = 0; j < column; j++) {
                line.add(i + "," + CellReference.convertNumToColString(j));
            }
            data.add(line);
        }
        return data;
    }

    public static List<Article> data1() {
        List<Article> list = Lists.newArrayListWithCapacity(row);
        for (int i = 0; i < row; i++) {
            Article data = new Article();
            data.setName("字符串" + i);
            data.setScore(i + 0.56);
            data.setDate(new Date());
            list.add(data);
        }
        return list;
    }
}
