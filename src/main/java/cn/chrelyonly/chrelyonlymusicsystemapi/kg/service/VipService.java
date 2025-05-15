package cn.chrelyonly.chrelyonlymusicsystemapi.kg.service;


import cn.chrelyonly.chrelyonlymusicsystemapi.util.SendRequest;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 11725
 */
@Service
@Slf4j
public class VipService {

    /**
     * 领取vip 一天领取一次
     */
    public JSONObject youthDayVip() {
        JSONObject body = new JSONObject();
        // 拼接 URL 参数
        String path = "/youth/day/vip";
        JSONObject responseBody = SendRequest.sendRequest(path, body, Method.GET);
        log.info(responseBody.toJSONString());
        return responseBody;
    }

}
