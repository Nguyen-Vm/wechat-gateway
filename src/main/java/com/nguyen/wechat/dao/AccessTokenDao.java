package com.nguyen.wechat.dao;

import com.nguyen.wechat.model.AccessToken;

public interface AccessTokenDao {

    AccessToken selectByAppId(String id);
}
