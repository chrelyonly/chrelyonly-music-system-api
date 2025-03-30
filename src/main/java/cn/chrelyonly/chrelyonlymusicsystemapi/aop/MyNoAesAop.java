package cn.chrelyonly.chrelyonlymusicsystemapi.aop;

import java.lang.annotation.*;

/**
 * @author chrelyonly
 * 对返回的数据进行加密 拦截器
 * @date 2022年11月2日12:00:34
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MyNoAesAop {

}
