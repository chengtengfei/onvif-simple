package com.github.chengtengfei.onvif.util;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.github.chengtengfei.onvif.diagnostics.logging.Logger;
import com.github.chengtengfei.onvif.diagnostics.logging.Loggers;


public class OkHttpUtils {

    private final static Logger LOGGER = Loggers.getLogger(OkHttpUtils.class);

    public String okHttp3XmlPost(String url, String body) throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(null, body))
                .addHeader("Content-Type", "application/soap+xml")
                .build();

        Response response = null;

        try {
            response = OkHttpClientSingleton.getInstance().newCall(request).execute();
        } catch (Exception e) {
            LOGGER.debug("连接[" + url + "]异常");
            throw e;
        }

        LOGGER.debug("连接 url=[" + url + "], 发送[" + body + "],  返回[" + response.toString() + "]");

        LOGGER.debug("response code = " + response.code());
        if (response.isSuccessful()) {
            String s = response.body().string();
            return s;
        } else if (response.code() >= 400 && response.code() < 500) {
            throw new Exception("设备认证失败(用户名或密码错误)");
        } else {
            LOGGER.debug("返回不成功:  "+ response.body().string());
            throw new Exception("返回不成功");
        }
    }

    public byte[] getFile(String url, String authorization) throws Exception {
        if (authorization == null) {
            authorization = "NOAuthorization";
        }

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", authorization)
                .build();
        Response response = null;
        try {
            response = OkHttpClientSingleton.getInstance().newCall(request).execute();
        } catch (Exception e) {
            LOGGER.debug("连接[" + url + "]异常");
            throw new RuntimeException(e);
        }

        if (response.isSuccessful()) {
            if (response .body() != null) {
                return response.body().bytes();
            }
            return new byte[0];
        } else {
            return new byte[0];
        }
    }
}
