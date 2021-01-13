package org.luvx.druid.lineage;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.luvx.druid.lineage.pojo.LineageField;
import org.luvx.druid.lineage.pojo.TreeNode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LineageAnalyzerTest {
    final static String sqlList = """
            ## 1
            select aa.user_id,aa.user_name
            from user aa

            ## 2
            select
            user_id as uid
            ,user_name as uname
            from
            (
                select user_id, concat("test",user_name) as user_name
                from user
            )t

            ## 3
            select
                user_id
                ,user_name
            from user
            union all
            select
                sub_user_id as user_id
                ,sub_user_name as user_name
            from sub_user

            ## 4
            select t1.user_id,t2.user_name,t2.sub_user_id,t2.sub_user_name
            from user t1
            left join sub_user t2
            on t1.user_id = t2.user_id
            """;

    String sql;

    @Before
    public void before() {
        String sqlId = "2";
        Matcher matcher = Pattern.compile("##\\s.*\\S").matcher(sqlList);
        List<String> list = Arrays.asList(sqlList.split("##\\s.*\\S"));
        int j = 1;
        Map<String, String> map = new LinkedHashMap<>();
        while (matcher.find()) {
            String key = matcher.group().replace("##", "").trim();
            map.put(key, list.get(j));
            j++;
        }
        sql = map.get(sqlId);
    }

    @Test
    public void test0() {
        log.info("解析sql:\n{}\n\n", SQLUtils.format(sql, JdbcConstants.MYSQL));
        TreeNode<LineageField> root = new TreeNode<>(new LineageField());
        LineageAnalyzer.mysqlFieldLineageAnalyzer(sql, root);
        for (TreeNode<LineageField> e : root.getChildren()) {
            Set<LineageField> leafNodes = e.getAllLeafData();
            for (LineageField f : leafNodes) {
                if (f.getIsEnd()) {
                    log.info("{}:\n{}", e.getData().getField(), JSONObject.toJSONString(f, true));
                }
            }
        }

        // log.info("--------------------------------------");

        // for (TreeNode<LineageColumn> node : root) {
        //     System.out.println(
        //             "     ".repeat(Math.max(0, node.getLevel() - 1))
        //                     + JSONObject.toJSONString(node.getData()) + "\n"
        //     );
        // }
    }
}