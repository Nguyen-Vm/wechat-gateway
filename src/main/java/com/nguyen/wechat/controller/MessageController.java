package com.nguyen.wechat.controller;

import com.nguyen.wechat.dto.request.NewsMessageRequest;
import com.nguyen.wechat.dto.request.TemplateMessageRequest;
import com.nguyen.wechat.dto.request.TextMessageRequest;
import com.nguyen.wechat.dto.response.TemplateResponse;
import com.nguyen.wechat.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author RWM
 * @date 2018/3/26
 * @description:
 */
@RestController
@Api(description = "微信消息相关接口")
@RequestMapping("/wechat")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation("发送文本消息")
    @RequestMapping(path = "/send/text/message", method = RequestMethod.POST)
    public void sendTextMessage(@RequestBody TextMessageRequest request){
        messageService.sendTextMessage(request);
    }

    @ApiOperation("发送图文消息")
    @RequestMapping(path = "/send/news/message", method = RequestMethod.POST)
    public void sendNewsMessage(@RequestBody NewsMessageRequest request){
        messageService.sendNewsMessage(request);
    }

    @ApiOperation("发送模板消息")
    @RequestMapping(path = "/send/template/message", method = RequestMethod.POST)
    public void sendTemplateMessage(@RequestBody TemplateMessageRequest request){
        messageService.sendTemplateMessage(request);
    }

    @ApiOperation("获取模板列表")
    @RequestMapping(path = "/{appId}/template/list", method = RequestMethod.GET)
    public List<TemplateResponse> templateList(@PathVariable String appId){
        return messageService.templateList(appId);
    }

}
