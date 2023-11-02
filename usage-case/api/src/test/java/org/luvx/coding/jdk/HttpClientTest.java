package org.luvx.coding.jdk;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MorePrints;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

class HttpClientTest {
    final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofMillis(5_000))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    @Test
    @SneakyThrows
    void m1() {
        // 2.set read timeout
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/LuVx21/doc"))
                .header("content-type", "application/json; charset=utf-8")
                .timeout(Duration.ofMillis(5_009))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        CompletableFuture<String> future = client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body);

        MorePrints.println(response.body(), future.get());
    }

    @Test
    @SneakyThrows
    void m2() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.w3school.com.cn/demo/demo_form.asp"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString("name1=value1&name2=value2"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        MorePrints.println(response.body());
    }

    @Test
    void m3() throws Exception {
    }
}