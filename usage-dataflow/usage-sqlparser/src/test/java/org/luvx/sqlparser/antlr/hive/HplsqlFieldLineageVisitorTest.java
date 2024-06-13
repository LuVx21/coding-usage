package org.luvx.sqlparser.antlr.hive;

import com.alibaba.fastjson2.JSON;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.luvx.sqlparser.antlr.hive.pojo.HiveFieldLineage;
import org.luvx.sqlparser.utils.ReadFileUtils;

import java.util.List;

/**
 * @author Ren, Xie
 */
@Slf4j
public class HplsqlFieldLineageVisitorTest {

    static String       sql;
    static CharStream   charStream;
    static HplsqlLexer  sqlLexer;
    static TokenStream  tokenStream;
    static HplsqlParser sqlParser;
    static ParseTree    parseTree;

    static {
        sql = ReadFileUtils.readFile("1.sql");
        charStream = CharStreams.fromString(sql);
        sqlLexer = new HplsqlLexer(charStream);
        tokenStream = new CommonTokenStream(sqlLexer);
        sqlParser = new HplsqlParser(tokenStream);
        parseTree = sqlParser.program();
    }

    @Test
    public void test0() {
        Stopwatch started = Stopwatch.createStarted();

        HplsqlFieldLineageVisitor visitor = new HplsqlFieldLineageVisitor(sql);
        visitor.visit(parseTree);
        List<HiveFieldLineage> list = visitor.getHiveFieldLineage1();
        log.info("字段血缘关系:{}", JSON.toJSONString(list));

        log.info("耗时:{}", started.stop());
    }
}