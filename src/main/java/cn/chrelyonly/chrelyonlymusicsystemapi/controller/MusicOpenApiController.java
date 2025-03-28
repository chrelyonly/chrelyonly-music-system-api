package cn.chrelyonly.chrelyonlymusicsystemapi.controller;

import cn.chrelyonly.chrelyonlymusicsystemapi.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chrelyonly
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/music-open-api")
public class MusicOpenApiController {
    private final LoginService loginService;
}
