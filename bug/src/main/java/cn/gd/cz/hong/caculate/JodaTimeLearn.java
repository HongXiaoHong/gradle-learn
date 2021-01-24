package cn.gd.cz.hong.caculate;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * <h1>学习如何使用JodaTime</h1>
 *
 * @author 洪晓鸿
 * 2021/1/24 22:16
 */

public class JodaTimeLearn {
    /**
     * 年,月,日,时,分,秒,毫秒
     */
    private static DateTime dt = new DateTime(2015, 12, 21,
            0, 0, 0, 333);

    public static void main(String[] args) {
        createDate();
        format();
        parse();
        plus();
        Days days = daysToNewYear(LocalDate.now());
        System.out.println("距离新年 ： " + days.getDays());
        translate();
        zone();
        between();
        compare();
        pratice(new DateTime(1995, 11, 14, 0, 0, 0));
    }

    /**
     * <h2>创建时间</h2>
     */
    public static void createDate() {
        System.out.println(dt);
    }

    /**
     * 格式化
     */
    public static void format() {
        System.out.println(dt.toString("yyyy/MM/dd HH:mm:ss"));
    }

    /**
     * 解析文本格式时间
     */
    public static void parse() {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime = DateTime.parse("2015-12-21 23:22:45", format);
        System.out.println(dateTime.toString("yyyy/MM/dd HH:mm:ss EE"));
    }

    /**
     * 在某个日期上加上90天并输出结果
     */
    public static void plus() {
        DateTime dateTime = new DateTime(2016, 1, 1, 0, 0, 0, 0);
        System.out.println(dateTime.plusDays(90).toString("E MM/dd/yyyy HH:mm:ss.SSS"));
    }

    /**
     * 到新年还有多少天
     */
    public static Days daysToNewYear(LocalDate fromDate) {
        LocalDate newYear = fromDate.plusYears(1).withDayOfYear(1);
        return Days.daysBetween(fromDate, newYear);
    }

    /**
     * 与JDK日期对象的转换
     */
    private static void translate() {
        DateTime dt = new DateTime();

        // 转换成java.util.Date对象
        Date d1 = new Date(dt.getMillis());
        Date d2 = dt.toDate();
    }

    /**
     * 时区
     */
    public static void zone() {
        //默认设置为日本时间
        DateTimeZone.setDefault(DateTimeZone.forID("Asia/Tokyo"));
        DateTime dt1 = new DateTime();
        System.out.println(dt1.toString("yyyy-MM-dd HH:mm:ss"));

        //伦敦时间
        DateTime dt2 = new DateTime(DateTimeZone.forID("Europe/London"));
        System.out.println(dt2.toString("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 计算间隔和区间
     */
    public static void between() {
        DateTime begin = new DateTime("2015-02-01");
        DateTime end = new DateTime("2016-05-01");

        //计算区间毫秒数
        Duration d = new Duration(begin, end);
        long millis = d.getMillis();

        //计算区间天数
        Period p = new Period(begin, end, PeriodType.days());
        int days = p.getDays();

        //计算特定日期是否在该区间内
        Interval interval = new Interval(begin, end);
        boolean contained = interval.contains(new DateTime("2015-03-01"));
    }

    /**
     * 日期比较
     */
    public static void compare() {
        DateTime d1 = new DateTime("2015-10-01");
        DateTime d2 = new DateTime("2016-02-01");

        //和系统时间比
        boolean b1 = d1.isAfterNow();
        boolean b2 = d1.isBeforeNow();
        boolean b3 = d1.isEqualNow();

        //和其他日期比
        boolean f1 = d1.isAfter(d2);
        boolean f2 = d1.isBefore(d2);
        boolean f3 = d1.isEqual(d2);
    }

    /**
     * 输入一个日期(生日，格式：yyyy-MM-dd Or yyyy-MM-dd HH:mm:ss)，
     * 计算出今天是你人生的第多少天/小时/分/秒
     */
    public static void pratice(DateTime birthday) {
        DateTime now = DateTime.now();
        Days days = Days.daysBetween(birthday, now);
        System.out.println("今天是你人生的第" + days.getDays() + "天");
    }
}
