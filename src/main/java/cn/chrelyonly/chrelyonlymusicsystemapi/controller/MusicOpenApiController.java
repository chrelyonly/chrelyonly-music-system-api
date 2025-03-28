package cn.chrelyonly.chrelyonlymusicsystemapi.controller;

import cn.chrelyonly.chrelyonlymusicsystemapi.component.R;
import cn.chrelyonly.chrelyonlymusicsystemapi.service.LoginService;
import cn.chrelyonly.chrelyonlymusicsystemapi.service.MusicService;
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
    @RequestMapping("/search")
    public R search(@RequestParam String keywords){
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
