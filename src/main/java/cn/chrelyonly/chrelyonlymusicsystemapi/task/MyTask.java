package cn.chrelyonly.chrelyonlymusicsystemapi.task;


import cn.chrelyonly.chrelyonlymusicsystemapi.music.kg.service.MusicKgLoginService;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MyTask {
    private final MusicKgLoginService musicKgLoginService;



    /**
     * 定时刷新 酷狗 token
     */
    @Scheduled(cron = "01 01 * * * *")
    public void TaskKgToken(){
        log.info("定时刷新微信token");
        JSONObject jsonObject = musicKgLoginService.resToken();
        log.info(jsonObject.toJSONString());
    }
}
