package cn.chrelyonly.chrelyonlymusicsystemapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 11725
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class ChrelyonlyMusicSystemApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChrelyonlyMusicSystemApiApplication.class, args);
    }

}
