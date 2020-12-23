package org.luvx.excel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import lombok.extern.slf4j.Slf4j;
import org.luvx.app.data.From;
import org.luvx.app.entity.DownloadData;

import java.io.File;
import java.util.List;

/**
 * 不定量字段数动态写入
 *
 * @author Ren, Xie
 */
@Slf4j
public class Write1 {
    private static final String path;

    static {
        String home = System.getProperty("user.home") + "/Desktop/";
        path = home + Write1.class.getSimpleName() + "1.xlsx";
    }

    public static void write0() {
        List<List<String>> header = From.header();
        List<List<String>> data = From.data();

        ExcelWriter writer = new ExcelWriterBuilder().file(path).excelType(ExcelTypeEnum.XLSX)
                // .needHead(Boolean.FALSE)
                .build();

        // 1: 写入 sheet-table
        WriteSheet sheet = EasyExcel.writerSheet(0, "no1").build();
        WriteTable table = EasyExcel.writerTable(0).head(header).build();
        writer.write(data, sheet, table);

        // 2: 写入多个 sheet
        // WriteSheet sheet1 = EasyExcel.writerSheet(0, "no1").head(header).build();
        // writer.write(data, sheet1);
        // WriteSheet sheet2 = EasyExcel.writerSheet(1, "no2").head(header).build();
        // writer.write(data, sheet2);

        writer.finish();
    }

    public static void write1() {
        EasyExcel.write(path)
                // .registerWriteHandler(new CustomCellWriteHandler())
                // .registerWriteHandler(this.createStyleStrategy())
                .sheet("no1")
                .head(From.header())
                .doWrite(From.data());
    }

    public static void write3() {
        List<List<String>> data = From.data();
        data.clear();
        EasyExcel.write(path)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet("no1")
                .head(From.header())
                .doWrite(data);
    }

    public static void write4() {
        ExcelWriter excelWriter = EasyExcel.write(path).head(From.header()).build();
        WriteSheet sheet = EasyExcel.writerSheet("no1")
                .build();
        for (int i = 0; i < 10; i++) {
            List<List<String>> data = From.data();
            excelWriter.write(data, sheet);
        }
        excelWriter.finish();
    }

    /**
     * 模板
     */
    public static void write5() {
        String template =
                "D:\\work\\open\\core\\src\\main\\resources\\templates\\隽宝贝计划保全信息分发.xlsx";
        String fileName =
                "D:\\work\\open\\core\\src\\main\\resources\\templates\\隽宝贝计划保全信息分发_1588902762710.xlsx";

        EasyExcel.write(fileName, DownloadData.class)
                .withTemplate(template)
                .sheet()
                .doWrite(From.data1());

        // EasyExcel.write(fileName)
        //         .head(head())
        //         .sheet("Sheet1")
        //         .doWrite(data());
    }

    public static void main(String[] args) {
        write3();
    }
}
