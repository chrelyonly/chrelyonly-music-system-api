package cn.chrelyonly.chrelyonlymusicsystemapi.kg.service;


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
public class MusicKgService {

    /**
     * 搜索音乐
     */
    public JSONObject searchMusic(String keywords) {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/search" +
                "?keywords=" + URLEncoder.encode(keywords, StandardCharsets.UTF_8) +
                "&pagesize=10";
        JSONObject responseBody = SendRequest.sendRequest(path, body, Method.GET);
        log.info(responseBody.toJSONString());
        return responseBody;
    }
    /**
     * 获取音乐信息
     */
    public JSONObject songUrl(String hash) {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/song/url" +
                "?hash=" + URLEncoder.encode(hash, StandardCharsets.UTF_8);
        JSONObject responseBody = SendRequest.sendRequest(path, body, Method.GET);
        log.info(responseBody.toJSONString());
        return responseBody;
    }
}
