package com.nguyen.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.nguyen.wechat.common.IConst;
import com.nguyen.wechat.dto.MessageCode;
import com.nguyen.wechat.dto.request.WechatMenuCreateRequest;
import com.nguyen.wechat.dto.response.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.linker.foundation.dto.exception.AppException;
import org.linker.foundation.utils.CollectionUtils;
import org.linker.foundation.utils.HttpRestUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author RWM
 * @date 2018/9/17
 */
@Slf4j
@Service
public class WechatMenuService {

    public void createWechatMenu(WechatMenuCreateRequest request) {
        // 获取ACCESS_TOKEN
        TokenResponse response = HttpRestUtils.get(String.format(IConst.GET_ACCESS_URL, request.appId, request.appSecret), TokenResponse.class);
        if (response == null || StringUtils.isBlank(response.accessToken)) {
            throw new AppException(MessageCode.GetAccessTokenError);
        }
        List<JSONObject> menus = Lists.newArrayList();
        for (WechatMenuCreateRequest.WechatMenu wechatMenu : request.wechatMenuList) {
            // 子菜单个数不能超过5个
            if (!CollectionUtils.isNullOrEmpty(wechatMenu.subMenus) && wechatMenu.subMenus.size() > 5) {
                throw new AppException(MessageCode.SubManuOversize);
            }
            JSONObject mainMenu = new JSONObject();
            List<JSONObject> subButton = Lists.newArrayList();
            // 构造二级菜单请求体
            if (!CollectionUtils.isNullOrEmpty(wechatMenu.subMenus)) {
                for (WechatMenuCreateRequest.WechatMenu menu : wechatMenu.subMenus) {
                    JSONObject menuJson = new JSONObject();
                    if (menu.type.equals("view")) {
                        menuJson.put("type", menu.type);
                        menuJson.put("name", menu.name);
                        menuJson.put("url", menu.url);
                    }
                    if (menu.type.equals("click")) {
                        menuJson.put("type", menu.type);
                        menuJson.put("name", menu.name);
                        menuJson.put("key", menu.key);
                    }
                    subButton.add(menuJson);
                }
            }
            if (CollectionUtils.isNullOrEmpty(subButton)) {
                // 一级菜单没有二级菜单
                if (wechatMenu.type.equals("view")) {
                    mainMenu.put("type", wechatMenu.type);
                    mainMenu.put("name", wechatMenu.name);
                    mainMenu.put("url", wechatMenu.url);
                }
                if (wechatMenu.type.equals("click")) {
                    mainMenu.put("type", wechatMenu.type);
                    mainMenu.put("name", wechatMenu.name);
                    mainMenu.put("key", wechatMenu.key);
                }
            } else {
                // 一级菜单有二级菜单
                mainMenu.put("name", wechatMenu.name);
                mainMenu.put("sub_button", subButton);
            }
            menus.add(mainMenu);
        }
        JSONObject json = new JSONObject();
        json.put("button", menus);
        RequestBody body = HttpRestUtils.buildBody(MediaType.APPLICATION_JSON, json);
        // 请求微信创建公众号菜单接口
        json = HttpRestUtils.post(String.format(IConst.CREATE_WECHAT_MENU_URL, response.accessToken), body, JSONObject.class);
        if (0 != json.getIntValue("errcode")) {
            log.error("create wechat menu error %s", json.toJSONString());
            throw new AppException(MessageCode.CreateWechatMenuError);
        }
    }
}
