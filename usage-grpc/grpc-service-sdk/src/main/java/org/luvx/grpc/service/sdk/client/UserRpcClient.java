package org.luvx.grpc.service.sdk.client;

import java.util.Iterator;

import org.luvx.grpc.service.proto.user.UserRequest;
import org.luvx.grpc.service.proto.user.UserResponse;

public interface UserRpcClient {
    UserResponse selectUserInfo(UserRequest request);

    Iterator<UserResponse> selectUserInfo1(UserRequest request);

    static UserRpcClient getClient() {
        return new UserRpcClientImpl();
    }
}
