package com.nguyen.wechat.dto.response;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("模板返回体")
public class TemplateResponse {
    @ApiModelProperty("模板ID")
    @JSONField(alternateNames = "template_id")
    public String templateId;
    @ApiModelProperty("模板标题")
    public String title;
    @ApiModelProperty("一级行业")
    @JSONField(alternateNames = "primary_industry")
    public String primaryIndustry;
    @ApiModelProperty("二级行业")
    @JSONField(alternateNames = "deputy_industry")
    public String deputyIndustry;
    @ApiModelProperty("模板内容")
    public String content;
    @ApiModelProperty("模析示例")
    public String example;
}
