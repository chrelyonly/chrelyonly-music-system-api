package cn.chrelyonly.chrelyonlymusicsystemapi.wyy.service;


import cn.chrelyonly.chrelyonlymusicsystemapi.util.SendRequest;
import cn.chrelyonly.chrelyonlymusicsystemapi.util.RedisUtil;
import cn.chrelyonly.chrelyonlymusicsystemapi.wyy.config.MyWyyConfig;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author 11725
 */
@Service
@Slf4j
public class MusicWyyService {

    //    登录key
    private final String COOKIE_KEY = "wyy:login:cookie";
    private final String USER_INFO_KEY = "wyy:login:userInfo";

    /**
     * 请求发送前置方法
     */
    public JSONObject prependSendRequest(String apiUrl,JSONObject body,Method method){
        var keyStatic = RedisUtil.hasKeyStatic(COOKIE_KEY);
        if (keyStatic){
            // 要追加的参数
            String appendParam = "cookie=" + RedisUtil.getKey(COOKIE_KEY);
            // 判断是否已有参数
            if (apiUrl.contains("?")) {
                // 已有参数，追加用 &
                apiUrl += "&" + appendParam;
            } else {
                // 没有参数，追加用 ?
                apiUrl += "?" + appendParam;
            }
        }
        apiUrl = MyWyyConfig.SERVICE_URL + apiUrl;
        return SendRequest.sendRequest(apiUrl,body,  method);
    }
    /**
     * 获取登录二维码
     */
    public JSONObject loginQr() {
        var keyStatic = RedisUtil.hasKeyStatic(COOKIE_KEY);
        if (keyStatic){
            return new JSONObject(){{
               put("message", "当前已经登录");
            }};
        }
//        获取二维码key
        String loginQrKey = "/login/qr/key";
        JSONObject qrKeyObject = prependSendRequest(loginQrKey,null,  Method.GET);
        String qrKey = qrKeyObject.getJSONObject("data").getString("unikey");
//        生成二维码
        String createQr = "/login/qr/create" +
                "?key=" + URLEncoder.encode(qrKey, StandardCharsets.UTF_8) +
                "&qrimg=true";
        //        String qr = qrKeyObject.getJSONObject("data").getString("qrimg");
        JSONObject qrInfo = prependSendRequest(createQr, null, Method.GET);
//        如何二维码信息没问题则把key单独返回
        if (qrInfo.getInteger("code") == 200){
            qrInfo.put("qrKey", qrKey);
            return qrInfo;
        }else {
            return new JSONObject(){{
                put("message", "错误的二维码信息");
                put("qrInfo",qrInfo);
            }};
        }
    }

    /**
     * 检查登录
     */
    public JSONObject loginCheck(String qrKey) {
        var keyStatic = RedisUtil.hasKeyStatic(COOKIE_KEY);
        if (keyStatic){
            return new JSONObject(){{
                put("message", "当前已经登录");
            }};
        }
//        生成二维码
        String loginQr = "/login/qr/check" +
                "?key=" + URLEncoder.encode(qrKey, StandardCharsets.UTF_8) +
                "&noCookie=true";
        JSONObject response = prependSendRequest(loginQr, null, Method.GET);
//        判断是否登录成功 803是登陆成功
        if (response.getInteger("code") == 803){
//            将登录信息存入到redis中  存一年
            RedisUtil.setObjectStatic(COOKIE_KEY,response.getString("cookie"),60 * 60 * 24 * 30 * 12);
//           删除上一次的登录用户信息
            RedisUtil.deleteKey(USER_INFO_KEY);
        }
        return response;
    }

    /**
     * 获取登录用户详情
     */
    public Object userAccount() {
        var keyStatic = RedisUtil.hasKeyStatic(COOKIE_KEY);
        if (!keyStatic){
            return new JSONObject(){{
                put("message", "未登录");
            }};
        }
        var userInfo = RedisUtil.hasKeyStatic(USER_INFO_KEY);
        if (userInfo){
            log.info("当前存在登录信息");
            return  RedisUtil.getKey(USER_INFO_KEY);
        }
        String userAccount = "/user/account";
        JSONObject userAccountInfo = prependSendRequest(userAccount, null, Method.GET);
        if (userAccountInfo.getInteger("code") == 200) {
//            登陆成功去查询VIP信息

            String userVip = "/vip/info/v2" +
                    "?uid=" + URLEncoder.encode(userAccountInfo.getJSONObject("account").getString("id"), StandardCharsets.UTF_8);
            JSONObject userVipInfo = prependSendRequest(userVip, null, Method.GET);
            userAccountInfo.put("userVipInfo", userVipInfo);
//            登录成功则缓存信息 1天
            RedisUtil.setObjectStatic(USER_INFO_KEY,userAccountInfo,60 * 60 * 24);
        }
        return userAccountInfo;
    }


    /**
     * app首页的搜索功能
     * @param keywords 关键字
     */
    public JSONObject searchMusic(String keywords) {
        String search  = "/search" +
                "?limit=15" +
                "&keywords=" + URLEncoder.encode(keywords, StandardCharsets.UTF_8);
        return prependSendRequest(search, null, Method.GET);
    }

    /**
     * 获取歌曲连接 ids逗号
     */
    public JSONObject songUrl(String ids) {
        String search  = "/song/url/v1" +
                "?id=" + URLEncoder.encode(ids, StandardCharsets.UTF_8) +
                "&level=standard";
        return prependSendRequest(search, null, Method.GET);
    }
}
