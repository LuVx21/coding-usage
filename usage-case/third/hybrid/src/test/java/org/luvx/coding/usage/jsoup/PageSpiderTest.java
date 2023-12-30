package org.luvx.coding.usage.jsoup;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.util.CsvUtils;
import org.luvx.coding.usage.jsoup.PageContentSpider.QueryRule;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
class PageSpiderTest {
    @Test
    void m1() throws IOException {
        new PageContentSpider()
                .chapterListRule("ul.chapters li a")
                .chapterTitleRule(QueryRule.of("", "text"))
                .chapterUrlRule(QueryRule.of("", "href"))
                .articleRule(QueryRule.of("div#novelcontent > p", "text"))
                .articleNextPageRule(QueryRule.of("div#novelcontent ul.novelbutton a#pb_next", "href"))
                .articleNextPagePostUrlProcessor(url -> url.endsWith("_2.html") ? url : "")
                .visit("https://m.lnsjkc.com/46/46869_7/");
    }

    /**
     * .me
     * .cc
     */
    @Test
    void m2() throws Exception {
        Predicate<String> filter = line -> StringUtils.isNotEmpty(line) && !line.contains("#genre#");
        Function<String, String> mapper = line -> {
            String[] split = line.split(",");
            return split[1];
        };
        List<String> lines = CsvUtils.csvPick("", filter, mapper);
        Set<String> sets = Sets.newHashSet(lines);

        Function<String, String> postProcessor = script -> {
            String json = script.substring(script.indexOf("{"));
            JSONObject jsonObject = JSON.parseObject(json);
            return jsonObject.getString("url");
        };
        new PageContentSpider()
                .countInPage(999)
                // .chapterListRule("div.detail_right_div > ul:eq(1) li p:eq(0)")
                // .chapterTitleRule(QueryRule.of("img", "title"))
                // .chapterUrlRule(QueryRule.of("a", "href"))
                // .articleRule(QueryRule.of("div.detail_right_tab div#bofang_box script:eq(0)", "data"))
                // .articlePostUrlProcessor(postProcessor)

                .chapterListRule("div.stui-vodlist__box a.stui-vodlist__thumb")
                .chapterTitleRule(QueryRule.of("", "title"))
                .chapterUrlRule(QueryRule.of("", "href"))
                .articleRule(QueryRule.of("div.stui-warp-content div.stui-pannel div.stui-player__video script", "data"))
                .articlePostUrlProcessor(postProcessor)
                .visit("")

                .stream()
                .filter(p -> !sets.contains(p.getValue()))
                .forEach(p -> log.info("结果: {},{}", p.getKey(), p.getValue()));
    }

    @Test
    void m3() throws IOException {
        Document doc = Jsoup.parse(new File("/Users/renxie/Library/CloudStorage/OneDrive-个人/Code/coding-usage/usage-case/1.html"));
        Elements e = doc.select("ul.stui-vodlist li div.stui-vodlist__box a.stui-vodlist__thumb");
        System.out.println(e.size());
        System.out.println(e.first());
        // System.out.println(e.select());
    }
}