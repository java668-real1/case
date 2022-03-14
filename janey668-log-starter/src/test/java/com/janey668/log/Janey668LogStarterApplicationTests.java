package com.janey668.log;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Janey668LogStarterApplicationTests {

    @Test
    void contextLoads() {
    }


//    public static void main(String[] args) {
//        SpelExpressionParser parser = new SpelExpressionParser();
//        Expression expression = parser.parseExpression("#root.purchaseName");
//        Order order = new Order();
//        order.setPurchaseName("张三");
//        System.out.println(expression.getValue(order));
//    }

    @Data
    static class Order {
        private String purchaseName;
    }

    public static void main(String[] args) {

        // 按指定模式在字符串查找
        String line = "123ra9040 123123aj234 adf12322ad 222jsk22";
        String pattern = "(\\d+)([a-z]+)(\\d+)";
//        String pattern1 = "([\\u4E00-\\u9FA5]+|\\w+)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        int i = 0;
        // m.find 是否找到正则表达式中符合条件的字符串
        while (m.find()) {
            // 拿到上面匹配到的数据
            System.out.println("----i=" + i);
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
            System.out.println("Found value: " + m.group(2));
            System.out.println("Found value: " + m.group(3));
            i++;
            System.out.println("|||||||");
        }
    }

    @Test
    public void test() {
        String content = "java123python456go";
        Matcher matcher = Pattern.compile("([a-z]+)([0-9]+)").matcher(content);
        while (matcher.find()) {
//            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
//            System.out.println(matcher.group(2));
        }
    }

    @Test
    public void test1() {
        String string = "12aa34 34bb45 55c66";
        Pattern pattern = Pattern.compile("(\\d+)([a-z]+)(\\d+)");
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            System.out.println("=========");
//            System.out.println(matcher.group(0));
            //group()默认返回group(0)
//            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
            System.out.println(matcher.group(3));
//                    +  + " "
//                    + matcher.group(3));
        }
    }

    @Test
    public void test3() {
        // 1. new一个parser对象用来解析表达式
        SpelExpressionParser parser = new SpelExpressionParser();

        // 2.解析表达式拿到Expression对象
        Expression expression = parser.parseExpression("2*2");

        // 3.运行表达式拿到运行结果
        Integer value = expression.getValue(Integer.class);
        EvaluationContext context = new
        expression.getValue()
        System.out.println(value);
    }
}
