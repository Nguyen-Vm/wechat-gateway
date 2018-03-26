package com.nguyen.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.IOException;
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

    /** 添加HEADER 生成Request.Builder **/
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

    /** RequestBody 请求体生产器 **/
    public static RequestBody buildBody(org.springframework.http.MediaType mediaType, final Object body){
        if (body == null){
            return Util.EMPTY_REQUEST;
        }
        MediaType mt = MediaType.parse(mediaType.toString());
        if (body instanceof File){
            return RequestBody.create(mt, (File) body);
        }
        if (log.isInfoEnabled()){
            log.info("ok http request body: {}", JSON.toJSONString(body));
        }
        return RequestBody.create(mt, JSON.toJSONBytes(body));
    }

    /** 生成请求Call **/
    private static Call buildCall(String url, final List<Pair<String, String>> headers, final RequestMethod method, final RequestBody body, final SSLSocketFactory ssl){
        Request.Builder builder = buildHeader(headers);
        switch (method){
            case GET:
                builder.url(url).get();
                break;
            case POST:
                builder.url(url).post(body);
                break;
        }
        if (ssl == null){
            return okHttpClient.newCall(builder.build());
        }
        return okHttpClient.newBuilder().sslSocketFactory(ssl).build().newCall(builder.build());
    }

    /** 异步请求Callback类 **/
    public static abstract class OkHttpCallback implements Callback{

        @Override
        public void onFailure(Call call, IOException e) {
            log.error("ok http async failure, error {}", e);
            if (call != null){
                call.cancel();
            }
        }

        @Override
        public void onResponse(Call call, Response response) {
            try {
                if (response.isSuccessful()){
                    try {
                        success(response.headers(), response.body());
                    } catch (Exception e) {
                        log.error("ok http async send do response error {}", e);
                    }
                }else {
                    log.error("ok http async send request: {}, response: {}", call.request().body(), response.toString());
                }
            }finally {
                if (response != null){
                    response.close();
                }
                if (call != null && !call.isCanceled()){
                    call.cancel();
                }
            }
        }

        protected abstract void success(Headers headers, ResponseBody body) throws Exception;
    }

    /** 异步请求 **/
    private static void asyncNoBody(String url, final List<Pair<String, String>> headers, final RequestMethod method, final OkHttpCallback callback){
        asyncWithBody(url, headers, method, null, callback);
    }

    /** 异步请求 **/
    private static void asyncWithBody(String url, final List<Pair<String, String>> headers, final RequestMethod method, final RequestBody body, final OkHttpCallback callback){
        if (callback == null){
            throw new RuntimeException("ok http asyncSend request must provide Callback");
        }
        buildCall(url, headers, method, body, null).enqueue(callback);
    }

    /** 异步Get请求 **/
    public static void asyncGet(String url, final OkHttpCallback callback){
        asyncNoBody(url, Lists.newArrayList(), RequestMethod.GET, callback);
    }

    /** 异步Get请求 **/
    public static void asyncGet(String url, final List<Pair<String, String>> headers, final OkHttpCallback callback){
        asyncNoBody(url, headers, RequestMethod.GET, callback);
    }

    /** 异步Post请求 **/
    public static void asyncPost(String url, final OkHttpCallback callback){
        asyncWithBody(url, Lists.newArrayList(), RequestMethod.POST, Util.EMPTY_REQUEST, callback);
    }

    /** 异步Post请求 **/
    public static void asyncPost(String url, final List<Pair<String, String>> headers, final OkHttpCallback callback){
        asyncWithBody(url, headers, RequestMethod.POST, Util.EMPTY_REQUEST, callback);
    }

    /** 异步Post请求 **/
    public static void asyncPost(String url, final RequestBody body, final OkHttpCallback callback){
        asyncWithBody(url, Lists.newArrayList(), RequestMethod.POST, body, callback);
    }

    /** 异步Post请求 **/
    public static void asyncPost(String url, final List<Pair<String, String>> headers, final RequestBody body, final OkHttpCallback callback){
        asyncWithBody(url, headers, RequestMethod.POST, body, callback);
    }

    private static Object noBody(String url, final List<Pair<String, String>> headers, final RequestMethod method){
        return withBody(url, headers, method, null);
    }

    private static Object withBody(String url, final List<Pair<String, String>> headers, final RequestMethod method, final RequestBody body){
        return sslWithBody(url, headers, method, body, null);
    }

    private static Object sslWithBody(String url, final List<Pair<String, String>> headers, final RequestMethod method, final RequestBody body, final SSLSocketFactory ssl){
        Response response = null;
        Call call = null;
        try {
            call = buildCall(url, headers, method, body, ssl);
            response = call.execute();
            if (log.isDebugEnabled()){
                log.debug("ok http response body: {}", response);
            }
            if (response.isSuccessful()){
                if (method == RequestMethod.HEAD){
                    return response.headers();
                }
                ResponseBody rb = response.body();
                return rb != null ? rb.string() : StringUtils.EMPTY;
            }else {
                throw new RuntimeException("service response unsuccessful msg:" + response.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("request service error......", e);
        } finally {
            if (response != null){
                response.close();
            }
            if (call != null && !call.isCanceled()){
                call.cancel();
            }
        }
    }

    /** 同步Get请求 返回单个对象 **/
    public static <T> T get(String url, final Class<T> clazz){
        String rs = (String) noBody(url, Lists.newArrayList(), RequestMethod.GET);
        return String.class.equals(clazz) ? (T) rs : JSON.parseObject(rs, clazz);
    }


    /** 同步Get请求 **/
    public static <T> T get(String url, final List<Pair<String, String>> headers, final Class<T> clazz){
        String rs = (String) noBody(url, headers, RequestMethod.GET);
        return String.class.equals(clazz) ? (T) rs : JSON.parseObject(rs, clazz);
    }


    /** 同步Get请求 返回对象数组 **/
    public static <T> List<T> getList(String url, final Class<T> clazz){
        String rs = (String) noBody(url, Lists.newArrayList(), RequestMethod.GET);
        return JSON.parseArray(rs, clazz);
    }

    /** 同步Get请求 **/
    public static <T> List<T> getList(String url, final List<Pair<String, String>> headers, final Class<T> clazz){
        String rs = (String) noBody(url, headers, RequestMethod.GET);
        return JSON.parseArray(rs, clazz);
    }

    /** 同步Post请求 返回单个对象 **/
    public static <T> T post(String url, final Class<T> clazz){
        String rs = (String) withBody(url, Lists.newArrayList(), RequestMethod.POST, Util.EMPTY_REQUEST);
        return String.class.equals(clazz) ? (T) rs : JSON.parseObject(rs, clazz);
    }
    /** 同步Post请求 **/
    public static <T> T post(String url, final List<Pair<String, String>> headers, final Class<T> clazz){
        String rs = (String) withBody(url, headers, RequestMethod.POST, Util.EMPTY_REQUEST);
        return String.class.equals(clazz) ? (T) rs : JSON.parseObject(rs, clazz);
    }
    /** 同步Post请求 **/
    public static <T> T post(String url, final RequestBody body, final Class<T> clazz){
        String rs = (String) withBody(url, Lists.newArrayList(), RequestMethod.POST, body);
        return String.class.equals(clazz) ? (T) rs : JSON.parseObject(rs, clazz);
    }
    /** 同步Post请求 **/
    public static <T> T post(String url, final List<Pair<String, String>> headers, final RequestBody body, final Class<T> clazz){
        String rs = (String) withBody(url, headers, RequestMethod.POST, body);
        return String.class.equals(clazz) ? (T) rs : JSON.parseObject(rs, clazz);
    }

    /** 同步Post请求 返回对象数组 **/
    public static <T> List<T> postList(String url, final Class<T> clazz){
        String rs = (String) withBody(url, Lists.newArrayList(), RequestMethod.POST, Util.EMPTY_REQUEST);
        return JSON.parseArray(rs, clazz);
    }

    /** 同步Post请求 **/
    public static <T> List<T> postList(String url, final List<Pair<String, String>> headers, final Class<T> clazz){
        String rs = (String) withBody(url, headers, RequestMethod.POST, Util.EMPTY_REQUEST);
        return JSON.parseArray(rs, clazz);
    }

    /** 同步Post请求 **/
    public static <T> List<T> postList(String url, final RequestBody body, final Class<T> clazz){
        String rs = (String) withBody(url, Lists.newArrayList(), RequestMethod.POST, body);
        return JSON.parseArray(rs, clazz);
    }

    /** 同步Post请求 **/
    public static <T> List<T> postList(String url, final List<Pair<String, String>> headers, final RequestBody body, final Class<T> clazz){
        String rs = (String) withBody(url, headers, RequestMethod.POST, body);
        return JSON.parseArray(rs, clazz);
    }

}
