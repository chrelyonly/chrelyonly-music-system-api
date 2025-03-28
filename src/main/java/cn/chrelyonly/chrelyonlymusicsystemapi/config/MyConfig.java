package cn.chrelyonly.chrelyonlymusicsystemapi.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author 11725
 */
@Configuration
public class MyConfig {
    public static String PHONE = "15008898674";
    public static String UUID = "Qe1dBF8AQH1hoE7Zo1EY";
    public static String TOKEN = "b41dcfe68a1d4a6390335bd5978739a3";
//    开发用这个
//    public static String DOMAIN = "192.168.10.96";
//    后端域名
    public static String DOMAIN = "172.18.0.9";
//    音乐后端地址
    public static String LOCAL_URL_API = "http://" + DOMAIN + ":3000";
    public static String LOCAL_URL = LOCAL_URL_API + "/api/wechat/notifyCallback";
}
