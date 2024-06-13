package org.luvx.sqlparser.druid.lineage;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.luvx.sqlparser.druid.lineage.pojo.LineageField;
import org.luvx.sqlparser.druid.lineage.pojo.TreeNode;

import java.util.Map;
import java.util.Set;

@Slf4j
public class LineageAnalyzerTest {

    String sql1 = "select aa.user_id,aa.user_name from user aa";
    String sql2 = """
            select
            user_id as uid
            ,user_name as uname
            from
            (
             select user_id, concat("test",user_name) as user_name
             from user
            )t
            """;
    String sql3 = """
            select
                user_id
                ,user_name
            from user
            union all
            select
                sub_user_id as user_id
                ,sub_user_name as user_name
            from sub_user
            """;
    String sql4 = """
            select t1.user_id,t2.user_name,t2.sub_user_id,t2.sub_user_name
            from user t1
            left join sub_user t2
            on t1.user_id = t2.user_id
            """;
    String sql;

    @Before
    public void before() {
        sql = sql1;
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
                    log.info("{}:\n{}", e.getData().getField(), JSONObject.toJSONString(f));
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

    @Test
    public void test1() {
        MySqlStatementParser parser = new MySqlStatementParser(sql1);
        SQLStatement stmt = parser.parseStatement();
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        stmt.accept(visitor);
        Map<TableStat.Name, TableStat> map = visitor.getTables();
        for (Map.Entry<TableStat.Name, TableStat> entry : map.entrySet()) {
            TableStat stat = entry.getValue();
            String operator = stat.toString();
            System.out.println(operator);
        }
    }
}