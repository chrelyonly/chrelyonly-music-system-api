package cn.chrelyonly.chrelyonlymusicsystemapi.music.kg.controller;

import cn.chrelyonly.chrelyonlymusicsystemapi.aop.FastRedisReturnData;
import cn.chrelyonly.chrelyonlymusicsystemapi.component.R;
import cn.chrelyonly.chrelyonlymusicsystemapi.music.kg.service.MusicKgLoginService;
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
@RequestMapping("/music-kg-api")
public class MusicKgApiController {

    private final MusicKgLoginService loginService;
    /**
     * 获取登录二维码
     * @return json
     */
    @RequestMapping("/loginQr")
    public R loginQr(){
        JSONObject loginQr = loginService.loginQr();
        return R.data(loginQr);
    }

    /**
     * 二维码检测扫码状态接口
     * @return json
     */
    @RequestMapping("/loginCheck")
    public R loginCheck(){
        return loginService.loginCheck();
    }



    /**
     * 刷新登录token
     * @return json
     */
    @RequestMapping("/resToken")
    public R resToken(){
        JSONObject cellphone = loginService.resToken();
        return R.data(cellphone);
    }
    /**
     * 获取用户信息
     * @return json
     */
    @RequestMapping("/userDetail")
    public R userDetail(){
        return loginService.userAccount();
    }
    /**
     * 领取vip  畅听 VIP
     * @return json
     */
    @RequestMapping("/youthDayVip")
    public R youthDayVip(){
        return loginService.youthDayVip();
    }
    /**
     * 升级概念版VIP
     * @return json
     */
    @RequestMapping("/upgradeYouthDayVip")
    public R upgradeYouthDayVip(){
        return loginService.upgradeYouthDayVip();
    }
    /**
     * 搜索API
     * @return json
     */
    @FastRedisReturnData(redisTime = 60 * 60 * 24 * 1)
    @RequestMapping("/searchMusic")
    public R searchMusic(@RequestParam String keywords){
        JSONArray musicListRes = new JSONArray();

        JSONObject searchMusic = loginService.searchMusic(keywords);
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
                JSONObject songUrl = loginService.songUrl(hash);
                JSONArray url = songUrl.getJSONArray("url");
                if (url.isEmpty()) {
                    continue;
                }

//                尝试获取歌词
                JSONObject songLyric = loginService.getSongLyric(hash);
                musicListRes.add(new JSONObject(){{
//                填充图片
                    put("image",music.getString("Image"));
//                作者
                    put("singerName",music.getString("SingerName"));
//                音乐地址
                    put("musicUrl",url.getString(0));
//                音乐地址
                    put("musicHash",hash);
//                音乐歌词
                    put("songLyric",songLyric.getString("decodeContent"));
//                    音乐名称
                    put("musicName",music.getString("OriSongName"));
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
