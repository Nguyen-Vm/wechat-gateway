package com.nguyen.wechat.model;

import com.nguyen.wechat.dto.response.UserInfoResponse;
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
@Table(name = "t_app_user_info")
public class AppUserInfo extends BaseEntity {

    @Column(name = "open_id", unique = true)
    public String openId;

    @Column(name = "nickname")
    public String nickname;

    @Column(name = "sex")
    public Integer sex;

    @Column(name = "province")
    public String province;

    @Column(name = "city")
    public String city;

    @Column(name = "country")
    public String country;

    @Column(name = "head_img_url")
    public String headImgUrl;

    @Column(name = "privilege")
    public String privilege;

    @Column(name = "union_id")
    public String unionId;

    public static AppUserInfo model(UserInfoResponse userInfoResponse) {
        AppUserInfo model = BeanUtils.map(userInfoResponse, AppUserInfo.class);
        return model;
    }
}
