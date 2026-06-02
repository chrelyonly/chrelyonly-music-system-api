package cn.chrelyonly.chrelyonlymusicsystemapi.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;

import java.io.InputStream;

/**
 * 发送请求
 * @author 11725
 */
public class MyHttpUtil {


    /**
     * 发送请求
     * @param apiUrl  /xxx
     * @param json json对象
     */
    public static JSONObject sendRequest(String apiUrl, JSONObject body, Method method) {
        // 发送请求并获取响应
        HttpRequest httpRequest;
        if (method == Method.GET){
            httpRequest = HttpRequest.get(apiUrl);
        }else{
            httpRequest = HttpRequest.post(apiUrl).body(body.toJSONString());
        }
        try(HttpResponse execute = httpRequest
                .execute()){
            String stringRes = execute.body();
            return JSONObject.parseObject(stringRes);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject(){{
                put("msg", "请求失败");
                put("code", 500);
                put("ret", 500);
            }};
        }
    }
    /**
     * 发送 HTTP 请求并返回响应流
     * @param apiUrl 请求地址
     * @param body 请求体（仅 POST 有效）
     * @param method 请求方法 GET 或 POST
     * @return 响应体的输入流 InputStream，调用方负责关闭
     */
    public static JSONObject sendRequestAsBase64(String apiUrl, JSONObject body, Method method) {
        HttpRequest httpRequest;

        if (method == Method.GET){
            httpRequest = HttpRequest.get(apiUrl);
        }else{
            httpRequest = HttpRequest.post(apiUrl)
                    .body(body.toJSONString());
        }
        try (HttpResponse response = httpRequest.execute();
             InputStream in = response.bodyStream()) {
            if (response.isOk()) {
                String rawContentType = response.header("Content-Type");
                String contentType = null;
// 如果存在 Content-Type，就规范化处理
                if (rawContentType != null && !rawContentType.isEmpty()) {
                    contentType = rawContentType.split(";")[0].trim().toLowerCase();
                }

// 如果还是为空（极端情况），你可以选择跳过前缀或返回错误
                if (contentType == null || contentType.isEmpty()) {
                    return new JSONObject(){{
                        put("msg", "服务端未返回 Content-Type，无法生成有效 base64 前缀");
                        put("code", 500);
                        put("ret", 500);
                    }};
                }
// 拼接前缀
                String base64Prefix = "data:" + contentType + ";base64,";
                String base64Body = Base64.encode(in);
                String result = base64Prefix + base64Body;
                return new JSONObject(){{
                    put("data",result);
                    put("msg", "操作成功");
                    put("code", 200);
                }};
            } else {
                System.err.println("请求失败，状态码: " + response.getStatus());
                return new JSONObject(){{
                    put("msg", "请求失败");
                    put("code", 500);
                    put("ret", 500);
                }};
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject(){{
                put("msg", "请求失败");
                put("code", 500);
                put("ret", 500);
            }};
        }
    }
}
