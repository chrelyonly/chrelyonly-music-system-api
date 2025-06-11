package cn.chrelyonly.chrelyonlymusicsystemapi.music.qq.config;

import cn.chrelyonly.chrelyonlymusicsystemapi.music.kg.config.MyKgConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author 11725
 */
@Configuration
@Slf4j
public class MyQqConfig {
    //    服务器地址
    public static String SERVICE_QQ_URL;

    @Value("${music.qq.serviceUrl}")
    public void setServiceUrl(String serviceUrl) {
        log.info("初始化QQ音乐插件");
        MyQqConfig.SERVICE_QQ_URL = serviceUrl;
    }

}
