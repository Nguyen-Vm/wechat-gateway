package com.nguyen.wechat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.nguyen.wechat.common.IConst;
import com.nguyen.wechat.dto.request.NewsMessageRequest;
import com.nguyen.wechat.dto.request.TemplateMessageRequest;
import com.nguyen.wechat.dto.request.TextMessageRequest;
import com.nguyen.wechat.dto.response.TemplateResponse;
import com.nguyen.wechat.mapper.AccessTokenMapper;
import com.nguyen.wechat.model.AccessToken;
import com.nguyen.wechat.utils.HttpRestUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author RWM
 * @date 2018/3/26
 * @description:
 */
@Slf4j
@Service
public class MessageService {

    private final AccessTokenMapper tokenMapper;

    @Autowired
    public MessageService(AccessTokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    /*获取ACCESS_TOKEN*/
    private String getAccessToken(String appId){
        AccessToken token = tokenMapper.findByAppId(appId);
        if (StringUtils.isBlank(token.accessToken)){
            log.error("can not find valid access token, appId: {}", appId);
            throw new RuntimeException("没有找到有效的token");
        }
        return token.accessToken;
    }

    /*发送文本消息*/
    public void sendTextMessage(TextMessageRequest request) {
        JSONObject json = new JSONObject();
        json.put("touser", request.openId);
        json.put("msgtype", "text");
        json.put("text", ImmutableMap.of("content", request.message));
        RequestBody body = HttpRestUtils.buildBody(MediaType.APPLICATION_JSON_UTF8, json);
        String url = String.format(IConst.WechatMessageUrl.CustomMessage, getAccessToken(request.appId));
        HttpRestUtils.asyncPost(url, body, new HttpRestUtils.OkHttpCallback() {
            @Override
            protected void success(Headers headers, ResponseBody body) throws Exception {
                log.debug("send text message response: {}", body.string());
            }
        });
    }

    /*发送图文消息*/
    public void sendNewsMessage(NewsMessageRequest request) {
        JSONObject json = new JSONObject();
        json.put("touser", request.openId);
        json.put("msgtype", "news");
        List<Map<String, String>> articles = Lists.newArrayList();
        for (NewsMessageRequest.CustomArticle ca : request.articles){
            articles.add(ImmutableMap.of("title",ca.title,
                    "description",ca.description,
                    "url",ca.redirect,
                    "picurl",ca.pictureUrl));
        }
        json.put("news", ImmutableMap.of("articles", articles));
        RequestBody body = HttpRestUtils.buildBody(MediaType.APPLICATION_JSON_UTF8, json);
        String url = String.format(IConst.WechatMessageUrl.CustomMessage, getAccessToken(request.appId));
        HttpRestUtils.asyncPost(url, body, new HttpRestUtils.OkHttpCallback() {
            @Override
            protected void success(Headers headers, ResponseBody body) throws Exception {
                log.debug("send news message response: {}", body.string());
            }
        });
    }

    /*发送模板消息*/
    public void sendTemplateMessage(TemplateMessageRequest request) {
        JSONObject json = new JSONObject();
        json.put("touser", request.openId);
        json.put("template_id", request.templateId);
        json.put("url", request.redirect);
        json.put("data", request.keyWordData);
        RequestBody body = HttpRestUtils.buildBody(MediaType.APPLICATION_JSON_UTF8, json);
        String url = String.format(IConst.WechatMessageUrl.TemplateMessage, getAccessToken(request.appId));
        HttpRestUtils.asyncPost(url, body, new HttpRestUtils.OkHttpCallback() {
            @Override
            protected void success(Headers headers, ResponseBody body) throws Exception {
                log.debug("send template message response: {}", body.string());
            }
        });
    }

    public List<TemplateResponse> templateList(String appId) {
        return getTemplateList(getAccessToken(appId));
    }

    private List<TemplateResponse> getTemplateList(String token) {
        String url = String.format(IConst.WechatMessageUrl.TemplateList, token);
        JSONObject json = HttpRestUtils.get(url, JSONObject.class);
        List<TemplateResponse> trList = JSON.parseArray(json.getString("template_list"), TemplateResponse.class);
        return CollectionUtils.isEmpty(trList) ? Lists.newArrayList() : trList;
    }
}
