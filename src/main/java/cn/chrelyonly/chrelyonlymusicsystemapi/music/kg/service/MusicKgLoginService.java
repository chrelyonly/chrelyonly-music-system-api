package cn.chrelyonly.chrelyonlymusicsystemapi.music.kg.service;


import cn.chrelyonly.chrelyonlymusicsystemapi.music.kg.config.MyKgConfig;
import cn.chrelyonly.chrelyonlymusicsystemapi.util.RedisUtil;
import cn.chrelyonly.chrelyonlymusicsystemapi.util.SendRequest;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONArray;
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
public class MusicKgLoginService {
    //    登录key
    private final String KEY = "kg:login:key";
    private final String COOKIE_KEY = "kg:login:cookie";
    private final String USER_INFO_KEY = "kg:login:userInfo";
    /**
     * 请求发送前置方法
     */
    public JSONObject prependSendRequest(String apiUrl,JSONObject body,Method method){
        var keyStatic = RedisUtil.hasKeyStatic(COOKIE_KEY);
        if (keyStatic){
//            // 要追加的参数
//            String appendParam = "cookie=" + RedisUtil.getKey(COOKIE_KEY);
//            // 判断是否已有参数
//            if (apiUrl.contains("?")) {
//                // 已有参数，追加用 &
//                apiUrl += "&" + appendParam;
//            } else {
//                // 没有参数，追加用 ?
//                apiUrl += "?" + appendParam;
//            }
        }
        apiUrl = MyKgConfig.SERVICE_URL + apiUrl;
        return SendRequest.sendRequest(apiUrl,body,  method);
    }
    /**
     * 获取登录二维码
     */
    public JSONObject loginQr() {
        var keyStatic = RedisUtil.hasKeyStatic(COOKIE_KEY);
        if (keyStatic){
            return new JSONObject(){{
                put("code", 201);
                put("message", "当前已经登录");
            }};
        }
//        获取二维码key
        String loginQrKey = "/login/qr/key";
        JSONObject qrKeyObject = prependSendRequest(loginQrKey,null,  Method.GET);
        if (qrKeyObject.getInteger("status") == 1){
            String qrKey = qrKeyObject.getJSONObject("data").getString("qrcode");
//            保存二维码key,方便登录
            RedisUtil.setObjectStatic(KEY,qrKey,1200);
            return qrKeyObject;
        }else {
            return new JSONObject(){{
                put("message", "错误的二维码信息");
                put("qrInfo",qrKeyObject);
            }};
        }
//        生成二维码
//        String createQr = "/login/qr/create" +
//                "?key=" + URLEncoder.encode(qrKey, StandardCharsets.UTF_8) +
//                "&qrimg=true";
//        //        String qr = qrKeyObject.getJSONObject("data").getString("qrimg");
//        JSONObject qrInfo = prependSendRequest(createQr, null, Method.GET);
////        如何二维码信息没问题则把key单独返回
//        if (qrInfo.getInteger("code") == 200){
//            qrInfo.put("qrKey", qrKey);
////            保存二维码key,方便登录
//            RedisUtil.setObjectStatic(KEY,qrKey,120);
//            return qrInfo;
//        }else {
//            return new JSONObject(){{
//                put("message", "错误的二维码信息");
//                put("qrInfo",qrInfo);
//            }};
//        }
    }

