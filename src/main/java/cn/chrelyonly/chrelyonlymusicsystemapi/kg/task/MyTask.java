package cn.chrelyonly.chrelyonlymusicsystemapi.kg.task;

import cn.chrelyonly.chrelyonlymusicsystemapi.kg.config.MyConfig;
import cn.chrelyonly.chrelyonlymusicsystemapi.kg.service.LoginService;
import cn.chrelyonly.chrelyonlymusicsystemapi.kg.service.VipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 * @author chrelyonly
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MyTask {
    private final LoginService loginService;
    private final VipService vipService;
//    @Scheduled(cron = "*/10 * * * * *")
//    public void testTask() {
//        log.info("测试定时任务");
//    }

    /**
     * 每30天执行一次
     */
    @Scheduled(cron = "01 01 01 */30 * *")
    public void loginToken() {
        log.info("刷新token过期时间");
        System.out.println(loginService.loginToken(MyConfig.USER_ID, MyConfig.TOKEN));
    }
    /**
     * 每1天执行一次 领取vip
     */
    @Scheduled(cron = "09 09 09 * * *")
    public void youthDayVip() {
        log.info("领取vip");
        System.out.println(vipService.youthDayVip());
    }
}
