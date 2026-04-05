package cn.chrelyonly.chrelyonlymusicsystemapi.music.qq.service;


import cn.chrelyonly.chrelyonlymusicsystemapi.music.kg.config.MyKgConfig;
import cn.chrelyonly.chrelyonlymusicsystemapi.music.qq.config.MyQqConfig;
import cn.chrelyonly.chrelyonlymusicsystemapi.util.RedisUtil;
import cn.chrelyonly.chrelyonlymusicsystemapi.util.SendRequest;
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
public class MusicQqService {
    /**
     * 请求发送前置方法
     */
    public JSONObject prependSendRequest(String apiUrl,JSONObject body,Method method){
        apiUrl = MyQqConfig.SERVICE_QQ_URL + apiUrl;
        return SendRequest.sendRequest(apiUrl,body,  method);
    }
    /**
     * 搜索音乐
     */
    public JSONObject getSearchByKey(String key,JSONObject headers) {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/search";
        body.put("keywords",key);
        body.put("headers",headers);
        return prependSendRequest(path, body, Method.POST);
    }
    /**
     * 获取音乐信息
     */
    public JSONObject getMusicPlay(String mid,JSONObject headers) {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/detail";
        body.put("mid",mid);
        body.put("headers",headers);
        return prependSendRequest(path, body, Method.POST);
    }
}
