package cn.chrelyonly.chrelyonlymusicsystemapi.service;

import cn.chrelyonly.chrelyonlymusicsystemapi.config.MyConfig;
import cn.chrelyonly.chrelyonlymusicsystemapi.util.SendRequest;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author 11725
 */
@Service
@Slf4j
public class LoginService {

    /**
     * 1.手机登录
     */
    public JSONObject cellphone(String code) {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/login/cellphone" +
                "?mobile=" + URLEncoder.encode(MyConfig.PHONE, StandardCharsets.UTF_8) +
                "&code=" + URLEncoder.encode(code, StandardCharsets.UTF_8);
        JSONObject responseBody = SendRequest.sendRequest(path, body, Method.POST);
        log.info(responseBody.toJSONString());
        return responseBody;
    }

    /**
     * 2.发送验证码
     */
    public JSONObject captchaSent() {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/captcha/sent" +
                "?mobile=" + URLEncoder.encode(MyConfig.PHONE, StandardCharsets.UTF_8);
        JSONObject responseBody = SendRequest.sendRequest(path, body, Method.POST);
        log.info(responseBody.toJSONString());
        return responseBody;
    }


    /**
     * 3.刷新登录 调用此接口，可刷新登录状态，可以延长 token 过期时间
     */
    public JSONObject loginToken(String userid,String token) {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/login/token" +
                "?userid=" + URLEncoder.encode(userid, StandardCharsets.UTF_8) +
                "&token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
        JSONObject responseBody = SendRequest.sendRequest(path,body,  Method.POST);
        log.info(responseBody.toJSONString());
        return responseBody;
    }

}
