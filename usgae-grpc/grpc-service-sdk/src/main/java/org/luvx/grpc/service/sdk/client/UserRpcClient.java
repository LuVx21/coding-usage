package org.luvx.grpc.service.sdk.client;

import org.luvx.grpc.service.proto.user.UserRequest;
import org.luvx.grpc.service.proto.user.UserResponse;

public interface UserRpcClient {
    UserResponse selectUserInfo(UserRequest request);

    UserResponse updateUser(UserRequest request);

    UserResponse updatePassword(UserRequest request);

    static UserRpcClient getClient() {
        return new UserRpcClientImpl();
    }
}
