package cn.chrelyonly.chrelyonlymusicsystemapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 11725
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class ChrelyonlyMusicSystemApiApplication {
	private static final String appName = "chrelyonly-music-system-api";


public static void main(String[] args) {
    SpringApplication app = new SpringApplication(ChrelyonlyMusicSystemApiApplication.class);

    // 获取命令行传递的 spring.profiles.active
    // 默认环境
    String profile = "dev";
    // 从启动参数读取
    for (String arg : args) {
        if (arg.startsWith("--spring.profiles.active=")) {
            profile = arg.substring("--spring.profiles.active=".length());
            break;
        }
    }

    // 设置 Nacos 配置
    Map<String, Object> defaultProps = new HashMap<>();
    defaultProps.put("spring.cloud.loadbalancer.nacos.enabled", "true");
    defaultProps.put("spring.cloud.nacos.server-addr", "192.168.10.47:8848");
    defaultProps.put("spring.cloud.nacos.discovery.server-addr", "192.168.10.47:8848");
    defaultProps.put("spring.cloud.nacos.config.namespace", appName);
    defaultProps.put("spring.cloud.nacos.discovery.namespace", appName);
    defaultProps.put("spring.cloud.nacos.config.file-extension", "yml");
    defaultProps.put("spring.config.import", "optional:nacos:application.yml,optional:nacos:application-" + profile + ".yml");

    app.setDefaultProperties(defaultProps);
    app.run(args);
}

}
