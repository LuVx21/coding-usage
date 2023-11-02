package org.luvx.coding.usage.jsoup;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.phantomthief.util.ThrowableFunction;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.CharSource;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.util.CsvUtils;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.luvx.coding.common.SupplierTool.httpClientSupplier;

@Slf4j
class MainTest {
    private final RateLimiter limiter = RateLimiter.create(0.5);

    private List<String> m1_1(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return Lists.newArrayList();
        }
        Document doc = Jsoup.connect(url).get();
        Element ul = doc.select("ul.stui-vodlist").first();

        URI uri = URI.create(url);
        String domain = uri.getScheme() + "://" + uri.getHost();
        return ul.children().stream()
                // .skip(3)
                .map(child -> {
                    Element child1 = child.child(0);
                    Elements select = child1.select("div.stui-vodlist__box a.stui-vodlist__thumb");
                    String title = select.attr("title");
                    String href = domain + select.attr("href");
                    log.info("{},{}", title, href);
                    return href;
                })
                .collect(Collectors.toList());
    }

    private Pair<String, String> m1_2(String url) throws Exception {
        limiter.acquire();
        Document doc = Jsoup.connect(url).get();
        Element first = doc.select("div.stui-warp-content div.stui-pannel__head h3.title").first();
        String title = first.text();

        Element child = doc.select("div.stui-warp-content div.stui-pannel div.stui-player__video script").first();
        String script = child.data();
        String json = script.substring(script.indexOf("{"));
        JSONObject jsonObject = JSON.parseObject(json);
        String _url = jsonObject.getString("url");

        // log.info("{},{}", title, _url);
        return Pair.of(title, _url);
    }

    private List<String> m2_1(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return Lists.newArrayList();
        }
        Document doc = Jsoup.connect(url).get();
        Element ul = doc.select("div.detail_right_div ul").first();

        URI uri = URI.create(url);
        String domain = uri.getScheme() + "://" + uri.getHost();
        return ul.children().stream()
                .map(child -> {
                    Elements select = child.select("p.img img.lazy");
                    String title = select.attr("title");
                    Elements select1 = child.select("p.img a");
                    String href = domain + select1.attr("href");
                    log.info("{},{}", title, href);
                    return href;
                })
                .collect(Collectors.toList());
    }

    private Pair<String, String> m2_2(String url) throws Exception {
        limiter.acquire();
        Document doc = Jsoup.connect(url).get();
        Element first = doc.select("div.detail_right_tab div.gc_video_content div.gc_vodeo div.watch span.title").first();
        String title = first.text();

        Element child = doc.select("div.detail_right_tab div#bofang_box script").first();
        String script = child.data();
        String json = script.substring(script.indexOf("{"));
        JSONObject jsonObject = JSON.parseObject(json);
        String _url = jsonObject.getString("url");

        // log.info("{},{}", title, _url);
        return Pair.of(title, _url);
    }

    private List<String> m3_1(String url) throws Exception {
        return Lists.newArrayList();
    }

    private Pair<String, String> m3_2(String url) throws Exception {
        limiter.acquire();
        Document doc = Jsoup.connect(url).get();
        Elements select = doc.select("head script");
        String json = select.get(1).data();
        JSONObject jsonObject = JSON.parseObject(json);
        String title = jsonObject.getString("name");
        String contentUrl = jsonObject.getString("contentUrl");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(contentUrl))
                .timeout(Duration.ofMillis(5_009))
                .build();
        HttpResponse<String> response = httpClientSupplier.get().send(request, HttpResponse.BodyHandlers.ofString());
        URI uri = response.uri();
        String _url = uri.toString();

        // log.info("{},{}", title, _url);
        return Pair.of(title, _url);
    }

    @Test
    void m1() throws Throwable {
        Predicate<String> filter = line -> StringUtils.isNotEmpty(line) && !line.contains("#genre#");
        Function<String, String> mapper = line -> {
            String[] split = line.split(",");
            return split[1];
        };
        List<String> lines = CsvUtils.csvPick("", filter, mapper);
        Set<String> sets = Sets.newHashSet(lines);

        ThrowableFunction<String, List<String>, Throwable> fun = this::m1_1;
        ThrowableFunction<String, Pair<String, String>, Throwable> fun1 = this::m1_2;
        fun = this::m2_1;
        fun1 = this::m2_2;

        fun = this::m3_1;
        fun1 = this::m3_2;

        List<String> urls = fun.apply("");
        String s = """
                """;
        urls.addAll(CharSource.wrap(s).readLines());
        for (String url : urls) {
            Pair<String, String> pair = fun1.apply(url);
            if (!sets.contains(pair.getValue())) {
                log.info("{},{}", pair.getKey(), pair.getValue());
            }
        }
    }
}