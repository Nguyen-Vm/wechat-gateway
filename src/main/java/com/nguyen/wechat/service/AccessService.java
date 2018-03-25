package com.nguyen.wechat.service;

import com.alibaba.fastjson.JSON;
import com.nguyen.wechat.dto.response.TokenResponse;
import com.nguyen.wechat.mapper.AccessTokenMapper;
import com.nguyen.wechat.model.AccessToken;
import com.nguyen.wechat.utils.HttpRestUtils;
import com.nguyen.wechat.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author RWM
 * @date 2018/3/25
 * @description:
 */
@Slf4j
@Service
public class AccessService {

    private static final Long MINTE_TIME = 60 * 1000L;
    private static final Long REFRESH_TIME = 100 * MINTE_TIME;

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    private static final String APPID = PropertiesUtil.getProperty("appID");
    private static final String SECRET = PropertiesUtil.getProperty("appsecret");

    private final AccessTokenMapper tokenMapper;

    @Autowired
    public AccessService(AccessTokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    public void starting(){
        refreshAccessToken();
    }

    private void refreshAccessToken(){
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            String url = String.format(ACCESS_TOKEN_URL, APPID, SECRET);
            Call call = HttpRestUtils.buildCall(url, null, RequestMethod.GET, null);
            try {
                String rs = call.execute().body().string();
                TokenResponse tokenResponse = JSON.parseObject(rs, TokenResponse.class);
                if (StringUtils.isNotBlank(tokenResponse.accessToken)){
                    AccessToken token = tokenMapper.findByAppId(APPID);
                    if (token == null){
                        token = new AccessToken();
                    }
                    token.appId = APPID;
                    token.accessToken = tokenResponse.accessToken;
                    token.expiresIn = tokenResponse.expiresIn;
                    tokenMapper.save(token);
                }else {
                    log.error("refresh access token error， code: {}, msg: {}", tokenResponse.errcode, tokenResponse.errmsg);
                }
            } catch (IOException e) {
                log.error("refresh access token error", e);
            }
        }, 1L, REFRESH_TIME, TimeUnit.MILLISECONDS);
    }
}
