package com.nguyen.wechat.controller;

import com.nguyen.wechat.service.AccessService;
import com.nguyen.wechat.utils.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author RWM
 * @date 2018/3/25
 * @description:
 */
@Controller
@RequestMapping("/api")
public class AccessController {

    @Autowired
    private AccessService accessService;

    /** 微信授权处理 **/
    @RequestMapping(value = "/access", method = RequestMethod.GET)
    public void accessWechat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        //timestamp	时间戳
        //nonce	    随机数
        //echostr	随机字符串
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        /*
        * 开发者通过检验signature对请求进行校验（下面有校验方式）。
        * 若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败。
        * 加密/校验流程如下：
        * 1）将token、timestamp、nonce三个参数进行字典序排序；
        * 2）将三个参数字符串拼接成一个字符串进行sha1加密；
        * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信。
        * */
        if (SignUtils.checkSignature(signature, timestamp, nonce)){
            response.getWriter().write(echostr);
        }
    }
}
