package cn.chrelyonly.chrelyonlymusicsystemapi.service;

import cn.chrelyonly.chrelyonlymusicsystemapi.config.MyConfig;
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
public class UserInfoService {

    /**
     * 1.手机登录
     */
    public JSONObject userDetail() {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/user/detail";
        JSONObject responseBody = SendRequest.sendRequest(path, body, Method.GET);
        log.info(responseBody.toJSONString());
        return responseBody;
    }

}
