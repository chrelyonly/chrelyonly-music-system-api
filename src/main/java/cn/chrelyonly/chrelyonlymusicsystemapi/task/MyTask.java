package cn.chrelyonly.chrelyonlymusicsystemapi.task;


import cn.chrelyonly.chrelyonlymusicsystemapi.music.kg.service.MusicKgLoginService;
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
        log.info("定时刷新酷狗token");
        musicKgLoginService.resToken();
    }

    /**
     * 自动签到升级VIP 酷狗
     */
    @Scheduled(cron = "01 01 08 * * *")
    public void upVip(){
        log.info("定时领取升级酷狗VIP");
        musicKgLoginService.youthDayVip();
        musicKgLoginService.upgradeYouthDayVip();
    }
}
