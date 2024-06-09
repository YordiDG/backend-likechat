package org.yoes.likechatbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application-jwt.properties")
public class LikeChatBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LikeChatBackendApplication.class, args);
    }

}
