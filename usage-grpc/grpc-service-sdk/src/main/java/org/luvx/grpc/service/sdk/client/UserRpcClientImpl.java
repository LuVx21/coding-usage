package org.luvx.grpc.service.sdk.client;

import java.util.Iterator;

import org.luvx.grpc.service.proto.user.UserInfoGrpc;
import org.luvx.grpc.service.proto.user.UserInfoGrpc.UserInfoBlockingStub;
import org.luvx.grpc.service.proto.user.UserRequest;
import org.luvx.grpc.service.proto.user.UserResponse;
import org.luvx.grpc.service.sdk.RpcConfig;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRpcClientImpl implements UserRpcClient {
    private static final UserInfoBlockingStub stub;

    static {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", RpcConfig.port)
                .usePlaintext()
                .build();
        stub = UserInfoGrpc.newBlockingStub(channel);
    }

    @Override
    public UserResponse selectUserInfo(UserRequest request) {
        return stub.selectUserInfo(request);
    }

    @Override
    public Iterator<UserResponse> selectUserInfo1(UserRequest request) {
        return stub.selectUserInfo1(request);
    }
}