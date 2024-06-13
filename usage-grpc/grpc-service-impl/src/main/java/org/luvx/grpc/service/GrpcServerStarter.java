package org.luvx.grpc.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.luvx.grpc.service.impl.UserInfoGrpcImpl;
import org.luvx.grpc.service.impl.UserOperateGrpcImpl;
import org.luvx.grpc.service.sdk.RpcConfig;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcServerStarter {
    private Server server;

    private void start() throws IOException {
        server = ServerBuilder.forPort(RpcConfig.port)
                .addService(new UserInfoGrpcImpl())
                .addService(new UserOperateGrpcImpl())
                .build()
                .start();
        log.info("服务启动成功, 监听:{}", RpcConfig.port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                GrpcServerStarter.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
            log.warn("服务停止...");
        }));
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final GrpcServerStarter server = new GrpcServerStarter();
        server.start();
        server.blockUntilShutdown();
    }
}