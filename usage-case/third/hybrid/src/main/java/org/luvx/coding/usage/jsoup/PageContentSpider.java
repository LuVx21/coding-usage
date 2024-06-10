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
import org.luvx.coding.common.consts.Commons;
import org.luvx.coding.common.net.UrlUtils;

import jakarta.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.luvx.coding.common.consts.Common.RATE_LIMITER_SUPPLIER;

@Slf4j
@Setter
@Getter
@ToString
@Accessors(fluent = true)
public class PageContentSpider {
    private static final Function<String, String> IDENTITY = Function.identity();

    /**
     * 翻几页
     */
    private int pageCount   = 2;
    /**
     * 当页的前几个
     */
    private int countInPage = 2;

    private String    chapterListRule;
    private QueryRule chapterTitleRule;
    private QueryRule chapterUrlRule;

    @Nullable
    private QueryRule                chapterNextPageRule;
    private Function<String, String> chapterNextPageUrlPostProcessor = IDENTITY;

    private QueryRule                articleRule;
    private Function<String, String> articlePostUrlProcessor         = IDENTITY;
    /**
     * 单一文章内可能翻页
     */
    @Nullable
    private QueryRule                articleNextPageRule;
    private Function<String, String> articleNextPageUrlPostProcessor = IDENTITY;

    public String article(String url) throws IOException {
        StringBuilder article = new StringBuilder();
        String pageUrl = url;
        for (int i = 0; isNotBlank(pageUrl); i++) {
            log.info("解析内容页: {}", pageUrl);
            Commons.getLimiter(pageUrl).acquire();
            Document document = Jsoup.connect(pageUrl).get();
            Elements a = document.select(articleRule.elementQuery);
            for (Element element : a) {
                article.append(getValue(element, articleRule.valueQuery)).append("\n");
            }

            String finalPageUrl = pageUrl;
            pageUrl = Optional.ofNullable(articleNextPageRule)
                    .map(r -> getValue(document, r))
                    .map(aa -> UrlUtils.urlAddDomain(url, aa))
                    .map(articleNextPageUrlPostProcessor)
                    .filter(u -> !finalPageUrl.equals(u))
                    .orElse(null);
        }
        return articlePostUrlProcessor.apply(article.toString());
    }

    public List<Pair<String, String>> visit(String url) throws IOException {
        List<Pair<String, String>> result = Lists.newArrayList();
        String pageUrl = url;
        for (int i = 0; i < pageCount && isNotBlank(pageUrl); i++) {
            log.info("解析目录页: {}", pageUrl);
            Commons.getLimiter(pageUrl).acquire();
            Document doc = Jsoup.connect(pageUrl).get();
            List<Element> indexList = doc.select(chapterListRule);
            if (CollectionUtils.isEmpty(indexList)) {
                return result;
            }
            int max = Math.min(indexList.size(), countInPage);
            for (int k = 0; k < max; k++) {
                Element element = indexList.get(k);
                String title = getValue(element, chapterTitleRule);

                String href = getValue(element, chapterUrlRule);
                log.info("目录页内容: {} {} {}", k, title, href);
                if (StringUtils.isBlank(href)) {
                    continue;
                }
                String article = article(href);
                log.info("内容页内容: {}", article);
                result.add(Pair.of(title, article));
            }

            String finalPageUrl = pageUrl;
            pageUrl = Optional.ofNullable(chapterNextPageRule)
                    .map(r -> getValue(doc, r))
                    .map(aa -> UrlUtils.urlAddDomain(url, aa))
                    .map(chapterNextPageUrlPostProcessor)
                    .filter(u -> !finalPageUrl.equals(u))
                    .orElse(null);
        }
        return result;
    }

    private String getValue(Element element, QueryRule rule) {
        Element e = isNotBlank(rule.elementQuery) ? element.selectFirst(rule.elementQuery) : element;
        return getValue(e, rule.valueQuery);
    }

    private String getValue(Element element, String elementQuery) {
        if (element == null || StringUtils.isBlank(elementQuery)) {
            return "";
        }
        return switch (elementQuery) {
            case "text" -> element.text();
            case "data" -> element.data();
            case "href" -> element.absUrl(elementQuery);
            default -> element.attr(elementQuery);
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
