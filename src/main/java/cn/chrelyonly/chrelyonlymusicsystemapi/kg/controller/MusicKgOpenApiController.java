package cn.chrelyonly.chrelyonlymusicsystemapi.kg.controller;

import cn.chrelyonly.chrelyonlymusicsystemapi.aop.FastRedisReturnData;
import cn.chrelyonly.chrelyonlymusicsystemapi.component.R;
import cn.chrelyonly.chrelyonlymusicsystemapi.kg.service.MusicKgService;
import cn.chrelyonly.chrelyonlymusicsystemapi.qq.service.MusicQqService;
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
@RequestMapping("/music-kg-open-api")
public class MusicKgOpenApiController {
    private final MusicKgService musicService;

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
            try {
                String fileHash = music.getString("FileHash");
                String sqFileHash = music.getString("SQFileHash");
//                String hash = music.getJSONObject("HQ").getString("Hash");
                String hash = StrUtil.isBlankIfStr(fileHash) ? sqFileHash : fileHash;
                if (StrUtil.isBlankIfStr(hash)) {
                    continue;
                }
                JSONObject songUrl = musicService.songUrl(hash);
                JSONArray url = songUrl.getJSONArray("url");
                if (StrUtil.isBlankIfStr(url.toJSONString())) {
                    continue;
                }
                musicListRes.add(new JSONObject(){{
//                填充图片
                    put("image",music.getString("Image"));
//                作者
                    put("singerName",music.getString("SingerName"));
//                音乐地址
                    put("musicUrl",url);
                }});
            }catch (Exception e){
                log.error("获取音乐信息失败");
                log.info(music.toJSONString());
                log.error(e.getMessage());
            }
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
