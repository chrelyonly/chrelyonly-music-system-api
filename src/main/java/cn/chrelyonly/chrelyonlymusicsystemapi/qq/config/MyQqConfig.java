package cn.chrelyonly.chrelyonlymusicsystemapi.qq.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author 11725
 */
@Configuration
public class MyQqConfig {
//    后端域名
    public static String DOMAIN = "172.18.0.12";
//    音乐后端地址
//    public static String SERVER_URL_API = "http://" + DOMAIN + ":3000";
    public static String SERVER_URL_API = "http://127.0.0.1:3200";
//    测试地址
//    public static String SERVER_URL_API = "https://chrelyonly-music-system.frp.chrelyonly.cn";
}
