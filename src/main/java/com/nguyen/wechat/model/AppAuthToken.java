package com.nguyen.wechat.model;

import com.nguyen.wechat.common.IConst;
import com.nguyen.wechat.dto.response.OAuthToken;
import com.nguyen.wechat.utils.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author RWM
 * @date 2018/4/3
 * @description:
 */
@Entity
@Table(name = "t_app_auth_token")
public class AppAuthToken extends BaseEntity {

    @Column(name = "app_id")
    public String appId;

    @Column(name = "access_token", length = 512)
    public String accessToken;

    @Column(name = "expires_in")
    public Long expiresIn;

    @Column(name = "refresh_token")
    public String refreshToken;

    @Column(name = "open_id", unique = true)
    public String openId;

    @Column(name = "scope")
    public String scope;

    public static AppAuthToken model(OAuthToken oAuthToken) {
        AppAuthToken model = BeanUtils.map(oAuthToken, AppAuthToken.class);
        model.appId = IConst.APPID;
        return model;
    }
}
