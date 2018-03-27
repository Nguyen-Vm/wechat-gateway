package com.nguyen.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.nguyen.wechat.common.DtProcessHelper;
import com.nguyen.wechat.common.IConst;
import com.nguyen.wechat.service.AccessService;
import com.nguyen.wechat.utils.HttpRestUtils;
import com.nguyen.wechat.utils.SignUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author RWM
 * @date 2018/3/25
 * @description:
 */
@Controller
public class AccessController {

    @Autowired
    private AccessService accessService;

    /** 接入微信 **/
    @RequestMapping(value = "/access", method = RequestMethod.GET)
    public void accessWechat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (SignUtils.checkSignature(signature, timestamp, nonce)){
            response.getWriter().write(echostr);
        }
    }

    /** 微信消息事件处理 **/
    @RequestMapping(value = "/access", method = RequestMethod.POST)
    public void MessageEventHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        accessService.handleMessageEvent(request, response);
    }

    /** 微信网页授权 **/
    @RequestMapping(value = "/entrance", method = {RequestMethod.GET, RequestMethod.POST})
    public String entrance(@RequestParam String appId,
                           @RequestParam(required = false) String state,
                           HttpServletRequest request,
                           Model model) throws Exception {
        model.addAllAttributes(accessService.entrance(appId, state, DtProcessHelper.httpServer(request)));
        return "entrance";
    }

    //http://rwming.free.ngrok.cc/oauth/20
    /** 微信授权回调 **/
    @RequestMapping(value = "/oauth/20", method = {RequestMethod.GET, RequestMethod.POST})
    public String oauth2(@RequestParam String code,
                         @RequestParam String state,
                         HttpServletRequest request,
                         Model model){
        JSONObject rs = HttpRestUtils.post(String.format(IConst.WechatAccessUrl.AccessUrl, "wx09a57365be7f2c53", "ff47a858e08e639c5a659e2c41f444fd", code), JSONObject.class);
        if (StringUtils.isBlank(rs.getString("access_token"))){
            return "failed";
        }
        return "success";
    }
}
