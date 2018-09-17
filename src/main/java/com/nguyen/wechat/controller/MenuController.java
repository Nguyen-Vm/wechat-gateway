package com.nguyen.wechat.controller;

import com.nguyen.wechat.dto.request.WechatMenuCreateRequest;
import com.nguyen.wechat.service.WechatMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author RWM
 * @date 2018/9/17
 */
@RestController
@Api(description = "微信菜单相关接口")
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private WechatMenuService wechatMenuService;

    @ApiOperation("创建微信公众号菜单")
    @PostMapping("/create")
    public void createMenu(@RequestBody WechatMenuCreateRequest request) {
        request.verify();
        wechatMenuService.createWechatMenu(request);
    }
}
