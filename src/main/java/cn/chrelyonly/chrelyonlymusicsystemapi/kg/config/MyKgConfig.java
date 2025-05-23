package cn.chrelyonly.chrelyonlymusicsystemapi.kg.config;

import cn.chrelyonly.chrelyonlymusicsystemapi.wyy.config.MyWyyConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author 11725
 */
@Configuration
public class MyKgConfig {
    //    服务器地址
    public static String SERVICE_URL;

    @Value("${music.kg.serviceUrl}")
    public void setServiceUrl(String serviceUrl) {
        MyKgConfig.SERVICE_URL = serviceUrl;
    }


}
