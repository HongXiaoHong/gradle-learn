package cn.gd.cz.hong.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: 洪晓鸿
 * @time: 2021/1/7 23:36
 */

@RestController
public class HelloGradle {

    @RequestMapping("hello")
    public String hello() {
        return "Hello Gradle";
    }
}
