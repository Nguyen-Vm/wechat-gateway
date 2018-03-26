package com.nguyen.wechat.dto.request;

import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author RWM
 * @date 2018/3/26
 * @description:
 */
@NoArgsConstructor
@ApiModel("模板消息请求体")
public class TemplateMessageRequest {

    @ApiModelProperty("AppID")
    public String appId;

    @ApiModelProperty("OpenID")
    public String openId;

    @ApiModelProperty("模板ID")
    public String templateId;

    @ApiModelProperty("跳转链接")
    public String redirect;

    @ApiModelProperty("KEY WORD 数据")
    public Map<String, TemplateData> keyWordData = Maps.newHashMap();

    @ApiModel
    public static class TemplateData{
        public String value;
        public String color;
    }
}
