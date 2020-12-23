package org.luvx.excel.write;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.luvx.app.data.From;
import org.luvx.app.entity.DownloadData;

/**
 * 配合实体类写入
 *
 * @author Ren, Xie
 */
@Slf4j
public class Write2 {
    private static final String path;

    static {
        String home = System.getProperty("user.home") + "/Desktop/";
        path = home + Write2.class.getSimpleName() + "1.xlsx";
        System.out.println(path);
    }

    public static void write0() {
        EasyExcel
                .write(path, DownloadData.class)
                .sheet("no1")
                .doWrite(From.data1());
    }

    public static void main(String[] args) {
        write0();
    }
}
