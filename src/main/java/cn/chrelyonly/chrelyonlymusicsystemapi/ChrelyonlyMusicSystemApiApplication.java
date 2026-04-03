package cn.chrelyonly.chrelyonlymusicsystemapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

/**
 * @author 11725
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class ChrelyonlyMusicSystemApiApplication {
	private static final String appName = "chrelyonly-music-system-api";


    public static void main(String[] args) {
    String profile = System.getProperty("spring.profiles.active"); // 或者从环境变量读取
        if (profile == null) {
            profile = "dev"; // 默认值
        }
		Properties props = System.getProperties();
//		nacos配置
		props.setProperty("spring.cloud.loadbalancer.nacos.enabled", "true");
		props.setProperty("spring.cloud.nacos.server-addr", "192.168.10.47:8848");
		props.setProperty("spring.cloud.nacos.discovery.server-addr", "192.168.10.47:8848");
		props.setProperty("spring.cloud.nacos.config.namespace",appName);
		props.setProperty("spring.cloud.nacos.discovery.namespace",appName);
		props.setProperty("spring.cloud.nacos.config.file-extension", "yml");
		props.setProperty("spring.config.import","optional:nacos:application.yml,optional:nacos:application-" + profile + ".yml");
        SpringApplication.run(ChrelyonlyMusicSystemApiApplication.class, args);
    }

}
