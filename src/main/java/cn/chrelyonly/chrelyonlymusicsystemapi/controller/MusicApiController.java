package cn.chrelyonly.chrelyonlymusicsystemapi.controller;

import cn.chrelyonly.chrelyonlymusicsystemapi.component.R;
import cn.chrelyonly.chrelyonlymusicsystemapi.service.LoginService;
import cn.chrelyonly.chrelyonlymusicsystemapi.service.UserInfoService;
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
@RequestMapping("/music-api")
public class MusicApiController {
    private final LoginService loginService;
    private final UserInfoService userInfoService;

    /**
     * 手机号登录 步骤1
     * @param code 验证码
     * @return json
     */
    @RequestMapping("/cellphone")
    public R cellphone(@RequestParam String code){
        JSONObject cellphone = loginService.cellphone(code);
        return R.data(cellphone);
    }

    /**
     * 获取手机验证码 步骤2
     * @return json
     */
    @RequestMapping("/captchaSent")
    public R captchaSent(){
        JSONObject cellphone = loginService.captchaSent();
        return R.data(cellphone);
    }
    /**
     * 获取手机验证码 步骤2
     * @return json
     */
    @RequestMapping("/userDetail")
    public R userDetail(){
        JSONObject cellphone = userInfoService.userDetail();
        return R.data(cellphone);
    }
}
