package cn.ys.jd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//使用定时任务需要开启注解
@EnableScheduling
public class JdApp {

    public static void main(String[] args) {
        SpringApplication.run(JdApp.class);
    }
}
