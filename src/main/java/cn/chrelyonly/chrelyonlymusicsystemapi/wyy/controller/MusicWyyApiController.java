package cn.chrelyonly.chrelyonlymusicsystemapi.wyy.controller;

import cn.chrelyonly.chrelyonlymusicsystemapi.aop.FastRedisReturnData;
import cn.chrelyonly.chrelyonlymusicsystemapi.component.R;
import cn.chrelyonly.chrelyonlymusicsystemapi.wyy.service.MusicWyyService;
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
@RequestMapping("/music-wyy-api")
public class MusicWyyApiController {
    private final MusicWyyService musicWyyService;


    /**
     * 获取登录二维码
     * @return json
     */
    @RequestMapping("/loginQr")
    public R loginQr(){
        JSONObject loginQr = musicWyyService.loginQr();
        return R.data(loginQr);
    }

    /**
     * 二维码检测扫码状态接口
     * @return json
     */
    @RequestMapping("/loginCheck")
    public R loginCheck(){
        return R.data(musicWyyService.loginCheck());
    }

    /**
     * 获取登录信息包括会员
     * @return json
     */
    @FastRedisReturnData(redisTime = 60)
    @RequestMapping("/userAccount")
    public R userAccount(){
        return R.data(musicWyyService.userAccount());
    }



    /**
     * 搜索API
     * @return json
     */
    @FastRedisReturnData(redisTime = 60 * 60 * 24 * 1)
    @RequestMapping("/searchMusic")
    public R searchMusic(@RequestParam String keywords){
        JSONArray musicListRes = new JSONArray();

        JSONObject searchMusic = musicWyyService.searchMusic(keywords);
//        循环获取音乐地址拼接处理
        JSONArray musicList = searchMusic.getJSONObject("result").getJSONArray("songs");
        for (Object object : musicList) {
            JSONObject music = (JSONObject) object;
            try {
                JSONObject songUrl = musicWyyService.songUrl(music.getString("id"));
                if (songUrl.getInteger("code") != 200){
                    continue;
                }
//                判断返回的音乐数组
                JSONArray jsonArray = songUrl.getJSONArray("data");
                if (jsonArray.isEmpty()){
                    continue;
                }
                musicListRes.add(new JSONObject(){{
//                歌曲id
                    put("id",music.getString("id"));
//                歌曲名
                    put("name",music.getString("name"));
//                填充图片
                    put("image",music.getString("Image"));
                    String artistsName = "";
                    String albumName = "";
                    try {
//                作者
                        artistsName = music.getJSONArray("artists").getJSONObject(0).getString("name");
                        albumName = music.getJSONObject("album").getString("name");
                        put("singerName", artistsName + albumName);
                    }catch (Exception e1){
                        log.error("获取作者失败");
                        log.info(music.toJSONString());
                        log.error(e1.getMessage());
                    }
//                音乐地址
                    put("musicUrl",jsonArray.getJSONObject(0).getString("url"));
                }});
            }catch (Exception e){
                log.error("获取音乐信息失败");
                log.info(music.toJSONString());
                log.error(e.getMessage());
            }
        }

        return R.data(musicListRes);
    }


}
