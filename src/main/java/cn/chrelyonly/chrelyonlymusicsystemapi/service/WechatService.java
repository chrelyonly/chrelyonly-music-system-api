package cn.chrelyonly.chrelyonlymusicsystemapi.service;

import cn.chrelyonly.chrelyonlymusicsystemapi.config.MyMusicConfig;
import cn.chrelyonly.chrelyonlymusicsystemapi.util.MyHttpUtil;
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
@RequiredArgsConstructor
public class WechatService {
    /**
     * 请求发送前置方法
     */
    private JSONObject prependSendRequest(String apiUrl, JSONObject body, Method method,Boolean isStream){

        apiUrl = MyMusicConfig.SERVICE_WECHAT_SDK_URL + apiUrl;
        if (isStream){
            return MyHttpUtil.sendRequestAsBase64(apiUrl,body,  method);
        }else{
            return MyHttpUtil.sendRequest(apiUrl,body,  method);
        }
    }


    public JSONObject notifyWechatGroupMessage(String message, Method method){
        JSONObject body = new JSONObject();
        body.put("text",message);
        return prependSendRequest("/v1/message/text",body,method,false);
    }
}
