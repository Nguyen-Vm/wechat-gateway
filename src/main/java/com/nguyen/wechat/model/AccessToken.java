package com.nguyen.wechat.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_access_token")
public class AccessToken extends EntityModel {

    /** 授权的AppId **/
    @Column(name = "app_id", length = 100, unique = true)
    public String appId;

    /** 授权ACCESS_TOKEN **/
    @Column(name = "accessToken", length = 512)
    public String accessToken;

    /** TOKEN过期时间 **/
    @Column(name = "expiresIn")
    public Long expiresIn;

}
