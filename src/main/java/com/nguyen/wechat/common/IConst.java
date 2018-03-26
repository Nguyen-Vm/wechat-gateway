package com.nguyen.wechat.common;

/**
 * @author RWM
 * @date 2018/3/26
 * @description:
 */
public class IConst {

    public interface WechatMessageUrl{
        String CustomMessage = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
        String TemplateMessage = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    }
}
