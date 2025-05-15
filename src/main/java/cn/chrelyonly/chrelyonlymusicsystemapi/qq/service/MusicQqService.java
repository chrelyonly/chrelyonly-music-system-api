package cn.chrelyonly.chrelyonlymusicsystemapi.qq.service;


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
     * 搜索音乐
     */
    public JSONObject getSearchByKey(String key) {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/getSearchByKey" +
                "?key=" + URLEncoder.encode(key, StandardCharsets.UTF_8);
        JSONObject responseBody = SendRequest.sendRequest(path, body, Method.GET);
        log.info(responseBody.toJSONString());
        return responseBody;
    }
    /**
     * 获取音乐信息
     */
    public JSONObject getMusicPlay(String songmid) {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/getMusicPlay" +
                "?songmid=" + URLEncoder.encode(songmid, StandardCharsets.UTF_8);
        JSONObject responseBody = SendRequest.sendRequest(path, body, Method.GET);
        log.info(responseBody.toJSONString());
        return responseBody;
    }
}
