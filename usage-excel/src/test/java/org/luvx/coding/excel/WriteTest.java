package org.luvx.coding.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.merge.LoopMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.luvx.app.entity.Article;
import org.luvx.app.entity.User;
import org.luvx.coding.data.DataSource;
import org.luvx.coding.utils.EasyExcelUtils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class WriteTest {
    private static final String home = System.getProperty("user.home") + "/Desktop/";
    private static final String path = home + "11_" + "1.xlsx";

    @Test
    void m1() {
        List<List<String>> header = DataSource.header();
        List<List<String>> data = DataSource.data();

        ExcelWriter writer = EasyExcel // new ExcelWriterBuilder()
                .write(path)
                .excelType(ExcelTypeEnum.XLSX)
                // .needHead(false)
                .build();

        // 1: 写入 sheet-table
        WriteSheet sheet1 = EasyExcel.writerSheet(0, "no1").build();
        WriteTable table = EasyExcel.writerTable(0).head(header).build();
        writer.write(data, sheet1, table);

        // 2: 写入多个 sheet
        // WriteSheet sheet1 = EasyExcel.writerSheet(0, "no1").head(header).build();
        // WriteSheet sheet2 = EasyExcel.writerSheet(1, "no2").head(header).build();
        // writer
        //         .write(data, sheet1)
        //         .write(data, sheet2);

        writer.finish();
    }

    @Test
    void m2() {
        String path = home + "2.xlsx";
        EasyExcel
                .write(path, Article.class)
                .sheet("no1")
                .doWrite(DataSource.data1());
    }

    @Test
    void m3() {
        String path = home + "1.xlsx";

        List<User> users = Lists.newArrayList();
        for (int i = 0; i < 8; i++) {
            User user = new User();
            user.setId(1L);
            user.setUserName("foo_" + (i + 1));
            user.setPassword("bar_" + (i + 1));
            user.setAge(i + 18);
            user.setBirthday(LocalDate.of(2023, 4, 1 + i));
            users.add(user);
        }

        LoopMergeStrategy writeHandler = new LoopMergeStrategy(4, 0);
        LongestMatchColumnWidthStyleStrategy writeHandler1 = new LongestMatchColumnWidthStyleStrategy();

        List<List<String>> header = List.of(
                List.of("id"),
                List.of("姓名-new"),
                List.of("密码"),
                List.of("年龄-new"),
                List.of("生日")
        );

        EasyExcel
                .write(path, User.class)
                .excelType(ExcelTypeEnum.XLSX)
                .head(header)
                .sheet("sheet1")
                .registerWriteHandler(writeHandler)
                .registerWriteHandler(writeHandler1)
                .doWrite(users);
    }


    /**
     * 模板
     */
    @Test
    void m4() {
        String template = home + "1.xlsx";
        String fileName = home + "2.xlsx";

        List<Article> data = DataSource.data1();
        EasyExcel.write(fileName, Article.class)
                .withTemplate(template)
                .sheet("no1")
                .doWrite(data);
    }

    @Test
    void m5() {
        Map<Integer, List<String>> header = EasyExcelUtils.getHeader(Article.class);
        header.entrySet()
                .forEach(System.out::println);
    }
}
