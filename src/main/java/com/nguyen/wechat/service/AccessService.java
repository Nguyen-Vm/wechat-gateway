package com.nguyen.wechat.service;

import com.google.common.collect.ImmutableMap;
import com.nguyen.wechat.common.DtProcessHelper;
import com.nguyen.wechat.common.IConst;
import com.nguyen.wechat.dto.response.OAuthToken;
import com.nguyen.wechat.dto.response.TokenResponse;
import com.nguyen.wechat.dto.response.UserInfoResponse;
import com.nguyen.wechat.mapper.AccessTokenMapper;
import com.nguyen.wechat.mapper.AppAuthTokenMapper;
import com.nguyen.wechat.mapper.AppUserInfoMapper;
import com.nguyen.wechat.model.AccessToken;
import com.nguyen.wechat.model.AppAuthToken;
import com.nguyen.wechat.model.AppUserInfo;
import com.nguyen.wechat.utils.HttpRestUtils;
import com.nguyen.wechat.utils.MessageUtils;
import com.nguyen.wechat.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.nguyen.foun.utils.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
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
    private final AppAuthTokenMapper appAuthTokenMapper;
    private final AppUserInfoMapper appUserInfoMapper;

    @Autowired
    public AccessService(AccessTokenMapper tokenMapper,
                         AppAuthTokenMapper appAuthTokenMapper,
                         AppUserInfoMapper appUserInfoMapper) {
        this.tokenMapper = tokenMapper;
        this.appAuthTokenMapper = appAuthTokenMapper;
        this.appUserInfoMapper = appUserInfoMapper;
    }

    public void starting(){
        refreshAccessToken();
    }

    private void refreshAccessToken(){
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            String url = String.format(ACCESS_TOKEN_URL, APPID, SECRET);
            TokenResponse tokenResponse = HttpRestUtils.get(url, TokenResponse.class);
            if (StringUtils.isNotBlank(tokenResponse.accessToken)){
                AccessToken token = tokenMapper.selectByAppId(APPID);
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
        }, 1L, REFRESH_TIME, TimeUnit.MILLISECONDS);
    }

    public void handleMessageEvent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = MessageUtils.parseXml(request);
        String openId = map.get("FromUserName");
        String appId = map.get("ToUserName");
        if (map.get("MsgType").equals("text") && map.get("Content").equals("若初医助")) {
            // todo:
        }
        /*InputStream is = request.getInputStream();
        String message = ByteUtils.string(is);
        System.out.println(message);
        response.getWriter().write(StringUtils.EMPTY);*/
    }

    public Map<String, String> entrance(String appId, String state, String server) throws UnsupportedEncodingException {
        return ImmutableMap.of("appId", appId,
                "oauth2", URLEncoder.encode(DtProcessHelper.oauth20(server), "UTF-8"),
                "state", state);
    }

    public String callback(String code, Model model) {
        OAuthToken oAuthToken = HttpRestUtils.post(String.format(IConst.WechatAccessUrl.AccessUrl, APPID, SECRET, code), OAuthToken.class);
        if (StringUtils.isNotBlank(oAuthToken.openId)) {
            AppAuthToken authToken = appAuthTokenMapper.findByOpenId(oAuthToken.openId);
            if (authToken != null) {
                appAuthTokenMapper.delete(authToken);
            }
            authToken = appAuthTokenMapper.save(AppAuthToken.model(oAuthToken));
            UserInfoResponse userInfoResponse = HttpRestUtils.get(String.format(IConst.WechatAccessUrl.userInfoUrl, authToken.accessToken, authToken.openId), UserInfoResponse.class);
            AppUserInfo appUserInfo = appUserInfoMapper.findByOpenId(userInfoResponse.openId);
            if (appUserInfo != null) {
                appUserInfoMapper.delete(appUserInfo);
            }
            appUserInfo = appUserInfoMapper.save(AppUserInfo.model(userInfoResponse));
            model.addAttribute("nickname", appUserInfo.nickname);
            return "success";
        }
        return "failed";
    }
}
