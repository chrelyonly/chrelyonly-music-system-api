package cn.chrelyonly.chrelyonlymusicsystemapi.aop;

import cn.chrelyonly.chrelyonlymusicsystemapi.component.R;
import cn.chrelyonly.chrelyonlymusicsystemapi.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chrelyonly
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FastRedisReturnDataImpl {
	private final HttpServletRequest httpServletRequest;

	@Around(value = "@annotation(ver)")
	public Object fastRedisReturnDataImpl(ProceedingJoinPoint point, FastRedisReturnData ver) {
		try {
			log.info("前后端交互数据快速返回拦截");
//            获取浏览器请求参数
			String url = httpServletRequest.getRequestURI();
			String parameter = httpServletRequest.getQueryString();
			Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
//            转换map
			Map<String, Object> map = new HashMap<>();
			while (parameterNames.hasMoreElements()) {
				String key = parameterNames.nextElement();
				String value = httpServletRequest.getParameter(key);
				map.put(key, value);
			}
			String keyParameter = url + "/" + parameter;
            log.info("传参转换 = {}", map);
			String key = "fast:return::Data::music" + keyParameter;
			if (Boolean.TRUE.equals(RedisUtil.hasKeyStatic(key))) {
				log.info("当前缓存Key:{}", key);
				log.info("剩余时间:{}", RedisUtil.getExpireStatic(key));
				Object key1 = RedisUtil.getKey(key);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.registerModule(new JavaTimeModule());
				return objectMapper.readValue(key1.toString(), R.class);
			}
//                    执行方法
			Object object = point.proceed();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			String writeValueAsString = objectMapper.writeValueAsString(object);
			RedisUtil.setObjectStatic(key, writeValueAsString, ver.redisTime());
			log.info("提示：数据正常读取！");
			return object;
		} catch (Throwable e) {
			System.out.println("e.getMessage() = " + e.getMessage());
			e.printStackTrace();
			log.error("提示：数据读取异常!");
			log.info("提示：数据读取异常!");
			return R.fail("提示：数据读取异常!" + e.getMessage());
		}

	}

}
