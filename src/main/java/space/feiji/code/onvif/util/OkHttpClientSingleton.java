package space.feiji.code.onvif.util;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class OkHttpClientSingleton {

    private static volatile OkHttpClient okHttpClient;

    private OkHttpClientSingleton() {}

    public static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            synchronized (OkHttpClientSingleton.class) {
                if (okHttpClient == null) {
                    okHttpClient = okHttpClient();
                }
            }
        }
        return okHttpClient;
    }

    private static OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        return builder.build();
    }
}
