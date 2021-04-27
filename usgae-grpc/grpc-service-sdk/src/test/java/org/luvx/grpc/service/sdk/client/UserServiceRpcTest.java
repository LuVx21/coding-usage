package org.luvx.grpc.service.sdk.client;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.luvx.grpc.service.proto.user.UserInfoGrpc;
import org.luvx.grpc.service.proto.user.UserInfoGrpc.UserInfoBlockingStub;
import org.luvx.grpc.service.proto.user.UserInfoGrpc.UserInfoFutureStub;
import org.luvx.grpc.service.proto.user.UserInfoGrpc.UserInfoStub;
import org.luvx.grpc.service.proto.user.UserRequest;
import org.luvx.grpc.service.proto.user.UserRequest.Builder;
import org.luvx.grpc.service.proto.user.UserResponse;
import org.luvx.grpc.service.proto.user.UserResponseList;
import org.luvx.grpc.service.sdk.RpcConfig;

import com.google.common.util.concurrent.ListenableFuture;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserServiceRpcTest {

    ManagedChannel channel = null;

    @Before
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", RpcConfig.port)
                .usePlaintext()
                .build();
    }

    /**
     * 流通讯模式
     */
    @Test
    public void selectUserInfo2Test() throws InterruptedException {
        UserInfoStub stub = UserInfoGrpc.newStub(channel);
        StreamObserver<UserRequest> observer = stub.selectUserInfo2(
                new StreamObserver<UserResponseList>() {
                    @Override
                    public void onNext(UserResponseList userResponseList) {
                        List<UserResponse> usersList = userResponseList.getUsersList();
                        for (UserResponse r : usersList) {
                            log.info("获取到: {}", r.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        log.error("异常:", throwable);
                    }

                    @Override
                    public void onCompleted() {
                        log.info("完成:selectUserInfo2");
                    }
                }
        );
        for (int i = 0; i < 2; i++) {
            observer.onNext(UserRequest.newBuilder()
                    .setId(i).setName("foo" + i).setPassword("bar" + i).setAge(18 + i)
                    .build()
            );
        }
        observer.onCompleted();

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void selectUserInfo3Test() throws InterruptedException {
        UserInfoStub stub = UserInfoGrpc.newStub(channel);
        StreamObserver<UserRequest> observer1 = stub.selectUserInfo3(
                new StreamObserver<UserResponse>() {
                    @Override
                    public void onNext(UserResponse userResponse) {
                        log.info("获取到: {}", userResponse.getMessage());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        log.error("异常:", throwable);
                    }

                    @Override
                    public void onCompleted() {
                        log.info("完成:selectUserInfo3");
                    }
                }
        );
        for (int i = 0; i < 2; i++) {
            observer1.onNext(UserRequest.newBuilder()
                    .setId(i).setName("foo" + i).setName("bar" + i).setAge(18 + i)
                    .build()
            );
        }
        observer1.onCompleted();

        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 同步
     */
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
                Iterator<UserResponse> it = stub.selectUserInfo1(request);
                while (it.hasNext()) {
                    UserResponse r = it.next();
                    log.info("执行结果: {}", r.getMessage());
                }
            }
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

    /**
     * 异步
     */
    @Test
    public void sayHello2Test() throws InterruptedException {
        UserInfoStub stub = UserInfoGrpc.newStub(channel);
        UserRequest request = UserRequest.newBuilder()
                .setId(1).setName("foo").setPassword("bar").setAge(18)
                .build();
        stub.selectUserInfo(request, new StreamObserver<UserResponse>() {
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

    /**
     * 异步
     */
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