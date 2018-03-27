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
        String TemplateList = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=%s";
    }

    public interface WechatAccessUrl{
        String AccessUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    }
}
