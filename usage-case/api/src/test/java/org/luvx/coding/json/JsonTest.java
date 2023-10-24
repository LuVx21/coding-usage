package org.luvx.coding.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Streams;
import com.google.common.io.Files;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class JsonTest {

    // private String json = "{\"age\":26,\"articles\":[{\"articleName\":\"test1\",\"createTime\":1544779082310},
    // {\"articleName\":\"test2\",\"createTime\":1544779082311}],\"password\":\"1234\",\"userId\":999,
    // \"userName\":\"Luvx\"}";
    //
    // @Test
    // public void toStr() {
    //     User user = User.builder().userName("Luvx").password("1234").age(26).build();;
    //     user.setId(999L);
    //     List<Article> articles = new ArrayList<>();
    //
    //     Article article = Article.builder().articleName("test1").createTime(new Date(System.currentTimeMillis()))
    //     .build();
    //     Article article1 = Article.builder().articleName("test2").createTime(new Date(System.currentTimeMillis() +
    //     1)).build();
    //     articles.add(article);
    //     articles.add(article1);
    //     user.setArticles(articles);
    //     String result = JSON.toJSONString(user);
    //     System.out.println(result);
    // }

    // @Test
    // public void toObj() {
    //     // 解析整个json
    //     User user = JSON.parseObject(json, User.class);
    //     System.out.println(user);
    // }

    @Test
    void arrayTest() {
        String json = """
                {
                "a":[{"b":"haha"}]
                }
                """;
        // 解析为json对象
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray array = jsonObject.getJSONArray("a");
        for (Object o : array) {
            System.out.println(o.getClass());
            System.out.println(o);
        }

        // JSONArray articles = jsonObject.getJSONArray("articles");
        // 方式1
        // List<Article> list = JSON.parseObject(articles.toJSONString(), new TypeReference<List<Article>>() {
        // });
        // 方式2
        // List<Article> newList = JSON.parseObject(articles.toJSONString(), ArrayList.class);
        // 方式3
        // List<Article> newNewList = JSONObject.parseArray(articles.toJSONString(), Article.class);
    }

    // @Test
    // public void mapTest() {
    //     Map<String, String> map = new HashMap<>();
    //     map.put("Luvx", "1234");
    //     map.put("foo", "bar");
    //     String json = JSON.toJSONString(map);
    //     System.out.println(json);
    //
    //     Map<String, String> map1 = JSON.parseObject(json, HashMap.class);
    //     System.out.println(map1);
    // }

    @Test
    void m1() throws IOException {
        String path = "/Users/renxie/code/legado/书源/必须导入精品";
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(path));
        List<String> sourceList = Streams.stream(files)
                .filter(File::isFile)
                .map(f -> {
                    try {
                        return JSON.parseArray(Files.asCharSource(f, Charsets.UTF_8).read());
                    } catch (IOException ignore) {
                    }
                    return null;
                })
                .flatMap(Collection::stream)
                .map(o -> (JSONObject) o)
                .map(item -> {
                    String bookSourceUrl = item.getString("bookSourceUrl");
                    String bookSourceName = item.getString("bookSourceName");
                    return bookSourceUrl + ":" + bookSourceName;
                })
                .sorted()
                .toList();
        System.out.println(sourceList.size());
        for (String s : sourceList) {
            System.out.println(s);
        }
    }
}