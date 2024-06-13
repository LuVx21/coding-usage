package org.luvx.grpc.service.sdk.client;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.luvx.grpc.service.proto.user.UserRequest;
import org.luvx.grpc.service.proto.user.UserRequest.Builder;
import org.luvx.grpc.service.proto.user.UserResponse;
import org.luvx.grpc.service.sdk.RpcConfig;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServiceRpcClientTest {

    ManagedChannel channel = null;

    @Before
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", RpcConfig.port)
                .usePlaintext()
                .build();
    }

    @Test
    public void selectUserInfoTest() {
        UserRpcClient client = UserRpcClient.getClient();
        Builder builder = UserRequest.newBuilder();
        int i = 1;
        UserRequest request = builder
                .setId(i).setName("foo" + i).setPassword("bar" + i).setAge(18 + i)
                .build();
        UserResponse response = client.selectUserInfo(request);
        log.info("客户端测试(selectUserInfo), 响应:{}", response.getMessage());

        Iterator<UserResponse> it = client.selectUserInfo1(request);
        while (it.hasNext()) {
            log.info("客户端测试(selectUserInfo1), 响应:{}", it.next().getMessage());
        }
    }
}