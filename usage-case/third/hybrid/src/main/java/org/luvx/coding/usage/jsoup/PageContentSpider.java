package org.luvx.coding.usage.jsoup;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Setter
@Getter
@ToString
@Accessors(fluent = true)
public class PageContentSpider {
    /**
     * 当页的前几个
     */
    private int countInPage = 2;

    private String    chapterListRule;
    private QueryRule chapterTitleRule;
    private QueryRule chapterUrlRule;

    private QueryRule                articleRule;
    private Function<String, String> articlePostUrlProcessor         = Function.identity();
    @Nullable
    private QueryRule                articleNextPageRule;
    private Function<String, String> articleNextPagePostUrlProcessor = Function.identity();

    public List<Element> getIndexList(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.select(chapterListRule);
    }

    public String article(String title, String url) throws IOException {
        StringBuilder article = new StringBuilder();
        String pageUrl = url;
        do {
            Document document = Jsoup.connect(pageUrl).get();
            Elements a = document.select(articleRule.elementQuery);
            for (Element element : a) {
                article.append(getValue(element, articleRule.valueQuery)).append("\n");
            }

            if (articleNextPageRule != null) {
                Element nextPageUrl = document.selectFirst(articleNextPageRule.elementQuery);
                pageUrl = getValue(nextPageUrl, articleNextPageRule.valueQuery);
                if (!pageUrl.startsWith("http")) {
                    URI uri = URI.create(url);
                    String domain = STR."\{uri.getScheme()}://\{uri.getHost()}";
                    pageUrl = domain + pageUrl;
                }
                pageUrl = articleNextPagePostUrlProcessor.apply(pageUrl);
            } else {
                pageUrl = null;
            }
        } while (isNotBlank(pageUrl));
        return articlePostUrlProcessor.apply(article.toString());
    }

    public List<Pair<String, String>> visit(String chapterListUrl) throws IOException {
        List<Element> indexList = getIndexList(chapterListUrl);
        if (CollectionUtils.isEmpty(indexList)) {
            return Lists.newArrayList();
        }

        List<Pair<String, String>> result = Lists.newArrayList();
        int max = Math.min(indexList.size(), countInPage);
        for (int i = 0; i < max; i++) {
            Element element = indexList.get(i);
            Element e1 = isNotBlank(chapterTitleRule.elementQuery) ? element.selectFirst(chapterTitleRule.elementQuery) : element;
            String title = getValue(e1, chapterTitleRule.valueQuery);

            Element e2 = isNotBlank(chapterUrlRule.elementQuery) ? element.selectFirst(chapterUrlRule.elementQuery) : element;
            String href = getValue(e2, chapterUrlRule.valueQuery);
            log.info("{}: {}", title, href);
            if (StringUtils.isBlank(href)) {
                continue;
            }
            String article = article(title, href);
            log.info("内容: {}", article);
            result.add(Pair.of(title, article));
        }
        return result;
    }

    private String getValue(Element element, String rule) {
        if (element == null || StringUtils.isBlank(rule)) {
            return "";
        }
        return switch (rule) {
            case "text" -> element.text();
            case "data" -> element.data();
            case "href" -> element.absUrl(rule);
            default -> element.attr(rule);
        };
    }

    public static class QueryRule {
        /**
         * 找到具体的标签
         */
        private String elementQuery;
        /**
         * 获取标签的指定值
         */
        private String valueQuery;

        public static QueryRule of(String elementQuery, String valueQuery) {
            QueryRule queryRule = new QueryRule();
            queryRule.elementQuery = elementQuery;
            queryRule.valueQuery = valueQuery;
            return queryRule;
        }
    }
}
