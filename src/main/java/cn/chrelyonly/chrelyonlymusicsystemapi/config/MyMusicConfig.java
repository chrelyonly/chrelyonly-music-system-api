package cn.chrelyonly.chrelyonlymusicsystemapi.config;

import cn.chrelyonly.chrelyonlymusicsystemapi.music.qq.config.MyQqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author 11725
 */
@Configuration
@Slf4j
public class MyMusicConfig {

    //    服务器地址
    public static String API_PROJECT_SERVER_URL;

    @Value("${apiProject.serviceUrl}")
    public void setServiceUrl(String serviceUrl) {
        log.info("初始化api-project插件");
        MyMusicConfig.API_PROJECT_SERVER_URL = serviceUrl;
    }

}