    /**
     * 检查登录
     */
    public Object loginCheck() {
        var keyStatic = RedisUtil.hasKeyStatic(COOKIE_KEY);
        if (keyStatic){
            return new JSONObject(){{
                put("code", 201);
                put("message", "当前已经登录");
            }};
        }
        var qrKeyFlag = RedisUtil.hasKeyStatic(KEY);
        if (!qrKeyFlag){
            return new JSONObject(){{
                put("code", 500);
                put("message", "请先扫描二维码后在登陆");
            }};
        }
//        检查登录
        String loginQr = "/login/qr/check" +
                "?key=" + URLEncoder.encode((String) RedisUtil.getKey(KEY), StandardCharsets.UTF_8);
        JSONObject response = prependSendRequest(loginQr, null, Method.GET);
//        判断是否登录成功 803是登陆成功
        if (response.getInteger("status") == 1){
//            将登录信息存入到redis中  存一年
            RedisUtil.setObjectStatic(COOKIE_KEY,response.toJSONString(),60 * 60 * 24 * 30 * 12);
//           删除上一次的登录用户信息
            RedisUtil.deleteKey(USER_INFO_KEY);
//            登陆成功后调用一次刷新用户信息
            resToken();
            return userAccount();
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
                put("code", 500);
                put("message", "未登录");
            }};
        }
        var userInfo = RedisUtil.hasKeyStatic(USER_INFO_KEY);
        if (userInfo){
            log.info("当前存在登录信息");
            return  RedisUtil.getKey(USER_INFO_KEY);
        }
        String userAccount = "/user/detail";
        JSONObject userAccountInfo = prependSendRequest(userAccount, null, Method.GET);
        if (userAccountInfo.getInteger("status") == 1) {
//            登陆成功去查询VIP信息
            String userVip = "/user/vip/detail";
            JSONObject userVipInfo = prependSendRequest(userVip, null, Method.GET);
            userAccountInfo.put("userVipInfo", userVipInfo);
//            登录成功则缓存信息 1天
            RedisUtil.setObjectStatic(USER_INFO_KEY,userAccountInfo,60 * 60 * 24 * 30 * 12);
        }
        return userAccountInfo;
    }
    /**
     * 3.刷新登录 调用此接口，可刷新登录状态，可以延长 token 过期时间
     */
    public JSONObject resToken() {
        var cookieKeyFlag = RedisUtil.hasKeyStatic(COOKIE_KEY);
        if (!cookieKeyFlag){
            return new JSONObject(){{
                put("code", 500);
                put("message", "请先登录");
            }};
        }
        JSONObject userAccountInfo = JSONObject.parseObject(JSONObject.toJSONString(RedisUtil.getKey(COOKIE_KEY)));
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/login/token" +
                "?userid=" + URLEncoder.encode(userAccountInfo.getJSONObject("data").getString("userid"), StandardCharsets.UTF_8) +
                "&token=" + URLEncoder.encode(userAccountInfo.getJSONObject("data").getString("token"), StandardCharsets.UTF_8);
        return prependSendRequest(path,body,  Method.POST);
    }

    /**
     * 领取vip 一天领取一次
     */
    public JSONObject youthDayVip() {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/youth/day/vip";
        return prependSendRequest(path, body, Method.GET);
    }

    /**
     * 搜索音乐
     */
    public JSONObject searchMusic(String keywords) {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/search" +
                "?keywords=" + URLEncoder.encode(keywords, StandardCharsets.UTF_8) +
                "&pagesize=20";
        return prependSendRequest(path, body, Method.GET);
    }
    /**
     * 获取音乐信息
     */
    public JSONObject songUrl(String hash) {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/song/url" +
                "?hash=" + URLEncoder.encode(hash, StandardCharsets.UTF_8);
        return prependSendRequest(path, body, Method.GET);
    }

    /**
     * 获取歌词, 分两步
     */
    public JSONObject getSongLyric(String hash) {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/search/lyric" +
                "?hash=" + URLEncoder.encode(hash, StandardCharsets.UTF_8);
        var lyricInfo = prependSendRequest(path, body, Method.GET);
        if (lyricInfo.getInteger("status") == 200){
            var candidates = lyricInfo.getJSONArray("candidates");
            if (!candidates.isEmpty()){
                var candidateInfo = candidates.getJSONObject(0);
                JSONObject lyricBody = new JSONObject();
                // 拼接 URL 参数
                String lyricPath = "/lyric" +
                        "?id=" + URLEncoder.encode(candidateInfo.getString("id"), StandardCharsets.UTF_8)
                         + "&accesskey=" + URLEncoder.encode(candidateInfo.getString("accesskey"), StandardCharsets.UTF_8)
                         + "&decode=true";
                return prependSendRequest(lyricPath, lyricBody, Method.GET);
            }
        }
        return new JSONObject();
    }
}
