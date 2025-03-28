package cn.chrelyonly.chrelyonlymusicsystemapi.service;

import cn.chrelyonly.chrelyonlymusicsystemapi.config.MyConfig;
import cn.chrelyonly.chrelyonlymusicsystemapi.util.SendRequest;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile", MyConfig.PHONE);
        jsonObject.put("code",code);
        JSONObject responseBody = SendRequest.sendRequest("/login/cellphone", jsonObject, Method.POST);
        log.info(responseBody.toJSONString());
        return responseBody;
    }

    /**
     * 2.发送验证码
     */
    public JSONObject captchaSent() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile", MyConfig.PHONE);
        JSONObject responseBody = SendRequest.sendRequest("/captcha/sent", jsonObject, Method.POST);
        log.info(responseBody.toJSONString());
        return responseBody;
    }


    /**
     * 3.刷新登录 调用此接口，可刷新登录状态，可以延长 token 过期时间
     */
    public JSONObject loginToken(String userid,String token) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid",userid);
        jsonObject.put("token",token);
        JSONObject responseBody = SendRequest.sendRequest("/login/token", jsonObject, Method.POST);
        log.info(responseBody.toJSONString());
        return responseBody;
    }

}
