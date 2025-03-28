package cn.chrelyonly.chrelyonlymusicsystemapi.task;

import cn.chrelyonly.chrelyonlymusicsystemapi.config.MyConfig;
import cn.chrelyonly.chrelyonlymusicsystemapi.service.LoginService;
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
//    @Scheduled(cron = "*/10 * * * * *")
//    public void testTask() {
//        log.info("测试定时任务");
//    }

    /**
     * 每30天执行一次
     */
    @Scheduled(cron = "01 01 01 */30 * *")
    public void testTask() {
        log.info("刷新token过期时间");
        loginService.loginToken(MyConfig.USER_ID,MyConfig.TOKEN);
    }
}
