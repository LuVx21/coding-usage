package org.luvx.grpc.service.sdk.client;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.luvx.grpc.service.proto.user.UserInfoGrpc;
import org.luvx.grpc.service.proto.user.UserInfoGrpc.UserInfoBlockingStub;
import org.luvx.grpc.service.proto.user.UserInfoGrpc.UserInfoFutureStub;
import org.luvx.grpc.service.proto.user.UserInfoGrpc.UserInfoStub;
import org.luvx.grpc.service.proto.user.UserOperateGrpc;
import org.luvx.grpc.service.proto.user.UserOperateGrpc.UserOperateBlockingStub;
import org.luvx.grpc.service.proto.user.UserRequest;
import org.luvx.grpc.service.proto.user.UserRequest.Builder;
import org.luvx.grpc.service.proto.user.UserResponse;
import org.luvx.grpc.service.sdk.RpcConfig;

import com.google.common.util.concurrent.ListenableFuture;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
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
    public void sayHelloTest() {
        UserRpcClient client = UserRpcClient.getClient();
        Builder builder = UserRequest.newBuilder();
        for (int i = 0; i < 3; i++) {
            UserRequest request = builder
                    .setId(i).setName("foo" + i).setPassword("bar" + i).setAge(18 + i)
                    .build();
            UserResponse response = client.selectUserInfo(request);
            log.info("执行结果: {}", response.getMessage());
            response = client.updateUser(request);
            log.info("执行结果: {}", response.getMessage());
            response = client.updatePassword(request);
            log.info("执行结果: {}", response.getMessage());
        }
    }

    @Test
    public void sayHello1Test() {
        try {
            UserInfoBlockingStub stub = UserInfoGrpc.newBlockingStub(channel);
            Builder builder = UserRequest.newBuilder();
            for (int i = 0; i < 3; i++) {
                UserRequest request = builder
                        .setId(i).setName("foo" + i).setPassword("bar" + i).setAge(18)
                        .build();
                UserResponse response = stub.selectUserInfo(request);
                log.info("执行结果: {}", response.getMessage());
                response = stub.updateUser(request);
                log.info("执行结果: {}", response.getMessage());
            }
            UserOperateBlockingStub stub1 = UserOperateGrpc.newBlockingStub(channel);
            UserResponse response = stub1.updatePassword(builder.build());
            log.info("执行结果: {}", response.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void sayHello2Test() throws InterruptedException {
        UserInfoStub stub0 = UserInfoGrpc.newStub(channel);
        UserRequest request = UserRequest.newBuilder()
                .setId(1).setName("foo").setPassword("bar").setAge(18)
                .build();
        stub0.selectUserInfo(request, new StreamObserver<UserResponse>() {
            @Override
            public void onNext(UserResponse response) {
                log.info("执行结果: {}", response.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("异常:", throwable);
            }

            @Override
            public void onCompleted() {
                // onNext()后执行
                log.info("结束...");
            }
        });

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void sayHello3Test() throws Exception {
        UserInfoFutureStub stub = UserInfoGrpc.newFutureStub(channel);
        UserRequest request = UserRequest.newBuilder()
                .setId(1).setName("foo").setPassword("bar").setAge(18)
                .build();
        ListenableFuture<UserResponse> future = stub.selectUserInfo(request);
        UserResponse response = future.get();
        log.info("执行结果: {}", response.getMessage());
    }
}