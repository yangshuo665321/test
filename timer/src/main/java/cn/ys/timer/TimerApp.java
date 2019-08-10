package cn.ys.timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TimerApp {

    public static void main(String[] args) {

        SpringApplication.run(TimerApp.class);
    }
}
