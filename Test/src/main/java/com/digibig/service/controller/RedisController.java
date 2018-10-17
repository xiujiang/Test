package com.digibig.service.controller;

import com.digibig.service.Utils.RedisUtils;
import com.digibig.service.doMain.person;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/test")
public class RedisController {
    @Resource
    RedisUtils redisUtils;


    @PostMapping("/getRedisVal")
    public void getRedisByUtils(@RequestBody String name){
        System.out.println("name:"+name);
        System.out.println(redisUtils.get(name));
    }

    @PostMapping("/setRedisVal")
    public void setRedisByUtils(@RequestBody String name){
        System.out.println("name:"+name);
        System.out.println(redisUtils.set(name,"中国"));
    }

    @PostMapping("/testRequest")
    public void testRequest(HttpServletRequest request, HttpServletResponse response){
        System.out.println("request:"+request);
        System.out.println("1:  "+request.getAttribute(DispatcherServlet.INPUT_FLASH_MAP_ATTRIBUTE));
        System.out.println("2:  "+request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
    }

    @PostMapping("/testFormatter")
    public void testFormatter(@RequestBody person person){
        System.out.println(person);
    }

    @PostMapping("/testFormatter1")
    public person testFormatter1(){
        person per = new person();
        per.setCreateTime(new Date());
        per.setName("123");
        System.out.println(per);
        return per;
    }

}
