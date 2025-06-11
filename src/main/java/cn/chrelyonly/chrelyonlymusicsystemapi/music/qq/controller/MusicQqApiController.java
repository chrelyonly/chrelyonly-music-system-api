package cn.chrelyonly.chrelyonlymusicsystemapi.music.qq.controller;

import cn.chrelyonly.chrelyonlymusicsystemapi.aop.FastRedisReturnData;
import cn.chrelyonly.chrelyonlymusicsystemapi.component.R;
import cn.chrelyonly.chrelyonlymusicsystemapi.music.qq.service.MusicQqService;
import cn.hutool.core.util.StrUtil;
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
//    @FastRedisReturnData(redisTime = 60 * 60 * 24 * 1)
    @RequestMapping("/searchMusic")
    public R searchMusic(@RequestParam String keywords){
        JSONArray musicListRes = new JSONArray();

        JSONObject searchMusic = musicQqService.getSearchByKey(keywords);
        try {
            JSONArray jsonArray = searchMusic.getJSONObject("response").getJSONObject("data").getJSONObject("song").getJSONArray("list");
            for (Object object : jsonArray) {
                JSONObject music = (JSONObject) object;
                try {
                    String songmid = music.getString("songmid");
                    JSONObject songUrl = musicQqService.getMusicPlay(songmid);
                    String url = songUrl.getJSONObject("data").getJSONObject("playUrl").getJSONObject(songmid).getString("url");
                    if (StrUtil.isBlankIfStr(url)){
                        continue;
                    }
                    musicListRes.add(new JSONObject() {{
                        put("musicName", music.getString("songname"));
                        put("musicUrl", url);
                        try {
//                作者
                            JSONArray singerList = music.getJSONArray("singer");
                            for (Object singerInfo : singerList) {
                                JSONObject  singer = (JSONObject) singerInfo;
                                put("singerName", singer.getString("name"));
                            }
                        }catch (Exception e1){
                            log.error("获取作者失败");
                            log.info(music.toJSONString());
                            log.error(e1.getMessage());
                        }
                    }});
                } catch (Exception e) {
                    log.error("获取音乐地址失败");
                    log.info(music.toJSONString());
                    log.error(e.getMessage());
                }
            }
        }catch (Exception e){
            log.error("获取音乐信息失败");
            log.info(searchMusic.toJSONString());
            log.error(e.getMessage());
        }
        return R.data(musicListRes);
    }



    /**
     * 搜索API
     * @return json
     */
    @RequestMapping("/getSearchByKey")
    public R searchList(@RequestParam String key){
        return R.data(musicQqService.getSearchByKey(key));
    }

    /**
     * 音乐信息
     * @return json
     */
    @RequestMapping("/getMusicPlay")
    public R getMusicPlay(@RequestParam String songmid){
        return R.data(musicQqService.getMusicPlay(songmid));
    }
}
