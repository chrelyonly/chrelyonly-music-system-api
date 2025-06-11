package cn.chrelyonly.chrelyonlymusicsystemapi.music.wyy.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author 11725
 */
@Configuration
@Slf4j
public class MyWyyConfig {
//    服务器地址
    public static String SERVICE_URL;



    @Value("${music.wyy.serviceUrl}")
    public void setServiceUrl(String serviceUrl) {
        log.info("初始化WYY音乐插件");
        MyWyyConfig.SERVICE_URL = serviceUrl;
    }
}
