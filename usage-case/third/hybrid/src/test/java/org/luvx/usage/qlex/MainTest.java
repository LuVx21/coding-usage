package org.luvx.usage.qlex;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.luvx.coding.usage.qlex.JoinOperator;

import com.google.common.collect.ImmutableMap;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import com.ql.util.express.Operator;

class MainTest {
    final ExpressRunner                   runner  = new ExpressRunner();
    final IExpressContext<String, Object> context = new DefaultContext<>();

    IExpressContext<String, Object> a(Map<String, Object> map) {
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.putAll(map);
        return context;
    }

    @Test
    void m1() throws Exception {
        IExpressContext<String, Object> context = a(ImmutableMap.of(
                "a", 1,
                "b", 2,
                "c", 3
        ));

        // String express = "a + b * c";
        String express = "a > b ? a : b";
        Object execute = runner.execute(express, context, null, true, true);
        System.out.println(execute);
    }

    @Test
    void test_basic_use_for() throws Exception {
        String express = """
                n = 100; sum = 0;
                for(i = 1; i <= n; i++){
                    sum = sum + i;
                }
                return sum;
                """;

        Object result = runner.execute(express, context, null, true, false);
        System.out.println("1...100的和是: " + result);
    }

    @Test
    void test_array_use() throws Exception {
        String express = """
                arr=new int[3];
                arr[0]=1;arr[1]=2;arr[2]=3;
                sum=arr[0]+arr[1]+arr[2];
                return sum;
                """;
        Object arrSum = runner.execute(express, context, null, true, false);
        System.out.println("arrSum: " + arrSum);
    }

    @Test
    void test_list_use() throws Exception {
        String express = """
                list = new ArrayList();
                list.add(1);list.add(2);list.add(3);
                sum=0;
                for(i=0;i<list.size();i++){
                    sum = sum+list.get(i);
                }
                return sum;
                """;

        Object listSum = runner.execute(express, context, null, true, false);
        System.out.println("listSum: " + listSum);
    }

    @Test
    void test_NewList() throws Exception {
        String express = """
                abc=NewList(1,2,3);
                return abc.get(0)+abc.get(1)+abc.get(2);
                """;
        Object listSum = runner.execute(express, context, null, true, false);
        System.out.println("listSum: " + listSum);
    }

    @Test
    void test_map_use() throws Exception {
        String express = "map=new HashMap();" +
                "map.put('a',2);map.put('b',2);map.put('c',2);" +
                "sum=0;" +
                "keySet=map.keySet();" +
                "keyArray=keySet.toArray();" +
                "for(i=0;i<keyArray.length;i++){" +
                "sum=sum+map.get(keyArray[i]);" +
                "}" +
                "return sum;";

        Object mapValueSum = runner.execute(express, context, null, true, false);
        System.out.println("mapValueSum: " + mapValueSum);
    }


    @Test
    void test_NewMap() throws Exception {
        String express = """
                abc=NewMap('a':1,'b':2,'c':3);
                return abc.get('a')+abc.get('b')+abc.get('c');
                """;
        Object mapSum = runner.execute(express, context, null, true, false);
        System.out.println("mapSum: " + mapSum);
    }

    @Test
    void test_add_func() throws Exception {
        String express = "function add(int a,int b){" +
                "return a+b;" +
                "};" +
                "a=2;b=2;" +
                "return add(a,b);";
        Object funcResult = runner.execute(express, context, null, true, false);
        System.out.println("add(a,b)=" + funcResult);
    }

    @Test
    void m2() throws Exception {
        String express = "1 join 2 join 3";
        runner.addOperator("join", new JoinOperator());

        Object result = runner.execute(express, context, null, true, false);
        System.out.println(result);
    }

    @Test
    void m3() throws Exception {
        String express = "1 + 2 + 3";
        runner.replaceOperator("+", new JoinOperator());

        Object result = runner.execute(express, context, null, true, false);
        System.out.println(result);
    }

    @Test
    void m4() throws Exception {
        String express = "join(1,2,3)";
        runner.addFunction("join", new JoinOperator());

        Object result = runner.execute(express, context, null, true, false);
        System.out.println(result);
    }

    // void m5()  throws Exception{
    //     ExpressRunner runner = new ExpressRunner();
    //     IExpressContext<String, Object> context = new DefaultContext<String, Object>();
    //
    //     runner.addFunctionOfClassMethod("取绝对值", Math.class.getName(), "abs",
    //             new String[] {"double"}, null);
    //     runner.addFunctionOfClassMethod("转换为大写", BeanExample.class.getName(),
    //             "upper", new String[] {"String"}, null);
    //
    //     runner.addFunctionOfServiceMethod("打印", System.out, "println", new String[] {"String"}, null);
    //     runner.addFunctionOfServiceMethod("contains", new BeanExample(), "anyContains",
    //             new Class[] {String.class, String.class}, null);
    //
    //     String exp = "取绝对值(-100);转换为大写(\"hello world\");打印(\"你好吗？\");contains(\"helloworld\",\"aeiou\");";
    //     runner.execute(exp, context, null, false, false);
    // }

    @Test
    void test_function_and_class_method() throws Exception {
        //给类增加方法
        runner.addFunctionAndClassMethod("isBlank", String.class, new Operator() {
            @Override
            public Object executeInner(Object[] list) throws Exception {
                Object obj = list[0];
                if (obj == null) {
                    return true;
                }

                String str = String.valueOf(obj);
                return str.length() == 0;
            }
        });

        String express = "a=\"\".isBlank()";
        Object r = runner.execute(express, context, null, true, false);
        System.out.println(r);
    }

    @Test
    void test_macro_use() throws Exception {
        IExpressContext<String, Object> context = a(ImmutableMap.of(
                "语文", 88,
                "数学", 99,
                "英语", 95
        ));

        runner.addMacro("计算平均成绩", "(语文 + 数学 + 英语) / 3");
        runner.addMacro("是否优秀", "计算平均成绩 > 90");

        Object result = runner.execute("是否优秀", context, null, false, false);
        System.out.println(result);
    }
}