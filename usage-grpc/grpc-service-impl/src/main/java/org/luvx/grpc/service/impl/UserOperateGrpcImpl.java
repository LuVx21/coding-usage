package org.luvx.grpc.service.impl;

import org.luvx.grpc.service.proto.user.UserOperateGrpc.UserOperateImplBase;
import org.luvx.grpc.service.proto.user.UserRequest;
import org.luvx.grpc.service.proto.user.UserResponse;

import io.grpc.stub.StreamObserver;

public class UserOperateGrpcImpl extends UserOperateImplBase {
    @Override
    public void updatePassword(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse response = UserResponse.newBuilder()
                .setMessage("更新密码..." + request.getId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
