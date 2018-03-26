package com.nguyen.wechat.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author RWM
 * @date 2018/3/26
 * @description:
 */
@NoArgsConstructor
@ApiModel("图文消息请求体")
public class NewsMessageRequest {

    @ApiModelProperty("AppID")
    public String appId;

    @ApiModelProperty("OpenId")
    public String openId;

    @ApiModelProperty("文章列表")
    public List<CustomArticle> articles;

    @ApiModel("图文信息")
    public class CustomArticle{

        @ApiModelProperty("题目")
        public String title;

        @ApiModelProperty("描述")
        public String description;

        @ApiModelProperty("跳转链接")
        public String redirect;

        @ApiModelProperty("图片链接")
        public String pictureUrl;
    }
}
