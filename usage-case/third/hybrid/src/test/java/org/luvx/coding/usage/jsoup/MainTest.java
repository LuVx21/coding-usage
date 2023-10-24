package org.luvx.coding.usage.jsoup;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

@Slf4j
class MainTest {
    private final RateLimiter limiter = RateLimiter.create(0.5);

    private List<String> m0(String url) throws Exception {
        Document doc = Jsoup.connect(url).get();
        Element ul = doc.select("ul.stui-vodlist").first();

        URI uri = URI.create(url);
        String domain = uri.getScheme() + "://" + uri.getHost();
        return ul.children().stream()
                .skip(3)
                .map(child -> {
                    Element child1 = child.child(0);
                    Elements select = child1.select("div.stui-vodlist__box a.stui-vodlist__thumb");
                    String title = select.attr("title");
                    String href = domain + select.attr("href");
//                    System.out.println(title + "," + href);
                    log.info("{},{}", title, href);
                    return href;
                })
                .toList();
    }

    private void m1(String url) throws Exception {
        limiter.acquire();
        Document doc = Jsoup.connect(url).get();
        Element first = doc.select("div.stui-warp-content div.stui-pannel__head h3.title").first();
        String title = first.text();

        Element child = doc.select("div.stui-warp-content div.stui-pannel div.stui-player__video script").first();
        String script = child.data();
        String json = script.substring(script.indexOf("{"));
        JSONObject jsonObject = JSON.parseObject(json);
        String _url = jsonObject.getString("url");

        System.out.println(title + "," + _url);
    }

    private List<String> m2(String url) throws Exception {
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
//                    System.out.println(title + "," + href);
                    return href;
                })
                .toList();
    }

    private void m3(String url) throws Exception {
        limiter.acquire();
        Document doc = Jsoup.connect(url).get();
        Element first = doc.select("div.detail_right_tab div.gc_video_content div.gc_vodeo div.watch span.title").first();
        String title = first.text();

        Element child = doc.select("div.detail_right_tab div#bofang_box script").first();
        String script = child.data();
        String json = script.substring(script.indexOf("{"));
        JSONObject jsonObject = JSON.parseObject(json);
        String _url = jsonObject.getString("url");

        System.out.println(title + "," + _url);
    }

    @Test
    void m1() throws Exception {
        List<String> urls = m0("");
        for (String url : urls) {
            m1(url);
        }
    }

    @Test
    void m2() throws Exception {
        List<String> urls = m2("");
        for (String url : urls) {
            m3(url);
        }
    }
}