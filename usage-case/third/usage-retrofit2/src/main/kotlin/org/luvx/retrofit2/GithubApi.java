package org.luvx.retrofit2;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubApi {
    @GET("users/{userName}")
    CompletableFuture<Map<String, Object>> users(@Path("userName") String userName);

    @GET("users/{userName}/repos")
    CompletableFuture<List<Object>> repos(@Path("userName") String userName);
}
