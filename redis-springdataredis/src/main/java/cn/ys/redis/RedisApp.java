package cn.ys.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication
public class RedisApp {

    public static void main(String[] args) {
        SpringApplication.run(RedisApp.class);
    }
}
