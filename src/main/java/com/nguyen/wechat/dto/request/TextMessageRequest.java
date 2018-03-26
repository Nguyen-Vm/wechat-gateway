package com.nguyen.wechat.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

/**
 * @author RWM
 * @date 2018/3/26
 * @description:
 */
@NoArgsConstructor
@ApiModel("发送微信文本消息请求体")
public class TextMessageRequest {

    @ApiModelProperty("AppID")
    public String appId;

    @ApiModelProperty("OpenID")
    public String openId;

    @ApiModelProperty("文本消息")
    public String message;

    public static TextMessageRequest create(String appId, String openId, String message){
        return new TextMessageRequest(appId, openId, message);
    }

    private TextMessageRequest(String appId, String openId, String message){
        this.appId = appId;
        this.openId = openId;
        this.message = message;
    }

}
