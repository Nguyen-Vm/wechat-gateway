package com.nguyen.wechat.utils;

import com.google.common.collect.Sets;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Set;

/**
 * @author RWM
 * @date 2018/3/25
 * @description:
 */
@Slf4j
public final class HttpRestUtils {

    private static final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();

    public static Call buildCall(String url, List<Pair<String, String>> headers, RequestMethod method, RequestBody body){
        Request.Builder builder = buildHeader(headers);
        switch (method){
            case GET:
                builder.url(url).get();
                break;
            case POST:
                builder.url(url).post(body);
                break;
        }
        return okHttpClient.newCall(builder.build());
    }

    private static Request.Builder buildHeader(List<Pair<String, String>> headers){
        Request.Builder builder = new Request.Builder();
        if (!CollectionUtils.isEmpty(headers)){
            Set<String> headSet = Sets.newHashSet();
            for (Pair<String, String> header : headers){
                if (!headSet.contains(header.getKey())){
                    builder.addHeader(header.getKey(), header.getValue());
                }else {
                    builder.header(header.getKey(), header.getValue());
                }
            }
        }
        return builder;
    }
}
