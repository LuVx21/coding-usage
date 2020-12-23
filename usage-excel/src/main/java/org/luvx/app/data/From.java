package org.luvx.app.data;

import org.apache.poi.ss.util.CellReference;
import org.luvx.app.entity.DownloadData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ren, Xie
 */
public class From {

    private static final int row    = 10;
    private static final int column = 10;

    public static List<List<String>> header() {
        List<List<String>> data = new ArrayList<>(1);
        for (int i = 0; i < column; i++) {
            List<String> line = new ArrayList<>(1);
            line.add("列:" + CellReference.convertNumToColString(i));
            data.add(line);
        }
        return data;
    }

    public static List<List<String>> data() {
        List<List<String>> data = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            List<String> line = new ArrayList<>();
            for (int j = 0; j < column; j++) {
                line.add(i + "," + CellReference.convertNumToColString(j));
            }
            data.add(line);
        }
        return data;
    }

    public static List<DownloadData> data1() {
        List<DownloadData> list = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            DownloadData data = new DownloadData();
            data.setString("字符串" + i);
            data.setDoubleData(i + 0.56);
            data.setDate(new Date());
            list.add(data);
        }
        return list;
    }
}
