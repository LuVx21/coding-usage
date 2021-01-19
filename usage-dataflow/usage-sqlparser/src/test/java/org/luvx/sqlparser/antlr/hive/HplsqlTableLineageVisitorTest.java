package org.luvx.sqlparser.antlr.hive;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.luvx.sqlparser.antlr.hive.pojo.HiveFieldLineage;
import org.luvx.sqlparser.antlr.hive.pojo.HiveTableLineage;
import org.luvx.sqlparser.utils.ReadFileUtils;

import java.util.List;

/**
 * @author Ren, Xie
 */
@Slf4j
public class HplsqlTableLineageVisitorTest {

    static String       sql;
    static CharStream   charStream;
    static HplsqlLexer  sqlLexer;
    static TokenStream  tokenStream;
    static HplsqlParser sqlParser;
    static ParseTree    parseTree;

    static {
        sql = ReadFileUtils.readFile("2.sql");
        charStream = CharStreams.fromString(sql);
        sqlLexer = new HplsqlLexer(charStream);
        tokenStream = new CommonTokenStream(sqlLexer);
        sqlParser = new HplsqlParser(tokenStream);
        parseTree = sqlParser.program();
    }

    @Test
    public void test0() {
        HplsqlTableLineageVisitor visitor = new HplsqlTableLineageVisitor();
        visitor.visit(parseTree);
        HiveTableLineage tableLineage = visitor.getTableLineage();
        System.out.println(JSON.toJSONString(tableLineage, true));

        HplsqlFieldLineageVisitor visitor1 = new HplsqlFieldLineageVisitor();
        visitor1.visit(parseTree);
        List<HiveFieldLineage> list = visitor1.getHiveFieldLineage();
        System.out.println(JSON.toJSONString(list, true));
    }
}