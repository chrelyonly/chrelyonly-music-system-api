package cn.chrelyonly.chrelyonlymusicsystemapi.config;

import cn.chrelyonly.chrelyonlymusicsystemapi.component.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.wildfly.common.annotation.NotNull;

/**
 * @author chrelyonly
 * 统一返回格式
 */
@ControllerAdvice(basePackages = "cn.chrelyonly.wechat")
@Slf4j
public class MyResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NotNull MethodParameter methodParameter, @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
//        只有加了@MyAesAop注解的方法才会被拦截
        return true;
    }

    @SneakyThrows
    //大胆的抛出异常
    @Override
    public Object beforeBodyWrite(Object o,@NotNull MethodParameter methodParameter,@NotNull MediaType mediaType,@NotNull Class<? extends HttpMessageConverter<?>> aClass,@NotNull ServerHttpRequest serverHttpRequest,@NotNull ServerHttpResponse serverHttpResponse) {
        if (o instanceof R) {
            return o;
        }
        //在这里我们统一返回我的自定义的ReturnFormat格式 将数据封装到这个格式里
        // o就是我们返回的数据
        return R.data(o);
    }
}
