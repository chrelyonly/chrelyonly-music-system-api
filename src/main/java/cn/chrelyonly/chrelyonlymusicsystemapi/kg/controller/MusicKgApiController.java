package cn.chrelyonly.chrelyonlymusicsystemapi.kg.controller;

import cn.chrelyonly.chrelyonlymusicsystemapi.component.R;
import cn.chrelyonly.chrelyonlymusicsystemapi.kg.config.MyConfig;
import cn.chrelyonly.chrelyonlymusicsystemapi.kg.service.LoginService;
import cn.chrelyonly.chrelyonlymusicsystemapi.kg.service.UserInfoService;
import cn.chrelyonly.chrelyonlymusicsystemapi.kg.service.VipService;
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
@RequestMapping("/music-kg-api")
public class MusicKgApiController {
    private final LoginService loginService;
    private final UserInfoService userInfoService;
    private final VipService vipService;

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
     * 刷新登录token
     * @return json
     */
    @RequestMapping("/loginToken")
    public R loginToken(){
        JSONObject cellphone = loginService.loginToken(MyConfig.USER_ID,MyConfig.TOKEN);
        return R.data(cellphone);
    }
    /**
     * 获取用户信息
     * @return json
     */
    @RequestMapping("/userDetail")
    public R userDetail(){
        JSONObject cellphone = userInfoService.userDetail();
        return R.data(cellphone);
    }
    /**
     * 领取vip
     * @return json
     */
    @RequestMapping("/youthDayVip")
    public R youthDayVip(){
        JSONObject youthDayVip = vipService.youthDayVip();
        return R.data(youthDayVip);
    }
}
