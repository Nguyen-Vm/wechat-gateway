package com.nguyen.wechat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author RWM
 * @date 2018/3/26
 * @description:
 */
@RestController
public class WechatGateway {

    @RequestMapping("/")
    public String wechat(){
        return "Wechat Gateway";
    }
}
