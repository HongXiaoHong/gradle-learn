package cn.gd.cz.hong.caculate;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @description: <h1>BigDecimal 与 精度问题</h1>
 * @author: 洪晓鸿
 * @time: 2021/1/24 11:01
 */

public class NumberCaculate {
    private static BigDecimal number = new BigDecimal("666.666");

    public static void main(String[] args) {
        output();
        System.out.println("-----------------------------");
        divide();
        System.out.println("-----------------------------");
        compare();
    }

    /**
     * <h2>输出的时候不设置精度 有可能出现</h2>
     */
    private static void output() {

        try {
            number.setScale(2);
            System.out.println(number);
        } catch (Exception e) {
            e.printStackTrace();
            /*
             * java.lang.ArithmeticException: Rounding necessary
             * 	at java.base/java.math.BigDecimal.commonNeedIncrement(BigDecimal.java:4628)
             * 	at java.base/java.math.BigDecimal.needIncrement(BigDecimal.java:4684)
             * 	at java.base/java.math.BigDecimal.divideAndRound(BigDecimal.java:4592)
             * 	at java.base/java.math.BigDecimal.setScale(BigDecimal.java:2892)
             * 	at java.base/java.math.BigDecimal.setScale(BigDecimal.java:2952)
             * 	at cn.gd.cz.hong.caculate.NumberCaculate.output(NumberCaculate.java:25)
             * 	at cn.gd.cz.hong.caculate.NumberCaculate.main(NumberCaculate.java:16)
             */
        }

        number.setScale(2, RoundingMode.HALF_UP);
        System.out.println(number);

    }

    /**
     * <h2>BigDecimal 除不尽的时候不设置精度会出现错误</h2>
     */
    private static void divide() {
        BigDecimal number = new BigDecimal("30");
        try {
            System.out.println(number.divide(new BigDecimal("7")));
        } catch (Exception e) {
            e.printStackTrace();
            /*
             * java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result.
             * 	at java.base/java.math.BigDecimal.divide(BigDecimal.java:1723)
             * 	at cn.gd.cz.hong.caculate.NumberCaculate.divide(NumberCaculate.java:39)
             * 	at cn.gd.cz.hong.caculate.NumberCaculate.main(NumberCaculate.java:18)
             */
        }

        System.out.println(number.divide(new BigDecimal("7"),
                2, RoundingMode.HALF_UP));
    }

    /**
     * <h2>比较的时候 用equals方法会带上精度 而使用compareTo不会使用精度</h2>
     */
    private static void compare() {
        BigDecimal another = new BigDecimal("666.666000");
        System.out.println(number.equals(another));
        System.out.println(number.compareTo(another) == 0);
    }
}
