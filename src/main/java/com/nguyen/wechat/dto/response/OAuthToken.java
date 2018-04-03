package com.nguyen.wechat.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author RWM
 * @date 2018/4/3
 * @description:
 */
@ApiModel("网页授权ACCESS_TOKEN")
public class OAuthToken extends ErrorResponse {

    @ApiModelProperty("ACCESS_TOKEN")
    public String accessToken;

    @ApiModelProperty("TOKEN有效期")
    public Long expiresIn;

    @ApiModelProperty("REFRESH_TOKEN")
    public String refreshToken;

    @ApiModelProperty("OPEN_ID")
    public String openId;

    @ApiModelProperty("SCOPE")
    public String scope;
}
