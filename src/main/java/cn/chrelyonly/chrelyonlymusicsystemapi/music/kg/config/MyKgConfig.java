package cn.chrelyonly.chrelyonlymusicsystemapi.music.kg.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author 11725
 */
@Configuration
@Slf4j
public class MyKgConfig {
    //    服务器地址
    public static String SERVICE_URL;

    @Value("${music.kg.serviceUrl}")
    public void setServiceUrl(String serviceUrl) {
        log.info("初始化KG音乐插件");
        MyKgConfig.SERVICE_URL = serviceUrl;
    }


}
