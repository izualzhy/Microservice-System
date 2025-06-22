package cn.izualzhy;

import java.awt.*;

public class Java17 {
    void testVar() {
        var a = 1;
        var b = 2;
        var c = a + b;
        System.out.println(c);
    }

    // sealed 关键字
    //使用permits关键字列出了允许继承的子类Circle、Rectangle和Triangle
    public sealed class Shape permits Circle {
        // 省略实现
    }

    // 在与密封类相同的模块或包中 定义以下三个允许的子类， Circle，Square和：Rectangle
    public final class Circle extends Shape {
        public float radius;
    }

//    public non-sealed class Square extends Shape {
//        public double side;
//    }

    record Rectangle(double length, double width) {}
    public void testRecord() {
        Rectangle r = new Rectangle(10, 20);
        System.out.println(r.length());
        System.out.println(r.width());
    }

    public enum Day { SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY; }
    public void testSwitch() {
        var day = Day.SATURDAY;
        System.out.println(
                switch (day) {
                    case MONDAY, FRIDAY, SUNDAY -> 6;
                    case TUESDAY                -> 7;
                    case THURSDAY, SATURDAY     -> 8;
                    case WEDNESDAY              -> 9;
                    default -> throw new IllegalStateException("Invalid day: " + day);
                }
        );
    }

    public void testMessage() {
        String message = """
    'The time has come,' the Walrus said,
    'To talk of many things:
    Of shoes -- and ships -- and sealing-wax --
    Of cabbages -- and kings --
    And why the sea is boiling hot --
    And whether pigs have wings.'
    """;
        System.out.println(message);
    }

    public void testInstanceOf() {
        String s = "hello world";
        Object obj = s;

        if (obj instanceof String str) {
            int length = str.length();
            System.out.println("字符串长度：" + length);
        }
    }

    public static void main(String[] args) {
        // test:
        // https://tech.meituan.com/2025/06/20/jdk17-zgc.html
        Java17 java17 = new Java17();
        java17.testVar();
        java17.testRecord();
        java17.testSwitch();
        java17.testMessage();
        java17.testInstanceOf();
    }
}
