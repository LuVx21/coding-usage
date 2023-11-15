package org.luvx.coding.usage.jsoup;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.consts.Properties;
import org.luvx.coding.common.net.HttpUtils;

import java.util.List;

@Slf4j
class ImageTest {
    private final RateLimiter limiter = RateLimiter.create(0.5);

    private List<String> m1_1(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return Lists.newArrayList();
        }
        return null;
    }

    private Pair<String, String> m1_2(String url) throws Exception {
        String no = "";
        url = "";
        int max = 1;
        for (int i = 1; i <= max; i++) {
            String _url = i > 1 ? url + "/" + i : url;
            limiter.acquire();
            log.info("请求url:{}", _url);
            Document doc = Jsoup.connect(_url).get();
            Elements es = doc.select("img.alignnone.size-full");
            for (Element e : es) {
                String src = e.attr("src");
                if (StringUtils.isEmpty(src)) {
                    src = e.attr("data-src");
                }
                HttpUtils.download(src, Properties.DIR_USER_HOME + "/Downloads/" + no, null, 0);
                log.info(src);
            }
            if (i <= 1) {
                Elements select = doc.select("div.page-links");
                Elements es1 = select.first().getElementsByClass("post-page-numbers");
                Elements span = es1.getLast().getElementsByTag("span");
                Element x = span.get(0);
                Node node = x.childNodes().getLast();
                max = NumberUtils.toInt(node.outerHtml());
            }
        }
        return null;
    }

    @Test
    void m1() throws Exception {
        m1_2("");
    }
}