package org.luvx.retrofit2;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ApiHolder {
    public static final Retrofit API_GITHUB = new Retrofit.Builder()
            // .baseUrl("https://api.github.com")
            .baseUrl("https://gitee.com/api/v5/")
            .addConverterFactory(JacksonConverterFactory.create())
            // .addCallAdapterFactory(CompletableFutureCallAdapterFactory)
            .build();
}
