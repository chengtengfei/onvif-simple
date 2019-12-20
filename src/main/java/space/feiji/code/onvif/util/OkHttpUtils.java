package space.feiji.code.onvif.util;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OkHttpUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(OkHttpUtils.class);

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
            LOGGER.error("连接{}异常", url);
            throw e;
        }

//        log.info("连接 url={}, 发送{},  返回={}", url, body, response.toString());

//        log.info("response code = {}", response.code());
        if (response.isSuccessful()) {
            String s = response.body().string();
//            log.info("连接 url={} 返回={}", url, s);
            return s;
        } else if (response.code() >= 400 && response.code() < 500) {
            return "设备认证失败(用户名或密码错误)";
        } else {
            LOGGER.error("返回不成功:  "+ response.body().string());
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
            LOGGER.error("连接{}异常", url);
            throw new RuntimeException(e);
        }

        if (response.isSuccessful()) {
            return response.body().bytes();
        } else {
            return new byte[0];
        }
    }
}
