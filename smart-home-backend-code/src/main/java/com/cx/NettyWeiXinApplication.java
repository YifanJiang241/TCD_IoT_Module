package com.cx;

import com.cx.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;

@SpringBootApplication
public class NettyWeiXinApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NettyWeiXinApplication.class, args);
    }

    @Autowired
    NettyServer nettyServer;
    @Override
    @Async
    public void run(String... args) throws Exception {
        nettyServer.run();
    }
}
