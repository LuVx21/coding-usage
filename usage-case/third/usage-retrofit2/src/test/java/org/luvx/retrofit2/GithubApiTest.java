package org.luvx.retrofit2;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

class GithubApiTest {
    @Test
    void m1() throws Exception {
        GithubApi api = ApiHolder.API_GITHUB.create(GithubApi.class);
        CompletableFuture<Map<String, Object>> users = api.users("LuVx");
        System.out.println(users.get());

        CompletableFuture<List<Object>> repos = api.repos("LuVx");
        System.out.println(repos.get());
    }
}