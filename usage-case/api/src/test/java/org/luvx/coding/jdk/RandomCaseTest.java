package org.luvx.coding.jdk;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MorePrints;

import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.IntStream;

/**
 * 随机数
 *
 * @author renxie
 */
public class RandomCaseTest {

    @Test
    void getRandom() {
        int start = 0, end = 10;
        int bound = end - start + 1;

        // [0.0 , 1.0)
        double num = Math.random();
        int i1 = (int) (num * bound) + start;

        Random random = new Random();
        int i2 = random.nextInt(bound) + start;
        MorePrints.println(i1, i2);
    }

    /**
     * 参数bound值域的宽度,
     * 函数外的数值即域的起点
     */
    @Test
    public void run00() {
        // 无参构造,默认种子是System.nanoTime()
        Random random = new Random();

        int m = 9;
        int n = 20;
        // [0,n)
        int num = random.nextInt(n);
        // [m,n)
        num = random.nextInt(n - m) + m;
        // [m,n]
        num = random.nextInt(n - m + 1) + m;
        // (m,n)<->[m+1,n)
        num = random.nextInt(n - m - 1) + m + 1;
    }

    @Test
    public void run01() {
        // 种子值确定时,产生的随机数都是确定的
        Random random = new Random(1);
        int num = random.nextInt();
        System.out.println(num);
    }

    /**
     * jdk8
     * 引入Stream概念
     */
    @Test
    public void run03() {
        Random random = new Random();
        // 5个10~20范围的随机数,同样是左闭右开,下面两行等价
        IntStream ints = random.ints(10, 20).limit(5);
        // IntStream ints = random.ints(5, 10, 20);

        ints.forEach(System.out::println);

        // int[] array = ints.toArray();
        // for (int i : array) {
        //     System.out.print(i);
        // }
    }

    /**
     * Math中的random(),无参
     * 取值:[0.0,1.0)
     */
    @Ignore
    public void run04() {
        int num = (int) (Math.random() * 10) + 5;
        num = (int) (Math.random() * 10 + 5);
    }

    /**
     * 引入RandomGenerator及RandomGeneratorFactory提供更好的随机数生成
     */
    @Test
    public void m1() {
        RandomGenerator generator = RandomGeneratorFactory.all()
                .filter(RandomGeneratorFactory::isJumpable)
                .filter(factory -> factory.stateBits() > 128)
                .findAny()
                .map(RandomGeneratorFactory::create)
                //  .map(JumpableGenerator.class::cast)
                .orElseThrow();
    }
}
