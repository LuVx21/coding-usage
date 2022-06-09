package org.luvx.guava;

import com.google.common.base.*;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Ordering;
import com.google.common.io.Files;
import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import org.junit.Test;
import org.luvx.common.more.MorePrints;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: org.luvx.guava
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/1/24 21:51
 */
public class ArgsCheckTest {
    /**
     * 前置条件: 参数检查
     */
    @Test
    public void method0() {
        String str = "aa";
        System.out.println(Strings.isNullOrEmpty(str));

        int count = 1;
        Preconditions.checkArgument(count > 0, "Argument was %s but expected non negative", count);

        Preconditions.checkNotNull(str);

        Preconditions.checkElementIndex(9, 10);

        Preconditions.checkPositionIndexes(1, 2, 10);
    }

    @Test
    public void method1() {
        String str = MoreObjects.toStringHelper("Person").add("age", 11).toString();
        System.out.println(str);
    }

    @Test
    public void method2() {
        Tuple3<String, String, Integer> person = Tuple.of("aa", "aa", 14);
        Tuple3<String, String, Integer> ps = Tuple.of("bb", "bb", 13);
        Ordering<Tuple3<String, String, Integer>> byOrdering = Ordering.natural()
                .nullsFirst()
                .onResultOf((u) -> u._3 + "");
        // 1 person的年龄比ps大 所以输出1
        System.out.println(byOrdering.compare(person, ps));
    }

    /**
     * org.springframework.util.StopWatch
     */
    @Test
    public void method3() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Thread.sleep(1000);
        long nanos = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println(nanos);
    }

    @Test
    public void method4() {
        File file = new File("/test.txt");
        List<String> list = null;
        try {
            list = Files.readLines(file, Charsets.UTF_8);
        } catch (Exception e) {
        }

        // 复制文件
        // Files.copy(from, to);
        // 清空指定文件夹
        // Files.deleteDirectoryContents(File directory); //删除文件夹下的内容(包括文件与子文件夹)
        // 删除文件
        // Files.deleteRecursively(File file); //删除文件或者文件夹
        // 移动文件
        // Files.move(File from, File to); //移动文件
        // 获取文件路径
        // URL url = Resources.getResource("abc.xml"); //获取classpath根下的abc.xml文件url
    }

    @Test
    public void method5() throws Exception {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(100L)
                .build(new CacheLoader<>() {
                    @Override
                    public String load(String key) {
                        return "hello " + key + "!";
                    }
                });
        cache.put("bar", "foo");
        MorePrints.println(
                cache.getUnchecked("foo"),
                cache.get("foo"),
                cache.get("bar")
        );
        MorePrints.println(
                cache.getIfPresent("code"),
                cache.get("code", () -> "begin code!"),
                cache.getIfPresent("code")
        );
    }

    /**
     * null值检测
     */
    @Test
    public void method7() {
        String str = "foobar";
        Optional<String> possible = Optional.of(str);
        MorePrints.println(possible.isPresent(), possible.get());

        Optional.presentInstances(Arrays.asList("1", null, "2").stream().map(Optional::fromNullable).toList())
                .forEach(API::println);
    }
}
