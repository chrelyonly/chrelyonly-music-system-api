package cn.chrelyonly.chrelyonlymusicsystemapi.controller;

import cn.chrelyonly.chrelyonlymusicsystemapi.aop.FastRedisReturnData;
import cn.chrelyonly.chrelyonlymusicsystemapi.component.R;
import cn.chrelyonly.chrelyonlymusicsystemapi.service.LoginService;
import cn.chrelyonly.chrelyonlymusicsystemapi.service.MusicService;
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
@RequestMapping("/music-open-api")
public class MusicOpenApiController {
    private final MusicService musicService;

    /**
     * 搜索API
     * @return json
     */
    @FastRedisReturnData(redisTime = 60 * 60 * 24 * 30)
    @RequestMapping("/searchMusic")
    public R searchMusic(@RequestParam String keywords){
        JSONArray musicListRes = new JSONArray();

        JSONObject searchMusic = musicService.searchMusic(keywords);
//        循环获取音乐地址拼接处理
        JSONArray musicList = searchMusic.getJSONObject("data").getJSONArray("lists");
        for (Object object : musicList) {
            JSONObject music = (JSONObject) object;
            String hash = music.getJSONObject("HQ").getString("Hash");
            JSONObject songUrl = musicService.songUrl(hash);
            JSONArray url = songUrl.getJSONArray("url");
            musicListRes.add(new JSONObject(){{
//                填充图片
                put("image",music.getString("Image"));
//                作者
                put("singerName",music.getString("SingerName"));
//                音乐地址
                put("musicUrl",url);
            }});
        }

        return R.data(musicListRes);
    }



    /**
     * 搜索API
     * @return json
     */
    @RequestMapping("/searchList")
    public R searchList(@RequestParam String keywords){
        return R.data(musicService.searchMusic(keywords));
    }

    /**
     * 音乐信息
     * @return json
     */
    @RequestMapping("/songUrl")
    public R songUrl(@RequestParam String hash){
        return R.data(musicService.songUrl(hash));
    }
}
