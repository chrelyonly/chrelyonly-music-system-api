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
    public R searchMusic(@RequestParam String keywords,@RequestParam(required = false)  String userCode){
        if (StrUtil.isEmptyIfStr(userCode)){
            userCode = "1";
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
            headers.put("cookie",jsonObject.getJSONObject("data").getString("token"));

            JSONObject searchMusicRes = musicQqService.getSearchByKey(keywords,headers);
            if (searchMusicRes.getInteger("code") == 200){
                JSONArray musicList = searchMusicRes.getJSONArray("data");
                for (int i = 0; i < musicList.size(); i++) {
                    JSONObject item =  musicList.getJSONObject(i);
                    JSONObject musicPlay = musicQqService.getMusicPlay(item.getString("mid"), headers);
                    item.put("musicPlay", musicPlay);
                    item.put("musicFrom", jsonObject.getString("name"));
                }
                return R.data(musicList);
            }else{
                return R.fail("从QQ音乐插件获取数据异常");
            }
        }
    }

}
