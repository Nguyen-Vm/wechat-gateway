package com.nguyen.wechat.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("获取ACCESS_TOKEN返回体")
public class TokenResponse extends ErrorResponse {

    @ApiModelProperty("ACCESS_TOKEN")
    public String accessToken;

    @ApiModelProperty("TOKEN有效期")
    public Long expiresIn;

}
