package cn.chrelyonly.chrelyonlymusicsystemapi.music.qq.controller;

import cn.chrelyonly.chrelyonlymusicsystemapi.aop.FastRedisReturnData;
import cn.chrelyonly.chrelyonlymusicsystemapi.component.R;
import cn.chrelyonly.chrelyonlymusicsystemapi.config.MyMusicConfig;
import cn.chrelyonly.chrelyonlymusicsystemapi.music.qq.service.MusicQqService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chrelyonly
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/music-qq-api")
public class MusicQqApiController {
    private final MusicQqService musicQqService;

    /**
     * 搜索API
     * @return json
     */
    @FastRedisReturnData(redisTime = 60 * 60 * 24 * 1)
    @RequestMapping("/searchMusic")
    public R searchMusic(@RequestParam String keywords, Integer userCode){
        JSONArray musicListRes = new JSONArray();
        if (userCode == null){
            userCode = 1;
        }
        JSONObject headers = new JSONObject();
//        用用户token获取登录信息
        // 发送请求
        String body;
        try (HttpResponse response = HttpRequest.get(MyMusicConfig.API_PROJECT_SERVER_URL + "/music-api/getUserInfo?userCode=" + userCode)
                .execute()) {
            // 获取响应体
            body = response.body();
            // 转换为json
            JSONObject jsonObject = JSONObject.parseObject(body);
            headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36");
            headers.put("cookie",jsonObject.getString("token"));
        }
        JSONObject searchMusic = musicQqService.getSearchByKey(keywords,headers);
        try {
        }catch (Exception e){
            log.error("获取音乐信息失败");
            log.info(searchMusic.toJSONString());
            log.error(e.getMessage());
        }
        return R.data(musicListRes);
    }

}
