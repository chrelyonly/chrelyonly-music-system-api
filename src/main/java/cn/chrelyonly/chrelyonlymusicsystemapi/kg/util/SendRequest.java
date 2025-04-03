package cn.chrelyonly.chrelyonlymusicsystemapi.kg.util;

import cn.chrelyonly.chrelyonlymusicsystemapi.kg.config.MyConfig;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;

/**
 * 发送请求
 * @author 11725
 */
public class SendRequest {


    /**
     * 发送请求
     * @param apiUrl  /xxx
     * @param json json对象
     */
    public static JSONObject sendRequest(String apiUrl,JSONObject body,Method method) {
        // 发送请求并获取响应
        HttpRequest httpRequest;
        if (method == Method.GET){
            httpRequest = HttpRequest.get(MyConfig.SERVER_URL_API + apiUrl);
        }else{
            httpRequest = HttpRequest.post(MyConfig.SERVER_URL_API + apiUrl).body(body.toJSONString());
        }
        try(HttpResponse execute = httpRequest
                .execute()){
            return JSONObject.parseObject(execute.body());
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject(){{
                put("msg", "请求失败");
                put("ret", 500);
            }};
        }
    }
}
