package cn.chrelyonly.chrelyonlymusicsystemapi.task;

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

    @Scheduled(cron = "*/10 * * * * *")
    public void testTask() {
        log.info("测试定时任务");
    }
}
