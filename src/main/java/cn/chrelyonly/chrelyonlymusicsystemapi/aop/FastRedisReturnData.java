package cn.chrelyonly.chrelyonlymusicsystemapi.aop;

import java.lang.annotation.*;

/**
 * @author chrelyonly
 * 前后端交互数据快速返回
 * 仅适用于get请求 或者 问号传参的请求
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FastRedisReturnData {

    /**
     * 缓存过期时间
     */
    long redisTime() default 180;
}
