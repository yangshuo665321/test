package cn.ys.mongo;

import cn.ys.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MongoApp {

    public static void main(String[] args) {
        SpringApplication.run(MongoApp.class);
    }

    /**
     * 将 IdWorker 注入到 Spring 容器中
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1, 1);
    }
}
