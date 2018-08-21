package com.nguyen.wechat;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import javafx.util.Pair;
import okhttp3.Headers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.linker.foundation.dto.DateFormat;
import org.linker.foundation.utils.CryptUtils;
import org.linker.foundation.utils.DateUtils;
import org.linker.foundation.utils.HttpRestUtils;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * @author RWM
 * @date 2018/8/16
 */
public class YtxCall {

    public static void main(String[] args) {
        String accountSID = "2b63d232e996453381a5fcaea559c848";
        String authToken = "73e5ab6a7a304d7f87d4a4541b82bd00";
        String appid = "3627e49b90de4f2d9d81034af4c4ed96";

        String date = DateUtils.now(DateFormat.NumDateTime);
        String sign = CryptUtils.md5(accountSID + authToken + date);
        String authorization = CryptUtils.encodeBase64(accountSID + "|" + date);

        JSONObject json = new JSONObject();
        json.put("action", "callDailBack");
        json.put("appid", appid);
        json.put("src", "18621629072");
        json.put("dst", "18817391936");
        json.put("srcclid", "01053189990");
        json.put("dstclid", "01053189990");

        String url = String.format("http://sandbox.ytx.net/201512/sid/%s/call/DailbackCall.wx?Sign=%s", accountSID, sign);
        List<Pair<String, String>> headers = headers(authorization);
        RequestBody body = HttpRestUtils.buildBody(MediaType.APPLICATION_JSON_UTF8, json);

        HttpRestUtils.asyncPost(url, headers, body, new HttpRestUtils.OkHttpCallback() {
            @Override
            protected void success(Headers headers, ResponseBody body) throws Exception {
                System.out.println(body.string());
            }
        });
    }

    public static List<Pair<String, String>> headers(String authorization){
        List<Pair<String, String>> headers = Lists.newArrayList(
                new Pair<>("Accept", "application/json"),
                new Pair<>("Content-Type", "application/json;charset=utf-8"),
                new Pair<>("Authorization", authorization)
        );
        return headers;
    }
}
