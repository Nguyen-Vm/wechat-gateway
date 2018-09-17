package com.nguyen.wechat.common;

/**
 * @author RWM
 * @date 2018/3/26
 * @description:
 */
public class IConst {

    public static final String APPID = "wx09a57365be7f2c53";
    public static final String APPSECRET = "ff47a858e08e639c5a659e2c41f444fd";

    public interface WechatMessageUrl{
        String CustomMessage = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
        String TemplateMessage = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
        String TemplateList = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=%s";
    }

    public interface WechatAccessUrl{
        String AccessUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
    }

    public static final String GET_ACCESS_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    public static final String CREATE_WECHAT_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
}
