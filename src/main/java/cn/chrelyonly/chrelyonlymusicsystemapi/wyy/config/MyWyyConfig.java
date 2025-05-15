package cn.chrelyonly.chrelyonlymusicsystemapi.wyy.config;

import cn.hutool.core.lang.Console;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author 11725
 */
@Configuration
public class MyWyyConfig {
//    服务器地址
    public static String SERVICE_URL;



    @Value("${music.wyy.serviceUrl}")
    public void setServiceUrl(String serviceUrl) {
        MyWyyConfig.SERVICE_URL = serviceUrl;
    }
}
