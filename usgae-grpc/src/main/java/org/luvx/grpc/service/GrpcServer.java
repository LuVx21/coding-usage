package org.luvx.grpc.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcServer {
    public static void main(String[] args) {
        int port = 8090;
        try {
            final Server server = ServerBuilder.forPort(port)
                    .addService(new HelloServiceImpl())
                    .build()
                    .start();
            log.info("服务启动成功, 监听:{}", port);
            server.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}