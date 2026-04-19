package cn.chrelyonly.chrelyonlymusicsystemapi.music.qq.controller;

import cn.chrelyonly.chrelyonlymusicsystemapi.aop.FastRedisReturnData;
import cn.chrelyonly.chrelyonlymusicsystemapi.component.R;
import cn.chrelyonly.chrelyonlymusicsystemapi.config.MyMusicConfig;
import cn.chrelyonly.chrelyonlymusicsystemapi.music.qq.service.MusicQqService;
import cn.hutool.core.date.DateUtil;
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
        try (HttpResponse response = HttpRequest.get(MyMusicConfig.API_PROJECT_SERVER_URL + "/music-api/getUserInfo?userCode=" + userCode)
                .execute()) {
            // 转换为json
            JSONObject musicUserInfo = JSONObject.parseObject(response.body());
            if (!musicUserInfo.getBoolean("success")){
                return R.fail("用户信息异常," + musicUserInfo.getString("msg"));
            }
            headers.put("musicid",musicUserInfo.getJSONObject("data").getJSONObject("token").getString("musicid"));
            headers.put("musickey",musicUserInfo.getJSONObject("data").getJSONObject("token").getString("musickey"));

            JSONObject searchMusicRes = musicQqService.getSearchByKey(keywords,headers);
            if (searchMusicRes.getInteger("code") == 200){
                JSONArray musicList = searchMusicRes.getJSONArray("data");
                for (int i = 0; i < musicList.size(); i++) {
                    JSONObject item =  musicList.getJSONObject(i);
                    try {
                        JSONObject musicFrom = new JSONObject();
                        musicFrom.put("name",musicUserInfo.getJSONObject("data").getString("name"));
                        musicFrom.put("expired_at", DateUtil.format(DateUtil.date(musicUserInfo.getJSONObject("data").getJSONObject("token").getLong("expired_at") * 1000),"yyyy-MM-dd HH:mm:ss"));
                        item.put("musicFrom", musicFrom);
                    } catch (Exception e) {
                        item.put("musicFrom", new JSONObject(){{
                            put("name","获取用户态异常");
                        }});
                    }
                }
                return R.data(musicList);
            }else{
                return R.fail("从QQ音乐插件获取数据异常" + searchMusicRes.getString("msg"));
            }
        }
    }

}
